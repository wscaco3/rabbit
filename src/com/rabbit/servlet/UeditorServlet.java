package com.rabbit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.ueditor.ActionEnter;

/**
 * Servlet implementation class UeditorServlet
 */
public class UeditorServlet extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6692689387075769935L;

	public UeditorServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding( "utf-8" );
		response.setHeader("Content-Type" , "text/html");
		PrintWriter out = response.getWriter();
		String rootPath = this.getServletContext().getRealPath("/");
		String outStr = new ActionEnter(request, rootPath).exec();
		out.print(outStr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
