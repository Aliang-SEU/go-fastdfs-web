package com.perfree.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.perfree.common.AjaxResult;
import com.perfree.common.DateUtil;
import com.perfree.common.PropertiesUtil;
import com.perfree.common.StringUtil;
import com.perfree.entity.User;
import com.perfree.service.InstallService;

/**
 * 安装
 * @author yinpengfei
 */
@Controller
public class InstallController {

	@Autowired
	private InstallService installService;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 安装页
	 * @return
	 */
	@RequestMapping("/install")
	public String index() {
		return "install";
	}
	
	/**
	 * 执行安装操作
	 * @param user
	 * @param server
	 * @return
	 */
	@RequestMapping("/doInstall")
	@ResponseBody
	public AjaxResult doInstall(User user,String server) {
		String uuid = StringUtil.getUUID();
		Md5Hash md5Hash = new Md5Hash(user.getPassword(),uuid);
		user.setPassword(md5Hash.toString());
		user.setCredentialsSalt(uuid);
		user.setCreateTime(DateUtil.getFormatDate(new Date()));
		installService.saveUser(user);
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			//动态更新配置文件,将server地址写入
			fileInputStream = new FileInputStream(propertiesUtil.getProperties());
			fileOutputStream = new FileOutputStream(propertiesUtil.getProperties());
			properties.load(fileInputStream);
			properties.setProperty("go.fastdfs.server.address", server);
			properties.store(fileOutputStream, "Update go.fastdfs.server.address");
			return new AjaxResult(AjaxResult.AJAX_SUCCESS);
		} catch (Exception e) {
			return new AjaxResult(AjaxResult.AJAX_ERROR,"安装失败");
		}finally {
			if(fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
