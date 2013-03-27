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
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class CreateGroupAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User.getInstance(request.getSession().getAttribute("userLogin")
							+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateGroupAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}
		if (request.getParameter("cancel") != null) {
			return (mapping.findForward("success"));
		}

		GroupForm f = (GroupForm) form;
		// fill data | first time
		if (f.getId() == null || "".equals(f.getId())) {
			// Forward to the view page
			/*
			try {

				// pour remplir les listes select
				Mapping.begin();
				request.setAttribute("userList", User.listAll());
				Mapping.rollback();

			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), CreateGroupAction.class,e);
			} finally {
				Mapping.rollback();
			}
			*/
			return (mapping.findForward("view"));
		}

		String id = "";
		try {

			// pour remplir les listes select
			Mapping.begin();
			request.setAttribute("userList", User.listAll());
			Mapping.rollback();

			// creation de l'objet
			Mapping.begin();
			id = f.getId();
			Group u = Group.create(f.getId());
			/*
			String[] g = f.getUsers();
			if (g != null) {
				u.addUser(User.getInstance("admin"));
				for (int i = 0; i < g.length; i++) {
					if (!"admin".equals(g[i]))
						u.addUser(User.getInstance(g[i]));
				}
			}*/
			Mapping.commit();

		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateGroupAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		// on va passer l'id en request pour aller directement à
		// la page d'edition
		request.setAttribute("id", id);

		return mapping.findForward("success");
	}

}