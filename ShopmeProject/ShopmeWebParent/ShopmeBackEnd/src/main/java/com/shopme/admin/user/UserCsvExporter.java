package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter {
	/**
	 * Hàm export CSV
	 * @param listUsers nhận một chuổi
	 * @param response 
	 * @throws IOException
	 */
	public void exprot(List<User> listUsers, HttpServletResponse response) throws IOException {
		//định dạng kểu ngày
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormat.format(new Date());
		//tên file khi xuất file
		String fileName = "users_" + timestamp + ".csv";
		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = { "User Id", "E-mail", "First Name", "Last Name", "Roles", "Enable" };
		String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enable" };
		csvWriter.writeHeader(csvHeader);
		//ghi dữ liệu vào file
		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		csvWriter.close();
	}
}
