package org.nextime.ion.backoffice.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;

public class Resources {

	private static ResourceXmlBean bean;

	public static ResourceXmlBean getResourceXmlBean(HttpServlet servlet,
			String id) throws Exception {
		if (bean == null) {
			FileInputStream fis = new FileInputStream(new File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "resources.xml"));
			bean = ResourceXmlBean.parse(fis);
			fis.close();
		}
		return ResourceXmlBean.getResource(id);
	}

	public static ResourceXmlBean getResourceXmlBean(ServletContext context,
			String id) throws Exception {
		if (bean == null) {
			FileInputStream fis = new FileInputStream(new File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "resources.xml"));
			bean = ResourceXmlBean.parse(fis);
			fis.close();
		}
		return ResourceXmlBean.getResource(id);
	}

	public static Vector getResourceXmlBeans() throws Exception {
		if (bean == null) {
			FileInputStream fis = new FileInputStream(new File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "resources.xml"));
			bean = ResourceXmlBean.parse(fis);
			fis.close();
		}
		return ResourceXmlBean.getItems();
	}

}

