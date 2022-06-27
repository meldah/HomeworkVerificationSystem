package com.tusofia.app.homeworkVerification.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Submission;
import com.tusofia.app.homeworkVerification.domain.models.service.SubmissionServiceModel;

public interface SubmissionService {
	SubmissionServiceModel findByStudentIdAndTaskId(String studentId, String taskId);

	Submission addSubmission(String courseName, String exerciseName, String taskName, Student student, MultipartFile multipartFile) throws IOException;

	void deleteSubmission(SubmissionServiceModel submission) throws IOException;

	Set<SubmissionServiceModel> findNonEvaluatedSubmissionsByTaskId(String taskId);

	void evaluateSubmission(String submissionId, double grade);
}
