package com.tusofia.app.homeworkVerification.domain.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.springframework.web.multipart.MultipartFile;

public class FileManager {
	
	public static class FileContent {
		
		private String name;
		private String extension;
		private long length;
		
		public FileContent() {
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getExtension() {
			return extension;
		}
		public void setExtension(String extension) {
			this.extension = extension;
		}
		public long getLength() {
			return length;
		}
		public void setLength(long length) {
			this.length = length;
		}
	}
	
	public static File createFileFrom(MultipartFile multipartFile) throws IOException {
		 	File convFile = new File( multipartFile.getOriginalFilename() );
	        FileOutputStream fos = new FileOutputStream( convFile );
	        fos.write( multipartFile.getBytes() );
	        fos.close();
	        return convFile;
	}
	
	public static int getIndexOfExtensionSeparator(String fileName) {
		return fileName.lastIndexOf(".");
	}
	
	public static List<FileContent> getZipFileContent(File file) throws IOException {
		
		ZipFile zipFile = new ZipFile(file);
		Enumeration<?> enu = zipFile.entries();
		List<FileContent> contentFiles = new ArrayList<FileContent>();
		
		while (enu.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) enu.nextElement();
			FileContent fileContent = new FileContent();
			String zipEntryName = zipEntry.getName();
			
			if(zipEntry.isDirectory()) {
				fileContent.setName(zipEntryName.substring(0, zipEntryName.length()-1));
				fileContent.setExtension("/");
			}
			else {
				int indexOfSeparator = getIndexOfExtensionSeparator(zipEntryName);
				fileContent.setName(zipEntryName.substring(0, indexOfSeparator));
				fileContent.setExtension(zipEntryName.substring(indexOfSeparator, zipEntryName.length()));
			}
			fileContent.setLength(zipEntry.getSize());
			contentFiles.add(fileContent);
		}
		zipFile.close();
		
		return contentFiles;
	}

	public static List<FileContent> get7ZFileContent(File file) throws IOException {
		List<FileContent> fileContents = new ArrayList<FileContent>();

		SevenZFile sevenZFile = new SevenZFile(file);
	    SevenZArchiveEntry entry = sevenZFile.getNextEntry();
		while (entry != null) {
			FileContent fileContent = new FileContent();
			String entryName = entry.getName();
			
			if (entry.isDirectory()) {
				fileContent.setName(entry.getName());
				fileContent.setExtension("/");
			} else {
				int indexOfSeparator = getIndexOfExtensionSeparator(entryName);
				fileContent.setName(entryName.substring(0, indexOfSeparator));
				fileContent.setExtension(entryName.substring(indexOfSeparator, entryName.length()));
			}
			fileContent.setLength(entry.getSize());
			fileContents.add(fileContent);
			
			entry = sevenZFile.getNextEntry();
		}
	    sevenZFile.close();
	    return fileContents;
	}
}
