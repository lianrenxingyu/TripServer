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

	private boolean isOpen = true;// 代表客户端的状态,是否开启,
	private String userId = null;

	// 定义一个发送返回线程
	private Thread sendThread = new Thread(new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "返回数据线程开启");
			try {
				outputStream = client.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				isOpen = true;// 每次开启都初始化
				while (isOpen) {
					Thread.sleep(4000);
					if (userId != null) {
						// 服务器返回数值
						String jsonMsg = SqlHelper.getNewMessage(userId);
						if (jsonMsg !=null) {
							dataOutputStream.write(jsonMsg.getBytes(Charset.forName("utf-8")));
							System.out.println("返回数据成功");
							SqlHelper.deleteNewMessage(userId);
						} else {
							Log.d(TAG, "没有数据需要返回");
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				isOpen = false;
				Log.d(TAG, "返回线程结束");
			}
		}
	});

	public HandleThread(Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			sendThread.start();//开启返回数据的线程
			inputStream = client.getInputStream();
			String jsonStr;
			byte[] data = new byte[2048];
			int len;
			isOpen = true;// 每次开启线程都要使状态标志初始化
			while (isOpen) {
				Log.d(TAG, "等待读取数据");
				Thread.sleep(2000);
				len = inputStream.read(data);
				if (len != -1) {
					jsonStr = new String(data, 0, len);
					System.out.println("服务端接收的数据" + jsonStr);
					JSONObject object = JSON.parseObject(jsonStr);
					userId = object.getString("userId");
					if (object.getString("friendId").equals("0")) {
						Log.d(TAG, "客户端发送的初始化数据");
					} else {
						// 接收到的数据写入操作
						SqlHelper.insertNewMessage(object.getString("userId"), object.getString("friendId"),
								object.getString("msg"));
					}
				}
			}
		} catch (IOException e) {
			Log.d(TAG, "输入流关闭");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				isOpen = false;
				Thread.sleep(3000);
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
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}