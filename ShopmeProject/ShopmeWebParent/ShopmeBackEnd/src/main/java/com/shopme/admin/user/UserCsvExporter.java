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

public class UserCsvExporter extends AbstractExporter {
	/**
	 * Hàm export CSV
	 * 
	 * @param listUsers nhận một chuổi
	 * @param response
	 * @throws IOException
	 */
	public void exprot(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "User Id", "E-mail", "First Name", "Last Name", "Roles", "Enable" };
		String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enable" };
		csvWriter.writeHeader(csvHeader);
		// ghi dữ liệu vào file
		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		csvWriter.close();
	}
}
