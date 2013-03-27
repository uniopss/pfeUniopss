package org.nextime.ion.backoffice.action.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class ListUsersAction extends SecurityAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User.getInstance(request.getSession().getAttribute("userLogin") + ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateGroupAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}


		boolean isSuperAdmin = isSuperAdmin(request);

		// fill data
		try {

			User currentUser = getCurrentUser(request);
			Vector currentGroups = currentUser.listGroups();

			Mapping.begin();
			Vector users = User.listAll();
			Mapping.rollback();

			Vector ret = new Vector();

			if (isSuperAdmin) {
				ret = users;
			} else { // on filtre sur les droits de l'utilisateur
				for (Iterator it=users.iterator(); it.hasNext(); ) {
					User user = (User)it.next();
					boolean userInGroup = userHasGroup(user, currentGroups);
					if (userInGroup) {
						ret.add(user);
					}
				}
			}

			Collections.sort(ret);
			Collections.reverse(ret);
			request.setAttribute("users", ret);

		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), ListUsersAction.class,e);
			return (mapping.findForward("errorIon"));
			//throw new ServletException(e);
		} finally  {
			Mapping.rollback();
		}
		// Forward to the next page
		return (mapping.findForward("view"));
	}

	/**
	 * 	Vérification si l'utilisateur a un groups qui fait partie de la liste des groupes
	 * @param p_Users
	 * @param p_Groups
	 * @return
	 */
	private boolean userHasGroup(User p_Users, Vector p_Groups) {
		return p_Groups.containsAll(p_Users.listGroups());
		/*
		boolean founded = false;
		for (Iterator it=p_Groups.iterator(); it.hasNext() && !founded; ) {
			Group group = (Group)it.next();
			if ( p_Users.isInGroup(group)) {
				founded = true;
			}
		}
		return founded; */
	}



}