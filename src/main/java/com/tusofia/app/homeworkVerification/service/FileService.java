package com.tusofia.app.homeworkVerification.service;

import java.io.IOException;

import com.tusofia.app.homeworkVerification.domain.models.service.FileServiceModel;
import com.tusofia.app.homeworkVerification.service.storage.CloudService.FileCloudData;

public interface FileService {
	
	void addFileByCloudData(FileCloudData cloudData);

	FileServiceModel getFileByUrl(String url);

	void deleteFile(String fileId) throws IOException;
}
