package com.siebre.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFiller;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.WebContentGenerator;

@SuppressWarnings("deprecation")
@Controller
public class ReportFormController extends WebContentGenerator{

	private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 51200;

	private boolean DEBUG = false;

	@Autowired
	private DataSource dataSource;

	@RequestMapping(value = "/rpt/{reportName}")
	public void generateReport(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable String reportName) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Enumeration<String> pNames = request.getParameterNames();

		while (pNames.hasMoreElements()) {
			String name = pNames.nextElement();
			String value = request.getParameter(name);
			
			if ("index".equals(name)) {
				int intValue = Integer.parseInt(value);
				if (intValue > 1)
					parameters.put(name, intValue);
			}else {
				parameters.put(name, value);
			}
		}

		String format = (String) parameters.get("format");

		JRExporter exporter = null;

		String contentType = "";
		String fileSuffix = "";

		boolean download = true;

		if ("xls".equalsIgnoreCase(format)) { // xls导出
			contentType = "application/vnd.ms-excel";
			fileSuffix = ".xls";
			exporter = new JRXlsExporter();
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		} else if("pdf".equalsIgnoreCase(format)) { // pdf导出
			contentType = "application/pdf";
			fileSuffix = ".pdf";
			exporter = new JRPdfExporter();
			exporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "UniGB-UCS2-H");
		} else { // html页面显示
			contentType = "text/html;charset=GBK";
			download = false;
			exporter = new JRHtmlExporter();
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		}

		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

		response.setContentType(contentType);

		String template = getServletContext().getRealPath("Report/jrxml/" + reportName);

		String jrxml = template + ".jrxml";
		String jasper = template + ".jasper";

		if (DEBUG || !new File(jasper).exists())
			JasperCompileManager.compileReportToFile(jrxml, jasper);

		Connection conn = dataSource.getConnection();
		
		File sourceFile = new File(jasper);
	    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(sourceFile);
	    JRBaseFiller filler = JRFiller.createFiller(jasperReport);
	    JasperPrint print = filler.fill(parameters, conn);

		//JasperPrint print = JasperFillManager.fillReport(jasper, parameters, conn);

		conn.close();

		if (!download) {
			JasperReportsUtils.render(exporter, print, response.getWriter());
		} else {
			response.setContentType("application/x-download");

			StringBuilder header = new StringBuilder("attachment; filename=\"");
			header.append(URLEncoder.encode(print.getName(), "UTF-8"));
			header.append(parameters.get("startDate"));
			header.append("~");
			header.append(parameters.get("endDate"));
			header.append(fileSuffix);
			header.append("\"");
			response.setHeader("Content-Disposition", header.toString());

			ByteArrayOutputStream baos = new ByteArrayOutputStream(OUTPUT_BYTE_ARRAY_INITIAL_SIZE);
			JasperReportsUtils.render(exporter, print, baos);

			response.setContentLength(baos.size());
			ServletOutputStream out = response.getOutputStream();

			baos.writeTo(out);
			out.flush();

			out.close();
			baos.close();
		}
	}
	
}