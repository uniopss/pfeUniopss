package org.nextime.ion.commons;

import java.util.Comparator;

import org.nextime.ion.framework.business.Publication;

public class PublicationNameComparator implements Comparator {

	public PublicationNameComparator() {
	}

	public int compare(Object arg0, Object arg1) {
		try {
			Publication p1 = (Publication) arg0;
			Publication p2 = (Publication) arg1;
			String n1 = (String) p1.getMetaData("name");
			String n2 = (String) p2.getMetaData("name");
			return n1.compareTo(n2);
		} catch (Exception e) {
			return 0;
		}
	}
}