package fr.asso.uniopss.stat;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

public class StatProdForm extends ActionForm {

	/**
	 *
	 */
	private static final long serialVersionUID = -1537746918127033855L;
	private Collection sites;
	private String selectedSite= StringUtils.EMPTY;

	private String dateCreationBeforeStr = StringUtils.EMPTY; // date de création inférieur
	private String dateCreationAfterStr = StringUtils.EMPTY; // date de création supérieur

	private String datePublicationBeforeStr = StringUtils.EMPTY; // date de création inférieur
	private String datePublicationAfterStr = StringUtils.EMPTY; // date de création supérieur

	private Collection typePublications;	// une collection des types de publications
	private String typePublicationId= StringUtils.EMPTY; // type de contenus

	private Collection profils;
	private String profilId = StringUtils.EMPTY;
	private Collection authors;
	private String authorId= StringUtils.EMPTY; // l'identifiant de la personne créateur de publication

	private Collection secteurActivites;
	private String secteurActiviteId = StringUtils.EMPTY;

	private boolean postBack = false; // first time ou le même formulaire est repost

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public Collection getAuthors() {
		return authors;
	}

	public void setAuthors(Collection authors) {
		this.authors = authors;
	}

	public String getDateCreationAfterStr() {
		return dateCreationAfterStr;
	}

	public void setDateCreationAfterStr(String dateCreationAfterStr) {
		this.dateCreationAfterStr = dateCreationAfterStr;
	}

	public String getDateCreationBeforeStr() {
		return dateCreationBeforeStr;
	}

	public void setDateCreationBeforeStr(String dateCreationBeforeStr) {
		this.dateCreationBeforeStr = dateCreationBeforeStr;
	}

	public boolean isPostBack() {
		return postBack;
	}

	public void setPostBack(boolean postBack) {
		this.postBack = postBack;
	}

	public String getProfilId() {
		return profilId;
	}

	public void setProfilId(String profilId) {
		this.profilId = profilId;
	}

	public Collection getProfils() {
		return profils;
	}

	public void setProfils(Collection profils) {
		this.profils = profils;
	}

	public String getSecteurActiviteId() {
		return secteurActiviteId;
	}

	public void setSecteurActiviteId(String secteurActiviteId) {
		this.secteurActiviteId = secteurActiviteId;
	}

	public Collection getSecteurActivites() {
		return secteurActivites;
	}

	public void setSecteurActivites(Collection secteurActivites) {
		this.secteurActivites = secteurActivites;
	}

	public String getSelectedSite() {
		return selectedSite;
	}

	public void setSelectedSite(String selectedSite) {
		this.selectedSite = selectedSite;
	}

	public Collection getSites() {
		return sites;
	}

	public void setSites(Collection sites) {
		this.sites = sites;
	}

	public String getTypePublicationId() {
		return typePublicationId;
	}

	public void setTypePublicationId(String typePublicationId) {
		this.typePublicationId = typePublicationId;
	}

	public Collection getTypePublications() {
		return typePublications;
	}

	public void setTypePublications(Collection typePublications) {
		this.typePublications = typePublications;
	}

	public String getDatePublicationAfterStr() {
		return datePublicationAfterStr;
	}

	public void setDatePublicationAfterStr(String datePublicationAfterStr) {
		this.datePublicationAfterStr = datePublicationAfterStr;
	}

	public String getDatePublicationBeforeStr() {
		return datePublicationBeforeStr;
	}

	public void setDatePublicationBeforeStr(String datePublicationBeforeStr) {
		this.datePublicationBeforeStr = datePublicationBeforeStr;
	}

}
