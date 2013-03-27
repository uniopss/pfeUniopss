package org.nextime.ion.backoffice.action.search;

import javax.servlet.http.HttpServletRequest;

import org.nextime.ion.framework.logger.Logger;

public class SearchLcAction extends BaseSearchAction {

	public String getType(HttpServletRequest request) {
		String sC = "";
		try {
			java.io.File f = new java.io.File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "recherche.xml");
			// lit le contenu du fichier
			sC = new String(org.nextime.ion.commons.Util.read(f));
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), SearchLcAction.class,e);
		}
		if (sC == null || "".equals(sC))
			sC = "<lc/>";
		request.setAttribute("sC", sC);
		return "" + request.getParameter("type");
	}

}