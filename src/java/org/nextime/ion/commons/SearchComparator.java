package org.nextime.ion.commons;

import java.util.Comparator;

import org.nextime.ion.framework.helper.SearchResult;
import org.nextime.ion.framework.logger.Logger;

public class SearchComparator implements Comparator {

	public SearchComparator() {
	}

	public int compare(Object arg0, Object arg1) {
		try {
			SearchResult p1 = (SearchResult) arg0;
			SearchResult p2 = (SearchResult) arg1;
			String n1 = (String) p1.getPublication().getMetaData("name");
			String n2 = (String) p2.getPublication().getMetaData("name");
			return n1.compareTo(n2);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), SearchComparator.class,e);
			return 0;
		}
	}
}