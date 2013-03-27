package org.nextime.ion.backoffice.action.content;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class UncachePublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		String id = request.getParameter("id");
		String v = request.getParameter("v");
		try {
			org.nextime.ion.framework.helper.Viewer.cleanCache(id);
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), UncachePublicationAction.class,e);
			return (mapping.findForward("errorIon"));
			//throw new ServletException(e);
		}

		// Forward to the next page
		return (mapping.findForward("ok"));

	}

}