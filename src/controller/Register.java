package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.SqlHelper;
import util.Configure;
import util.Log;
import util.StringUtil;

//@WebServlet("/register")

public class Register extends HttpServlet {

	private static final String TAG = "Register";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = URLDecoder.decode(req.getParameter("username"),"utf-8");
		String password = URLDecoder.decode(req.getParameter("password"),"utf-8");
		Log.d(TAG, "name :" + username + " password :" + password);
		String userId = StringUtil.md5(username, 10);
		//TODO: 检查用户名字Id是否已经被注册
		String sessionId = req.getSession().getId();
		String token = StringUtil.md5(sessionId, 16);// 用户第一次的sessionId md6加密作为token,token 16位
		// TODO :检查数据的合法性
		SqlHelper.insertUser(username, userId, password, token, "imagePath");
		Cookie tokenCookie = new Cookie("token", token);
		resp.addCookie(tokenCookie);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		out.print("注册成功");
		Log.d(TAG, "已经注册成功");
		out.close();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log.d(TAG, "接收到注册请求");
		doPost(req, resp);
	}

}
