package org.nextime.ion.backoffice.action.message;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.message.*;
import org.nextime.ion.framework.logger.Logger;

public class DeleteMessageAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		try {
			MessageBoard p = (MessageBoard) request.getSession().getAttribute(
					"messageBoard");
			p.removeMessage(Integer.parseInt(request.getParameter("nb") + ""));
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), DeleteMessageAction.class,e);
		}
		// Forward to the next page
		return (mapping.findForward("success"));

	}

}