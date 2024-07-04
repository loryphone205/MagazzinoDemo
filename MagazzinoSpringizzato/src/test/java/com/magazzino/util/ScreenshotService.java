package com.magazzino.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotService {
	public void captureScreenShot(WebDriver driver, Class clazz) {
		String path = getFileDirectoryAndName(clazz);
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File(path));
		} catch (IOException e) {
		}
	}

	private String getFileDirectoryAndName(Class clazz) {
		String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
		String testClassName = clazz.getName();
		String directory = "target/screenshot/" + testClassName + "/" + methodName + "/";
		String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
		return directory + fileName + ".png";
	}
}
