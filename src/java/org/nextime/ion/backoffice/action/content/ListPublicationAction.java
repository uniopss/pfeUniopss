package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.logger.Logger;



/**
 * 	la recherche standard dans le backoffice
 *
 *
 */
public class ListPublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// set the locales list
		request.setAttribute("indexs", Config.getInstance().getIndexNames());

		// get the keywords
		String keyWords = request.getParameter("keyWords");
		if (keyWords != null) {
			if (!keyWords.trim().equals("")) {
				try {
					java.util.Vector v = new java.util.Vector();
					v.add(request.getParameter("selectedSite"));
					Vector result = Searcher.searchBO(v, keyWords, request.getParameter("index"), "co", null);
					if (result != null && result.size() > 0) {
						request.setAttribute("result", result);
					}
				} catch (Exception e) {
					Logger.getInstance().error(e.getMessage(), ListPublicationAction.class,e);
					return (mapping.findForward("errorIon"));
					//throw new ServletException(e);
				}
			} else {
				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError("error.search.noKeyWords");
				errors.add("keys", error);
				request.setAttribute(ERROR_KEY, errors);
			}
		}

		// Forward to the next page
		return (mapping.findForward("view"));
	}
}