package com.tusofia.app.homeworkVerification.service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tusofia.app.homeworkVerification.domain.entities.File;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Submission;
import com.tusofia.app.homeworkVerification.domain.models.service.SubmissionServiceModel;
import com.tusofia.app.homeworkVerification.domain.utils.DateManager;
import com.tusofia.app.homeworkVerification.domain.utils.FileManager;
import com.tusofia.app.homeworkVerification.error.SubmissionNotFoundException;
import com.tusofia.app.homeworkVerification.repository.SubmissionRepository;
import com.tusofia.app.homeworkVerification.service.storage.CloudService;
import com.tusofia.app.homeworkVerification.service.storage.CloudService.FileCloudData;

@Service
public class SubmissionServiceImpl implements SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final FileService fileService;
	private final CloudService cloudService;
	private final ModelMapper modelMapper;
	private final DateManager dateManager;
	
	@Autowired
	public SubmissionServiceImpl(SubmissionRepository submissionRepository, FileService fileService, CloudService cloudService, ModelMapper modelMapper,
			DateManager dateManager) {
		this.submissionRepository = submissionRepository;
		this.fileService = fileService;
		this.cloudService = cloudService;
		this.modelMapper = modelMapper;
		this.dateManager = dateManager;
	}
	
	@Override
	public SubmissionServiceModel findByStudentIdAndTaskId(String studentId, String taskId) {
		Submission submission = this.submissionRepository.
				findByStudentIdAndTaskId(studentId, taskId)
				.orElse(null);
		return submission == null ? null : this.modelMapper.map(submission, SubmissionServiceModel.class);
	}


	@Override
	public Submission addSubmission(String courseName, String exerciseName, String taskName, Student student, MultipartFile multipartFile) throws IOException {
		Submission submission = new Submission();
		submission.setStudent(student);
		
		java.io.File submissionFile = FileManager.createFileFrom(multipartFile);
		
		FileCloudData cloudData = this.cloudService.uploadFile(courseName, exerciseName, taskName, submissionFile);
		this.fileService.addFileByCloudData(cloudData);

		File file = this.modelMapper.map(this.fileService.getFileByUrl(cloudData.getUrl()), File.class);
		submission.setFile(file);

		submission.setCreatedOn(this.dateManager.getCurrentDate());
		
		submissionFile.delete();

		return this.submissionRepository.saveAndFlush(submission);
	}

	@Override
	public void deleteSubmission(SubmissionServiceModel submission) throws IOException {
		this.fileService.deleteFile(submission.getFile().getId());
		this.submissionRepository.deleteById(submission.getId());
		this.submissionRepository.flush();
	}

	@Override
	public Set<SubmissionServiceModel> findNonEvaluatedSubmissionsByTaskId(String taskId) {
		return this.submissionRepository.findNonEvaluatedByTaskId(taskId)
				.stream()
				.map(c -> this.modelMapper.map(c, SubmissionServiceModel.class))
				.collect(Collectors.toSet());
	}

	@Override
	public void evaluateSubmission(String submissionId, double grade) {
		Submission submission = this.submissionRepository.findById(submissionId)
				.orElseThrow(() -> new SubmissionNotFoundException("Submission was not found!"));
		submission.setGrade(grade);
		submission.setIsGraded(true);
		
		this.submissionRepository.saveAndFlush(submission);
	}
	
}
