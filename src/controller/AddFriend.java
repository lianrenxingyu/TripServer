package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import database.SqlHelper;
import util.Log;

public class AddFriend extends HttpServlet {

	private final static String TAG = "AddFriend"; 
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String friendId = req.getParameter("friendId");
		Log.d(TAG, "userId :"+userId+"  friendId :"+friendId);
		
		boolean hasFriendId = SqlHelper.searchId(friendId);//朋友id是否存在
		
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		JSONObject object = new JSONObject();
		object.put("hasFriend", "false");
		object.put("isMyFriend", "false");
		object.put("isAddSuccess", "false");
		if(!hasFriendId) {
			object.put("hasFriend", "false");
			out.write(JSON.toJSONString(object));
			out.close();
			return;
		}
		object.put("hasFriend", "true");
		boolean isMyFriend = SqlHelper.isMyFriend(userId, friendId);
		if (isMyFriend) {
			object.put("isMyFriend", "true");
			out.write(JSON.toJSONString(object));
			out.close();
			Log.d(TAG, "已经是好友关系,不能添加");
			return;
		}
		SqlHelper.addFriend(userId, friendId);
		object.put("isAddSuccess", "true");
		out.write(JSON.toJSONString(object));
		out.close();
		return;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
}
