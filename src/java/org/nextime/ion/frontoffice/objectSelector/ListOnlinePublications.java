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

package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.jdom.output.DOMOutputter;
import org.nextime.ion.commons.IsOnline;
import org.nextime.ion.commons.PublicationSorter;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.impl.PublicationVersionImpl;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;
import org.w3c.dom.Document;


/**
 * 		Récupération des publications online d'une section<br/>

 * 		Des paramètres attendus : <br>
 * 				- section : l'indentifiant de la section dont on veut récupérer les publications online <br/>
 * 				- view : le nom la vue xsl de publication : long, court ... qu'on veut afficher la publication sous cette forme <br/>
 * 				- min (optionel) : la position basse dans la collection de la publication ( defaut min = 1 )
 * 				- max (optionel) : la position hautes dans la colletion de la publication ( défaut max = 9999 )
 * 				- type (optionel) : filtre sur le type de la publicaiton qu'on veur récupérer ( dans le cas ou il y a plusieurs publications de<br/>
 * 				différents types liés à la même section. ( si'il est null ou de type objet, tous les publications seront renvoyés )
 * 				- sort (optionel) :
 * 				- la locale ( récupéré de la requete http ) : pour plusieurs versions multi-lingues.
 * 				- currentSite : le site courant ( récupéré de l'objet IonStatus ) cette paramètre sera ensuite passé au moment de création la vue html
 *
 *		Renvoi : <br/>
 *			- un vector contient les élément bean de type <code>PublicationResult</code> : ce bean contient la présentation de la publication<br/>
 *				que peut afficher directement dans les page jsp <br/>
 *			- dans le cas d'une erreur ou la l'indentifiant n'est pas trouvé, une collection vide est renvoyé
 */
public class ListOnlinePublications implements ObjectSelector {

	protected int _nb = 0;

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {
		try {
			String section = (String) params.get("section");
			String view = (String) params.get("view");
			String type = (String) params.get("type");
			int min = 1;
			if (params.get("min") != null && !"".equals(params.get("min")))
				min = Integer.parseInt((String) params.get("min"));
			int max = 9999;
			if (params.get("max") != null && !"".equals(params.get("max")))
				max = Integer.parseInt((String) params.get("max"));
			if (min < 0 || max < 0)
				return null;
			String condition = (String) params.get("condition");

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
			

			List v = null;
			if (params.get("sort") != null
					&& ("d".equals(params.get("sort"))
							|| "-d".equals(params.get("sort"))
							|| "dp".equals(params.get("sort")) || "-dp"
							.equals(params.get("sort")))) {
				if ("d".equals(params.get("sort"))
						|| "-d".equals(params.get("sort")))
					v = PublicationSorter.sortDatePublications(Section.getInstance(section));
				else
					v = PublicationSorter.sortDpPublications(Section.getInstance(section));

				if ("d".equals(params.get("sort"))
						|| "dp".equals(params.get("sort")))
					java.util.Collections.reverse(v);
			} else {
				Collection c=PublicationSorter.sortPublications(Section.getInstance(section));
				v = new Vector(c);
			}


			Vector v2 = new Vector();
			for (int i = 0; (i < v.size()); i++) {
				Publication p = (Publication) v.get(i);
				int version = IsOnline.getMostRecentVersion(p);
				if ((type == null || "".equals(type)
						|| "objet".equals(p.getType().getId())
						|| type.equals(p.getType().getId()))
						&& IsOnline.getStatus(p))
					{
					boolean conditionResult = true;
					if (condition != null && !"".equals(condition)) {
						Document doc = new DOMOutputter().output(((PublicationVersionImpl)p.getVersion(version)).getXmlDoc());
						XObject o = XPathAPI.eval(doc, condition);
						conditionResult = o.bool();
					}

					if (conditionResult) {
						_nb++;
						if (_nb >= min && _nb <= max) {
							PublicationResult r = new PublicationResult();
							r.setPublication(p);
							r.setVersion(version);
							if (view != null && !"objet".equals(p.getType().getId())) {
								try {
									byte[] bView = Viewer.getView(p, version, view, currentLocale, siteCode );
									if (bView!=null && bView.length > 0) {
										r.setView(new String(bView));
									} else {
										r.setView(StringUtils.EMPTY);
									}
								} catch (Exception e) {
									// TODO
									Logger.getInstance().error(e.getMessage(), ListOnlinePublications.class,e);
								}
								if (r.getView() != null	&& !"".equals(r.getView().trim()))
									v2.add(r);
								else
									_nb--;
							} else
								v2.add(r);
						}
					}
				}
			}
			//Mapping.rollback();
			request.setAttribute("totalSize", "" + _nb);
			return v2;
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