package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import database.SqlHelper;
import util.Log;

public class IdentifyUser extends HttpServlet {

	public static final String TAG = "IdentifyUser";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = req.getHeader("token");
		Log.d(TAG, "header中得到的token:"+ token);
		JSONObject isLoginJson = new JSONObject();
		if (SqlHelper.searchToken(token)) {
			Log.d(TAG, "token验证成功");
			Log.d(TAG, "登录验证过滤操作验证成功");
			isLoginJson.put("isLogin", "true");
			PrintWriter out = resp.getWriter();
			out.write(JSON.toJSONString(isLoginJson));
			out.close();
		}else{
			isLoginJson.put("isLogin", "false");
			PrintWriter out = resp.getWriter();
			out.write(JSON.toJSONString(isLoginJson));
			out.close();
		}
		
	}

}
