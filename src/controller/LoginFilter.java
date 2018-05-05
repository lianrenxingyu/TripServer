package controller;

import javax.servlet.*;

import java.io.IOException;
import java.util.*;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	    // 获取初始化参数
//        String site = config.getInitParameter("Site"); 

        // 输出初始化参数
//        System.out.println("网站名称: " + site); 
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
	}

}
