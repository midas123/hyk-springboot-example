package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
	@GetMapping("/admin/main")
	public ModelAndView getMainPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/main");
		return modelAndView;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/main2")
	public ModelAndView getMainPage2() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/main");
		return modelAndView;
	}
}
