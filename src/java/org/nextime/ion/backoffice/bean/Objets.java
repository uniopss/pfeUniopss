package org.nextime.ion.backoffice.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

public class Objets {

	private static ObjetXmlBean bean;

	public static ObjetXmlBean getObjetXmlBean(String id) throws Exception {
		if (bean == null) {
			FileInputStream fis = new FileInputStream(new File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "objets.xml"));
			bean = ObjetXmlBean.parse(fis);
			fis.close();
		}
		return ObjetXmlBean.getObjet(id);
	}

	public static Vector getObjetXmlBeans() throws Exception {
		if (bean == null) {
			FileInputStream fis = new FileInputStream(new File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "objets.xml"));
			bean = ObjetXmlBean.parse(fis);
			fis.close();
		}
		return ObjetXmlBean.getItems();
	}
}

