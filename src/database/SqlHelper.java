package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.xdevapi.Result;

import bean.Message;
import util.Log;

public class SqlHelper {

	private static final String TAG = "SqlHelper";
	// 驱动程序名
	private static String driver = "com.mysql.cj.jdbc.Driver";
	// URL指向要访问的数据库名
	private static String url = "jdbc:mysql://localhost:3306/trip1.0?useUnicode=true&characterEncoding=UTF-8";
	// MySQL配置时的用户名
	private static String user = "root";
	// MySQL配置时的密码
	private static String password = "gongcheng";

	/**
	 * 连接到数据库
	 */
	public static Connection getConnect() {
		// 声明Connection对象
		Connection connection = null;
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			connection = DriverManager.getConnection(url, user, password);
			if (!connection.isClosed()) {
				Log.i(TAG, "成功连接到数据库!");
			}
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "连接数据库失败");
		return null;
	}

	/**
	 * 向数据库添加新用户
	 * 
	 * @param username
	 * @param userId
	 * @param password
	 * @param token
	 * @param imagePath
	 */
	public static void insertUser(String username, String userId, String password, String token, String imagePath) {
		Connection connection = getConnect();
		String sql = "insert into user_table (username,userId,password,token,imagePath) values(?,?,?,?,?)";
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, username);
			preStatement.setString(2, userId);
			preStatement.setString(3, password);
			preStatement.setString(4, token);
			preStatement.setString(5, imagePath);
			preStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 登录数据库操作,检查是否由用户名,密码是否正确
	 * 
	 * @return 返回false,账号密码错误
	 */
	public static boolean login(String userId, String password) {
		Connection connection = getConnect();
		String sql = "select userId,password from user_table where userId = ? and password =?";
		PreparedStatement preStatement;
		try {
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			preStatement.setString(2, password);
			ResultSet rs = preStatement.executeQuery();
			rs.last();
			int rowCount = rs.getRow();// 查询到结果的数量
			if (rowCount == 0) {
				Log.i(TAG, userId + "用户登录失败");
				return false;
			}
			if (rowCount == 1) {
				Log.i(TAG, userId + "用户登录成功");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 更新用户的token
	 */
	public static void updateToken(String userId, String token) {
		Connection connection = getConnect();
		String sql = "update user_table set token = ? where userId = ? ";
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, token);
			preStatement.setString(2, userId);
			preStatement.execute();
			Log.d(TAG, "用户token已经更新");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 寻找特定用户Id
	 */
	public static boolean searchId(String userId) {
		Connection connection = getConnect();
		String sql = "select * from user_table where userId = ?";
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			ResultSet rs = preStatement.executeQuery();
			rs.last();
			int rowCount = rs.getRow();
			if (rowCount == 1) {
				Log.d(TAG, "存在用户id" + userId);
				return true;
			} else {
				Log.d(TAG, "不存在用户id" + userId);
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 寻找token是否存在
	 */
	/**
	 * 寻找特定用户Id
	 */
	public static boolean searchToken(String token) {
		Connection connection = getConnect();
		String sql = "select * from user_table where token = ?";
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, token);
			ResultSet rs = preStatement.executeQuery();
			rs.last();
			int rowCount = rs.getRow();
			if (rowCount == 1) {
				Log.d(TAG, "存在用户token : " + token);
				return true;
			} else {
				Log.d(TAG, "不存在用户token : " + token);
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 添加朋友,互相添加
	 */
	public static void addFriend(String userId, String friendId) {
		Connection connection = getConnect();
		String sql = "insert into friend_table (userId,friendId) values (?,?)";
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			preStatement.setString(2, friendId);
			preStatement.execute();

			// 反向添加,互相位好友
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, friendId);
			preStatement.setString(2, userId);
			preStatement.execute();
			Log.d(TAG, "已经添加好友");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 是否已经添加朋友
	 */
	public static boolean isMyFriend(String userId, String friendId) {
		Connection connection = getConnect();
		String sql = "select * from friend_table where (userId = ? and friendId = ?) or (userId = ? and friendId = ?)";
		PreparedStatement preStatement;
		try {
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			preStatement.setString(2, friendId);
			preStatement.setString(3, friendId);
			preStatement.setString(4, userId);
			ResultSet rs = preStatement.executeQuery();
			rs.last();
			int rowCount = rs.getRow();
			if (rowCount == 2) {
				Log.d(TAG, "二者是好友关系");
				return true;
			} else {
				Log.d(TAG, "二者不是好友关系");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	/**
	 * 插入新的message内容
	 * 
	 * @param userId
	 * @param friendId
	 * @param msg
	 */
	public static void insertNewMessage(String userId, String friendId, String msg) {
		Connection connection = getConnect();
		String sql = "insert into message_table  (userId,friendId,message,msgDate,msgTime) values (?,?,?,?,?)";
		long timeMillis = System.currentTimeMillis();
		Time time = new Time(timeMillis);
		Date date = new Date(timeMillis);
		PreparedStatement preStatement;
		try {
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			preStatement.setString(2, friendId);
			preStatement.setString(3, msg);
			preStatement.setDate(4, date);
			preStatement.setTime(5, time);
			preStatement.execute();
			Log.d(TAG, "消息插入完成");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返回用户收到的消息,json字符串
	 * @param userId
	 * @return
	 */
	public static String getNewMessage(String userId) {
		Connection connection = getConnect();
		String sql = "select * from message_table where friendId = ?";//要发送的消息
		PreparedStatement preStatement;
		List<Message> messageList = new ArrayList<Message>();
		try {
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			ResultSet resultSet = preStatement.executeQuery();
			while(resultSet.next()) {
				Message msg = new Message(resultSet.getString("userId"), resultSet.getString("friendId"), resultSet.getString("message"), 
						resultSet.getDate("msgDate"), resultSet.getTime("msgTime"));
				messageList.add(msg);
			}
			String msgJson = JSON.toJSONString(messageList);
			if(msgJson.equals("[]")) {
				Log.d(TAG, "没有用用户"+userId+"的消息");
				return null;
			}
			Log.d(TAG, msgJson);
			return msgJson;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 清空已经转发的消息
	 */
	public static void deleteNewMessage(String userId) {
		Connection connection = getConnect();
		String sql = "delete from message_table where friendId = ?";
		PreparedStatement preStatement;
		try {
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, userId);
			preStatement.execute();
			Log.d(TAG, "已经删除用户"+userId+"的消息");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
