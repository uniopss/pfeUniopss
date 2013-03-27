package org.nextime.ion.framework.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.nextime.ion.commons.SearchQueryUtil;
import org.nextime.ion.framework.business.*;
import org.nextime.ion.framework.business.impl.PublicationDocument;
import org.nextime.ion.framework.cache.Cache;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.frontoffice.objectSelector.NavigationBar;
import org.nextime.ion.frontoffice.smartCache.SmartCacheManager;
import org.apache.commons.collections.FastArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;

public class SearchResult implements java.lang.Comparable {

	private Publication publication;

	private String section;

	private int version;

	private float score;

	private String nameAuteur;

	private Hashtable result = new Hashtable();

	private final static String[] RESULT_KEYS = {
			PublicationDocument.FILED_ID,
			PublicationDocument.FILED_NO_FICHE,
			PublicationDocument.FIELD_VERSION,
			PublicationDocument.FIELD_VERSION_ID,
			PublicationDocument.FIELD_NAME,
			PublicationDocument.FIELD_TYPE,
			PublicationDocument.FIELD_AUTEUR,
			PublicationDocument.FIELD_AUTEUR_COMMENTAIRE,
			PublicationDocument.FIELD_DATE,
			PublicationDocument.FIELD_LC_DATE_PUBLICATION,
			PublicationDocument.FIELD_SECTIONS,
			PublicationDocument.FIELD_ION_PUBLICATION,
			PublicationDocument.FIELD_DATE_CREATION,
			PublicationDocument.FIELD_DATE_EVENT_BEGIN,
			PublicationDocument.FIELD_VISIBILITY };

	public SearchResult(Publication p, int version, float s) {
		setPublication(p);
		setVersion(version);
		setScore(s);
	}

