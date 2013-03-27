package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.SortField;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.commons.SearchQueryUtil;

import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.impl.PublicationDocument;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.helper.SortCriteria;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
/**
 *
 * 	List des publications ( la prévue )
 *
 */
public class ViewSectionPublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// fill data
		try {
			Mapping.begin();
			String id = (String) request.getParameter("id");
			Section section = Section.getInstance(id);
			request.setAttribute("section", section);

			/*
			Vector result = Searcher.searchBO(v, id + "?", ,
									"sections", null);
			*/

			String locale = ""+ request.getSession().getAttribute("currentLocale");

			StringBuffer sb = new StringBuffer();
			sb.append(" ").append(PublicationDocument.FIELD_SECTIONS_SEPARATOR).append(":( *");
			sb.append(PublicationDocument.SECTIONS_SEPARATOR_BEGIN).append(id).append(PublicationDocument.SECTIONS_SEPARATOR_END);
			sb.append("* ) ");
			List result =Searcher.searchAdvanced(new Vector() ,
								StringUtils.EMPTY,
								sb.toString(),
								locale,
								null,
								null,
								0,
								5000);



			if (result != null && result.size() > 0) {
				request.setAttribute("result", result);
			}

		} catch (Exception e) {
			Logger.getInstance().error(
					e.getMessage(), ViewSectionPublicationAction.class,e);
			return (mapping.findForward("errorIon"));
			//throw new ServletException(e);
		} finally {
			Mapping.rollback();
		}
		// Forward to the next page
		return (mapping.findForward("view"));
	}
}