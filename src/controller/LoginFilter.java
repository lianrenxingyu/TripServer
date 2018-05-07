package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import util.Log;

public class LoginFilter implements Filter {

	public static final String TAG = "LoginFilter"; 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	    // 获取初始化参数
//        String site = config.getInitParameter("Site"); 

        // 输出初始化参数
//        System.out.println("网站名称: " + site); 
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//TODO 执行验证登录的行为,如果成功则继续,否则跳转到登录界面
		
		Log.d(TAG,"登录验证过滤操作已经执行");
		chain.doFilter(req, resp);

		
	}

	@Override
	public void destroy() {
	}

}
