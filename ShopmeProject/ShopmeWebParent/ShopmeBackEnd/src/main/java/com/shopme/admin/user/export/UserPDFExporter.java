package com.shopme.admin.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserPDFExporter extends AbstractExporter {

	public void exprot(List<User> lisUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf");
		// khởi tạo đối tượng chọn khổ giấy A4
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		// font chữ
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		// cỡ chữ
		font.setSize(18);
		font.setColor(Color.BLUE);
		// title page và font chữ
		Paragraph paragraph = new Paragraph("List of User", font);
		// canh giữa
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		// tạo bản
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		//tạo độ rộng của cột
		table.setWidths(new float[] {1.2f,3.5f,3.0f,3.0f,3.0f,1.7f});
		
		// tạo header
		writeTableHeader(table);
		// đưa dữ liệu vào table
		writeTableData(table,lisUsers);
		document.add(table);
		document.close();
	}

	/**
	 * Hàm tạo bảng và ghi dữ liệu
	 * @param table nhận một bảng
	 * @param lisUsers nhận 1 danh sách user
	 */
	private void writeTableData(PdfPTable table, List<User> lisUsers) {
		for (User user : lisUsers) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstName());
			table.addCell(user.getLastName());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnable()));
		}
		
	}

	/**
	 * Hàm tạo header của bảng
	 * @param table nhận một bảng
	 */
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		// tạo màu nền cho header
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		// font chữ
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		//tạo cột header
		cell.setPhrase(new Phrase("ID",font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail",font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("First Name",font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Last Name",font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Roles",font));
		table.addCell(cell);
		

		cell.setPhrase(new Phrase("Enable",font));
		table.addCell(cell);
		
	}

}
