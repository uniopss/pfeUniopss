package org.nextime.ion.backoffice.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.form.LoginForm;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class LoginAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		LoginForm lform = (LoginForm) form;
		// première fois
		if (lform.getLogin() == null)
			return (mapping.findForward("view"));

		String login = lform.getLogin();
		String password = lform.getPassword();

		// vérifier l'utilisateur sur le framework
		try {
			Mapping.begin();
			User user = User.getInstance(login);
			Mapping.rollback();
			if (!org.nextime.ion.commons.Authenticator.checkPassword(user, password)) {
				ActionError error = new ActionError("error.login.badPassword");
				ActionErrors errors = new ActionErrors();
				errors.add("password", error);
				request.setAttribute(ERROR_KEY, errors);
				return (new ActionForward(mapping.getInput()));
			}
			// TODO : activer une fois la base est ok
/*
			Boolean active = (Boolean)user.getMetaData("active");
			if (active==null || !active.booleanValue()) {
				ActionError error = new ActionError("error.login.badPassword");
				ActionErrors errors = new ActionErrors();
				errors.add("password", error);
				request.setAttribute(ERROR_KEY, errors);
				return (new ActionForward(mapping.getInput()));
			}
*/
			request.getSession().setAttribute("userLogin", user.getLogin());
			request.getSession().setAttribute("userGroup", user.getGroupsIds());
			request.getSession().setAttribute("userMail", user.getMetaData("email"));


		} catch (Exception e) {
			Mapping.rollback();
			ActionError error = new ActionError("error.login.badLogin");
			ActionErrors errors = new ActionErrors();
			errors.add("login", error);
			request.setAttribute(ERROR_KEY, errors);
			return (new ActionForward(mapping.getInput()));
		} finally {
			Mapping.rollback();
		}

		// Forward to the newt page
		LogoutAction.purge(request);
		request.setAttribute("reload", "true");
		return (mapping.findForward("ok"));

	}

}