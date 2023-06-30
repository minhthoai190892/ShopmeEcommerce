package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExpoter extends AbstractExporter {
	public void exprot(List<Category> listCategories, HttpServletResponse response) throws IOException {
//		prefix = "users_"
//		(response, "text/csv", ".csv")
		super.setResponseHeader(response, "text/csv", ".csv", "categories_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Category ID", "Category Name"};
		String[] fieldMapping = { "id", "name"};
		csvWriter.writeHeader(csvHeader);
		// ghi dữ liệu vào file
		for (Category category : listCategories) {
			category.setName(category.getName().replace("--", " "));
			csvWriter.write(category, fieldMapping);
		}
		csvWriter.close();
	}
	

}
