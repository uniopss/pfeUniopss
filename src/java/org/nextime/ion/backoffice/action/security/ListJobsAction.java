package org.nextime.ion.backoffice.action.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;

public class ListJobsAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User
					.getInstance(request.getSession().getAttribute("userLogin")
							+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(
					e.getMessage(), ListJobsAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		if ("admin".equals(request.getSession().getAttribute("userLogin"))) {
			if (request.getParameter("job") != null
					&& ("IndexJob".equals(request.getParameter("job"))
							|| "MainJob".equals(request.getParameter("job"))
							|| "ImportJob".equals(request.getParameter("job")) || "ServiceJob"
							.equals(request.getParameter("job"))))
				org.nextime.ion.framework.job.LcSched.execute(request
						.getParameter("job"));
			else if (request.getParameter("restart") != null
					&& "1".equals(request.getParameter("restart")))
				org.nextime.ion.framework.job.LcSched.reStart();
			else if (request.getParameter("rewf") != null
					&& !"".equals(request.getParameter("rewf"))) {
				try {
					Mapping.begin();
					org.nextime.ion.framework.workflow.Workflow.reInitV(request
							.getSession().getAttribute("currentLocale")
							+ "", request.getParameter("rewf"));
					Mapping.commit();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), ListJobsAction.class,e);
					Mapping.rollback();
				} finally {
					Mapping.rollback();
				}
			}
		}
		// Forward to the next page
		return (mapping.findForward("view"));

	}

}