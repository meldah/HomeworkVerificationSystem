package com.tusofia.app.homeworkVerification.domain.checkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.utils.FileManager;
import com.tusofia.app.homeworkVerification.domain.utils.FileManager.FileContent;
import com.tusofia.app.homeworkVerification.domain.utils.constants.XMLConstants;

@Component
public class HomeworkChecker {
	
	public static final String SUCCESS_MESSAGE = "Homework uploaded successfully! No errors have been found!";
	public static final String ERROR_MESSAGE = "Upload unsuccessful!";
	
	private static final String ZIP_FILE = ".zip";
	private static final String SEVENZ_FILE = ".7z";
	
	private final static int FIRST_NAME_INDEX = 0;
	private final static int LAST_NAME_INDEX = 1;
	private final static int FACULTY_NUMBER_INDEX = 2;
	private final static int GROUP_INDEX = 3;
	private final static int FILE_NAME_TOKENS_SIZE = 4;
	
	//ERROR MESSAGES
	private static final String FILE_EMPTY_SUFFIX = " file is empty!";
	private static final String FILE_IS_BIGGER_MESSAGE = " file is bigger than wanted size!";
	
	private static final String WANTED_FILE_NOT_FOUND_MESSAGE = "Wanted file not found!";
	
	private final static String FILE_EXTENSION_EROR = ERROR_MESSAGE + " File's extension doesn't match wanted extension!";
	private final static String FILE_NAME_EROR = ERROR_MESSAGE + " File name doesn't match student's credentials!";
	private static final String NO_FILES_MESSAGE = ERROR_MESSAGE + " Archive containtains no files!";
	private static final String WRONG_CONDITIONS_FILE_TEXT = "Conditions file could not be read!";

	private static final int SIZE_MULTIPLICATOR = 1024;
	
	private static final String FOLDER_EXTENSION = "/";
	
	public class SubmissionResult{
		public boolean isSuccessful;
		public String message;
		
		public SubmissionResult(){
			isSuccessful = true;
			message = null;
		}
	}
	
	public SubmissionResult checkSubmission(MultipartFile submission, File contentChecks, String extension, Student student)
			throws IOException {
		SubmissionResult result = new SubmissionResult();

		result.isSuccessful = checkIfFileExtensionMatchesWanted(submission.getOriginalFilename(), extension);
		if (!result.isSuccessful) {
			result.message = FILE_EXTENSION_EROR;
			return result;
		}

		result.isSuccessful = checkIfFileNameMatchStudentCredentials(submission.getOriginalFilename(), student);
		if (!result.isSuccessful) {
			result.message = FILE_NAME_EROR;
			return result;
		}

		List<FileContent> files = new ArrayList<FileContent>();
		if (extension.equals(ZIP_FILE)) {
			files = getZipFileContent(submission);
		} else if (extension.equals(SEVENZ_FILE)) {
			files = get7ZFileContent(submission);
		}

		result = doSpecificFileChecks(files, contentChecks);

		if (result.isSuccessful) {
			result.message = SUCCESS_MESSAGE;
		}

		return result;
	}
	
