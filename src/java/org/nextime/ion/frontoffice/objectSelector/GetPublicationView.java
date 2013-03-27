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

package org.nextime.ion.frontoffice.objectSelector;



import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.nextime.ion.commons.IsOnline;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;



/**
 * 	Récupération d'un view de la publication en question<br/>
 * 		Des paramètres attendus : <br>
 * 				- publication : l'indentifiant de la publication dont on veut récupérer  <br/>
 * 				- version (optionel): le numéro version de la publication, par défaut 1  <br/>
 * 				- view : le nom la vue xsl de publication : long, court ...   <br/>
 * 				- la locale ( récupéré de la requete http ) : pour plusieurs versions multi-lingues.
 *
 *		Renvoi : <br/>
 *			- un vector contient un seul élément bean de type <code>PublicationResult</code> : ce bean contient la présentation de la publication<br/>
 *				que peut afficher directement dans les page jsp <br/>
 *
 *			- dans le cas d'une erreur ou la l'indentifiant n'est pas trouvé, une valuer null est renvoyé
 */


public class GetPublicationView implements ObjectSelector {

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {
		try {
			String publication = (String) params.get("publication");
			String view = (String) params.get("view");

			String currentLocale = searchLocale(request);

			// param site code est passé dans la vue xsl
			Hashtable siteCode = null;
			if (params.get("currentSite") !=null ) {
				siteCode = new Hashtable();
				siteCode.put("currentSite", params.get("currentSite"));
				// param base url est passé dans la vue xsl	
				if (params.get("baseUrl") !=null ) {				
					siteCode.put("baseUrl", params.get("baseUrl"));
				} else
					//si test sur localhost
					siteCode.put("baseUrl", "localhost");
			} else {
				siteCode = new Hashtable();
				siteCode.put("baseUrl", "localhost");
			}
			
			if (StringUtils.isEmpty(publication)) {
				Logger.getInstance().log("Id de la publication entrée vide", this);
				return null;
			}

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
			Vector v2 = new Vector();
			if (view != null && !"objet".equals(p.getType().getId())) {
				try {
					r.setView(new String(Viewer.getView(p, version, view, currentLocale, siteCode)));
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), GetPublicationView.class,e);
				}
				if (r.getView() != null && !"".equals(r.getView()))
					v2.add(r);
			} else
				v2.add(r);
			return v2;
		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject", this, e);
			//throw new SelectException(e.getMessage());
		}
		return null;
	}

	/**
	 * 	Récupération le locale en ordre suivant : de la requête, et en session
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