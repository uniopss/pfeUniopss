package org.nextime.ion.backoffice.action.content;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
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
import org.nextime.ion.backoffice.bean.Objets;
import org.nextime.ion.backoffice.bean.Option;
import org.nextime.ion.backoffice.bean.SectionTypes;
import org.nextime.ion.backoffice.bean.TypeBean;
import org.nextime.ion.backoffice.exception.StandardException;
import org.nextime.ion.backoffice.form.PublicationForm;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.backoffice.tree.TreeCache;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.business.impl.Style;
import org.nextime.ion.framework.business.impl.TypePublicationImpl;
import org.nextime.ion.framework.cache.Cache;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public class PublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// user need cancel
		if (request.getParameter("cancel") != null) {
			// Forward to the next page
			return (mapping.findForward("cancel"));
		}

		// retrieve id
		String id = (request.getAttribute("id") != null) ? request.getAttribute("id").toString()
											: request.getParameter("id").toString();
		String sid = request.getParameter("sid").toString();

		Vector sites = (java.util.Vector) request.getSession().getAttribute("currentSite");
		request.setAttribute("sites", generateBeanSites(sites));


		// retrieve version
		int version = Integer.parseInt((request.getAttribute("version") != null) ?
						request.getAttribute("version").toString() :
						request.getParameter("version").toString());
		request.setAttribute("version", version + "");

		// check if publication can be edited
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canEditPublication(Publication.getInstance(id), version,
							User.getInstance(request.getSession().getAttribute("userLogin")	+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), PublicationAction.class, e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		// fill data | first time
		PublicationForm f = (PublicationForm) form;
		if (! f.isPostBack()) {
			try {
				f.reset();
				Mapping.begin();
				Publication publication = Publication.getInstance(id);
				Section section = Section.getInstance(sid);
				request.setAttribute("section", section);

				TypePublication typeP = publication.getType();
				Vector styleNames = typeP.listStyles();
				Vector styles = new Vector();
				boolean check = false;
				for (int i = 0; i < styleNames.size(); i++) {
					String n = styleNames.get(i).toString();
					if (((TypePublicationImpl) typeP).getStyleSheet(n)
							.getType() == Style.XSL
							&& !n.startsWith("indexation")
							&& !n.startsWith("special")) {
						styles.add(n);
					} else if (n.startsWith("indexation") && n.endsWith("pk"))
						check = true;
				}
				if (check)
					f.setType(publication.getType().getId());
				java.util.Collections.sort(styles);
				request.setAttribute("styles", styles);

				f.setSections(publication.getSectionsIds());
				f.setN(publication.getMetaData("name") + "");
				if (publication.getMetaData("lcPresentation") != null)
					f.setP(publication.getMetaData("lcPresentation") + "");
				if (publication.getMetaData("lcDatePublication") != null)
					f.setDp(publication.getMetaData("lcDatePublication") + "");
				if (publication.getMetaData("lcDateFin") != null)
					f.setDf(publication.getMetaData("lcDateFin") + "");
				if (publication.getMetaData("lcDateChange") != null)
					f.setDc(publication.getMetaData("lcDateChange") + "");
				if (publication.getMetaData("lcDateChange") != null)
					f.setDc(publication.getMetaData("lcDateChange") + "");
				if (publication.getType().getId().equals("objet")
						&& publication.getMetaData("objet") != null) {
					f.setO(publication.getMetaData("objet") + "");
					if (publication.getMetaData("objetPresentation") != null)
						f.setOp(publication.getMetaData("objetPresentation") + "");
				}
				if (publication.getMetaData("visibility") != null)
					f.setCurseurVisibilite((publication.getMetaData("visibility") + ""));

				String siteid=getSiteId(sid);
				// récupérer la section
				request.setAttribute("sectionList", generate(request, siteid, publication));

				f.setSiteId(siteid);

				request.setAttribute("publication", publication);
				if (publication.getType().getId().equals("objet"))
					request.setAttribute("objets", Objets.getObjetXmlBeans());

			} catch (Exception e) {
				Mapping.rollback();
				Logger.getInstance().error(	e.getMessage(), PublicationAction.class,e);
				return (mapping.findForward("errorIon"));
				//throw new ServletException(e);
			} finally {
				Mapping.rollback();
			}
			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// dans le cas ce n'est pas la validation
		if (request.getParameter("ok") == null) {
			try {
				init(f, id, sid, request);
			} catch (Exception e) {
				Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
				return (mapping.findForward("errorIon"));
			}
			return (mapping.findForward("view"));
		}

		// sinon, on vérifie et mettre à jour la fiche
		// fill data | errors
		ActionErrors errors = f.myValidate(request);
		if (errors.size() > 0) {
			try {
				init(f, id, sid, request);
			} catch (Exception e) {
				Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
				return (mapping.findForward("errorIon"));
			}
			request.setAttribute(ERROR_KEY, errors);
			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// all it's ok : update publication
		try {
			Mapping.begin();
			Publication publication = Publication.getInstance(id);

			String siteId = f.getSiteId();

			// Update section
			String[] sectionIdForms = f.getSections();
			if (sectionIdForms ==null) {
				sectionIdForms = new String[0];
			}
			String[] publiSectionIds = publication.getSectionsIds(siteId);

			// on ne mets à jour que s'il y a une section changée dans la liste
			if (! Arrays.equals(sectionIdForms, publiSectionIds)){
				Vector _reset = new Vector();
				for (int i = 0; i < publiSectionIds.length; i++)
					_reset.add(publiSectionIds[i]);

				// publication.resetSections(siteId);
				List sectionsAdd = new ArrayList();
				for (int i = 0; i < sectionIdForms.length; i++) {
					sectionsAdd.add(Section.getInstance(sectionIdForms[i]));
				}
				Vector sections=publication.getSections();
				List sectionsSite = new ArrayList();
				for (Iterator it=sections.iterator(); it.hasNext();) {
					Section section = (Section)it.next();
					if (section.getId().startsWith(siteId)) {
						sectionsSite.add(section);
					}
				}

				for (Iterator it= sectionsAdd.iterator(); it.hasNext(); ){
					Section section = (Section)it.next();
					if (!sectionsSite.contains(section)) {
						publication.addSection(section);
					}
				}
				for (Iterator it= sectionsSite.iterator(); it.hasNext(); ){
					Section section = (Section)it.next();
					if (!sectionsAdd.contains(section)) {
						publication.removeSection(section);
					}
				}
/*


				for (int i = 0; i < sectionIdForms.length; i++) {
					publication.addSection(Section.getInstance(sectionIdForms[i]));
					if (!_reset.contains(sectionIdForms[i]))
						PublicationSorter.resetPublication(publication, Section.getInstance(sectionIdForms[i]));
				}
*/
				// lancer une exception dans le cas ou la publication ne contient pas de section
				if (publication.getSections().size()==0) {
					throw new StandardException("La publication liée à aucune section");
				}
			}
			if (f.getN() != null && !"".equals(f.getN())
						&& !f.getN().equals(publication.getMetaData("name"))) {
				publication.setMetaData("name", f.getN());
			}
			publication.setMetaData("lcPresentation", f.getP());
			publication.setMetaData("lcDatePublication", f.getDp());
			publication.setMetaData("lcDateFin", f.getDf());
			publication.setMetaData("lcDateChange", f.getDc());
			if (publication.getType().getId().equals("objet")) {
				publication.setMetaData("objet", f.getO());
				publication.setMetaData("objetPresentation", f.getOp());
			}
			publication.setMetaData("visibility", f.getCurseurVisibilite());
			publication.setDate(new java.util.Date());
			Mapping.commit();
		} catch (StandardException e) {
			errors = new ActionErrors();
			ActionError error = new ActionError("error.createPublication.nosection");
			errors.add("n", error);
		}
		catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		if (errors.size() > 0) {
			try {
				init(f, id, sid, request);
			} catch (Exception e) {
				Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
				return (mapping.findForward("errorIon"));
			}
			request.setAttribute(ERROR_KEY, errors);
			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// Forward to the next page
		request.setAttribute("sid", sid);
		return (mapping.findForward("ok"));
	}

	/**
	 * 	Initialisation du formulaire
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void init(PublicationForm f, String p_PublicationId, String p_SectionId, HttpServletRequest request) throws Exception {
		try {
			Mapping.begin();
			Publication publication = Publication.getInstance(p_PublicationId);
			Section section = Section.getInstance(p_SectionId);
			request.setAttribute("section", section);

			TypePublication typeP = publication.getType();
			Vector styleNames = typeP.listStyles();
			Vector styles = new Vector();
			boolean _check = false;
			for (int i = 0; i < styleNames.size(); i++) {
				String n = styleNames.get(i).toString();
				if (((TypePublicationImpl) typeP).getStyleSheet(n)
						.getType() == Style.XSL
						&& !n.startsWith("indexation")
						&& !n.startsWith("special")) {
					styles.add(n);
				} else if (n.startsWith("indexation") && n.endsWith("pk"))
					_check = true;
			}
			if (_check)
				f.setType(publication.getType().getId());
			java.util.Collections.sort(styles);
			request.setAttribute("styles", styles);

			String siteid=f.getSiteId();
			request.setAttribute("sectionList", generate(request, siteid, publication));

			f.setSections(publication.getSectionsIds());

			request.setAttribute("publication", publication);
			if (publication.getType().getId().equals("objet"))
				request.setAttribute("objets", Objets.getObjetXmlBeans());

		} catch (MappingException e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
			throw e;
			//throw new ServletException(e);
		} finally {
			Mapping.rollback();
		}
	}


	private String getSiteId(String p_SectionId) {
		String[] ids = p_SectionId.split("_");
		String id = ids[0];
		return id;
	}

	/**
	 * 	Génération des sites
	 * @param p_Sections
	 * @return
	 */
	private Collection generateBeanSites(Vector p_Sections) {
		ArrayList ret = new ArrayList();
		for (Iterator it=p_Sections.iterator(); it.hasNext();) {
			Section section=(Section)it.next();
			Option option = new Option((String)section.getMetaData("name"), section.getId());
			ret.add(option);
		}
		return ret;
	}

	/**
	 * 	Générer la liste des sections possibles pour attacher la publication
	 * 	La règle générale est que la publication peut être attachée à une section si
	 * 	la section contient au moins un type de publication commun au type de publication de la publication
	 * 	La liste est ensuite triée par l'ordre alphabétique d'identifiant
	 * @param p_Request
	 * @param sid
	 * @param p_Publication
	 * @return
	 */
	private Vector generate(HttpServletRequest p_Request, String p_SiteId, Publication p_Publication) {


		Vector retSections = new Vector();
		String typePublication = p_Publication.getType().getId();
		Vector vTemplate = new Vector();
		try {
			if (org.nextime.ion.framework.cache.Util.check()) {
				String id = p_SiteId;
				File[] files = org.nextime.ion.framework.cache.Util.list(id + "_");
				if (files != null) {
					for (int j = 0; j < files.length; j++) {
						try {
							Hashtable ht = TreeCache.load(files[j]);
							String template = (String) ht.get("template");
							if (vTemplate.contains(template))
								continue;
							TypeBean type = SectionTypes.getSectionBean(servlet, template, p_Request);
							// on ne peut qu'affecter un publication à une section qui a au moins un
							// publication-types commun au type publication de la publication passé en paramètre
							if (type!=null && type.getPublicationTypes().contains(typePublication))
								retSections.add(new org.nextime.ion.framework.cache.Section(TreeCache.reverse(files[j])));
							else
								vTemplate.add(template);
						} catch (Exception e) {
							// TODO
							Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
						}
					}
				}
			} else {
				Vector v = new Vector();
				v.add(Section.getInstance(p_SiteId));
				java.util.Vector vLc = org.nextime.ion.framework.business.Section.listAll(v);
				for (int t = 0; t < vLc.size(); t++) {
					try {
						String template = (String) ((org.nextime.ion.framework.business.Section) vLc
								.get(t)).getMetaData("template");
						if (vTemplate.contains(template))
							continue;
						TypeBean type = SectionTypes.getSectionBean(servlet, template, p_Request);
						if (type!=null &&  type.getPublicationTypes().contains(typePublication))
							retSections.add(vLc.get(t));
						else
							vTemplate.add(template);
					} catch (Exception e) {
						// TODO
						Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
					}
				}
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationAction.class,e);
		}

		// on trie les identifiants de sections par son id
		Collections.sort(retSections, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					Cache section1 = (Cache) arg0;
					Cache section2 = (Cache) arg1;
					return section1.getId().compareTo(section2.getId());
				}
		});
		return retSections;
	}
}