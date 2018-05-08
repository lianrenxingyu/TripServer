package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import database.SqlHelper;
import util.Configure;
import util.Log;
import util.StringUtil;

public class Login extends HttpServlet {

	private static final String TAG = "Login";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = URLDecoder.decode(req.getParameter("username"),"utf-8");
		String password = URLDecoder.decode(req.getParameter("password"),"utf-8");
		Log.d(TAG, "name :" + username + " password :" + password);
		String userId = StringUtil.md5(username, 10);
		
		resp.setCharacterEncoding("utf-8");
		JSONObject object = new JSONObject();
		if(!SqlHelper.login(userId, password)) {
			object.put("loginResult", Configure.LoginFail);
			String objectStr = JSON.toJSONString(object);
			PrintWriter out = resp.getWriter();
			out.println(objectStr);
			out.close();
			return;
		}
		
		String sessionId = req.getSession().getId();
		String token = StringUtil.md5(sessionId, 16);
		Cookie cookie = new Cookie("token", token);
		resp.addCookie(cookie);
		PrintWriter out = resp.getWriter();
		SqlHelper.updateToken(userId, token);
		object.put("loginResult", Configure.LoginSuccess);
		String objectStr = JSON.toJSONString(object);
		out.println(objectStr);
		out.close();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}

}
