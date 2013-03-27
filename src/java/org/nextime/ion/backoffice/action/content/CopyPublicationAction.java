package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.business.impl.IdentityImpl;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.helper.IdGenerator;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class CopyPublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canCreatePublication(Section
					.getInstance(request.getParameter("id").toString()), User
					.getInstance(request.getSession().getAttribute("userLogin")
							+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			//throw new BackofficeSecurityException();
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CopyPublicationAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		// copy publication
		try {
			Mapping.begin();
			Publication p = Publication
					.getInstance(request.getParameter("pId"));
			int version = Integer.parseInt(request.getParameter("version")
					.toString());

			// Pk
			String _name = "Copie de " + p.getMetaData("name") + " (version "
					+ version + ")";
			TypePublication typeP = p.getType();
			Vector styleNames = typeP.listStyles();
			boolean _check = false;
			for (int i = 0; i < styleNames.size(); i++) {
				String n = styleNames.get(i).toString();
				if (n.startsWith("indexation") && n.endsWith("pk")) {
					_check = true;
					break;
				}
			}
			if (_check
					&& (p.getMetaData("name").toString().startsWith("Copie de") || Searcher
							.check("+pk:" + p.getMetaData("name")
									+ " +pk:Copie +type:" + typeP.getId(), ""
									+ request.getSession().getAttribute(
											"currentLocale"), "pk") > 0)) {
				// pk error
			} else
				_check = false;
			if (!_check) {
				Section s = Section.getInstance(request.getParameter("id"));
				String newId = IdGenerator.nextPublicationId();
				User u = User.getInstance(request.getSession().getAttribute("userLogin")+ "");
				Publication publi = Publication.create(u, newId, p.getType(), s.getMetaData("workflow")+ "");
				s.addPublication(publi);
				// copier all metadatas
				((PublicationImpl) publi).setMetaData((java.util.Hashtable) p.getMetaData().clone());
				// and override name and noFiche
				publi.setMetaData("name", _name);
				publi.setMetaData("noFiche", Publication.getNextPublicationNumber());
				publi.newVersion(u);
				publi.getVersion(1).setData(p.getVersion(version).getData());
				PublicationSorter.initPublication(publi, s);
				Mapping.commit();
			} else
				Mapping.rollback();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CopyPublicationAction.class,e);
			return (mapping.findForward("errorIon"));
			//throw new ServletException(e);
		} finally {
			Mapping.rollback();
		}

		// Forward to the next page
		request.setAttribute("sid", request.getParameter("id"));
		return (mapping.findForward("ok"));

	}

}