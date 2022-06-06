package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestVo;


@WebServlet("/gbc")
public class GuestbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//포스트 방식일때 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
		
		String action = request.getParameter("action");
		System.out.println("action");
		
		if("list".equals(action)) {
			//데이터가져오기
			GuestDao guestDao = new GuestDao();
			List<GuestVo> guestList = guestDao.getguestList();
			
			//request에 데이터 추가
			request.setAttribute("gList", guestList); //주소값 넘기기
			
			
			//데이터 + html --> jsp 시킨다
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			
			/*
			RequestDispatcher rd = request.getRequestDispatcher("/addList.jsp");
			rd.forward(request, response);
			*/
			
		}else if("add".equals(action)) {

			//파라미터값 받기
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");
			
			GuestVo guestVo = new GuestVo(name, password, content);
			
			//GuestDao의 guestInsert 를 이용하여 데이터 추가
			GuestDao guestDao = new GuestDao();
			guestDao.guestInsert(guestVo);
			
			//리다이렉트 list
			WebUtil.redirect(request, response, "./gbc?action=list");
			
			/*
			response.sendRedirect("/guestbook2/gbc?action=list");
			*/
		}else if("deleteForm".equals(action)) {
			
			//데이터 + html --> jsp 시킨다
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
			/*
			RequestDispatcher rd = request.getRequestDispatcher("/deleteForm.jsp");
			rd.forward(request, response);
			*/
		}else if("delete".equals(action)) {
			//파라미터값 받기
	
			int no = Integer.parseInt(request.getParameter("no"));
			String deletePw= request.getParameter("deletePw");
			
			GuestVo vo = new GuestVo();
			vo.setNo(no);
			vo.setPassword(deletePw);
				
			GuestDao dao = new GuestDao();
			dao.guestDelete(vo);
			
			//리다이렉트 list
			
			WebUtil.redirect(request, response, "./gbc?action=list");
			/*
			response.sendRedirect("/guestbook2/gbc?action=list");
			*/
		}else {
			System.out.println("action 파라미터 없음");
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
