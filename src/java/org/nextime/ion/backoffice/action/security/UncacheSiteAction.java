package org.nextime.ion.backoffice.action.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class UncacheSiteAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));
		try {
			Mapping.begin();
			if (org.nextime.ion.framework.cache.Util.check()
					&& request.getParameter("id") != null) {
				if (request.getParameter("rd") != null)
					org.nextime.ion.framework.cache.Util.generate(request
							.getParameter("id"));
				else
					org.nextime.ion.framework.cache.Util.cleanCache(request
							.getParameter("id"));
			}
			Mapping.rollback();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), UncacheSiteAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}
		return mapping.findForward("success");
	}

}