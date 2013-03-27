package org.nextime.ion.backoffice.action.content;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class MovePublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// get publication
		try {
			Mapping.begin();

			Publication p = Publication
					.getInstance(request.getParameter("pId"));
			Section s = Section.getInstance(request.getParameter("id"));
			// up or down ?
			if (request.getParameter("type").equals("up")) {
				PublicationSorter.upPublication(p, s);
			} else {
				PublicationSorter.downPublication(p, s);
			}

			Mapping.commit();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), MovePublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		// Forward to the next page
		request.setAttribute("sid", request.getParameter("id"));
		return (mapping.findForward("ok"));

	}

}