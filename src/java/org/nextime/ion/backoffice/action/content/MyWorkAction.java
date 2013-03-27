package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.helper.SortCriteria;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

/**
 *
 * 	Liste de tâche dans le backoffice
 *
 */
public class MyWorkAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		try {
			String etat = request.getParameter("etat");
			if (etat != null && !"".equals(etat)) {
				Mapping.begin();
				User u = User.getInstance("" + request.getSession().getAttribute("userLogin"));
				// dans le cas c'est un admin ou valideur, on ne filtre pas sur l'auteur
				boolean all = (u.isInGroup(Group.getInstance("gValideurs")) || u
						.isInGroup(Group.getInstance("admins")));
				Mapping.rollback();
				Vector v = new Vector();
				v.add(request.getParameter("selectedSite"));
				Vector result = Searcher.searchBO(v, "+workflow:" + etat + " "
						+ (all ? "" : "+auteur:")
						+ request.getSession().getAttribute("userLogin"), ""
						+ request.getSession().getAttribute("currentLocale"),
						"co",
						SortCriteria.SORT_SECTION);
				if (result != null && result.size() > 0) {
					request.setAttribute("result", result);
				}
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), ListPublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		}  finally {
			Mapping.rollback();
		}

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}