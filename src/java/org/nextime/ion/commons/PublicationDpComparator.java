package org.nextime.ion.commons;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import org.nextime.ion.framework.business.Publication;

public class PublicationDpComparator implements Comparator {

	static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public PublicationDpComparator() {
	}

	public int compare(Object arg0, Object arg1) {
		try {
			Publication p1 = (Publication) arg0;
			Publication p2 = (Publication) arg1;
			String date1 = (String) p1.getMetaData("lcDatePublication");
			String date2 = (String) p2.getMetaData("lcDatePublication");
			return (formatter.parse(date1)).compareTo(formatter.parse(date2));
		} catch (Exception e) {
			return 0;
		}
	}
}