/*
 * �ON content management system. Copyright (C) 2002 Guillaume
 * Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * �ON is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * �ON core framework, �ON content server, �ON backoffice, �ON frontoffice and
 * �ON admin application are parts of �ON and are distributed under same terms
 * of licence.
 *
 *
 * �ON includes software developed by the Apache Software Foundation
 * (http://www.apache.org/) and software developed by the Exolab Project
 * (http://www.exolab.org).
 *
 * �ON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.nextime.ion.commons.IsOnline;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.logger.Logger;

/**
 * 	R�cup�ration de la publication par son identifiant<br/>
 * 		Des param�tres attendus : <br>
 * 				- publication : l'indentifiant de la publication dont on veut r�cup�rer  <br/>
 * 				- version (optionel): le num�ro version de la publication, par d�faut 1
 *
 *		Renvoi : <br/>
 *			- un vector contient un seul �l�ment bean de type <code>PublicationResult</code><br/>
 *			- dans le cas d'une erreur ou la l'indentifiant n'est pas trouv�, une valuer null est renvoy�
 */
public class GetPublication implements ObjectSelector {

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {
		try {
			String publication = (String) params.get("publication");
			String currentLocale = searchLocale(request);

			Publication p = Publication.getInstance(publication);
			int version = 1;
			if (params.get("version") == null) {
				version = IsOnline.getMostRecentVersion(p);
			} else {
				version = Integer.parseInt(params.get("version") + "");
			}
			PublicationResult r = new PublicationResult();
			r.setVersion(version);
			r.setPublication(p);
			Vector v = new Vector();
			v.add(r);
			return v;
		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject", this, e);
			//throw new SelectException(e.getMessage());
		}
		return null;
	}
	/**
	 * 	R�cup�ration le locale en ordre suivant : de la requ�te, et en session
	 * @param request
	 * @return la locale, sinon vide
	 */
	private String searchLocale(HttpServletRequest request) {
		String LOCAL_ATT = "currentLocale";
		String locale = StringUtils.EMPTY;
		if (request.getAttribute(LOCAL_ATT)!=null) {
			locale = request.getAttribute(LOCAL_ATT)+"";
		} else {
			locale=request.getSession().getAttribute(LOCAL_ATT)+ "";
		}
		return locale;
	}


}