package org.nextime.ion.backoffice.action.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.form.GroupForm;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.business.impl.GroupImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class EditGroupAction extends BaseAction {

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
			Logger.getInstance().error(e.getMessage(), EditGroupAction.class,e);
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

			// pour remplir les listes select
			/*
			Mapping.begin();
			request.setAttribute("userList", User.listAll());
			Mapping.rollback();
			*/

			if (request.getParameter("ok") == null) {
				Mapping.begin();

				// initialisation du formulaire
				Group u = Group.getInstance(id);
				GroupForm f = (GroupForm) form;
				f.setId(u.getId());
				// f.setUsers(u.getUsersIds());
				f.setName((String) ((GroupImpl) u).getMetaData("name"));
				Mapping.rollback();
				return new ActionForward(mapping.getInput());
			}

			// effectu les modifications
			Mapping.begin();
			GroupForm f = (GroupForm) form;
			Group u = Group.getInstance(id);
			/*
			String[] g = f.getUsers();
			u.resetUsers();
			if (g != null) {
				u.addUser(User.getInstance("admin"));
				for (int i = 0; i < g.length; i++) {
					if (!"admin".equals(g[i]))
						u.addUser(User.getInstance(g[i]));
				}
			}
			*/
			u.setMetaData("name", f.getName());
			Mapping.commit();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), EditGroupAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		return mapping.findForward("success");
	}

}