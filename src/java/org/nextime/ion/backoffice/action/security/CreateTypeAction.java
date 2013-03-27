package org.nextime.ion.backoffice.action.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class CreateTypeAction extends BaseAction {

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
			Logger.getInstance().error(e.getMessage(), CreateTypeAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}
		if (request.getParameter("cancel") != null) {
			return (mapping.findForward("success"));
		}

		TypeForm f = (TypeForm) form;
		// fill data | first time
		if (f.getId() == null || "".equals(f.getId())) {
			// Forward to the view page
			return (mapping.findForward("view"));
		}

		String id = "";

		try {

			// creation de l'objet
			Mapping.begin();
			id = f.getId();
			TypePublication u = TypePublication.create(f.getId());

			// creation du modele.xml de base
			File file = new File(new File(Config.getInstance().getTypePublicationDirectory(), "models"), f.getId()+ ".xml");
			if (!file.exists()) {
				PrintStream os = new PrintStream(new FileOutputStream(file));
				os.println("<publication name=\"" + f.getId() + "\"/>");
				os.close();
			}

			// creation du repertoire pour les styles
			File dir = new File(new File(Config.getInstance().getTypePublicationDirectory(), "styles"), f.getId());
			if (!dir.exists()) {
				dir.mkdir();
			}

			Mapping.commit();

		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateTypeAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// on va passer l'id en request pour aller directement à
		// la page d'edition
		request.setAttribute("id", id);

		return mapping.findForward("success");
	}

}