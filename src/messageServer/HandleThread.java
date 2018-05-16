package messageServer;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import database.SqlHelper;
import util.Log;

public class HandleThread implements Runnable {

	private static final String TAG = "HandleThread";
	private Socket client = null;
	private InputStream inputStream = null;

	private OutputStream outputStream = null;
	private DataOutputStream dataOutputStream = null;

	public HandleThread(Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			inputStream = client.getInputStream();
			outputStream = client.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			String jsonStr;
			byte[] data = new byte[2048];
			int len;
			String userId = null;
			boolean isOpen = true;//代表客户端的状态,是否开启
			while (true) {

				Thread.sleep(2000);
				len = inputStream.read(data);
				if (len != -1) {
					jsonStr = new String(data, 0, len);
					System.out.println("服务端接收的数据" + jsonStr);
					JSONObject object = JSON.parseObject(jsonStr);
					userId = object.getString("userId");
					if (object.getString("friendId").equals("0")) {
						Log.d(TAG, "客户端发送的初始化数据");
					} if (object.get("friendId").equals("1")) {
						isOpen = false;
						Log.d(TAG, "客户端已经下线");
						continue;
					}else {
						//接收到的数据写入操作
						SqlHelper.insertNewMessage(object.getString("userId"), object.getString("friendId"),
								object.getString("msg"));
					}
				}
				if (userId != null) {
					// 服务器返回数值
					String jsonMsg = SqlHelper.getNewMessage(userId);
					if (!jsonMsg.equals("[]")) {
						dataOutputStream.write(jsonMsg.getBytes(Charset.forName("utf-8")));
						System.out.println("返回数据成功");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (client != null) {
					client.close();
					Log.d(TAG, "socket连接关闭,线程关闭");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}