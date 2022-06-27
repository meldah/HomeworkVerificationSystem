package com.tusofia.app.homeworkVerification.service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.File;
import com.tusofia.app.homeworkVerification.domain.models.service.FileServiceModel;
import com.tusofia.app.homeworkVerification.error.FileNotFoundException;
import com.tusofia.app.homeworkVerification.repository.FileRepository;
import com.tusofia.app.homeworkVerification.service.storage.CloudService;
import com.tusofia.app.homeworkVerification.service.storage.CloudService.FileCloudData;

@Service
public class FileServiceImpl implements FileService {
	
	private FileRepository fileRepository;
	private final ModelMapper modelMapper;
	private final CloudService cloudService;
	
	@Autowired
	public FileServiceImpl(FileRepository fileRepository, ModelMapper modelMapper, CloudService cloudService) {
		this.fileRepository = fileRepository;
		this.modelMapper = modelMapper;
		this.cloudService = cloudService;
	}

	@Override
	public void addFileByCloudData(FileCloudData cloudData) {
		File file = new File();
		file.setUrl(cloudData.getUrl());
		file.setPublicId(cloudData.getPublicId());
		
		this.fileRepository.saveAndFlush(file);
	}

	@Override
	public FileServiceModel getFileByUrl(String url) {
		
		File file = this.fileRepository.findByUrl(url)
				.orElseThrow(() -> new FileNotFoundException("File at link: " + url + " was not found!"));
		
		return this.modelMapper.map(file, FileServiceModel.class);
	}

	@Override
	public void deleteFile(String fileId) throws IOException {
		File file = this.fileRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException("File was not found!"));
		this.cloudService.deleteFile(file.getPublicId());
	}
	
}
