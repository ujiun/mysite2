package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;
import com.javaex.vo.GuestVo;
import com.javaex.vo.UserVo;

public class BoardDao {
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
	
	// list
	public List<BoardVo> getboardList() {
		
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		
		getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			
			String query = "";
			query += " select  b.no, ";
			query += "         b.title, ";
			query += "         u.name, ";
			query += "         b.hit, ";
			query += "         to_char(b.reg_date, 'yy-mm-dd hh:mi') regDate, ";
			query += "         b.user_no userNo ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			query += " order by regDate desc ";
			
			
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			// 4.결과처리
			//반복문으로 Vo 만들기 List에 추가하기
			while(rs.next()) {
				
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				int userNo = rs.getInt("userNo");
				BoardVo boardVo = new BoardVo(no, title, name, hit, regDate, userNo);
				
				boardList.add(boardVo);
				
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		
		return boardList;
		
	}
	
	// delete
	public int boardDelete(int no) {
		int count = -1;
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " delete from board ";
			query += " where no = ? ";
			
			pstmt = conn.prepareStatement(query); 
			
			pstmt.setInt(1, no); 
			
			
			count = pstmt.executeUpdate();
			
			System.out.println(count + "건 삭제되었습니다");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		close();
		return count;
	
	}
	
	
	// write
	public int write(BoardVo boardVo) {
		int count = -1;
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			//SQL문 준비
			String query = "";
			query += " insert into board ";
			query += " values (seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";
			
			//바인딩
			pstmt = conn.prepareStatement(query); 
			
			pstmt.setString(1, boardVo.getTitle()); 
			pstmt.setString(2, boardVo.getContent()); 
			pstmt.setInt(3, boardVo.getUserNo());
			
			//실행
			count = pstmt.executeUpdate();
			
			System.out.println(count + "건 작성되었습니다");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		
		close();
		
		return count;
	}
	
	// read
	public BoardVo getBoard(int boardNo) {
		BoardVo boardVo = null;

		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " select  b.no, ";
			query += " 	       u.name, ";
			query += "         b.hit, ";
			query += "         b.title, ";
			query += "         to_char(b.reg_date, 'yy-mm-dd') regDate, ";
			query += "         b.content, ";
			query += "         b.user_no userNo ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			query += " and b.no = ? ";

			// 바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardNo);

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String title = rs.getString("title");
				String regDate = rs.getString("regDate");
				String content = rs.getString("content");
				int userNo = rs.getInt("userNo");
				
				boardVo = new BoardVo(no, name, hit, title, regDate, content, userNo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return boardVo;
	}
	
	// update
	public int update(BoardVo boardVo) {
		
		int count = -1;
		
		this.getConnection();
		

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " update board ";
			query += " set title = ?, ";
			query += "     content = ? ";
			query += " where no = ? ";
			System.out.println(query);
			
			// 바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getNo());
			// 실행
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건이 수정되었습니다.");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		this.close();
		
		return count;
		
		
	}
}
