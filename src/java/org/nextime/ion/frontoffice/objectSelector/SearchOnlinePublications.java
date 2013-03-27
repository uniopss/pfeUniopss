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
import org.nextime.ion.framework.helper.SearchResult;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;
/**
 * 	Renvoi une collection pour la recherche front office les Publication "online" ( basé sur des requête lucene )
 * 		Des paramètres attendues
 *
 * 				- queryString : la chaine représentante le query lucene  ;
 * 				- la view commun de tous les publication
 * 				- le nom par défaut du champ qu'on veut rechercher
 * 				- la locale currentLocale : dans la session http
 * 				- min :
 * 				- max :
 * 				- rootSection : les document de la racine : rootSection
 *
 *		Une collection d'objet SearchPublicationResult sera retourné, sonon une collection vide
 */
public class SearchOnlinePublications implements ObjectSelector {

	protected int _nb = 0;

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {

		try {
			String queryString = (String) params.get("queryString");
			String view = (String) params.get("view");
			String index = (String) params.get("index");
			String type = (String) params.get("type");
			String currentLocale = searchLocale(request);
			int min = 1;
			if (params.get("min") != null && !"".equals(params.get("min")))
				min = Integer.parseInt((String) params.get("min"));
			int max = 9999;
			if (params.get("max") != null && !"".equals(params.get("max")))
				max = Integer.parseInt((String) params.get("max"));

			Vector returnResults = new Vector();
			if (queryString.equals("") || index.equals(""))
				return returnResults;
			Vector results = null;
			Vector v = new Vector();
			if (params.get("rootSection") != null) {
				String rootSection = (String) params.get("rootSection");
				String[] _site = rootSection.split(",");
				for (int i = 0; i < _site.length; i++)
					try {
						v.add(_site[i]);
					} catch (Exception ee) {
						// TODO
						Logger.getInstance().error(ee.getMessage(),SearchOnlinePublications.class,ee);
					}
			}
			results = Searcher.searchFO(v, queryString, index, type);
			if (results == null || results.isEmpty())
				return returnResults;

			for (int i = 0; i < results.size(); i++) {
				SearchResult result = (SearchResult) results.get(i);
				_nb++;
				if (_nb >= min && _nb <= max) {
					boolean _do = false;
					String viewResult = "";
					if (!view.equals("") && !"objet".equals(result.getType())) {
						byte[] _viewResult = Viewer.getView(result.getId(),
								result.getVersion(), result.getType(), result
										.getContent(), view, currentLocale);
						if (_viewResult != null
								&& !"".equals(new String(_viewResult).trim())) {
							_do = true;
							viewResult = new String(_viewResult);
						} else
							_nb--;
					} else
						_do = true;
					if (_do) {
						SearchPublicationResult spr = new SearchPublicationResult();
						spr.setSearchResult(result);
						spr.setView(viewResult);
						returnResults.add(spr);
					}
				}
			}
			//_nb--;
			request.setAttribute("totalSize", "" + _nb);
			return returnResults;
		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject", this, e);
			//throw new SelectException(e.getMessage());
		}
		request.setAttribute("totalSize", "0");
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