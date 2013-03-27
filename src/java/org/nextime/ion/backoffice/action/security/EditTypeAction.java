package org.nextime.ion.backoffice.action.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.form.TypeForm;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.business.impl.TypePublicationImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class EditTypeAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User
					.getInstance(request.getSession().getAttribute("userLogin")
							+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), EditTypeAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}
		if (request.getParameter("cancel") != null) {
			return (mapping.findForward("success"));
		}

		if (request.getParameter("id") != null)
			request.setAttribute("id", request.getParameter("id"));
		String id = request.getAttribute("id") + "";

		try {

			if (request.getParameter("ok") == null) {
				Mapping.begin();

				// initialisation du formulaire
				TypePublication u = TypePublication.getInstance(id);
				TypeForm f = (TypeForm) form;
				f.setId(u.getId());
				f.setName((String) ((TypePublicationImpl) u)
						.getMetaData("name"));

				// liste les styles
				request.setAttribute("styles", u.listStyles());
				Mapping.rollback();
				return new ActionForward(mapping.getInput());
			}

			// effectu les modifications
			Mapping.begin();
			TypeForm f = (TypeForm) form;
			TypePublication u = TypePublication.getInstance(id);
			u.setMetaData("name", f.getName());
			Mapping.commit();

			// ***********************************************************

		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), EditTypeAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		return mapping.findForward("success");
	}

}