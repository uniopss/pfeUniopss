package org.nextime.ion.backoffice.action.resource;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.bean.Resources;
import org.nextime.ion.framework.logger.Logger;

public class ResourceManagementAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// set resources directory
		try {
			request.setAttribute("resources", Resources.getResourceXmlBeans());
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), ResourceManagementAction.class,e);
		}

		String selectedType = request.getParameter("selectedType");
		try {
			if (selectedType == null)
				selectedType = ((org.nextime.ion.backoffice.bean.ResourceXmlBean) org.nextime.ion.backoffice.bean.Resources
						.getResourceXmlBeans().get(0)).getId();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), ResourceManagementAction.class,e);
		}
		request.setAttribute("selectedType", selectedType);

		String selectedSite = request.getParameter("selectedSite");
		try {
			if (selectedSite == null)
				selectedSite = ((org.nextime.ion.framework.business.Section) ((java.util.Vector) request
						.getSession().getAttribute("currentSite")).get(0))
						.getId();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), ResourceManagementAction.class,e);
		}
		request.setAttribute("selectedSite", selectedSite);
		// Forward back to the test page
		return (mapping.findForward("view"));

	}

}