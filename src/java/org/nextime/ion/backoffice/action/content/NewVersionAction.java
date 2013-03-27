package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class NewVersionAction extends BaseAction {
	private static int max = 5;

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		String id = request.getParameter("nid");

		try {
			Mapping.begin();
			Publication p = Publication.getInstance(id);
			if (p.getLastVersion().getVersion() < max) {
				p.newVersion(User.getInstance(request.getSession()
						.getAttribute("userLogin")
						+ ""));
				p.setDate(new java.util.Date());
				Mapping.commit();
			} else
				Mapping.rollback();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), NewVersionAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// Forward to the next page
		request.setAttribute("sid", request.getParameter("sid"));
		return (mapping.findForward("ok"));

	}

}