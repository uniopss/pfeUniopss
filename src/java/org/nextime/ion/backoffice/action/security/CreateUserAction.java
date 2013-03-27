package org.nextime.ion.backoffice.action.security;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.exception.StandardException;
import org.nextime.ion.backoffice.form.CreateUserForm;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;


public class CreateUserAction extends SecurityAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User.getInstance(request.getSession().getAttribute("userLogin")+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateUserAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		User currentUser = null;
		try {
			currentUser = getCurrentUser(request);
		} catch (MappingException e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), EditUserAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}
		boolean isSuperAdmin = isSuperAdmin(request);

		// get the form
		CreateUserForm sform = (CreateUserForm) form;
		ActionErrors errors = sform.myValidate(request);

		// user need cancel
		if (request.getParameter("cancel") != null) {
			// Forward to the next page
			return (mapping.findForward("cancel"));
		}

		try {
			Vector groups = null;
			Mapping.begin();
			if (isSuperAdmin) {
				groups = Group.listAll();
			} else {
				groups = currentUser.listGroups();
			}
			Mapping.rollback();
			request.setAttribute("groups", groups);
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), CreateUserAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		// fill data | first time
		if (sform.getName() == null) {
			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// fill data | errors
		if (errors.size() > 0) {
			request.setAttribute(ERROR_KEY, errors);
			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// all it's ok : update user
		try {
			// creation user os
			String userLogin=sform.getLogin();

			Mapping.begin();
			User user = null;
			try {
				user = User.getInstance(userLogin);
			} catch (MappingException e) {
				Logger.getInstance().error("Test effectué que l'utilisateur n'existe pas", CreateUserAction.class,e);
			}
			if (user !=null) {// existe déjà
				throw new StandardException("L'utilisateur n'existe déjà avec le login " + userLogin);
			}

			user = User.create(userLogin);
			user.setMetaData("name", sform.getName());
			user.setMetaData("email", sform.getEmail());
			user.setMetaData("active", sform.getActive());

			user.setPassword(org.nextime.ion.commons.Authenticator.createPassword(sform.getPassword()));
			user.resetGroups();
			if (sform.getGroups() != null) {
				for (int i = 0; i < sform.getGroups().length; i++) {
					Group group = Group.getInstance(sform.getGroups()[i]);
					user.addGroup(group);
				}
			}

			Mapping.commit();

		} catch (StandardException e) {
			// message
			errors = new ActionErrors();
			ActionError error = new ActionError("error.createUser.userExisted");
			errors.add("login", error);
			request.setAttribute(ERROR_KEY, errors);
			Logger.getInstance().error(e.getMessage(), CreateUserAction.class,e);
			return (mapping.findForward("view"));
		}
		catch (MappingException e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateUserAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

	// Forward to the next page
		return (mapping.findForward("ok"));
	}

}