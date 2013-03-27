package org.nextime.ion.backoffice.action.data;

import javax.servlet.http.HttpServletRequest;

public class ListCTAction extends BaseDataProduitAction {

	public String getType() {
		return "caracteristique";
	}

	public String getXml(HttpServletRequest request, String name) {
		return "<i><![CDATA[" + request.getParameter("i_" + name) + "]]></i>"
				+ "<c><![CDATA[" + request.getParameter("c_" + name)
				+ "]]></c>" + "<l><![CDATA["
				+ request.getParameter("l_" + name) + "]]></l>";
	}

}