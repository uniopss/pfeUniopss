package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class HistoryPublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		String id = request.getParameter("id");
		int version = Integer.parseInt(request.getParameter("version"));

		try {
			Mapping.begin();

			Publication publication = Publication.getInstance(id);
			request.setAttribute("pub", publication.getMetaData("name"));
			Vector history = publication.getVersion(version).getWorkflow()
					.getHistorySteps();
			Vector current = publication.getVersion(version).getWorkflow()
					.getCurrentSteps();
			// desactiver suite à la monté de version de OSWorkflow
			//Collections.reverse(history);
			//Collections.reverse(current);
			request.setAttribute("history", history);
			request.setAttribute("current", current);

			Mapping.rollback();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(
					e.getMessage(), HistoryPublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}