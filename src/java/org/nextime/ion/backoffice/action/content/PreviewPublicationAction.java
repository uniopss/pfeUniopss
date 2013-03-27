package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Vector;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.impl.Style;
import org.nextime.ion.framework.business.impl.TypePublicationImpl;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.locale.Locale;
import org.nextime.ion.framework.locale.LocaleList;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.logger.Logger;



public class PreviewPublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// set the locales list
		request.setAttribute("locales", LocaleList.getInstance().getLocales());

		String id = request.getParameter("id");
		String sid = request.getParameter("sid");
		int version = Integer.parseInt(request.getParameter("version"));
		String style = request.getParameter("style");
		String locale = request.getParameter("locale");

		try {
			Mapping.begin();

			Publication publication = Publication.getInstance(id);
			Section section = Section.getInstance(sid);
			request.setAttribute("section", section);
			request.setAttribute("publication", publication);
			TypePublication type = publication.getType();
			// list only XSL styles (not XSL-FO)
			Vector styleNames = type.listStyles();
			Vector styles = new Vector();
			for (int i = 0; i < styleNames.size(); i++) {
				String n = styleNames.get(i).toString();
				if (((TypePublicationImpl) type).getStyleSheet(n).getType() == Style.XSL
						&& !n.startsWith("indexation")
						&& !n.startsWith("special")) {
					styles.add(n);
				}
			}
			Collections.sort(styles);
			request.setAttribute("styles", styles);

			if (style == null && styles.size() > 0)
				style = styles.get(0) + "";
			if (style != null) {
				if (locale == null)
					locale = ((Locale) LocaleList.getInstance().getLocales().iterator().next()).getLocale();

				String view = new String(Viewer.getView(publication, version, style, locale));
				request.setAttribute("view", view);
			}
			Mapping.rollback();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), PreviewPublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}