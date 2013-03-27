package fr.asso.uniopss.stat;

import java.util.Vector;

import org.nextime.ion.framework.helper.SearchResult;


/**
 *	Réprésent le résultat de la statistique production
 * @author q.nguyen
 *
 */
public class StatResult {

	private int _count;
	private Vector<SearchResult> _searchResults;

	public StatResult () {
		_count = 0;
		_searchResults = new Vector<SearchResult>();
	}

	public StatResult (int p_Count, Vector<SearchResult> p_SearchResult) {
		_count = p_Count;
		_searchResults = p_SearchResult;
	}

	public int getCount() {
		return _count;
	}
	public void setCount(int count) {
		this._count = count;
	}
	public Vector<SearchResult> getSearchResults() {
		return _searchResults;
	}
	public void setSearchResults(Vector<SearchResult> searchResults) {
		this._searchResults = searchResults;
	}

}
