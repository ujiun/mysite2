package com.javaex.dao;

import java.util.List;

import com.javaex.vo.BoardVo;

public class DaoTest {

	public static void main(String[] args) {
		//리스트 가져오기 테스트
		BoardDao boardDao = new BoardDao();
		List<BoardVo> boardList = boardDao.getboardList();
		
		System.out.println(boardList); 
		
		
	}

}
