package com.tusofia.app.homeworkVerification.domain.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateManager {

	public String getCurrentDate() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime currentDateAndTime = LocalDateTime.now();

		return dateTimeFormatter.format(currentDateAndTime);
	}
}
