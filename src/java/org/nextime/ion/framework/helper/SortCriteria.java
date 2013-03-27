package org.nextime.ion.framework.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.SortField;

public class SortCriteria {

	public static final SortField SORT_NAME = new SortField("name", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_SCORE = SortField.FIELD_SCORE;
	public static final SortField SORT_DATE_CREATION = new SortField("dateCreation", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_DATE_PUBLICATION = new SortField("lcDatePublication", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_TYPE_CONTENU = new SortField("type", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_AUTHOR = new SortField("auteur", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_DATE_EVENT_BEGIN= new SortField("dateEventBegin", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_SECTION = new SortField("sections", org.apache.lucene.search.SortField.STRING);
	public static final SortField SORT_DATE_COMPARABLE_DESC = new SortField("dateComparable", org.apache.lucene.search.SortField.STRING, true);
	public static final SortField SORT_DATE_CREATION_DESC = new SortField("dateCreation", org.apache.lucene.search.SortField.STRING, true);
	public static final SortField SORT_DATE_PUBLICATION_DESC = new SortField("lcDatePublication", org.apache.lucene.search.SortField.STRING, true);
	public static final SortField SORT_TYPE_CONTENU_DESC = new SortField("type", org.apache.lucene.search.SortField.STRING, true);
	public static final SortField SORT_AUTHOR_DESC = new SortField("auteur", org.apache.lucene.search.SortField.STRING, true);
	public static final SortField SORT_DATE_EVENT_BEGIN_DESC= new SortField("dateEventBegin", org.apache.lucene.search.SortField.STRING, true);
	public static final SortField SORT_SECTION_DESC = new SortField("sections", org.apache.lucene.search.SortField.STRING, true);


	public static final  Map<String, SortField> CRITERES = new HashMap<String, SortField>();
    static {
    	CRITERES.put("pertinence", SortCriteria.SORT_SCORE);
    	CRITERES.put("dateCreationAsc", SortCriteria.SORT_DATE_CREATION);
        CRITERES.put("datePublicationAsc", SortCriteria.SORT_DATE_PUBLICATION);
        CRITERES.put("typeAsc", SortCriteria.SORT_TYPE_CONTENU);
        CRITERES.put("auteurAsc", SortCriteria.SORT_AUTHOR);
        CRITERES.put("dateEventBeginAsc", SortCriteria.SORT_DATE_EVENT_BEGIN);
        CRITERES.put("sectionsAsc", SortCriteria.SORT_SECTION);
        CRITERES.put("dateComparableDesc", SORT_DATE_COMPARABLE_DESC);
        CRITERES.put("dateCreationDesc", SortCriteria.SORT_DATE_CREATION_DESC);
        CRITERES.put("datePublicationDesc", SortCriteria.SORT_DATE_PUBLICATION_DESC);
        CRITERES.put("typeDesc", SortCriteria.SORT_TYPE_CONTENU_DESC);
        CRITERES.put("auteurDesc", SortCriteria.SORT_AUTHOR_DESC);
        CRITERES.put("dateEventBeginDesc", SortCriteria.SORT_DATE_EVENT_BEGIN_DESC);
        CRITERES.put("sectionsDesc", SortCriteria.SORT_SECTION_DESC);
    }

}