package org.nextime.ion.backoffice.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.getSession().removeAttribute("userLogin");
		request.getSession().removeAttribute("userGroup");
		request.getSession().removeAttribute("userMail");
		request.getSession().removeAttribute("panier");
		request.getSession().removeAttribute("messageBoard");
		request.getSession().removeAttribute("loginForm");
		request.getSession().removeAttribute("editUserForm");
		purge(request);
		// Forward to the next page
		return (mapping.findForward("ok"));

	}

	public static void purge(HttpServletRequest request)
			throws ServletException, IOException {
		request.getSession().removeAttribute("localizedMenu");
		request.getSession().removeAttribute("currentLocale");
		request.getSession().removeAttribute("currentSite");
		request.getSession().removeAttribute("currentSection");
		request.getSession().removeAttribute("treeControlTest");
		request.getSession().removeAttribute("sectionTree");
		request.getSession().removeAttribute("pageInfos");
		request.getSession().removeAttribute("versionDisplayInfos");
	}

}