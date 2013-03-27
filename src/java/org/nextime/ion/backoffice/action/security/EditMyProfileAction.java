package org.nextime.ion.backoffice.action.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.form.EditUserForm;

import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class EditMyProfileAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// get the form
		EditUserForm sform = (EditUserForm) form;
		ActionErrors errors = sform.myValidate(request);

		// user need cancel
		if (request.getParameter("cancel") != null) {
			// Forward to the next page
			return (mapping.findForward("cancel"));
		}

		// retrieve id
		String id = request.getSession().getAttribute("userLogin") + "";

		// fill data | first time
		if (sform.getName() == null) {
			try {
				Mapping.begin();
				User user = User.getInstance(id);
				Mapping.rollback();

				sform.setEmail((String) user.getMetaData("email"));
				sform.setName((String) user.getMetaData("name"));
				sform.setGroups(user.getGroupsIds());

			} catch (Exception e) {
				Mapping.rollback();
				Logger.getInstance().error(e.getMessage(), EditMyProfileAction.class,e);
				return (mapping.findForward("errorIon"));
			}  finally {
				Mapping.rollback();
			}

			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// fill data | errors
		if (errors.size() > 0) {
			try {
				request.setAttribute(ERROR_KEY, errors);

			} catch (Exception e) {
				Mapping.rollback();
				Logger.getInstance().error(
						e.getMessage(), EditMyProfileAction.class,e);
				return (mapping.findForward("errorIon"));
				//throw new ServletException(e);
			}

			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// all it's ok : update user
		try {
			Mapping.begin();
			User user = User.getInstance(id);
			user.setMetaData("name", sform.getName());
			user.setMetaData("email", sform.getEmail());
			if (sform.getPassword() != null && !"".equals(sform.getPassword()))
				user.setPassword(org.nextime.ion.commons.Authenticator
						.createPassword(sform.getPassword()));
			Mapping.commit();
			request.getSession().setAttribute("userMail", sform.getEmail());

		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(
					e.getMessage(), EditMyProfileAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("ok"));
	}

}