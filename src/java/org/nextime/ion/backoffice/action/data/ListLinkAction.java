package org.nextime.ion.backoffice.action.data;

import javax.servlet.http.HttpServletRequest;

public class ListLinkAction extends BaseDataAction {

	public java.io.File getFile(HttpServletRequest request) {
		return new java.io.File(org.nextime.ion.framework.config.Config
				.getInstance().getLienDirectory(), "link.xml");
	}

	public String getXml(HttpServletRequest request, String name) {
		return
		// libelle
		"<l><![CDATA["
				+ request.getParameter("l_" + name).replaceAll("'", "")
						.replaceAll("\"", "")
				+ "]]></l>"
				// target
				+ "<t><![CDATA["
				+ request.getParameter("t_" + name)
				+ "]]></t>"
				// menu
				+ "<m><![CDATA["
				+ request.getParameter("m_" + name)
				+ "]]></m>"
				// Redim
				+ "<d><![CDATA["
				+ request.getParameter("d_" + name)
				+ "]]></d>"
				// Height
				+ "<h><![CDATA["
				+ request.getParameter("h_" + name)
				+ "]]></h>"
				// Width
				+ "<w><![CDATA["
				+ request.getParameter("w_" + name)
				+ "]]></w>"
				// URL
				+ "<u><![CDATA["
				+ request.getParameter("u_" + name).replaceAll("'", "")
						.replaceAll("\"", "") + "]]></u>";
	}

}