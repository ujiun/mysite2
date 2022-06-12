package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;


@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		BoardDao boardDao = new BoardDao();
		BoardVo boardVo = new BoardVo();
		
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		String action = request.getParameter("action");
		
		
		if("list".equals(action)) {
			
			List<BoardVo> boardList = boardDao.getboardList();
			
			//request에 데이터 추가
			request.setAttribute("bList", boardList); //주소값 넘기기
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		}else if("delete".equals(action)) {
			
			int no = Integer.parseInt(request.getParameter("boardNo"));
			
			boardDao.boardDelete(no);
			
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		}else if("writeForm".equals(action)) {

			if(authUser == null) {
				
				WebUtil.redirect(request, response, "/mysite2/user?action=loginForm");
			}else {
				
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			}
				
		}else if("write".equals(action)) {
			
			//세션에서 로그인한 사용자의 no 값 가져오기
			session = request.getSession();
			authUser = (UserVo)session.getAttribute("authUser");
			int userNo = authUser.getNo();
			
			//조회수 카운트
			
			//파라미터값 꺼내기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			
			boardVo = new BoardVo(title, content, userNo);
			boardDao.write(boardVo);
			
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		}else if("read".equals(action)) {
			
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			BoardVo board = boardDao.getBoard(boardNo);
			
			request.setAttribute("board", board);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		}else if("modifyForm".equals(action)) {
			
			
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			
			BoardVo board = boardDao.getBoard(boardNo);
			System.out.println(boardNo);
			System.out.println(board);
			request.setAttribute("board", board);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
		}else if("modify".equals(action)) {
			
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			String title= request.getParameter("title");
			String content = request.getParameter("content");
			
			boardVo = new BoardVo();
			
			boardVo.setNo(boardNo);
			boardVo.setTitle(title);
			boardVo.setContent(content);
			
			boardDao.update(boardVo);
			
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
