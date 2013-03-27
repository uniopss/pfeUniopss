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

public class AddMessageAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		MessageBoard p = (MessageBoard) request.getSession().getAttribute(
				"messageBoard");
		Message mes = new Message();
		mes.setPoster(request.getSession().getAttribute("userLogin") + "");
		mes.setDate(new java.util.Date());
		mes.setMessage(request.getParameter("message"));
		p.addMessage(mes);

		// Forward to the next page
		return (mapping.findForward("success"));

	}

}