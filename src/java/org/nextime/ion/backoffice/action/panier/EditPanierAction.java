package org.nextime.ion.backoffice.action.panier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.panier.Panier;

public class EditPanierAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		if (request.getParameter("cancel") != null) {
			// Forward to the next page
			return (mapping.findForward("success"));
		}
		if (request.getParameter("ok") != null) {
			Panier p = (Panier) request.getSession().getAttribute("panier");
			if ("1".equals(request.getParameter("etat")))
				p.removeElement("" + request.getParameter("id"));
			else
				p.addElement("" + request.getParameter("id"));
			// Forward to the next page
			return (mapping.findForward("success"));
		}

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}