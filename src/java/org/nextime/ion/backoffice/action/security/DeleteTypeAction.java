package org.nextime.ion.backoffice.action.security;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class DeleteTypeAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if the user is authorized to perform this action
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User
					.getInstance(request.getSession().getAttribute("userLogin")
							+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), DeleteTypeAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		String id = request.getParameter("id");

		try {
			Mapping.begin();
			TypePublication.getInstance(id).remove();
			// creation du modele.xml de base
			File file = new File(new File(Config.getInstance()
					.getTypePublicationDirectory(), "models"), id + ".xml");
			file.delete();

			// creation du repertoire pour les styles
			File dir = new File(new File(Config.getInstance()
					.getTypePublicationDirectory(), "styles"), id);
			if (dir.exists()) {
				File[] fs = dir.listFiles();
				for (int i = 0; i < fs.length; i++) {
					fs[i].delete();
				}
			}
			dir.delete();
			Mapping.commit();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), DeleteTypeAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		return mapping.findForward("success");
	}
}