package com.rabbit.jfinal.upload;

import com.oreilly.servlet.multipart.FileRenamePolicy;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

public class RenamePolicyCos implements FileRenamePolicy {

	public RenamePolicyCos() {
	}

	public File rename(File file) {
		file = new File(file.getParent(), getNewFileName(file.getName()));
		return file;
	}

	public String getNewFileName(String fileName) {
		String fType = getFiletype(fileName);
		String ffrontname = getFilefrontname(fileName);
		String newFileName = (new StringBuilder(String.valueOf(ffrontname))).append(".")
				.append(fType).toString();
		return newFileName;
	}

	public static String getFiletype(String fileName) {
		String type = "";
		if (fileName == null || fileName.equals(""))
			return type;
		int position = fileName.lastIndexOf(".");
		if (position != -1)
			type = fileName.substring(position + 1, fileName.length());
		return type.toLowerCase();
	}

	public static String getFilefrontname(String fileName) {
		Date dt = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(dt)+RandomStringUtils.randomAlphanumeric(12);
	}
}
