package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class HomeConrollerAdvice {
	@ResponseStatus(value=HttpStatus.FORBIDDEN)
	@ExceptionHandler({RuntimeException.class})
	public ModelAndView getError() {
		ModelAndView view = new ModelAndView();
		view.setViewName("error/403");
		return view;
	}
}
