package org.nextime.ion.backoffice.action.search;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.SortField;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.form.SearchAdvancedForm;
import org.nextime.ion.commons.SearchQueryUtil;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.impl.PublicationDocument;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.helper.SortCriteria;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.framework.xml.XMLException;


import fr.asso.uniopss.synonyme.SynonymeService;
import fr.asso.uniopss.synonyme.SynonymeManager;
/**
 *	La recherche avancé le backoffice
 * @author q.nguyen
 */
public class SearchAdvancedAction extends BaseAction {

	private static int MAX_RESULT = 25;

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {


		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		SearchAdvancedForm frm = (SearchAdvancedForm)form;

		initForm(mapping, form, request, response);

		SynonymeManager synonymes=null;
		try {
			synonymes=SynonymeService.getInstance().getSynonymes();
			synonymes.setFileName(Config.getInstance().getSynonymeFileName());
		} catch (XMLException e) {
			Logger.getInstance().fatal("Erreur de paser le fichier XML de synonyme ", SearchAdvancedAction.class);
			saveKeyError(request, "errorSynonyme");
			return (mapping.findForward("view"));
		} catch (IOException e) {
			Logger.getInstance().fatal("Erreur de paser le fichier XML de synonyme ", SearchAdvancedAction.class);
			saveKeyError(request, "errorSynonyme");
			return (mapping.findForward("view"));
		}

		List  result = new FastArrayList();
		try {
		if (frm.isPostBack()) {
				// site
				Vector sites = new Vector();
				sites.add(frm.getSelectedSite());

				String  humankeyWords= null;
				StringBuffer  sbKeyWords= new StringBuffer();
				if (StringUtils.isNotEmpty(frm.getKeyWords())) {
					humankeyWords=frm.getKeyWords();
				}

				StringBuffer nonTranslateQuery = new StringBuffer();

				if (StringUtils.isNotEmpty(frm.getAuthorId())) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery, "auteur",frm.getAuthorId());
				}

				if (StringUtils.isNotEmpty(frm.getNoFiche())) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery, "noFiche",frm.getNoFiche());
				}
				if (StringUtils.isNotEmpty(frm.getTypePublicationId())) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery, "type", frm.getTypePublicationId());
				}

				// fuzionner les mot clés
				Set keyWordsThesaurus = parseKeyWords(frm.getFIELD_mots_cles());
				Set synKeyWordsThesaurus=synonymes.findKeyWords(keyWordsThesaurus);
				keyWordsThesaurus.addAll(synKeyWordsThesaurus);

				Set localisationOuvrage = parseKeyWords(frm.getFIELD_ouvrage_local());
				Set synKeyWordsOuvrage=synonymes.findKeyWords(localisationOuvrage);
				localisationOuvrage.addAll(synKeyWordsOuvrage);

				Date dateCreationLess = SearchQueryUtil.parseDate(frm.getDateCreationBeforeStr());
				Date dateCreationMore = SearchQueryUtil.parseDate(frm.getDateCreationAfterStr());
				Date datePublicationLess = SearchQueryUtil.parseDate(frm.getDatePublicationBeforeStr());
				Date datePublicationMore = SearchQueryUtil.parseDate(frm.getDatePublicationAfterStr());



	            // recherche par range de date creation
	            RangeQuery dateCreationQuery = SearchQueryUtil.createDateRange("dateCreation", dateCreationLess ,dateCreationMore);
	            RangeQuery datePublicationQuery = SearchQueryUtil.createDateRange("lcDatePublication", datePublicationLess, datePublicationMore);

	            if (dateCreationQuery!=null ) {
	            	SearchQueryUtil.addKeyWord(nonTranslateQuery , dateCreationQuery.toString());
				}

	            if (datePublicationQuery !=null ) {
	            	SearchQueryUtil.addKeyWord(nonTranslateQuery , datePublicationQuery.toString());
				}

	            String thesaurusQuery = SearchQueryUtil.generateKeyWordQuery(keyWordsThesaurus);
				if (StringUtils.isNotEmpty(thesaurusQuery )) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery, "lcThesaurus",thesaurusQuery);
				}
				String localisationOuvrageQuery = SearchQueryUtil.generateKeyWordQuery(localisationOuvrage);
				if (StringUtils.isNotEmpty(localisationOuvrageQuery)) {
					SearchQueryUtil.addKeyWord(nonTranslateQuery , "lcLocalisationOuvrage",localisationOuvrageQuery);
				}


	            // Construction du query renseigné par l'utilisateur
                if (StringUtils.isNotEmpty(humankeyWords)) {
                		String humanQueryString = SearchQueryUtil.generateHumanQuery(synonymes, "co", humankeyWords, 10);
                		sbKeyWords.append(humanQueryString);

                		sbKeyWords.append(" ");
                		//SearchQueryUtil.addKeyWord(sbKeyWords,humanQueryString );

                		// Ajout d'un critère de recherche sur l'auteur commentaire avec un poids de 100.
                		String auteurCommentaireQuery = PublicationDocument.FIELD_AUTEUR_COMMENTAIRE + ":"+"\"" + humankeyWords+ "\"^"+ 100;
                	    sbKeyWords.append(auteurCommentaireQuery);
                }

                SortField sortField = SortCriteria.CRITERES.get(frm.getSortBy() + frm.getSortDirection());

                result=Searcher.searchAdvanced(sites,
												sbKeyWords.toString(),
												nonTranslateQuery.toString(),
												"fr",
												null,
												sortField,
												0,
												MAX_RESULT);

			}
			request.setAttribute("result", result);

		}
		catch (ParseException e ) {
			Logger.getInstance().fatal("Erreur de paser les critère entré ", SearchAdvancedAction.class);
			saveKeyError(request, "errorParser");
			return (mapping.findForward("view"));
		}
		catch (XMLException e) {
			Logger.getInstance().fatal("Erreur de paser le fichier XML de synonyme ", SearchAdvancedAction.class);
			saveKeyError(request, "errorSynonyme");
			return (mapping.findForward("view"));
		}
		catch (IllegalArgumentException e) {
			Logger.getInstance().fatal("Veuillez sasir au moins un critère ", SearchAdvancedAction.class);
			saveKeyError(request, "errorFewParam");
			return (mapping.findForward("view"));
		}
		//		 Forward to the next page
		return (mapping.findForward("view"));
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

			SearchAdvancedForm frm = (SearchAdvancedForm)form;
			Mapping.begin();
			Vector typePublications=TypePublication.listAll();
			Mapping.rollback();
			frm.setTypePublications(typePublications);
		} catch (MappingException e) {
			Logger logger=Logger.getInstance();
			logger.error("Erreur de récupération des type de publications ", SearchAdvancedAction.class,e);
			throw new ServletException(e);
		} finally {
			Mapping.rollback();
		}
	}


	/**
	 *	Récuperer les mot-clés depuis la chaine de caractère repésentent les mot-clés
	 * @param p_DynamicListXml
	 * @return
	 */
	private Set parseKeyWords(String p_DynamicListXml) {
		Set ret = new HashSet();
		if (p_DynamicListXml==null || p_DynamicListXml.length()==0)
					return ret;
		Pattern pattern=Pattern.compile("<d>(.*?)</d>");
		Matcher matcher=pattern.matcher(p_DynamicListXml);
		while (matcher.find()) {
	           String texte = matcher.group();
	           ret.add(texte.substring(3, texte.length()-4));
		}
		return ret;
	}

	public static void main(String[] args) {
		String testStr="<lc><r ordre='0'><d>Mot clé 2</d></r><r ordre='1'><d>mot clé 2.2</d></r></lc>";
		SearchAdvancedAction action = new SearchAdvancedAction();
		Set test=action.parseKeyWords(testStr);
		testStr="<lc><r ordre='0'><l></l><d>Ouvrage localisation 1</d></r><r ordre='1'><l></l><d>Ouvrage localisation 2</d></r><r ordre='2'><l></l><d>Ouvrage localisation 3</d></r><r ordre='3'><l></l><d>Ouvrage localisation 4</d></r></lc>";
		test=action.parseKeyWords(testStr);
	}




}

