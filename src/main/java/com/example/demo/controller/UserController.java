package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;
	
	@GetMapping("/main")
	public ModelAndView getMainPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		return modelAndView;
	}
	
	@GetMapping("/login")
	public ModelAndView getLoginPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/login");
		return modelAndView;
	}
	
	@GetMapping("/user/join")
	public ModelAndView getJoinForm() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/join");
		return modelAndView;
	}
	
	@PostMapping("/user/join")
	public ModelAndView JoinUser(UserDto dto) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/join");
		
		userService.saveUser(UserInfo.dtoToEntity(dto));
		
		return modelAndView;
	}
}
