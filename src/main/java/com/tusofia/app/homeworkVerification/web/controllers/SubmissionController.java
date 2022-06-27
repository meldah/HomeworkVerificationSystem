package com.tusofia.app.homeworkVerification.web.controllers;

import java.io.IOException;
import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tusofia.app.homeworkVerification.domain.models.binding.SubmissionEvaluateBindingModel;
import com.tusofia.app.homeworkVerification.service.SubmissionService;

@Controller
@RequestMapping("/submissions")
public class SubmissionController extends BaseController {

	private final SubmissionService submissionService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public SubmissionController(SubmissionService submissionService, ModelMapper modelMapper) {
		this.submissionService = submissionService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping("/evaluate")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ModelAndView submitHomework(
			@ModelAttribute(name = "submissionModel") SubmissionEvaluateBindingModel submission) {
		this.submissionService.evaluateSubmission(submission.getSubmissionId(), submission.getDegree());
		
		return super.redirect("/exercise/evaluate/" + submission.getCourseId() + '/' + submission.getExerciseId() + "/"
				+ submission.getTaskId());
	}
	
}