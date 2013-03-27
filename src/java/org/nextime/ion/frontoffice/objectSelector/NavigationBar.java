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
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.frontoffice.smartCache.SmartCacheManager;



/**
 * 	Renvoi une collection d'objets Section parents ( la section passée en paramètre incluse )
 * 	Des paramètres attendus : <br>
 * 				- currentSection : l'indentifiant de la section dont on veut récupérer les parents<br/>
 *		Renvoi : <br/>
 *			- une collection contient les élément bean de type <code>Section</code>, la liste est ordonée commme suit<br />
 *				parent niveau 0, parent niveau 1, parent niveau 3 ... <br/>
 *			- dans le cas d'une erreur ou la l'indentifiant n'est pas trouvé, une collection vide est renvoyée <br/>
 */

public class NavigationBar implements ObjectSelector {

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {
		Vector v = new Vector();
		try {
			//Mapping.begin();
			String currentSection = (String) params.get("currentSection");
			org.nextime.ion.framework.cache.Section cs = null;
			if (org.nextime.ion.framework.config.Config.getInstance()
					.getSectionCache())
				cs = new org.nextime.ion.framework.cache.Section(currentSection);
			if (cs != null && cs.exists()) {
				try {
					String path = (String) cs.getMetaData("path");
					if (!"".equals(path.trim())) {
						String[] paths = path.substring(1).split("/");
						for (int i = 0; i < paths.length; i++) {
							Object s = SmartCacheManager.getSection(paths[i]);
							if (s != null)
								v.add(s);
						}
					}
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(), NavigationBar.class,ee);
				}
			} else {
				Section current = Section.getInstance(currentSection);
				while (current != null) {
					v.add(current);
					current = current.getParent();
				}
				Collections.reverse(v);
			}
			//Mapping.rollback();
			return v;
		} catch (Exception e) {
			Logger.getInstance().error("Erreur du SelectObject", this, e);
			//throw new SelectException(e.getMessage());
		}
		return v;
	}

}