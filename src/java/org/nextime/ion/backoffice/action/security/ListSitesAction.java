package org.nextime.ion.backoffice.action.security;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import org.nextime.ion.framework.business.*;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.*;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;

public class ListSitesAction extends BaseAction {

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

		try {
			Mapping.begin();
			Vector v = Section.listRootSections();
			Mapping.rollback();
			request.setAttribute("sites", v);
		} catch (MappingException e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), ListSitesAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		return mapping.findForward("view");
	}

}