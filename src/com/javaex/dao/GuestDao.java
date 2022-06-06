package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestVo;

public class GuestDao {
	// 0. import java.sql.*;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	private void getConnection() {
		try {
		    // 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

		    // 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			
		} catch (ClassNotFoundException e) {
		    System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}   
	}
	
	
	private void close() {
		 // 5. 자원정리
	    try {
	        if (rs != null) {
	            rs.close();
	        }                
	        if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
		} catch (SQLException e) {
	        System.out.println("error:" + e);
	    }
	}
	
	//정보 전체 조회 메소드
	public List<GuestVo> getguestList() {
		
		//리스트 만들기
		List<GuestVo> guestList = new ArrayList<GuestVo>();
		
		getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			
			String query = "";
			query += " select  no, ";
			query += "         name, ";
			query += "         password, ";
			query += "         content, ";
			query += "         to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') regDate ";
			query += " from guestbook ";
			
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			// 4.결과처리
			//반복문으로 Vo 만들기 List에 추가하기
			while(rs.next()) {
				
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("regDate");
				
				
				GuestVo guestVo = new GuestVo(no, name, password, content, regDate);
				
				guestList.add(guestVo);
				
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		
		return guestList;
		
	}
	
	
	
	//정보 추가
	public int guestInsert(GuestVo guestVo) {
		int count = -1;
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into guestbook ";
			query += " values (seq_guestbook_no.nextval, ? , ?, ?, sysdate) ";
			
			pstmt = conn.prepareStatement(query); 
			
			pstmt.setString(1, guestVo.getName()); 
			pstmt.setString(2, guestVo.getPassword()); 
			pstmt.setString(3, guestVo.getContent());
			
			count = pstmt.executeUpdate();
			
			System.out.println(count + "건 추가되었습니다");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		close();
		return count;
		
	}
	
	//정보 삭제
	public int guestDelete(GuestVo vo) {
		int count = -1;
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " delete from guestbook ";
			query += " where no = ? ";
			query += " and password = ? ";
			
			pstmt = conn.prepareStatement(query); 
			
			pstmt.setInt(1, vo.getNo()); 
			pstmt.setString(2, vo.getPassword());
			
			count = pstmt.executeUpdate();
			
			System.out.println(count + "건 삭제되었습니다");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		close();
		return count;
	
	}
	
	
	

}
