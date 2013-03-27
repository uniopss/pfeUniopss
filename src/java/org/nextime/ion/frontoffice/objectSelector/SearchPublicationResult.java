package org.nextime.ion.frontoffice.objectSelector;

import org.nextime.ion.framework.helper.SearchResult;

public class SearchPublicationResult {

	private SearchResult searchResult;

	private String view;

	private PublicationResult publicationResult;

	/**
	 * Returns the publicationResult.
	 * 
	 * @return PublicationResult
	 */
	public PublicationResult getPublicationResult() {
		return publicationResult;
	}

	/**
	 * Returns the searchResult.
	 * 
	 * @return SearchResult
	 */
	public SearchResult getSearchResult() {
		return searchResult;
	}

	/**
	 * Returns the view.
	 * 
	 * @return String
	 */
	public String getView() {
		return view;
	}

	/**
	 * Sets the publicationResult.
	 * 
	 * @param publicationResult
	 *            The publicationResult to set
	 */
	public void setPublicationResult(PublicationResult publicationResult) {
		this.publicationResult = publicationResult;
	}

	/**
	 * Sets the searchResult.
	 * 
	 * @param searchResult
	 *            The searchResult to set
	 */
	public void setSearchResult(SearchResult searchResult) {
		this.searchResult = searchResult;
	}

	/**
	 * Sets the view.
	 * 
	 * @param String
	 *            The view to set
	 */
	public void setView(String view) {
		this.view = view;
	}
}