package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Configure;
import util.Log;

public class Register extends HttpServlet {

	private static final String TAG = "Register";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String userName = req.getParameter("name");
		String password = req.getParameter("password");
		Log.d(TAG, "name :" + userName + " password :" + password);
		// 用户第一次的sessionId作为token
		String sessionId = req.getSession().getId();
		String token = sessionId;
		// TODO :在数据库中插入新注册的用户数据
		
		Cookie tokenCookie = new Cookie("token", token);
		Cookie loginStatus = new Cookie("isLogin", Configure.Login);
		resp.addCookie(tokenCookie);
		resp.addCookie(loginStatus);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.write("已经注册成功");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}

}
