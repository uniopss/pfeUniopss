package org.nextime.ion.backoffice.action.data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.*;
import java.util.*;

import org.apache.struts.upload.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.form.UploadResourceForm;
import org.nextime.ion.framework.logger.Logger;

public class XlsManagementAction extends BaseDataAction {

	public String[] excel;

	public String type;

	public void prepare(HttpServletRequest request) {
		if (request.getParameter("type") != null
				&& !"".equals(request.getParameter("type"))) {
			type = request.getParameter("type");
			request.setAttribute("type", type);
			excel = request.getParameter("col").split(",");
		}
		String content = "";
		try {
			File f = new File(org.nextime.ion.framework.config.Config
					.getInstance().getConfigDirectoryPath(), "xlsbase.xml");
			// lit le contenu du fichier
			content = new String(org.nextime.ion.commons.Util.read(f));
			if (content == null || "".equals(content.trim()))
				content = "<lc/>";

		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), XlsManagementAction.class,e);
			content = "<lc/>";
		}
		request.setAttribute("base", content);
	}

	public java.io.File getFile(HttpServletRequest request) {
		java.io.File resources = org.nextime.ion.framework.config.Config
				.getInstance().getResourcesDirectory();
		return new java.io.File(resources, "/common/xml/" + type + "/data.xml");
	}

	public String getXml(HttpServletRequest request, String name) {
		return "" + request.getParameter(name);
	}

	private String readExcel(HttpServletRequest request, FormFile f) {
		try {
			StringBuffer lc = new StringBuffer();
			POIFSFileSystem fs = new POIFSFileSystem(f.getInputStream());
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0); // Feuille 1
			Iterator rows = sheet.rowIterator();
			int j = -1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				if (row.getRowNum() != 0) {
					if (!"".equals(readExcel(row, 0))) {
						j++;
						lc.append("<r ordre='" + j + "'>" + readExcel(row)
								+ "</r>\n");
					} else
						break;
				}
			}
			return lc.toString();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), XlsManagementAction.class,e);

		}
		return "";
	}

	private String readExcel(HSSFRow row) {
		try {
			String e = "";
			for (int i = 0; i < excel.length; i++)
				e += "<" + excel[i] + "><![CDATA[" + readExcel(row, i)
						+ "]]></" + excel[i] + ">";
			return e;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), XlsManagementAction.class,e);

		}
		return "";
	}

	private String readExcel(HSSFRow row, int c) {
		try {
			HSSFCell cell = row.getCell((short) c);
			if (cell == null)
				return "";
			if ((cell).getCellType() == 1)
				return ((cell).getStringCellValue()).trim();
			if ((cell).getCellType() == 0)
				return "" + (int) (cell).getNumericCellValue();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), XlsManagementAction.class,e);
		}
		return "";
	}

	public void execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String laction = request.getParameter("laction");
		String sXml = "";
		if ("n".equals(laction)) {
			try {
				UploadResourceForm sform = (UploadResourceForm) form;
				sXml = readExcel(request, sform.getFile());
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), XlsManagementAction.class,e);
			}
			//<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>
			request.setAttribute("content", "<lc>" + sXml + "</lc>");
		} else if ("t".equals(laction)) {
			sXml = getXml(request, "xml");
			try {
				java.io.File f = getFile(request);
				java.io.PrintStream os = new java.io.PrintStream(
						new java.io.FileOutputStream(f));
				os.println(sXml);
				os.close();

			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), XlsManagementAction.class,e);
			}
			request.setAttribute("content", sXml);
		}
	}

}