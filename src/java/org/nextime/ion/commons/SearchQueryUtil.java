package org.nextime.ion.commons;

import fr.asso.uniopss.synonyme.SynonymeManager;
import org.apache.commons.collections.FastArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.helper.FrenchAnalyzer;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.xml.XMLException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQueryUtil {

    private static Log _log = LogFactory.getLog(SearchQueryUtil.class);

    /**
     * Génération le query avec Parser, si il y a une erreur, on parse en suite avec WildaCard Query
     *
     * @param p_Synonymes
     * @param p_Field
     * @param p_HumanKeyWord
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws XMLException
     */

    // + - && || ! ( ) { } [ ] ^ " ~ * ? : \
    // Tester si la chaine contient des mot-clés
    private static Pattern KEYWORD_PATTERN = Pattern.compile("(.*?)([\\+\\&\\|\\(\\)\\{\\}\\^\"\\~\\*\\\\]|( and )|( or )|( not ))(.*?)");

    public static void main(String[] args) {
        String testString = "participation NOTchances";
        Matcher matcher = KEYWORD_PATTERN.matcher(testString);
        if (matcher.matches()) {
            System.out.print("MATCH");
        } else {
            System.out.print("non");
        }
    }

    /**
     * Générer la query lucene pour les mot-clés entrés par l'utilisateur
     * - Dans le cas la chaine entrée contient un opérateur lucene, on passe tel quel au moteur de recherche
     * - Sinon	:
     * - Elle met le poid sur la chaine
     * - parse la chaine et déterminer les mots ( terms ) individuellement
     * - pour chaque mot, on récupérer les synonymes si il y en a.
     * - composer la query lucene avec tous les mots et le poid
     *
     * @param p_Synonymes
     * @param p_Field
     * @param p_HumanKeyWord
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws XMLException
     */
    public static String generateHumanQuery(SynonymeManager p_Synonymes, String p_Field, String p_HumanKeyWord, int p_PhraseWeigth) throws IOException, ParseException, XMLException {
        String query = StringUtils.EMPTY;
        Matcher matcher = KEYWORD_PATTERN.matcher(p_HumanKeyWord.toLowerCase());
        if (matcher.matches()) {
            query = Util.translateAccent(p_HumanKeyWord);
            //query = query.toLowerCase();
            _log.info(" Query avec parser syntax : " + query);

        } else {
            boolean parseOK = false;
            // essayer de parser avec le query standard
            try {
                String humanQueryString = Util.translate(p_HumanKeyWord);

                Set setHumanKeyWords = parseHumanKeyWords(p_Synonymes, p_Field, humanQueryString);
                query = generateQuery(setHumanKeyWords, p_Field, p_PhraseWeigth);
                // on met le poids sur la chaine
                query = p_Field + ":" + "\"" + humanQueryString + "\"^" + p_PhraseWeigth + query;
                parseOK = true;
            } catch (UnsupportedOperationException e) {
                _log.info(" Error de paser avec queryParser, utiliser dans ce cas le parser avec wildcard ");
            }
            if (!parseOK) {
                _log.info(" Parser avec wildcard query ");
                WildcardQuery wildcardQuery = new WildcardQuery(new Term(p_Field, p_HumanKeyWord));
                query = wildcardQuery.toString();
            }

            query = StringUtils.lowerCase(query);
        }
        return query;
    }


    private static Set parseHumanKeyWords(SynonymeManager p_Synonymes, String p_Field, String p_HumanKeyWord) throws ParseException, XMLException, IOException {
        // essayer de parser le mot clé renseigne par l'utilisateur
        QueryParser parser = new QueryParser(p_Field, new FrenchAnalyzer());
        Query humanQuery = parser.parse(p_HumanKeyWord);
        Set terms = new HashSet();
        humanQuery.extractTerms(terms);

        // pour chaque term, on cherche des mots-clés synonyme
        Set ret = new HashSet();
        for (Iterator it = terms.iterator(); it.hasNext();) {
            Term term = (Term) it.next();
            String text = term.text();
            ret.add(text);
            // on ajouter aussi les mot clé synonyme
            ret.addAll(p_Synonymes.findKeyWords(text));
        }

        // Recherche des synonymes pour tout les mots.
        ret.addAll(p_Synonymes.findKeyWords(p_HumanKeyWord));
        return ret;
    }

    /**
     * Retourner une chaine contenant des mot-clés espacé
     *
     * @param p_List
     * @return
     */
    public static String generateOrQuery(List p_List) {
        if (p_List == null)
            return StringUtils.EMPTY;
        StringBuffer ret = new StringBuffer();
        for (Iterator<String> it = p_List.iterator(); it.hasNext();) {
            ret.append(it.next()).append(" ");
        }
        return ret.toString();
    }


    /**
     * Génération de la query OR par l'ensemeble des mot-clés entrés
     *
     * @param p_Keywords
     * @return
     */
    public static String generateKeyWordQuery(Set p_Keywords) {
        StringBuffer ret = new StringBuffer();
        if (p_Keywords == null || p_Keywords.size() == 0) {
            return ret.toString();
        }
        ret.append("( ");
        List keyWords = new FastArrayList(p_Keywords);
        for (int i = 0; i < keyWords.size() && keyWords.get(i) != null; i++) {
            String aKeyWord = (String) keyWords.get(i);
            if (StringUtils.isNotEmpty(aKeyWord)) {
                ret.append(" \"").append(Searcher.translate(aKeyWord)).append("\" ");
            }
        }
        ret.append(" ) ");
        return ret.toString();
    }

    /**
     * Génération de la query OR par l'ensemeble des mot-clés entrés
     *
     * @param p_Keywords
     * @return
     */
    private static String generateQuery(Set p_Keywords, String p_Field, int p_Weight) {
        StringBuffer ret = new StringBuffer();
        List keyWords = new FastArrayList(p_Keywords);
        for (int i = 0; i < keyWords.size() && keyWords.get(i) != null; i++) {
            String aKeyWord = (String) keyWords.get(i);
            if (StringUtils.isNotEmpty(aKeyWord)) {
                ret.append(" ").append(p_Field).append(":").append(" \"").append(aKeyWord).append("\"^").append(p_Weight).append(" ");
            }
        }
        return ret.toString();
    }


    /**
     * Construire dans le string buffer la query AND
     * Cette fonction modifie la valeur du String Buffer entrée
     *
     * @param p_InOutBuffer
     * @param p_Field
     * @param p_Value
     */
    public static void addKeyWord(StringBuffer p_InOutBuffer, String p_Field, String p_Value) {
        if (p_InOutBuffer.length() > 0) {
            p_InOutBuffer.append(" AND ");
        }
        p_InOutBuffer.append(p_Field).append(":").append(p_Value);
    }

    /**
     * Construire dans le string buffer la query AND
     * Cette fonction modifie la valeur du String Buffer entrée
     *
     * @param p_InOutBuffer
     * @param p_Value
     */
    public static void addKeyWord(StringBuffer p_InOutBuffer, String p_Value) {
        if (p_InOutBuffer.length() > 0) {
            p_InOutBuffer.append(" AND ");
        }
        p_InOutBuffer.append(p_Value);
    }

    /**
     * Construire dans le string buffer la query AND
     * Cette fonction modifie la valeur du String Buffer entrée
     *
     * @param p_InOutBuffer
     * @param p_Value
     */
    public static void addKeyWordOr(StringBuffer p_InOutBuffer, String p_Value) {
        if (StringUtils.isEmpty(p_Value)) {
            return;
        }
        if (p_InOutBuffer.length() > 0) {
            p_InOutBuffer.append(" OR ");
        }
        p_InOutBuffer.append(p_Value);
    }

    /**
     * Utilitaire afin de parse la chaine de date entré par l'utilisateur
     * Si le format n'est pas correct, la valeur retournée est null
     *
     * @param p_Date
     * @return
     */
    public static Date parseDate(String p_Date) {
        try {
            Date date = DateUtils.parseDate(p_Date, new String[]{Config.getInstance().getDateFormatPattern()});
            return date;
        } catch (java.text.ParseException e) {
            _log.error("La date " + p_Date + " n'est pas correct ");
        }
        return null;
    }

    public static RangeQuery createDateRange(String p_FieldName, Date p_lowerDate, Date p_upperDate) {

        if (p_lowerDate == null && p_upperDate == null) {
            return null;
        }

        Date lowerDate = null;
        if (p_lowerDate != null) {
            lowerDate = (Date) p_lowerDate.clone();
        }
        Date upperDate = null;
        if (p_upperDate != null) {
            upperDate = (Date) p_upperDate.clone();
        }


        Term lowerTerm = null;
        String dateMin = "0";
        if (lowerDate != null) {
            Date date = lowerDate;
            date.setDate(date.getDate());
            dateMin = DateTools.dateToString(date, Resolution.DAY);
        }
        lowerTerm = new Term(p_FieldName, dateMin);
        /*
        if (p_lowerDate !=null) {
            lowerTerm=new Term(p_FieldName, DateTools.dateToString( p_lowerDate, Resolution.DAY));
        }*/
        Term upperTerm = null;
        String dateMax = "99999999";
        if (upperDate != null) {
            upperDate.setDate(upperDate.getDate());
            dateMax = DateTools.dateToString(upperDate, Resolution.DAY);
        }
        upperTerm=new Term(p_FieldName, dateMax);
        return new RangeQuery(lowerTerm, upperTerm, true);
    }

}
