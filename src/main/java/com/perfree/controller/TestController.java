package com.perfree.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfree.entity.User;
import com.perfree.service.UserService;

/**
 * 测试
 * @author Perfree
 *
 */
@RestController
public class TestController {

	@Autowired
	private UserService testService;
	
	@RequestMapping("/")
	public User index() {
		return testService.getUserById(1);
	}
}
