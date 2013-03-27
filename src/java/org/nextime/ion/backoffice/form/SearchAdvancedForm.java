package org.nextime.ion.backoffice.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * 	Formulaire représentant les critère de recherche backooffice c
 * 	dans le mode avancé
 *
 * @author q.nguyen
 *
 */
public class SearchAdvancedForm extends ActionForm {

	/**
	 *
	 */
	private static final long serialVersionUID = -8074530317599943474L;

	private String EMPTY_STRING = "";

	private String keyWords= EMPTY_STRING;

	private Collection sites;
	private String selectedSite= EMPTY_STRING;
	private String authorId= EMPTY_STRING; // l'identifiant de la personne créateur de publication

	private String datePublicationBeforeStr = EMPTY_STRING; // date publication inférieur
	private String datePublicationAfterStr = EMPTY_STRING; // date publication supérieur

	private String dateCreationBeforeStr = EMPTY_STRING; // date de création inférieur
	private String dateCreationAfterStr = EMPTY_STRING; // date de création supérieur

	private String FIELD_mots_cles = EMPTY_STRING; // un ensemble de mot clés saisit depuis l'interface

	private String noFiche = EMPTY_STRING; // le numéro de fiche

	private Collection typePublications;	// une collection des types de publications
	private String typePublicationId= EMPTY_STRING; // type de contenus

	private String FIELD_ouvrage_local = EMPTY_STRING; // location ouvrage

	private String sortDirection= EMPTY_STRING; // ascending ou descending
	private String sortBy= EMPTY_STRING; // par quel colonne

	private boolean postBack = false; // first time ou le même formulaire est repost

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
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

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}



	public String getNoFiche() {
		return noFiche;
	}

	public void setNoFiche(String noFiche) {
		this.noFiche = noFiche;
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

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public boolean isPostBack() {
		return postBack;
	}

	public void setPostBack(boolean postBack) {
		this.postBack = postBack;
	}

	public String getSelectedSite() {
		return selectedSite;
	}

	public void setSelectedSite(String selectedSite) {
		this.selectedSite = selectedSite;
	}

	public String getFIELD_mots_cles() {
		return FIELD_mots_cles;
	}

	public void setFIELD_mots_cles(String field_mots_cles) {
		FIELD_mots_cles = field_mots_cles;
	}

	public String getFIELD_ouvrage_local() {
		return FIELD_ouvrage_local;
	}

	public void setFIELD_ouvrage_local(String field_ouvrage_local) {
		FIELD_ouvrage_local = field_ouvrage_local;
	}



}
