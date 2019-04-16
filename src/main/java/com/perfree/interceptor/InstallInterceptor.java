package com.perfree.interceptor;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.perfree.common.PropertiesUtil;

/**
 * 安装拦截器,如用户未安装,则执行安装操作
 * @author Perfree
 */
public class InstallInterceptor implements HandlerInterceptor{

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesUtil.getProperties()));
		String serverAddress = properties.getProperty("go.fastdfs.server.address");
		if(serverAddress == null || serverAddress == "" || serverAddress.equals("default")) {
			//用户未安装,重定向至安装页
			response.sendRedirect("/install");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	
}
