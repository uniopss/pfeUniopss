package org.nextime.ion.backoffice.action.data;

import javax.servlet.http.HttpServletRequest;

import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class ListPPAction extends BaseDataProduitAction {

	public void prepare(HttpServletRequest request) {
		if (request.getParameter("zaction") != null
				&& !"".equals(request.getParameter("zaction"))) {
			try {
				Mapping.begin();
				Section section = Section.getInstance(request
						.getParameter("sid"));
				String[] s = section.getPath().split("/");
				if (s != null && s.length > 3)
					request.setAttribute("fm", s[3]);

			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), ListPPAction.class,e);
			} finally {
				Mapping.rollback();
			}
		} else
			super.prepare(request);
	}

	public String getType() {
		return "plusProduit";
	}

	public String getXml(HttpServletRequest request, String name) {
		return "<d><![CDATA[" + request.getParameter("d_" + name) + "]]></d>";
	}

}