package util;

public class Log {

	/**
	 * 用于调试的日志方法
	 * @param tag
	 * @param information
	 */
	public static void d(String tag, String information) {
		if (Configure.isDebug) {
			System.out.println(tag + " :" + information);
		}
	}

	/**
	 * 用于重要的日志输出,服务器正常运行时也会输出
	 */
	public static void i(String tag, String information) {
		System.out.println(tag + " :" + information);
	}

}
