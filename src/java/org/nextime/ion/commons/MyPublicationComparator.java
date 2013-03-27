package org.nextime.ion.commons;

import java.util.Comparator;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

public class MyPublicationComparator implements Comparator {

	private Section section;

	public MyPublicationComparator(Section s) {
		section = s;
	}

	public int compare(Object arg0, Object arg1) {
		try {
			Publication p1 = (Publication) arg0;
			Publication p2 = (Publication) arg1;
			int index1 = Integer.parseInt(p1.getMetaData("index_"
					+ section.getId())
					+ "");
			int index2 = Integer.parseInt(p2.getMetaData("index_"
					+ section.getId())
					+ "");
			return index2 - index1;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), MyPublicationComparator.class,e);
			return 0;
		}
	}
}