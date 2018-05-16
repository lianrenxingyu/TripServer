package messageServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import util.Log;


public class InitMsgServer extends HttpServlet {
	public static String TAG = "InitMsgServer" ;
	@Override
	public void init() throws ServletException {
		System.out.println("Socket message 服务初始化开始");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ServerSocket server = null;
				try {
					server = new ServerSocket(8081);
					int i = 0;
					while (true) {
//						接收连接
						System.out.println("服务器准备接收数据1");
						Socket client = server.accept();
						System.out.println("client :"+ i++);
//						开启线程处理本次连接
						new Thread(new HandleThread(client),"client thread "+i).start();
						Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
						for (Thread t : maps.keySet()) {
							Log.i(TAG, t.getName());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					try {
						if (server != null) {
							server.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		System.out.println("Socket message 服务初始化完成");

	}
}
