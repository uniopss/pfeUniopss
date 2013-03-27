package org.nextime.ion.backoffice.action.search;

import javax.servlet.http.HttpServletRequest;

public class SearchAction extends BaseSearchAction {

	public String getType(HttpServletRequest request) {
		return "co";
	}

}