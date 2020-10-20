package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/main")
	public ModelAndView getMainPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/main");
		return modelAndView;
	}
}
