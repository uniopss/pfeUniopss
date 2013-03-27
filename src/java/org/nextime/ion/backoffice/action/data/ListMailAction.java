package org.nextime.ion.backoffice.action.data;

import javax.servlet.http.HttpServletRequest;

public class ListMailAction extends BaseDataAction {

	public java.io.File getFile(HttpServletRequest request) {
		return new java.io.File(org.nextime.ion.framework.config.Config
				.getInstance().getLienDirectory(), "mail.xml");
	}

	public String getXml(HttpServletRequest request, String name) {
		return
		// Libelle
		"<l><![CDATA[" + request.getParameter("l_" + name) + "]]></l>"
		// URL
				+ "<u><![CDATA[" + request.getParameter("u_" + name)
				+ "]]></u>";
	}

}