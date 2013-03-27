package org.nextime.ion.commons;

import org.nextime.ion.framework.helper.SearchResult;
import org.nextime.ion.framework.logger.Logger;

import java.util.Comparator;

public class SearchDpComparator implements Comparator<SearchResult> {

    public SearchDpComparator() {
    }

    public int compare(SearchResult arg0, SearchResult arg1) {
        try {
            String n1 = (String) arg0.getResult().get("lcDatePublication");
            String n2 = (String) arg1.getResult().get("lcDatePublication");
            return n1.compareTo(n2);
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), SearchDpComparator.class, e);
            return 0;
        }
    }
}