package org.nextime.ion.framework.helper;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;



public class FrenchAnalyzer extends StandardAnalyzer {
	private Set frenchStopSet;

	/**
	   * Extended list of typical French stopwords.
	   */
	public final static String[] FRENCH_STOP_WORDS = {
	    "a", "afin", "ai", "ainsi", "apr�s", "attendu", "au", "aujourd", "auquel", "aussi",
	    "autre", "autres", "aux", "auxquelles", "auxquels", "avait", "avant", "avec", "avoir",
	    "c", "car", "ce", "ceci", "cela", "celle", "celles", "celui", "cependant", "certain",
	    "certaine", "certaines", "certains", "ces", "cet", "cette", "ceux", "chez", "ci",
	    "combien", "comme", "comment", "concernant", "contre", "d", "dans", "de", "debout",
	    "dedans", "dehors", "del�", "depuis", "derri�re", "des", "d�sormais", "desquelles",
	    "desquels", "dessous", "dessus", "devant", "devers", "devra", "divers", "diverse",
	    "diverses", "doit", "donc", "dont", "du", "duquel", "durant", "d�s", "elle", "elles",
	    "en", "entre", "environ", "est", "et", "etc", "etre", "eu", "eux", "except�", "hormis",
	    "hors", "h�las", "hui", "il", "ils", "j", "je", "jusqu", "jusque", "l", "la", "laquelle",
	    "le", "lequel", "les", "lesquelles", "lesquels", "leur", "leurs", "lorsque", "lui", "l�",
	    "ma", "mais", "malgr�", "me", "merci", "mes", "mien", "mienne", "miennes", "miens", "moi",
	    "moins", "mon", "moyennant", "m�me", "m�mes", "n", "ne", "ni", "non", "nos", "notre",
	    "nous", "n�anmoins", "n�tre", "n�tres", "on", "ont", "ou", "outre", "o�", "par", "parmi",
	    "partant", "pas", "pass�", "pendant", "plein", "plus", "plusieurs", "pour", "pourquoi",
	    "proche", "pr�s", "puisque", "qu", "quand", "que", "quel", "quelle", "quelles", "quels",
	    "qui", "quoi", "quoique", "revoici", "revoil�", "s", "sa", "sans", "sauf", "se", "selon",
	    "seront", "ses", "si", "sien", "sienne", "siennes", "siens", "sinon", "soi", "soit",
	    "son", "sont", "sous", "suivant", "sur", "ta", "te", "tes", "tien", "tienne", "tiennes",
	    "tiens", "toi", "ton", "tous", "tout", "toute", "toutes", "tu", "un", "une", "va", "vers",
	    "voici", "voil�", "vos", "votre", "vous", "vu", "v�tre", "v��tres", "y", "�", "�a", "�s",
	    "�t�", "�tre", "�"
	  };

	/** Builds an analyzer with the given stop words. */
	public FrenchAnalyzer() {
		this(FRENCH_STOP_WORDS);
	  }

	/** Builds an analyzer with the given stop words. */
	public FrenchAnalyzer(String[] stopWords ){
		frenchStopSet = StopFilter.makeStopSet(stopWords);
	}

	/** Constructs a {@link StandardTokenizer} filtered by a {@link
	  StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}. */
	  public TokenStream tokenStream(String fieldName, Reader reader) {
	    TokenStream result = new LetterOrDigitTokenizer(reader);
	    result = new StandardFilter(result);
	    result = new LowerCaseFilter(result);
	    result = new StopFilter(result, frenchStopSet);
	    return result;
	  }
}
