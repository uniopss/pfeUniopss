package org.nextime.ion.backoffice.action.workflow;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.nextime.ion.backoffice.action.BaseAction;

public class CommentaireAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// si on a tous les elements
		if (request.getParameter("commentaire") != null) {
			Hashtable inputs = new Hashtable();
			inputs.put("commentaire", request.getParameter("commentaire"));
			request.setAttribute("inputs", inputs);
			request.setAttribute("id", request.getParameter("id"));
			request.setAttribute("version", request.getParameter("version"));
			request.setAttribute("action", request.getParameter("action"));
			return (mapping.findForward("actions"));
		}

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}