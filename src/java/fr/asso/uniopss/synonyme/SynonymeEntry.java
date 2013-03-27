package fr.asso.uniopss.synonyme;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.lang.StringUtils;
import org.nextime.ion.commons.Util;

/**
 * 	Réprésente en ensemble des mot-clés synonymee
 * @author q.nguyen
 *
 */
public class SynonymeEntry {

	private List _synonymes;

	public SynonymeEntry() {
		_synonymes=new FastArrayList();
	}

	public void add(String p_KeyWord) {
		if (StringUtils.isNotEmpty(p_KeyWord)) {
			_synonymes.add(p_KeyWord);
		}
	}

	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("type[Synonyme] properties[");
		for (Iterator it=_synonymes.iterator(); it.hasNext();) {
			sb.append(it.next()).append("/");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 *	Déterminer si le mot-clés se trouve dans cet ensemble
	 *		Le recherche est sans distinction entre les minuscule et les majuscule
	 *		ainsi que les accents
	 * @param p_KeyWordToFind
	 * @return
	 */
	public boolean isMatchKeywords(String p_KeyWordToFind) {
		if (StringUtils.isEmpty(p_KeyWordToFind))
				return false;
		boolean founded = false;
		String keyWordToFind = Util.translate(p_KeyWordToFind);
		for (Iterator it=_synonymes.iterator(); it.hasNext() && !founded;) {
				String keyword=(String)it.next();
				keyword=Util.translate(keyword);
				if( keyWordToFind.equalsIgnoreCase(keyword.trim())) {
					founded=true;
				}
		}
		return founded;
	}

	public List getKetWords() {
		return _synonymes;
	}

}
