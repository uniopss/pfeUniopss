package org.nextime.ion.backoffice.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;

public class SectionTypes {

	private static TypeBean bean;

	private static TypeBean getLocalizedTypeBean(HttpServlet servlet,
			HttpServletRequest request) throws Exception {

		FileInputStream fis = new FileInputStream(new File(
				org.nextime.ion.framework.config.Config.getInstance()
						.getConfigDirectoryPath(), "templates.xml"));
		bean = TypeBean.parse(fis);
		fis.close();

		return bean;
	}

	private static TypeBean getLocalizedTypeBean(ServletContext c)
			throws Exception {

		FileInputStream fis = new FileInputStream(new File(
				org.nextime.ion.framework.config.Config.getInstance()
						.getConfigDirectoryPath(), "templates.xml"));
		bean = TypeBean.parse(fis);
		fis.close();

		return bean;
	}

	public static TypeBean getSectionBean(HttpServlet servlet, String template,
			HttpServletRequest request) throws Exception {
		bean = getLocalizedTypeBean(servlet, request);
		return TypeBean.getBean(template);
	}

	public static Vector getSectionsBeans(HttpServlet servlet,
			HttpServletRequest request) throws Exception {
		bean = getLocalizedTypeBean(servlet, request);
		return bean.getItems();
	}

	public static TypeBean getSectionBean(ServletContext c, String template)
			throws Exception {
		bean = getLocalizedTypeBean(c);
		return TypeBean.getBean(template);
	}
}

