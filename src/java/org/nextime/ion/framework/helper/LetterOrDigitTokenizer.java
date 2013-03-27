package org.nextime.ion.framework.helper;

import java.io.Reader;

import org.apache.lucene.analysis.CharTokenizer;

public class LetterOrDigitTokenizer extends CharTokenizer {


	/** Construct a new LetterOrDigitTokenizer. */
	public LetterOrDigitTokenizer(Reader input) {
		super(input);
	}

	/** Collects only characters which satisfy
	   * {@link Character#isLetterOrDigit(char)}.*/
	protected boolean isTokenChar(char c) {
		return Character.isLetterOrDigit(c)|| (c == '/');
	}

}
