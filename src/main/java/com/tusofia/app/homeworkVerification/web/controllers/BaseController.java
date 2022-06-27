package com.tusofia.app.homeworkVerification.web.controllers;

import org.springframework.web.servlet.ModelAndView;

public class BaseController {
	
	public ModelAndView view(String viewName, ModelAndView modelAndView) {
		modelAndView.setViewName(viewName);
		
		return modelAndView;
	}
	
	public ModelAndView view(String viewName) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(viewName);
		
		return modelAndView;
	}
	
	public ModelAndView redirect(String url) {
		return this.view("redirect:" + url);
	}
}
