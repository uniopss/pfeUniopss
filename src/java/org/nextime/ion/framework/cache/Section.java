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

package org.nextime.ion.framework.cache;

import java.util.*;
import java.io.*;

import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;

public class Section implements java.io.Serializable,
		org.nextime.ion.framework.cache.Cache {

	private String id;

	private String r;

	private Hashtable metaData;

	private File possibleCache;

	public String getId() {
		return id;
	}

	public int getLevel() {
		int l = -1;
		String path = (String) getMetaData("path");
		if (!"".equals(path.trim())) {
			String[] paths = path.substring(1).split("/");
			l = paths.length - 1;
		}
		return l;
	}

	public Hashtable getMetaData() {
		return metaData;
	}

	public String getRootSection() {
		if (r == null || "".equals(r)) {
			try {
				String[] _r = getId().split("_");
				r = _r[0];
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), Section.class,e);
			}
		}
		return r;
	}

	public Object getMetaData(String m) {
		String md = "";
		try {
			md = (String) metaData.get(m);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Section.class,e);
		}
		return md;
	}

	public Section(String id) {
		this.id = id;
		possibleCache = new File(Config.getInstance().getHtmlCacheDirectory(),
				"s$" + id + ".cache");
		if (exists())
			load();
	}

	public boolean exists() {
		return possibleCache.exists();
	}

	public void load() {
		try {
			FileInputStream fis = new FileInputStream(possibleCache);
			ObjectInputStream ois = new ObjectInputStream(fis);
			metaData = (java.util.Hashtable) ois.readObject();
			fis.close();
		} catch (Exception ee) {
			// TODO
			Logger.getInstance().error(ee.getMessage(), Section.class,ee);
		}
	}

}