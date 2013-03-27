package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.tree.TreeControl;
import org.nextime.ion.backoffice.tree.TreeControlNode;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.SectionPublication;
import org.nextime.ion.framework.business.impl.SectionImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class HighlightAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		Vector sites = (java.util.Vector) request.getSession().getAttribute("currentSite");
		String id = request.getParameter("id");

		try {

			Mapping.begin();

			Publication p = Publication.getInstance(id);
			//Section section = (Section) p.listSections().firstElement();
			Section section = findAutorizedSection(sites, p);
			// on ne trouve pas la section
			if (section ==null) {
				return (mapping.findForward("errorUserIon"));
			}

			request.setAttribute("sid", section.getId());

			// faire un bel arbre
			TreeControl control = (TreeControl) request.getSession()
					.getAttribute("treeControlTest");
			collapse(control.getRoot());

			control.selectNode(section.getId());

			// expand all
			while (section != null) {
				TreeControlNode node = control.findNode(section.getId());
				node.setExpanded(true);
				section = section.getParent();
			}

			// trouve la page
			//section = (Section) p.listSections().firstElement();
			section = findAutorizedSection(sites, p);

			// Collection collection = PublicationSorter.sortPublications(section);
			// Vector ps = new Vector(collection);
			SectionPublication sectionPublication = SectionPublication.findSectionPublication(section, p);
			if (sectionPublication!=null) {
				int pos = sectionPublication.getIndex();
				// inverser la position
				pos = ((SectionImpl)section).getSectionPublications().size() - pos;

				int page = pos / ViewSectionAction.pageSize;
				((Hashtable) (request.getSession().getAttribute("pageInfos"))).put(section.getId(), new Integer(page* ViewSectionAction.pageSize));
			}

		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(e.getMessage(), HighlightAction.class,e);
		} finally {
			Mapping.rollback();
		}

		request.getSession().setAttribute("highlightId", id);

		// Forward to the next page
		return (mapping.findForward("ok"));

	}

	/**
	 * 	Récupération la première section authorisée pour l'utilisateur
	 * 	car la publication liée à plusieurs section des autres sites
	 * @param request
	 * @param p_Publication
	 * @return
	 */
	private Section findAutorizedSection(Vector p_Sites, Publication p_Publication) {

		Section sectionFounded = null;

		// récupération les sites courantes de l'utilisateur en session

		Vector sections = p_Publication.listSections();

		for (Iterator it=p_Sites.iterator(); it.hasNext() && sectionFounded==null;) {
			Section section=(Section)it.next();
			String siteId = section.getId();

			// on chercher sur les sections liées pour voir si utilisateur est autorisé à voir la section
			for (Iterator sectionIt= sections.iterator(); sectionIt.hasNext() && sectionFounded==null;) {
				Section sectionPublication = (Section)sectionIt.next();
				if (StringUtils.contains(sectionPublication.getId(), siteId)) {
					sectionFounded = sectionPublication;
					break;
				}
			}
		}
		return sectionFounded;
	}



	private void collapse(TreeControlNode n) {
		for (int i = 0; i < n.findChildren().length; i++) {
			n.findChildren()[i].setExpanded(false);
			collapse(n.findChildren()[i]);
		}
	}

}