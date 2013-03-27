/*
 * ÏON content management system. Copyright (C) 2002 Guillaume
 * Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * ÏON is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * ÏON core framework, ÏON content server, ÏON backoffice, ÏON frontoffice and
 * ÏON admin application are parts of ÏON and are distributed under same terms
 * of licence.
 *
 *
 * ÏON includes software developed by the Apache Software Foundation
 * (http://www.apache.org/) and software developed by the Exolab Project
 * (http://www.exolab.org).
 *
 * ÏON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

package org.nextime.ion.framework.helper;

import fr.asso.uniopss.stat.StatResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Searcher {


    // max result return in searchIndex
    private static final int MAX_INDEX_RESULT = 500;

    /**
     * Recherche back Office
     *
     * @param p_SiteIds
     * @param p_KeyWords
     * @param p_IndexName
     * @param p_Type
     * @param p_SortField
     * @return
     */
    public static Vector searchBO(Vector p_SiteIds, String p_KeyWords, String p_IndexName, String p_Type, SortField p_SortField) {
        if (p_Type == null || "".equals(p_Type))
            p_Type = "co";
        Vector result = null;
        try {
            QueryParser parser = new QueryParser(p_Type, new FrenchAnalyzer());
            Query query = parser.parse(translate(p_KeyWords));
            QueryFilter filter = null;
            if (p_SiteIds != null && !p_SiteIds.isEmpty()) {
                String filterSites = "";
                for (Object p_SiteId : p_SiteIds) {
                    filterSites += p_SiteId + " ";
                }
                filter = new QueryFilter(parser.parse("site:(" + filterSites + ")"));
            }

            SortField sortCritere = SortCriteria.SORT_NAME;
            if (p_SortField != null) {
                sortCritere = p_SortField;
            }

            SortField[] sf = {sortCritere, new SortField("id", org.apache.lucene.search.SortField.STRING), new SortField("version", org.apache.lucene.search.SortField.INT)};

            Sort sort = new Sort(sf);
            result = searchIndex(p_SiteIds, p_IndexName, query, filter, sort);
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
        }
        return result;
    }


    /**
     *
     * @param p_SelectedSites
     * @param p_KeyWords
     * @param p_NonTranslateQuery
     * @param p_IndexName
     * @param p_Type
     * @param p_SortField
     * @param p_MinResult
     * @param p_MaxResult
     * @return
     * @throws IllegalArgumentException
     * @throws ParseException
     */
    public static List searchAdvanced(Vector p_SelectedSites, String p_KeyWords, String p_NonTranslateQuery, String p_IndexName, String p_Type, SortField p_SortField, int p_MinResult, int p_MaxResult) throws IllegalArgumentException, ParseException {

        Vector result = searchPerform(p_SelectedSites, p_KeyWords, p_NonTranslateQuery, p_IndexName, p_Type, p_SortField, p_MinResult, p_MaxResult);
        int maxResult = Math.min(result.size(), p_MaxResult);
        int minResult = Math.min(p_MinResult, maxResult);

        return result.subList(minResult, maxResult);
    }

    /**
     *
     * @param p_SelectedSites
     * @param p_KeyWords
     * @param p_NonTranslateQuery
     * @param p_IndexName
     * @param p_Type
     * @param p_SortField
     * @param p_MinResult
     * @param p_MaxResult
     * @return
     * @throws IllegalArgumentException
     * @throws ParseException
     */
    public static PaginatedResult paginatedSearchAdvanced(Vector p_SelectedSites, String p_KeyWords, String p_NonTranslateQuery, String p_IndexName, String p_Type, SortField p_SortField, int p_MinResult, int p_MaxResult) throws IllegalArgumentException, ParseException {

        Vector result = searchPerform(p_SelectedSites, p_KeyWords, p_NonTranslateQuery, p_IndexName, p_Type, p_SortField, p_MinResult, p_MaxResult);
        int maxResult = Math.min(result.size(), p_MaxResult);
        int minResult = Math.min(p_MinResult, maxResult);

        return new PaginatedResult(result.subList(minResult, maxResult), result.size(), minResult);
    }

    private static Vector searchPerform(Vector p_SelectedSites, String p_KeyWords, String p_NonTranslateQuery, String p_IndexName, String p_Type, SortField p_SortField, int p_MinResult, int p_MaxResult) throws IllegalArgumentException, ParseException {

        if (p_Type == null || "".equals(p_Type))
            p_Type = "co";
        Vector result = null;
        try {
            QueryParser parser = new QueryParser(p_Type, new FrenchAnalyzer());
            parser.setAllowLeadingWildcard(true);

            // construction le mot clés des critère par défaut
            String queryComplet = StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(p_KeyWords)) {
                queryComplet = " ( " + p_KeyWords + " ) ";
            }
            if (StringUtils.isNotEmpty(p_NonTranslateQuery)) {
                if (StringUtils.isNotEmpty(queryComplet)) {
                    queryComplet += " AND ";
                }
                queryComplet += p_NonTranslateQuery;
            }
            if (StringUtils.isEmpty(queryComplet)) {
                throw new IllegalArgumentException("Too few criteria");
            }
            Query query = parser.parse(queryComplet);

            QueryFilter filter = null;
            if (p_SelectedSites != null && !p_SelectedSites.isEmpty()) {
                String aSiteFilter = "";
                for (int i = 0; i < p_SelectedSites.size(); i++) {
                    aSiteFilter += p_SelectedSites.get(i) + " ";
                }
                filter = new QueryFilter(parser.parse("site:(" + aSiteFilter + ")"));

            }
            SortField sortCritere = SortCriteria.SORT_SCORE;
            if (p_SortField != null) {
                sortCritere = p_SortField;
            }

            SortField[] sf = {
            		sortCritere,
            		SortCriteria.SORT_DATE_PUBLICATION_DESC,
            		SortCriteria.SORT_DATE_COMPARABLE_DESC,
            		new SortField("name", org.apache.lucene.search.SortField.STRING),
                    new SortField("id", org.apache.lucene.search.SortField.STRING),
                    new SortField("version", org.apache.lucene.search.SortField.INT) };
            Sort sort = new Sort(sf);
            result = searchIndex(p_SelectedSites, "fr", query, filter, sort);
        } catch (ParseException e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
            throw e;
        }
        return result;

    }


    public static Vector searchFO(Vector v, String keyWords, String indexName, String type) {
        if (type == null || "".equals(type))
            type = "co";
        Vector result = null;
        try {
            QueryParser parser = new QueryParser(type, new FrenchAnalyzer());
            Query query = parser.parse(translate(keyWords));
            String _filter = "";
            if (v != null && !v.isEmpty()) {
                for (int i = 0; i < v.size(); i++) {
                    _filter += v.get(i) + " ";
                }
                _filter = " AND (site:(" + _filter + ") or siteSection:(" + _filter + "))";
            }
            QueryFilter filter = new QueryFilter(parser.parse("_ionIsOnline:1 " + _filter));
            Sort sort = new Sort(new SortField("name", org.apache.lucene.search.SortField.STRING));
            result = searchIndex(v, indexName, query, filter, sort);
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
        }
        return result;
    }


    /**
     * Retourn le nombre de document
     * @param p_SelectedSites
     * @param p_NonTranslateQuery
     * @param p_IndexName
     * @param p_Type
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static StatResult searchStat(Vector p_SelectedSites, String p_NonTranslateQuery, String p_IndexName, String p_Type) throws IOException, ParseException {

        StatResult statResult = null;
        String fieldSearch = StringUtils.EMPTY;
        if (StringUtils.isEmpty(p_Type))
            fieldSearch = "co";
        try {
            QueryParser parser = new QueryParser(fieldSearch, new FrenchAnalyzer());

            String queryComplet = p_NonTranslateQuery;

            String aSiteFilter = StringUtils.EMPTY;
            if (p_SelectedSites != null && !p_SelectedSites.isEmpty()) {
                for (int i = 0; i < p_SelectedSites.size(); i++) {
                    aSiteFilter += p_SelectedSites.get(i) + " ";
                }
            }

            if (StringUtils.isNotEmpty(aSiteFilter.trim())) {
                queryComplet += " +site:(" + aSiteFilter + ") ";
            }

            Query query = null;
            if (StringUtils.isNotBlank(queryComplet)) {
                query = parser.parse(queryComplet);
            }
            IndexSearcher searcher = IndexSearcherFactory.getInstance().getIndexSearcher(p_IndexName);

            int count = 0;
            if (query == null) {
                count = searcher.maxDoc();
                statResult = new StatResult(count, new Vector());
            } else {
                Vector result = new Vector();
                Hits hits = searcher.search(query);
                count = hits.length();
                for (int i = 0; i < hits.length() && i <= MAX_INDEX_RESULT; i++) {
                    try {
                        Document doc = hits.doc(i);
                        float score = hits.score(i);
                        result.add(new SearchResult(doc, score));
                    } catch (Exception ee) {
                        Logger.getInstance().error(ee.getMessage(), Searcher.class, ee);
                    }
                }
                statResult = new StatResult(count, result);
            }
        } catch (ParseException e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
            throw e;
        }
        return statResult;
    }

    public static Vector searchIndex(Vector v, String indexName, Query query, QueryFilter filter, Sort sort) {
        Date dateBeg = new Date();
        Vector result = new Vector();
        try {
            IndexSearcher searcher = IndexSearcherFactory.getInstance().getIndexSearcher(indexName);
            Hits hits = searcher.search(query, filter, sort);
            for (int i = 0; i < hits.length() && i <= MAX_INDEX_RESULT; i++) {
                try {

                    Document doc = hits.doc(i);
                    float score = hits.score(i);
                    result.add(new SearchResult(doc, score));
                    /*
                         Explanation explanation =
                             searcher.explain(query, hits.id(i));
                         */
                } catch (Exception ee) {
                    Logger.getInstance().error(ee.getMessage(), Searcher.class, ee);
                }
            }
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
        }
        Date dateEnd = new Date();
        Logger.getInstance().log("Temps d'execution = " + (dateEnd.getTime() - dateBeg.getTime()) + " ms ", Searcher.class);
        return result;
    }

    public static Vector search(Vector v, String keyWords, String indexName, String type) {
        if (type == null || "".equals(type))
            type = "co";
        Vector result = new Vector();
        try {
            Vector vS = new Vector();
            if (v != null)
                for (int i = 0; i < v.size(); i++) {
                    try {
                        vS.add(((Section) v.get(i)).getId());
                    } catch (Exception ee) {
                        // TODO
                        Logger.getInstance().error(ee.getMessage(), Searcher.class, ee);
                    }
                }
            QueryParser parser = new QueryParser(type, new FrenchAnalyzer());
            Query query = parser.parse(translate(keyWords));
            IndexSearcher searcher = IndexSearcherFactory.getInstance().getIndexSearcher(indexName);
            Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                if (v == null || (doc.get("site") != null && vS.contains(doc.get("site").trim())) || (doc.get("siteSection") != null && vS.contains(doc.get("siteSection").trim()))) {
                    String id = doc.get("id");
                    try {
                        Publication p = Publication.getInstance(id);
                        SearchResult sr = new SearchResult(p, Integer.parseInt(doc.get("version")), hits.score(i));
                        result.add(sr);
                    } catch (Exception ee) {
                        // TODO
                        Logger.getInstance().error(ee.getMessage(), Searcher.class, ee);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
            return null;
        }
        return result;
    }

    public static Vector search(String keyWords, String indexName, String type) {
        return search(null, keyWords, indexName, type);
    }

    public static int check(String keyWords, String indexName, String type) {
        try {
            QueryParser parser = new QueryParser(type, new FrenchAnalyzer());
            Query query = parser.parse(translate(keyWords));
            IndexSearcher searcher = IndexSearcherFactory.getInstance().getIndexSearcher(indexName);
            Hits hits = searcher.search(query);
            return hits.length();
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
        }
        return -1;
    }

    public static Vector searchExpire(String indexName, String expire) {
        Hits hits = null;
        Vector result = new Vector();
        try {
            QueryParser parser = new QueryParser("dateFin", new FrenchAnalyzer());
            Query query = parser.parse(expire);
            IndexSearcher searcher = IndexSearcherFactory.getInstance().getIndexSearcher(indexName);
            hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                String valeur = doc.get("id") + ";" + doc.get("version") + ";" + doc.get("name") + ";" + doc.get("auteur");
                result.add(valeur);
            }
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage(), Searcher.class, e);
            return null;
        }
        return result;
    }

    public static String translate(String src) {
        return src.replaceAll("[ ]+(['|\\?|\\*|-])", "$1").replaceAll("[\"|\\{|\\}|\\[|\\]|\\(|\\)]", "");
    }

    /*
    public static Vector sortPublications(Vector v) {
        Collections.sort(v, new PublicationNameComparator());
        Collections.reverse(v);
        return v;
    } */

    public static class PaginatedResult {
        private List result;
        private int total;
        private int start;

        public PaginatedResult() {
        }

        public PaginatedResult(List result, int total, int start) {
            this.result = result;
            this.total = total;
            this.start = start;
        }

        public List getResult() {
            return result;
        }

        public void setResult(List result) {
            this.result = result;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }
    }
}
