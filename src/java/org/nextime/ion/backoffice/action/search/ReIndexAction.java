package org.nextime.ion.backoffice.action.search;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.logger.Logger;

public class ReIndexAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// reindex
		try {
			org.nextime.ion.framework.job.LcSched.execute("IndexJob");
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), ReIndexAction.class,e);
			return (mapping.findForward("errorIon"));
			//throw new ServletException(e);
		}

		// Forward to the next page
		return (mapping.findForward("ok"));
	}

}