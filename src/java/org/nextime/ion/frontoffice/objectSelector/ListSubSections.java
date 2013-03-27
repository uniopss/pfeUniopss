/*
 * �ON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * �ON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * �ON core framework, �ON content server, �ON backoffice, �ON frontoffice
 * and �ON admin application are parts of �ON and are distributed under
 * same terms of licence.
 *
 *
 * �ON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * �ON is distributed in the hope that it will be useful,
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
 * 	Renvoi une collection d'objets Section (online et offline )situ�es directement sous la Section
 * 	sp�cifi�e par le parametre "rootSection"
 * 		Des param�tres attendus : <br>
 * 				- rootSection : l'indentifiant de la section dont on veut r�cup�rer les publications online <br/>
 *		Renvoi : <br/>
 *			- un vector contient les �l�ment bean de type <code>Section</code><br/>
 *			- dans le cas d'une erreur ou la l'indentifiant n'est pas trouv�, une collection vide est renvoy�
 */
public class ListSubSections implements ObjectSelector {

	public Collection selectObjects(Hashtable params,
			HttpServletRequest request, HttpServletResponse response)
			throws SelectException {
		Vector v = new Vector();
		try {
			String rootSection = (String) params.get("rootSection");
			//Mapping.begin();
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
							Object s = SmartCacheManager.getSection(childs[i]);
							v.add(s);
						}
					}
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(), ListSubSections.class,ee);
				}

			} else {
				Section root = Section.getInstance(rootSection);
				v = root.listSubSections();
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