	public SearchResult(Document doc, float s) {
		setScore(s);
		setVersion(Integer.parseInt(doc.get(PublicationDocument.FIELD_VERSION)));

		// Récupération du nom/prénom de l'auteur
		if (doc.get(PublicationDocument.FIELD_AUTEUR) != null) {

			try {
				if (Mapping.getInstance().isTransactionActive()){
					User user = User.getInstance(doc.get(PublicationDocument.FIELD_AUTEUR));

					if(user != null && user.getMetaData("name") != null)
						setNameAuteur(user.getMetaData("name").toString());
				}
				else{
					try {
						Mapping.begin();
						User user = User.getInstance(doc.get(PublicationDocument.FIELD_AUTEUR));

						if(user != null && user.getMetaData("name") != null)
							setNameAuteur(user.getMetaData("name").toString());
					} catch (Exception e) {
						Mapping.rollback();
						Logger.getInstance().error(e.getMessage(), SearchResult.class,e);
					} finally {
						Mapping.rollback();
					}
				}
			} catch (MappingException e) {
				Logger.getInstance().error(e.getMessage(), SearchResult.class,e);
			}
		}
		for (int i = 0; i < RESULT_KEYS.length; i++) {
			String value = StringUtils.EMPTY;
			if (doc.get(RESULT_KEYS[i]) != null) {
				value = doc.get(RESULT_KEYS[i]);

			}
			result.put(RESULT_KEYS[i], value);
		}
		try {
			String[] sections = doc.get(PublicationDocument.FIELD_SECTIONS).split(" ");
			// first section
			setSection(sections[0]);

			// set secteurActivie Map

		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), SearchResult.class, e);
		}
	}

	/**
	 * Returns the publication.
	 *
	 * @return Publication
	 */
	public Publication getPublication() {
		return publication;
	}

	/**
	 * Returns the section.
	 *
	 * @return String
	 */
	public String getSection() {
		return section;
	}

	/**
	 * Returns the score.
	 *
	 * @return float
	 */
	public float getScore() {
		return score;
	}

	/**
	 * Sets the publication.
	 *
	 * @param publication
	 *            The publication to set
	 */
	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	public PublicationVersion getPublicationVersion() {
		return publication.getVersion(version);
	}

	/**
	 * Sets the section.
	 *
	 * @param String
	 *            The section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * Sets the score.
	 *
	 * @param score
	 *            The score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}

	/**
	 * Returns the version.
	 *
	 * @return int
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version
	 *            The version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Returns the result.
	 *
	 * @return Hashtable
	 */
	public Hashtable getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param Hashtable
	 *            The result to set
	 */
	public void setResult(Hashtable result) {
		this.result = result;
	}

	/**
	 * Returns the type.
	 *
	 * @return String
	 */
	public String getType() {
		return (String) result.get("type");
	}

	/**
	 * Returns the Auteur.
	 *
	 * @return String
	 */
	public String getAuteur() {
		return (String) result.get(PublicationDocument.FIELD_AUTEUR);
	}

	/**
	 * Returns the Auteur commentaire.
	 *
	 * @return String
	 */
	public String getAuteurCommentaire() {
		return (String) result.get(PublicationDocument.FIELD_AUTEUR_COMMENTAIRE);
	}

	/**
	 * Returns the Activities Sector.
	 *
	 * @return String
	 */
	public String getSecteurActivites() {

		// récupérer tous les secteur d'activité de trco
		try {
			String[] sectionsTab = getSections().split(" ");
			StringBuffer secteursActivites = new StringBuffer();
			if(sectionsTab.length > 0){
			for(int i=0;i<sectionsTab.length;i++){
				if(sectionsTab[i].contains("sact_"))
				secteursActivites.append(sectionsTab[i].split("sact_")[1] + " ");
			}
			return secteursActivites.toString();
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * Returns the id.
	 *
	 * @return String
	 */
	public String getId() {
		return (String) result.get("id");
	}

	/**
	 * Returns the noFiche.
	 *
	 * @return String
	 */
	public String getNoFiche() {
		return (String) result.get(PublicationDocument.FILED_NO_FICHE);
	}

	/**
	 * Returns the versionId.
	 *
	 * @return String
	 */
	public String getVersionId() {
		return (String) result.get("versionId");
	}

	/**
	 * Returns the name.
	 *
	 * @return String
	 */
	public String getName() {
		return (String) result.get("name");
	}

	/**
	 * Returns the date.
	 *
	 * @return String
	 */
	public String getDate() {
		return (String) result.get("date");
	}

	/**
	 * Returns the author.
	 *
	 * @return String
	 */
	public String getAuthor() {
		return (String) result.get("auteur");
	}

	/**
	 * Returns the content.
	 *
	 * @return String
	 */
	public String getContent() {
		return (String) result.get("_ionPublication");
	}

	/**
	 * Returns the sections.
	 *
	 * @return String
	 */
	public String getSections() {
		return (String) result.get("sections");
	}

	/**
	 * Returns the sections.
	 *
	 * @return String
	 */
	public String getVisibility() {
		return (String) result.get(PublicationDocument.FIELD_VISIBILITY);
	}

	/**
	 * Returns the date création
	 *
	 * @return String
	 */
	public String getDateCreation() {
		String dateStr = (String) result.get("dateCreation");
		try {
			Date date = DateTools.stringToDate(dateStr);
			return (new SimpleDateFormat(Config.getInstance()
					.getDateFormatPattern())).format(date);
		} catch (ParseException e) {
			Logger.getInstance().log("Erreur de date creation ",
					SearchResult.class);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Retourne la date de début d'événement
	 * @return String
	 */
	public String getDateEventBegin() {
		String dateStr = (String) result.get("dateEventBegin");
		try {
			Date date = DateTools.stringToDate(dateStr);
			return (new SimpleDateFormat(Config.getInstance()
					.getDateFormatPattern())).format(date);
		} catch (ParseException e) {
			Logger.getInstance().log("Erreur de date creation ",
					SearchResult.class);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Returns the date publication
	 *
	 * @return String
	 */
	public String getDatePublication() {
		String dateStr = (String) result
				.get(PublicationDocument.FIELD_LC_DATE_PUBLICATION);
		try {
			Date date = DateTools.stringToDate(dateStr);
			return (new SimpleDateFormat(Config.getInstance()
					.getDateFormatPattern())).format(date);
		} catch (ParseException e) {
			Logger.getInstance().log("Erreur de date publication ",
					SearchResult.class);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Returns the first section name found for this publication this methode
	 * does not throw exception
	 *
	 * @return String
	 */
	public String getSectionName() {
		String sectionId = getSection();
		return getSectionNameById(sectionId);
	}

	private String getSectionNameById(String p_SectionId) {
		try {
			Mapping.begin();
			Object cache = SmartCacheManager.getSection(p_SectionId);
			if (cache == null) {
				Section section = null;
				section = Section.getInstance(p_SectionId);
				return (String) section.getMetaData("name");
			} else {
				return (String) ((org.nextime.ion.framework.cache.Cache) cache)
						.getMetaData("name");
			}
		} catch (MappingException e) {
			Logger.getInstance().log("Erreur de récuperer la section ",
					p_SectionId);
		} finally {
			Mapping.rollback();
		}
		return StringUtils.EMPTY;

	}

	/**
	 * Returns all sites for Publications this methode does not throw exception
	 *
	 * @return String
	 */
	public List getAllSiteNames() {
		List ret = new FastArrayList();
		String sectionIds = getSections();
		// sections id is separated by one space
		String[] ids = sectionIds.split(" ");
		for (int i = 0; i < ids.length; i++) {
			// recupérer l'identifiant du site,
			String codeSite = StringUtils.substringBefore(ids[i], "_");
			if (StringUtils.isNotEmpty(codeSite)) {
				String siteName = getSectionNameById(codeSite);
				if (!ret.contains(siteName)) {
					ret.add(siteName);
				}
			}
		}
		return ret;
	}

	/**
	 * Returns all section for Publications this methode does not throw
	 * exception
	 *
	 * @return String
	 */
	public List getAllSectionNames() {
		List ret = new FastArrayList();
		String sectionIds = getSections();
		// sections id is separated by one space
		String[] ids = sectionIds.split(" ");
		for (int i = 0; i < ids.length; i++) {
			String sectionName = getPathName(ids[i]);
			ret.add(sectionName);
		}
		return ret;
	}

	private String getPathName(String p_SectionId) {
		List listPaths = new FastArrayList();
		try {
			Mapping.begin();
			org.nextime.ion.framework.cache.Section cs = null;
			if (Config.getInstance().getSectionCache()) {
				cs = new org.nextime.ion.framework.cache.Section(p_SectionId);
			}
			if (cs != null && cs.exists()) {
				try {
					String path = (String) cs.getMetaData("path");
					if (StringUtils.isNotEmpty(path.trim())) {
						String[] paths = path.substring(1).split("/");
						for (int i = 0; i < paths.length; i++) {
							Object s = SmartCacheManager.getSection(paths[i]);
							if (s != null) {
								listPaths.add(s);
							}
						}
					}
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(),
							NavigationBar.class, ee);
				}
			} else {
				Section current = Section.getInstance(p_SectionId);
				while (current != null) {
					listPaths.add(current);
					current = current.getParent();
				}
				Collections.reverse(listPaths);
			}
			Mapping.rollback();

		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject", this, e);
		} finally {
			Mapping.rollback();
		}
		StringBuffer ret = new StringBuffer();

		for (int i = 0; i < listPaths.size(); i++) {
			if (i > 1) {
				Cache cache = (Cache) listPaths.get(i);
				ret.append((String) cache.getMetaData("name"));
				if (i < listPaths.size() - 1) {
					ret.append(">");
				}

			}
		}
		return ret.toString();

	}

	public String getNameAuteur() {
		return nameAuteur;
	}

	public void setNameAuteur(String nameAuteur) {
		this.nameAuteur = nameAuteur;
	}

	/**
	 * Comprare les dates de début d'événement d'une publication.
	 */
	public int compareTo(Object other) {
		int compare = 0;

		String dateStr = ((SearchResult) other).getDateEventBegin();
		String dateStr1 = this.getDateEventBegin();

		if (StringUtils.isBlank(dateStr))
			dateStr = "01/01/3000";
		if (StringUtils.isBlank(dateStr1))
			dateStr1 = "01/01/3000";

		Date date = SearchQueryUtil.parseDate(dateStr);
		Date date1 = SearchQueryUtil.parseDate(dateStr1);

		if (date != null && date1 != null) {
			if (date.after(date1))
				compare = -1;
			else if (date.before(date1))
				compare = 1;
			else
				compare = 0;
		}
		else {
			compare = 1;
		}
		return compare;
	}
}