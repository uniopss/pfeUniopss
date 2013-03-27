package org.nextime.ion.commons;

import java.util.Comparator;
import org.nextime.ion.framework.business.Publication;

public class PublicationDateComparator implements Comparator {

	public PublicationDateComparator() {
	}

	public int compare(Object arg0, Object arg1) {
		try {
			Publication p1 = (Publication) arg0;
			Publication p2 = (Publication) arg1;
			java.util.Date date1 = p1.getDate();
			java.util.Date date2 = p2.getDate();
			return date1.compareTo(date2);
		} catch (Exception e) {
			return 0;
		}
	}
}