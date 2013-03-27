package org.nextime.ion.backoffice.action.content;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.tree.TreeControl;
import org.nextime.ion.backoffice.tree.TreeControlNode;

public class ListSectionAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		HttpSession session = request.getSession();
		TreeControl control = (TreeControl) session
				.getAttribute("treeControlTest");
		String name = request.getParameter("tree");
		if (control != null && name != null) {
			TreeControlNode node = control.findNode(name);
			if (node != null)
				node.setExpanded(!node.isExpanded());
		}
		// Forward back to the test page
		return (mapping.findForward("view"));

	}
}