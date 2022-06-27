package com.tusofia.app.homeworkVerification.service.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tusofia.app.homeworkVerification.domain.utils.LanguageManager;

@Service
public class CloudServiceImpl implements CloudService {

	private final Cloudinary cloudinary;
	private final LanguageManager languageManager;

    @Autowired
    public CloudServiceImpl(Cloudinary cloudinary, LanguageManager languageManager) {
        this.cloudinary = cloudinary;
        this.languageManager = languageManager;
    }
	
	@Override
	public FileCloudData uploadFile(String courseName, String exerciseName, String taskName, File file)
			throws IOException{
		FileCloudData cloudData = new FileCloudData();
		String publicId = languageManager.transliterate(courseName) + "/" + languageManager.transliterate(exerciseName)
				+ "/" + languageManager.transliterate(taskName) + "/" + file.getName();
		
		cloudData.setPublicId(publicId);
		
		cloudData.setUrl(this.cloudinary.uploader()
				.upload(file, ObjectUtils.asMap("public_id", publicId, "resource_type", "raw"))
				.get("url").toString());
		
		return cloudData;
	}

	@Override
	public void deleteFile(String publicId) throws IOException {
		this.cloudinary.uploader()
		.destroy(publicId, ObjectUtils.
        		asMap("resource_type", "raw"));
	}
}
