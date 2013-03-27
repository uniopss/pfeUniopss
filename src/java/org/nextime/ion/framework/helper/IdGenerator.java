/*
 * ÏON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * ÏON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * ÏON core framework, ÏON content server, ÏON backoffice, ÏON frontoffice
 * and ÏON admin application are parts of ÏON and are distributed under
 * same terms of licence.
 *
 *
 * ÏON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * ÏON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.helper;

/**
 * Générateur d'id pour les publications et les sections
 * 
 * @author gbort
 * @version 1.0
 */
public class IdGenerator {

	/**
	 * Prochain id libre pour une section
	 */
	public static String nextSectionId() {
		return "s" + Thread.currentThread().getId()
				+ System.currentTimeMillis();
	}

	/**
	 * Prochain id libre pour une publication
	 */
	public static String nextPublicationId() {

		return "p" + Thread.currentThread().getId()
				+ System.currentTimeMillis();
	}

	/**
	 * Prochain id libre pour une version
	 */
	public static String nextPublicationVersionId() {
		return "pv" + Thread.currentThread().getId()
				+ System.currentTimeMillis();
		//return id + "_v" + version + "_" + Math.abs(new Random().nextInt());
	}

	/**
	 * Prochain id libre pour un entry
	 */
	public static String nextEntryId() {
		return "en" + Thread.currentThread().getId()
				+ System.currentTimeMillis();
	}

	/**
	 * Prochain id libre pour un step
	 */
	public static String nextStepId() {
		return "st" + Thread.currentThread().getId()
				+ System.currentTimeMillis();
	}
}