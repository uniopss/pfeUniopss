package org.nextime.ion.backoffice.action.content;

import java.io.IOException;

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
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.logger.Logger;

public class DeletePublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// retrieve selected publication
		String selectedId = request.getParameter("did");

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canDeletePublication(Publication
					.getInstance(selectedId), User.getInstance(request
					.getSession().getAttribute("userLogin")
					+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), DeletePublicationAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		try {
			Mapping.begin();
			Publication publication = Publication.getInstance(selectedId);
			publication.resetSections();
			//PublicationSorter.removePublication(publication, section);
			publication.remove();

			Mapping.commit();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), DeletePublicationAction.class,e);
			//return (mapping.findForward("errorIon"));
			//throw new ServletException(e);
		} finally {
			Mapping.rollback();
		}

		// Forward to the next page
		request.setAttribute("sid", request.getParameter("sid"));
		return (mapping.findForward("ok"));

	}

}