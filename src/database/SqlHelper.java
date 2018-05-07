package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
	 * @param username
	 * @param userId
	 * @param password
	 * @param token
	 * @param imagePath
	 */
	public static void insertUser(String username,String userId,String password,String token,String imagePath) {
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
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void addUser() {

	}

}
