package org.nextime.ion.backoffice.exception;

import javax.servlet.ServletException;

public class BackofficeSecurityException extends ServletException {

	/**
	 * Constructor for BackofficeSecurityException.
	 */
	public BackofficeSecurityException() {
		super();
	}

	/**
	 * Constructor for BackofficeSecurityException.
	 * 
	 * @param arg0
	 */
	public BackofficeSecurityException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor for BackofficeSecurityException.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public BackofficeSecurityException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for BackofficeSecurityException.
	 * 
	 * @param arg0
	 */
	public BackofficeSecurityException(Throwable arg0) {
		super(arg0);
	}

}

