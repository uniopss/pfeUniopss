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
 * 	Récupération du parent de la section sur le niveau donné <br/>
 * 		Des paramètres attendus : <br/>
 * 				- section : l'indentifiant de la section dont on veut chercher le parent<br/>
 * 				- level : le niveau de parent dont on veut récupérer
 *
 *		Renvoi : <br/>
 *			- un vector contient un seul élément, c'est le parent sur le niveau level de la section en question<br/>
 *			- dans le cas le niveau passé est suppérieur ou égal du niveau de la section en question, un vector vide est retourné
 *
 */
public class FindParentSection implements ObjectSelector {

	public Collection selectObjects(Hashtable params, HttpServletRequest request, HttpServletResponse response)
			throws SelectException {
		Vector v = new Vector();
		try {
			int level = Integer.parseInt(params.get("level") + "");
			org.nextime.ion.framework.cache.Section cs = null;
			if (org.nextime.ion.framework.config.Config.getInstance().getSectionCache())
				cs = new org.nextime.ion.framework.cache.Section(params.get("section")+ "");

			if (cs != null && cs.exists()) {
				String path = (String) cs.getMetaData("path");
				if (!"".equals(path.trim())) {
					String[] paths = path.substring(1).split("/");
					if (level >= paths.length)
						return v;
					Object s = SmartCacheManager.getSection(paths[level]);
					if (s != null)
						v.add(s);
				}
			} else {
				Section s = Section.getInstance(params.get("section") + "");
				if (level > s.getLevel())
					return v;
				Section parent = s;
				while (parent.getLevel() > level) {
					parent = parent.getParent();
				}
				v.add(parent);
			}
			return v;
		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject : " + e.getMessage(), this, e);
			//throw new SelectException(e.getMessage());
		}
		return v;
	}

}