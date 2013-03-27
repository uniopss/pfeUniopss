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
import org.nextime.ion.backoffice.tree.TreeControl;
import org.nextime.ion.backoffice.tree.TreeControlNode;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class DeleteSectionAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canDeleteSection(Section
					.getInstance(request.getParameter("id").toString()), User
					.getInstance(request.getSession().getAttribute("userLogin")
							+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(
					e.getMessage(), DeleteSectionAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		request.setAttribute("sid", request.getParameter("sid"));

		// retrieve selected section
		TreeControl tree = (TreeControl) request.getSession().getAttribute(
				"treeControlTest");
		String selectedId = request.getParameter("id");
		TreeControlNode node = tree.findNode(selectedId);

		try {
			Mapping.begin();

			Section section = Section.getInstance(selectedId);
			section.remove();
			// update tree
			try {
				TreeControlNode parent = node.getParent();
				node.remove();
				if (parent != null) {
					parent.rebuildLastChildren();
				}
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), DeleteSectionAction.class,e);
			}

			Mapping.commit();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(
					e.getMessage(), DeleteSectionAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("ok"));

	}

}