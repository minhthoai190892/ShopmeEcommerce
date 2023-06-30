package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {
	public void setResponseHeader( HttpServletResponse response,String contentType,String extension,String prefix) throws IOException {
		// định dạng kểu ngày
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormat.format(new Date());
		// tên file khi xuất file
		String fileName = prefix + timestamp + extension;
		response.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
}
