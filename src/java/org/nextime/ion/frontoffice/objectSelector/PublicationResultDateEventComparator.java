package org.nextime.ion.frontoffice.objectSelector;

import java.util.Comparator;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.nextime.ion.commons.SearchQueryUtil;

/**
 * Classe implémente l'interface Comparator, elle permet de trier
 * une liste de manifestations selon la date de début d'événement.
 * @author m.idri
 */
public class PublicationResultDateEventComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		int compare = 0;

		PublicationResult publicationResult1 = (PublicationResult) o1;
		PublicationResult publicationResult2 = (PublicationResult) o2;

		String dateStr = publicationResult1.getView();
		String dateStr1 = publicationResult2.getView();

		if (StringUtils.isBlank(dateStr))
			dateStr = "01/01/3000";
		if (StringUtils.isBlank(dateStr1))
			dateStr1 = "01/01/3000";

		Date date = SearchQueryUtil.parseDate(dateStr);
		Date date1 = SearchQueryUtil.parseDate(dateStr1);

		if (date != null && date1 != null) {
			if (date.after(date1))
				compare = 1;
			else if (date.before(date1))
				compare = -1;
			else
				compare = 0;
		}
		else {
			compare = -1;
		}

		return compare;
	}
}