	private boolean checkIfFileNameMatchStudentCredentials(String fileName, Student student) {
		String fileNameWithoutExtension = fileName.substring(0, FileManager.getIndexOfExtensionSeparator(fileName));
		String nameTokens[] = fileNameWithoutExtension.split("_");

		if ((nameTokens.length == FILE_NAME_TOKENS_SIZE) 
				&& nameTokens[FIRST_NAME_INDEX].toLowerCase().equals(student.getFirstName().toLowerCase())
				&& nameTokens[LAST_NAME_INDEX].toLowerCase().equals(student.getLastName().toLowerCase())
				&& nameTokens[FACULTY_NUMBER_INDEX].toLowerCase().equals(student.getFacultyNumber().toLowerCase())
				&& nameTokens[GROUP_INDEX].equals(String.valueOf(student.getGroup()))) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkIfFileExtensionMatchesWanted(String fileName, String wantedExtension) {
		String fileExtension = fileName.substring(FileManager.getIndexOfExtensionSeparator(fileName), fileName.length());
		return fileExtension.equals(wantedExtension);
	}
	
	private List<FileContent> get7ZFileContent(MultipartFile submission) throws IOException {
		File file = FileManager.createFileFrom(submission);
		List<FileContent> sevenZFileContent = FileManager.get7ZFileContent(file);
		//file.delete();
		return sevenZFileContent;
	}

	private List<FileContent> getZipFileContent(MultipartFile submission) throws IOException {
		
		File file = FileManager.createFileFrom(submission);
		List<FileContent> zipFileContent = FileManager.getZipFileContent(file);
		//file.delete();
		return zipFileContent;
	}
	
	private SubmissionResult checkFileContent(FileContent fileContent, String wantedExtension, long wantedMaxFileSize) {
		SubmissionResult res = new SubmissionResult();
		res.isSuccessful = fileContent.getExtension().equals(wantedExtension);
		if(!res.isSuccessful) {
			res.message = fileContent.getName() + " has wrong extension!";
			return res;
		}
		if (!fileContent.getExtension().equals(FOLDER_EXTENSION)) {
			res.isSuccessful = fileContent.getLength() > 0;
			if (!res.isSuccessful) {
				res.message = fileContent.getName() + FILE_EMPTY_SUFFIX;
				
			} else {
				res.isSuccessful = fileContent.getLength() <= wantedMaxFileSize;
				if(!res.isSuccessful) {
					res.message = fileContent.getName() + FILE_IS_BIGGER_MESSAGE;
				}
			}
		}
		return res;
	}
	
	private SubmissionResult doSpecificFileChecks(List<FileContent> contentFiles, File conditionChecksFile) {
		SubmissionResult result = new SubmissionResult();
		
		try {
			if(contentFiles.isEmpty()) {
				result.isSuccessful = false;
				result.message = NO_FILES_MESSAGE;
				return result;
			}
			
			List<Element> fileConditions = getFileConditionsFromXML(conditionChecksFile);
			long wantedFilesCount = fileConditions.size();
			
			result.isSuccessful = false;
			result.message = WANTED_FILE_NOT_FOUND_MESSAGE;
			long wantedFilesFound = 0;
			
			for (FileContent fileContent : contentFiles) {
				Element fileConditionDataElement = getFileConditionData(fileConditions, fileContent.getName());
				String wantedExtension = "";
				long wantedMaxFileSize = 0;
				
				if ((fileConditionDataElement != null)) {
					wantedFilesFound++;
					wantedExtension = getWantedFileExtension(fileConditionDataElement);
					wantedMaxFileSize = getSizeInBytes(getWantedMaximumFileSizeInMbs(fileConditionDataElement));
				}
				else {
					continue;
				}
				
				result = checkFileContent(fileContent, wantedExtension, wantedMaxFileSize);
				if (!result.isSuccessful || (wantedFilesFound == wantedFilesCount)) {
					return result;
				}
			};
			
			if(wantedFilesFound < wantedFilesCount) {
				result.isSuccessful = false;
				result.message = WANTED_FILE_NOT_FOUND_MESSAGE;
				return result;
			}
			
		} catch (JDOMException | IOException e) {
			result.isSuccessful = false;
			result.message = WRONG_CONDITIONS_FILE_TEXT;
		}
		
		return result;
	}
	
	private long getSizeInBytes(long sizeInMbs) {
		return SIZE_MULTIPLICATOR * SIZE_MULTIPLICATOR * sizeInMbs;
	}
	
	private long getWantedMaximumFileSizeInMbs(Element fileConditionDataElement) {
		Element fileMaxSizeElement = fileConditionDataElement.getChild(XMLConstants.MAXSIZE_TAG_NAME);
		return Long.parseLong(fileMaxSizeElement.getText());
	}
	
	private String getWantedFileExtension(Element fileConditionDataElement) {
		Element fileExtensionElement = fileConditionDataElement.getChild(XMLConstants.EXTENSION_TAG_NAME);
		return fileExtensionElement.getText();
	}
	
	private Element getFileConditionData(List<Element> fileConditions, String wantedFileName) {
		for(Element fileElement: fileConditions) {
			String fileNameText = fileElement.getChildText(XMLConstants.NAME_TAG_NAME);
			if((fileNameText != null) && (fileNameText.equals(wantedFileName))) {
				return fileElement;
			}
		}
		
		return null;
	}
	
	private List<Element> getFileConditionsFromXML(File file) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(file);
		Element condition = doc.getRootElement();
		Element files = condition.getChild(XMLConstants.FILES_TAG_NAME);
		
		List<Element> titles = files.getChildren(XMLConstants.FILE_TAG_NAME);
		
		return titles;
	}
}