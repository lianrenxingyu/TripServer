package bean;

import java.sql.Date;
import java.sql.Time;

import com.alibaba.fastjson.annotation.JSONField;

public class Message {

	private String userId;
	private String friendId;
	private String msg;
	
	@JSONField(format = "yyyy-MM-dd")
	private Date date;
	@JSONField(format = "HH:mm:ss")
	private Time time;
	
	
	public Message(String userId, String friendId, String msg, Date date, Time time) {
		this.userId = userId;
		this.friendId = friendId;
		this.msg = msg;
		this.date = date;
		this.time = time;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
}
