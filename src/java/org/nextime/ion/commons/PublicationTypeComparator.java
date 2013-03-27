package org.nextime.ion.commons;

import java.util.Comparator;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.logger.Logger;

public class PublicationTypeComparator implements Comparator {

	public PublicationTypeComparator() {
	}

	public int compare(Object arg0, Object arg1) {
		try {
			Publication p1 = (Publication) arg0;
			Publication p2 = (Publication) arg1;
			String n1 = (String) p1.getType().getId();
			String n2 = (String) p2.getType().getId();
			return n1.compareTo(n2);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationTypeComparator.class,e);
			return 0;
		}
	}
}