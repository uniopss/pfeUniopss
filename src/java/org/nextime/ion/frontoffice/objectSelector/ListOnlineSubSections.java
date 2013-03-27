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
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.frontoffice.smartCache.SmartCacheManager;

/**
 *
 *
 * 	Renvoi une collection d'objets Section "online" situées directement sous la Section spécifiée par le parametre "rootSection"<br/>
 * 		Des paramètres attendus : <br>
 * 				- rootSection : l'indentifiant de la section dont on veut récupérer les sous section<br/>
 * 				- min (optionel) : la position basse dans la collection de la publication ( defaut min = 1 )
 * 				- max (optionel) : la position hautes dans la colletion de la publication ( défaut max = 9999 )
 *
 *		Renvoi : <br/>
 *			- une collection contient les élément bean de type <code>Section</code><br/>
 *			- dans le cas d'une erreur ou la l'indentifiant n'est pas trouvé, une collection vide est renvoyée
 */

public class ListOnlineSubSections implements ObjectSelector {

	protected int _nb = 0;

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {

		Vector result = new Vector();
		try {
			String rootSection = (String) params.get("rootSection");
			int min = 1;
			if (params.get("min") != null && !"".equals(params.get("min")))
				min = Integer.parseInt((String) params.get("min"));
			int max = 9999;
			if (params.get("max") != null && !"".equals(params.get("max")))
				max = Integer.parseInt((String) params.get("max"));
			if (min < 0 || max < 0)
				return result;

			org.nextime.ion.framework.cache.Section cs = null;
			if (org.nextime.ion.framework.config.Config.getInstance()
					.getSectionCache())
				cs = new org.nextime.ion.framework.cache.Section(rootSection);
			if (cs != null && cs.exists()) {
				try {
					String child = (String) cs.getMetaData("child");
					if (!"".equals(child.trim())) {
						String[] childs = child.split(",");
						for (int i = 0; i < childs.length; i++) {
							Object s = SmartCacheManager.getOnlineSection(childs[i]);
							if (s != null) {
								_nb++;
								if (_nb >= min && _nb <= max)
									result.add(s);
							}
						}
					}
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(), ListOnlineSubSections.class,ee);
				}

			} else {
				Section root = Section.getInstance(rootSection);
				Vector v = root.listSubSections();
				for (int i = 0; i < v.size(); i++) {
					try {
						Section s = (Section) v.get(i);
						if (!"offline".equalsIgnoreCase(s.getMetaData("status") + "")) {
							_nb++;
							if (_nb >= min && _nb <= max)
								result.add(s);
						}
					} catch (Exception e) {
						// TODO
						Logger.getInstance().error(e.getMessage(), ListOnlineSubSections.class,e);
					}
				}
			}

			request.setAttribute("totalSize", "" + _nb);
			return result;
		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject", this, e);
			//throw new SelectException(e.getMessage());
		}
		request.setAttribute("totalSize", "0");
		return result;
	}

}