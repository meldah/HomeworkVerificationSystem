package com.tusofia.app.homeworkVerification.service.storage;

import java.io.File;
import java.io.IOException;

public interface CloudService extends StorageService {
	
	public class FileCloudData{
		private String url;
		private String publicId;
		
		public FileCloudData(String url, String publicId) {
			this.url = url;
			this.publicId = publicId;
		}
		
		public FileCloudData() {
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPublicId() {
			return publicId;
		}

		public void setPublicId(String publicId) {
			this.publicId = publicId;
		}
	}
	
	FileCloudData uploadFile(String courseName, String exerciseName, String taskName, File file) throws IOException;

	void deleteFile(String publicId) throws IOException;
}
