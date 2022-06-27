package com.tusofia.app.homeworkVerification.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
public class UnauthorizedController {

	@GetMapping("/unauthorized")
	@PageTitle("ERROR 403 - UNAUTHORIZED")
	public String displayUnauthorizedPage() {
		return "unauthorized";
	}
}