package com.widget.CustomWidgetReport.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.widget.CustomWidgetReport.dto.AgentReportRequestDTO;
import com.widget.CustomWidgetReport.dto.AgentReportResponseDTO;
import com.widget.CustomWidgetReport.util.DateRange;
import com.widget.CustomWidgetReport.util.Response;

import lombok.extern.log4j.Log4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Log4j
@RequestMapping("/api/agent")
public class DewaReportController {
	
	
	@Autowired
	private Environment env;

	@Autowired
	private DateRange dateRange;

	@GetMapping("/list/{supportId}")
	public Response<List<String>> getSupportType(@PathVariable String supportId) {
		log.info("supportIds---->" + supportId);
		List<String> supportTypeList = new ArrayList<>();
		supportTypeList.add("All");

		String pom_driver = env.getProperty("spring.datasource.driver-class-name");
		String pom_url = env.getProperty("spring.datasource.url");
		String pom_user = env.getProperty("spring.datasource.username");
		String pom_password = env.getProperty("spring.datasource.password");
		 String Query = env.getProperty("getSupportTypeByRole");
		// String Query = "select support_type as \"SupportType\" from support_types_tbl
		// where support_type in (@query) order by priority;";
		String supportIdMod = supportId.replace(",", "','").trim();
		supportIdMod = "'" + supportIdMod.replaceAll("\\s", "") + "'";
		// supportIdMod.replaceAll(",", "','");
		// log.info(supportIdMod + " supportIdMod test");
		@SuppressWarnings("unused")
		String[] supportIDS = supportIdMod.split(",");
		// log.info(supportIdMod + "test");
		CallableStatement callableStatement = null;
		Connection pom_con = null;
		try {
			Class.forName(pom_driver);
			pom_con = DriverManager.getConnection(pom_url, pom_user, pom_password);
			// log.info(Query.replace("@query", supportIdMod));

			PreparedStatement stmt = pom_con.prepareStatement(Query.replace("@query", supportIdMod));
		//	callableStatement = pom_con.prepareCall("{call surveyTypeByRole");
		//	callableStatement.setString(1, supportIdMod);
			// log.info(stmt + " statement");
	//	ResultSet rs = callableStatement.executeQuery();
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				supportTypeList.add(rs.getString("SupportType"));
			}
			log.info(supportTypeList);
		} catch (Exception e) {
			log.error("stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<List<String>>(500, false, HttpStatus.INTERNAL_SERVER_ERROR,
					"Error while getting supportTypeList").setResponse(null);
		}
		return new Response<List<String>>(200, true, HttpStatus.OK,
				supportTypeList.size() + " SupportTypeList has been retrieved").setResponse(supportTypeList);
	}

	@PostMapping("/agentReport")
	public List<AgentReportResponseDTO> getTjxData(@Valid @RequestBody AgentReportRequestDTO agentRequestDto)
			throws SQLException {

		log.info("agentRequestDto : " + agentRequestDto);
		List<AgentReportResponseDTO> agentRequestDtos = new ArrayList<>();
		String startDate = null;
		String endDate = null;
		String SupportType = agentRequestDto.getType().replaceAll("\\s", "");
		// log.info(SupportType+ "input type");

		String st_edDates = dateRange.getDateRanges(agentRequestDto.getDateRange(), agentRequestDto.getStartDate(),
				agentRequestDto.getEndDate());
		String arr[] = st_edDates.split("###");
		startDate = arr[0];
		endDate = arr[1];

		startDate = startDate.replace("/", "-");
		endDate = endDate.replace("/", "-");
		// log.info("f1");
		log.info("Start Date before calling DB:" + startDate + "end date=> " + endDate);
		// log.info("f2");
		String driver = env.getProperty("spring.datasource.driver-class-name");
		String url = env.getProperty("spring.datasource.url");
		String user = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");

		Connection tjxConnection = null;
		CallableStatement callableStatement = null;
		try {

			Class.forName(driver);
			tjxConnection = DriverManager.getConnection(url, user, password);
			log.info("Connection created successfully for the DB");

			callableStatement = tjxConnection.prepareCall("{ call DEWA_AGENT_REPORT(?,?,?) }");

			callableStatement.setString(1, startDate);
			callableStatement.setString(2, endDate);
			callableStatement.setString(3, SupportType);
			callableStatement.execute();

			ResultSet resultset = callableStatement.getResultSet();

			agentRequestDtos = agentRequestDtos(resultset);

			resultset.close();
		} catch (Exception e) {
			log.error("Error while getting records from database : " + e);
			log.error("stack strace : " + Arrays.toString(e.getStackTrace()));
			return null;
		} finally {
			if (callableStatement != null) {
				callableStatement.close();
			}
			if (tjxConnection != null) {
				tjxConnection.close();
			}
		}
		// log.info("returnnnnnnn" + agentRequestDtos);
		return agentRequestDtos;
	}

	private List<AgentReportResponseDTO> agentRequestDtos(ResultSet resultset) throws SQLException {
		List<AgentReportResponseDTO> agentRequestDtos = new ArrayList<>();
		AgentReportResponseDTO tjxDto = null;
		while (resultset.next()) {

			tjxDto = new AgentReportResponseDTO();
			tjxDto.setName(resultset.getString("NAME"));
			tjxDto.setContact(resultset.getString("CONTACT"));
			tjxDto.setType(resultset.getString("TYPE"));
			tjxDto.setOffered(resultset.getString("OFFERED"));
			tjxDto.setAnswered(resultset.getString("ANSWERED"));
			tjxDto.setAbandoned(resultset.getString("ABANDONED"));
			tjxDto.setPerAnswered(resultset.getString("PER_ANSWERED") + "%");
			agentRequestDtos.add(tjxDto);
			
		}
		return agentRequestDtos;
	}

	@PostMapping("/agentReport/excel")
	public HttpEntity<ByteArrayResource> downloadExcel(@Valid @RequestBody AgentReportRequestDTO agentReportRequestDto)
			throws IOException, SQLException {
		List<AgentReportResponseDTO> aReportResponseDTOs = new ArrayList<>();
		byte[] buf = null;

		HttpHeaders headers = new HttpHeaders();
		try {
			String startDate = null;
			String endDate = null;
			String SupportType = agentReportRequestDto.getType().replaceAll("\\s", "");

			String st_edDates = dateRange.getDateRanges(agentReportRequestDto.getDateRange(),
					agentReportRequestDto.getStartDate(), agentReportRequestDto.getEndDate());
			String arr[] = st_edDates.split("###");
			startDate = arr[0];
			endDate = arr[1];

			startDate = startDate.replace("/", "-");
			endDate = endDate.replace("/", "-");

			log.info("Start Date before calling DB:" + startDate);
			log.info("End Date before calling DB:" + endDate);

			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");

			Connection tjxConnection = null;
			CallableStatement callableStatement = null;
			try {

				Class.forName(driver);
				tjxConnection = DriverManager.getConnection(url, user, password);

				callableStatement = tjxConnection.prepareCall("{ call DEWA_AGENT_REPORT(?,?,?) }");

				callableStatement.setString(1, startDate);
				callableStatement.setString(2, endDate);
				callableStatement.setString(3, SupportType);
				callableStatement.execute();

				ResultSet resultset = callableStatement.getResultSet();
				aReportResponseDTOs = agentRequestDtos(resultset);

				resultset.close();
			} catch (Exception e) {
				log.error("Error while getting records from database in excel : " + e);
				log.error("stack strace : " + Arrays.toString(e.getStackTrace()));
				return null;
			} finally {
				if (callableStatement != null) {
					callableStatement.close();
				}
				if (tjxConnection != null) {
					tjxConnection.close();
				}
			}

			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				XSSFSheet sheet = workbook.createSheet("IT EMERGENCY REPORT");

				CellStyle style = workbook.createCellStyle();
				Font font = workbook.createFont();// Create font font.setBold(true); style.setFont(font);
				style.setAlignment(HorizontalAlignment.CENTER);

				Row headerRow = sheet.createRow(0);
				Cell headerCell = headerRow.createCell(0);
				headerCell.setCellValue("" + "");
				headerCell.setCellStyle(style);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

				Row headerRow1 = sheet.createRow(1);
				Cell headerCell1 = headerRow1.createCell(0);
				headerCell1.setCellValue("From date : " + startDate + "  To date : " + endDate);
				headerCell1.setCellStyle(style);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));

				Row columnRow = sheet.createRow(3);
				Cell headerCells = columnRow.createCell(0);
				headerCells.setCellValue("Name");
				sheet.setColumnWidth(0, 5000);
				headerCells.setCellStyle(style);

				Cell headerCellh12 = columnRow.createCell(1);
				headerCellh12.setCellValue("Contact");
				sheet.setColumnWidth(1, 5000);
				headerCellh12.setCellStyle(style);

				Cell headerCellh1 = columnRow.createCell(2);
				headerCellh1.setCellValue("Support Type");
				sheet.setColumnWidth(2, 5000);
				headerCellh1.setCellStyle(style);

				Cell headerCellh2 = columnRow.createCell(3);
				headerCellh2.setCellValue("Offered");
				sheet.setColumnWidth(3, 5000);
				headerCellh2.setCellStyle(style);

				Cell headerCellh3 = columnRow.createCell(4);
				headerCellh3.setCellValue("Answered");
				sheet.setColumnWidth(4, 5000);
				headerCellh3.setCellStyle(style);

				Cell headerCells1 = columnRow.createCell(5);
				headerCells1.setCellValue("Abandoned");
				sheet.setColumnWidth(5, 5000);
				headerCells1.setCellStyle(style);

				Cell headerCells2 = columnRow.createCell(6);
				headerCells2.setCellValue("% Answered");
				sheet.setColumnWidth(6, 5000);
				headerCells2.setCellStyle(style);

				int rowCount = 4;
				Row row = sheet.createRow(rowCount++);

				int j = 4;
				for (int i = 0; i < aReportResponseDTOs.size(); i++) {
					row = sheet.createRow(j++);

					Cell cell = row.createCell(0);
					cell.setCellValue(aReportResponseDTOs.get(i).getName());

					Cell cell0 = row.createCell(1);
					cell0.setCellValue(aReportResponseDTOs.get(i).getContact());

					Cell cell1 = row.createCell(2);
					cell1.setCellValue(aReportResponseDTOs.get(i).getType());

					Cell cell2 = row.createCell(3);
					cell2.setCellValue(aReportResponseDTOs.get(i).getOffered());

					Cell cell3 = row.createCell(4);
					cell3.setCellValue(aReportResponseDTOs.get(i).getAnswered());

					Cell cell4 = row.createCell(5);
					cell4.setCellValue(aReportResponseDTOs.get(i).getAbandoned());

					Cell cell5 = row.createCell(6);
					cell5.setCellValue(aReportResponseDTOs.get(i).getPerAnswered());

				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					workbook.write(baos);
				} finally {
					baos.close();
				}

				buf = baos.toByteArray();
			}
			headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "force-download"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=my_file.xlsx");
		} catch (Exception e) {
			log.error("Error while downloading excel file TJX report : " + e);
			log.error("stack trace : " + Arrays.toString(e.getStackTrace()));
			return null;
		}

		return new HttpEntity<>(new ByteArrayResource(buf), headers);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })

	@PostMapping("/agentReport/pdf")
	public HttpEntity<ByteArrayResource> downloadPdf(@Valid @RequestBody AgentReportRequestDTO agentReportRequestDto) {

		Document document = new Document(PageSize.A2, 10f, 10f, 10f, 0f);

		byte[] buf = null;
		List<List<String>> arrlist = new ArrayList<>();
		List<String> list = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		try {
			String startDate = null;
			String endDate = null;
			String SupportType = agentReportRequestDto.getType().replaceAll("\\s", "");

			String st_edDates = dateRange.getDateRanges(agentReportRequestDto.getDateRange(),
					agentReportRequestDto.getStartDate(), agentReportRequestDto.getEndDate());
			String arr[] = st_edDates.split("###");
			startDate = arr[0];
			endDate = arr[1];

			startDate = startDate.replace("/", "-");
			endDate = endDate.replace("/", "-");

			log.info("Start Date before calling DB:" + startDate);
			log.info("End Date before calling DB:" + endDate);

			String driver = env.getProperty("spring.ctsreport.datasource.driver-class-name");
			String url = env.getProperty("spring.ctsreport.datasource.url");
			String user = env.getProperty("spring.ctsreport.datasource.username");
			String password = env.getProperty("spring.ctsreport.datasource.password");

			Connection tjxConnection = null;
			CallableStatement callableStatement = null;
			try {

				Class.forName(driver);
				tjxConnection = DriverManager.getConnection(url, user, password);

				callableStatement = tjxConnection.prepareCall("{ call DEWA_AGENT_REPORT(?,?,?) }");

				callableStatement.setString(1, startDate);
				callableStatement.setString(2, endDate);
				callableStatement.setString(3, SupportType);
				callableStatement.execute();

				ResultSet resultset = callableStatement.getResultSet();
				while (resultset.next()) {

					list = new ArrayList<>();
					String name = resultset.getString("NAME");
					String conatct = resultset.getString("CONTACT");
					String type = resultset.getString("TYPE");
					String offered = resultset.getString("OFFERED");
					String answered = resultset.getString("ANSWERED");
					String abandoned = resultset.getString("ABANDONED");
					String perAnswered = resultset.getString("PER_ANSWERED");

					list.add(name);
					list.add(conatct);
					list.add(type);
					list.add(offered);
					list.add(answered);
					list.add(abandoned);
					list.add(perAnswered);
					arrlist.add(list);

				}

				resultset.close();
			} catch (Exception e) {
				log.error("Error while getting records from database in excel : " + e);
				log.error("stack strace : " + Arrays.toString(e.getStackTrace()));
				return null;
			} finally {
				if (callableStatement != null) {
					callableStatement.close();
				}
				if (tjxConnection != null) {
					tjxConnection.close();
				}
			}

			com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.COURIER, 7);
			titleFont.setColor(BaseColor.WHITE);
			com.itextpdf.text.Font bodyFont = FontFactory.getFont(FontFactory.COURIER, 6);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			@SuppressWarnings("unused")
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();
			PdfPTable table = new PdfPTable(7); // 11 columns. table.setWidthPercentage(100); // Width 100%
			float[] columnWidths = { 4f, 4f, 4f, 4f, 4f, 4f, 4f };
			table.setWidths(columnWidths);

			PdfPCell header = new PdfPCell(new Paragraph("IT EMERGENCY REPORT"));
			header.setPaddingLeft(20);
			header.setColspan(20);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(header);

			PdfPCell fromToDate = new PdfPCell(new Paragraph("Start Date: " + startDate + " End Date: " + endDate));
			fromToDate.setPaddingLeft(20);
			fromToDate.setColspan(20);
			fromToDate.setHorizontalAlignment(Element.ALIGN_CENTER);
			fromToDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(fromToDate);

			PdfPCell cell = new PdfPCell(new Paragraph("Name", titleFont));
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Contact", titleFont));
			cell1.setBackgroundColor(BaseColor.DARK_GRAY);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(new Paragraph("Support Type", titleFont));
			cell2.setBackgroundColor(BaseColor.DARK_GRAY);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell2);

			PdfPCell cell3 = new PdfPCell(new Paragraph("Offered", titleFont));
			cell3.setBackgroundColor(BaseColor.DARK_GRAY);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell3);

			PdfPCell cell4 = new PdfPCell(new Paragraph("Answered", titleFont));
			cell4.setBackgroundColor(BaseColor.DARK_GRAY);
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell4);

			PdfPCell cell5 = new PdfPCell(new Paragraph("Abandoned", titleFont));
			cell5.setBackgroundColor(BaseColor.DARK_GRAY);
			cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell5);

			PdfPCell cell6 = new PdfPCell(new Paragraph("% Answered", titleFont));
			cell6.setBackgroundColor(BaseColor.DARK_GRAY);
			cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell6);

			for (int i = 0; i < arrlist.size(); i++) {
				list = (List) arrlist.get(i);
				for (int j = 0; j < list.size(); j++) {
					PdfPCell body = new PdfPCell(new Paragraph((String) list.get(j), bodyFont));
					body.setPaddingLeft(10);
					body.setHorizontalAlignment(Element.ALIGN_CENTER);
					body.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(body);
				}
			}

			document.add(table);
			document.close();

			buf = baos.toByteArray();

			headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "force-download"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=my_file.pdf");

		} catch (Exception e) {
			log.error("Error while downloading pdf file TJX report : " + e);
			log.error("stack trace : " + Arrays.toString(e.getStackTrace()));
			return null;
		}
		return new HttpEntity<>(new ByteArrayResource(buf), headers);
	}

}
