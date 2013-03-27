package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class PublicationMdAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// retrieve id
		String id = (request.getAttribute("id") != null) ? request
				.getAttribute("id").toString() : request.getParameter("id")
				.toString();
		String sid = request.getParameter("sid").toString();
		request.setAttribute("sid", sid);

		// retrieve version
		int version = Integer
				.parseInt((request.getAttribute("version") != null) ? request
						.getAttribute("version").toString() : request
						.getParameter("version").toString());
		request.setAttribute("version", version + "");

		// check if publication can be edited
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canEditPublication(Publication
					.getInstance(id), version, User.getInstance(request
					.getSession().getAttribute("userLogin")
					+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		try {
			Mapping.begin();
			Publication u = Publication.getInstance(id);
			request.setAttribute("publication", u);
			Section section = Section.getInstance(sid);
			request.setAttribute("section", section);

			// *********************************************************

			// *********************************************************
			// les metaData sont gérées separement car je n'ai
			// aucune idée du nombre et du nom des champs qu'il va
			// y avoir.
			// cette partie est un peu bancale mais en général dans un
			// veritable backoffice le nombre de champs composant le
			// formulaire est connu et on peut les gérer comme ce que j'ai
			// fait plus haut ( login, password ... )
			// CETTE PARTIE N'EST DONC PAS A REPRENDRE.
			boolean save = false;
			// sauve les metaData
			if (request.getParameter("metaToSave") != null
					&& !"".equals(request.getParameter("metaToSave"))) {
				Enumeration ps = request.getParameterNames();
				while (ps.hasMoreElements()) {
					String name = ps.nextElement() + "";
					if (name.startsWith("META_")) {
						name = name.substring(5);
						u.setMetaData(name, request.getParameter("META_" + name));
					}
				}
				save = true;
			}
			// efface la metaData si il y a besoin
			String mtd = request.getParameter("metaToDelete");
			if ((mtd + "").trim().equals(""))
				mtd = null;
			if (mtd != null) {
				u.removeMetaData(mtd);
				save = true;
			}
			// ajoute la metaData si il y a besoin
			String mtd2 = request.getParameter("newMETA");
			if ((mtd2 + "").trim().equals(""))
				mtd2 = null;
			if (mtd2 != null && u.getMetaData(mtd2) == null) {
				u.setMetaData(mtd2, "");
				save = true;
			}

			request.setAttribute("metaData", u.getMetaData());
			if (save)
				Mapping.commit();
			else
				Mapping.rollback();

		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), PublicationMdAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("view"));
	}
}