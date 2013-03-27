package org.nextime.ion.backoffice.action.content;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.locale.LocaleList;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class ViewPublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// set the locales list
		request.setAttribute("locales", LocaleList.getInstance().getLocales());

		String id = request.getParameter("id");
		String sid = request.getParameter("sid");
		int version = Integer.parseInt(request.getParameter("version"));
		String locale = request.getParameter("locale");

		try {
			Mapping.begin();

			Publication publication = Publication.getInstance(id);
			Section section = Section.getInstance(sid);
			request.setAttribute("section", section);
			request.setAttribute("publication", publication);
			String sXml = publication.getVersion(version).getDataAsString();
			String sMd = "<metaDatas>";
			java.util.Enumeration mdks = publication.getMetaData().keys();
			while (mdks.hasMoreElements()) {
				String key = mdks.nextElement() + "";
				sMd += "<metaData name='" + key + "'>"
						+ publication.getMetaData(key) + "</metaData>";
			}
			sMd += "</metaDatas>";
			sXml = sXml.replace("</publication>", sMd + "</publication>");
			sXml = sXml.substring(sXml.indexOf("<publication"));
			// a l'ancienne pke ca L?FJKFKJFKFD
			sXml = sXml.replaceAll("ion:lang", "xml:lang");
			// hack for € symbol
			sXml = sXml.replaceAll("&euro;", "€");
			request.setAttribute("xml", sXml);
			Mapping.rollback();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), ViewPublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}