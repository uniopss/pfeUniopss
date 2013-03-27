package fr.asso.uniopss.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.RangeQuery;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.action.security.CreateGroupAction;
import org.nextime.ion.backoffice.bean.Option;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.commons.SearchQueryUtil;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
/**
 *	La recherche avancé le backoffice
 * @author q.nguyen
 */
public class StatProdAction extends BaseAction {

		private static Collection<String> AVAIL_GROUPS_PROFILS = new ArrayList<String>();
	static {
		AVAIL_GROUPS_PROFILS.add("admins");
		AVAIL_GROUPS_PROFILS.add("gAuteurs");
		AVAIL_GROUPS_PROFILS.add("gValideurs");
	}

	private static String SECTEUR_ACTIVITE_SECTION = "trco_cont_alau_sact";

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {


		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canAdminSecurity(User.getInstance(request.getSession().getAttribute("userLogin") + ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreateGroupAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		StatProdForm frm = (StatProdForm)form;
		initForm(mapping, form, request, response);

		StatResult result = new StatResult();
		try {
		if (frm.isPostBack()) {
				// site
				Vector sites = new Vector();
				sites.add(frm.getSelectedSite());

				StringBuffer nonTranslateQuery = new StringBuffer();

				if (StringUtils.isNotEmpty(frm.getTypePublicationId())) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery, "+type", frm.getTypePublicationId());
				}

				Date dateCreationLess = SearchQueryUtil.parseDate(frm.getDateCreationBeforeStr());
				Date dateCreationMore = SearchQueryUtil.parseDate(frm.getDateCreationAfterStr());

				if (StringUtils.isNotEmpty(frm.getAuthorId())) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery, "+auteur",frm.getAuthorId());
				}

	            // recherche par range de date creation
	            RangeQuery dateCreationQuery = SearchQueryUtil.createDateRange("+dateCreation", dateCreationLess ,dateCreationMore);

	            if (dateCreationQuery!=null ) {
	            	SearchQueryUtil.addKeyWord(nonTranslateQuery , dateCreationQuery.toString());
				}

	            // Recherche suivant le paramètre date de publication
	            Date datePublicationLess = SearchQueryUtil.parseDate(frm.getDatePublicationBeforeStr());
				Date datePublicationMore = SearchQueryUtil.parseDate(frm.getDatePublicationAfterStr());

				// Créer l'intervalle de date de publication
				RangeQuery datePublicationQuery = SearchQueryUtil.createDateRange("+lcDatePublication", datePublicationLess ,datePublicationMore);

				if (datePublicationQuery!=null ) {
	            	SearchQueryUtil.addKeyWord(nonTranslateQuery , datePublicationQuery.toString());
				}

	            String secteurActivite = frm.getSecteurActiviteId();
				if (StringUtils.isNotEmpty(secteurActivite)) {
					// prendre le dernier domaine
					String domaineId = StringUtils.substringAfterLast(secteurActivite ,"_");
					if (StringUtils.isNotEmpty(domaineId)) {
						nonTranslateQuery.append(" +domaine:( "+domaineId+ " ) ");
					}
				}

				result = Searcher.searchStat(sites, nonTranslateQuery.toString(), "fr", "co");
			}
			request.setAttribute("resultCount", result.getCount());
			request.setAttribute("result", result.getSearchResults());
		}
		catch (ParseException e ) {
			Logger.getInstance().fatal("Erreur de paser les critère entré ", StatProdAction.class);
			saveKeyError(request, "errorParser");
			return (mapping.findForward("ok"));
		}
		catch (IllegalArgumentException e) {
			Logger.getInstance().fatal("Veuillez sasir au moins un critère ", StatProdAction.class);
			saveKeyError(request, "errorFewParam");
			return (mapping.findForward("ok"));
		}
		//		 Forward to the next page
		return (mapping.findForward("ok"));
	}

	/**
	 * 	Initilisation des données communes du formulaire
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void initForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		try {
			StatProdForm frm = (StatProdForm)form;

			// récupération les sites courantes de l'utilisateur en session
			Vector sites = (java.util.Vector) request.getSession().getAttribute("currentSite");
			frm.setSites(generateBeanSites(sites));


			Mapping.begin();
			Vector typePublications=TypePublication.listAll();

			// récupérer les groupes techiques
			Collection<Group> profils = new ArrayList<Group>();
			for (Iterator<String> it=AVAIL_GROUPS_PROFILS.iterator(); it.hasNext(); ){
				profils.add(Group.getInstance(it.next()));
			}
			frm.setProfils(profils);

			// récupérer les utilisateurs ( selon sites et groupe technique )
			Vector users = User.listAll();
			// filtre par rapport au list
			Group groupSite = null;
			if (StringUtils.isNotEmpty(frm.getSelectedSite())) {
				groupSite = Group.getInstance("_"+ frm.getSelectedSite());
			}
			Group groupProfil = null;
			if (StringUtils.isNotEmpty(frm.getProfilId())) {
				groupProfil = Group.getInstance(frm.getProfilId());
			}
			Vector <User> filtreUsers = new Vector <User>();
			for (Iterator<User> it=users.iterator(); it.hasNext();) {
				boolean addInList = true;
				User user = it.next();
				if (groupSite!=null) {
					if (!user.isInGroup(groupSite)) {
						addInList = false;
					}
				}
				if (groupProfil!=null) {
					if (!user.isInGroup(groupProfil)) {
						addInList = false;
					}
				}
				if (addInList) {
					filtreUsers.add(user);
				}
			}
			frm.setAuthors(filtreUsers);
			// récupérer tous les secteur d'activité de trco
			Section section = Section.getInstance(SECTEUR_ACTIVITE_SECTION);
			Vector sections = section.listSubSections();
			frm.setSecteurActivites(sections);

			Mapping.rollback();
			frm.setTypePublications(typePublications);
		} catch (MappingException e) {
			Logger logger=Logger.getInstance();
			logger.error("Erreur de récupération des type de publications ", StatProdAction.class,e);
			throw new ServletException(e);
		} finally {
			Mapping.rollback();
		}
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










}

