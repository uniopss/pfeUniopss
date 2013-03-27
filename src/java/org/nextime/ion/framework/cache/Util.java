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

import java.io.*;
import java.util.*;

import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;

public class Util {

	public static boolean check() {
		return Config.getInstance().getTreeCache()
				&& Config.getInstance().getHtmlCacheDirectory() != null;
	}

	public static File[] list(String id) {
		final String _filter = id;
		java.io.FilenameFilter filter = new java.io.FilenameFilter() {
			public boolean accept(java.io.File dir, String name) {
				return name.matches("s\\$" + _filter + ".*cache");
			}
		};
		java.io.File[] files = null;
		try {
			files = org.nextime.ion.framework.config.Config.getInstance()
					.getHtmlCacheDirectory().listFiles(filter);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Util.class,e);
		}
		return files;
	}

	public static void cleanCache(String id) {
		File[] files = list(id);
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				try {
					files[i].delete();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(),Util.class,e);
				}
			}
		}

	}

	public static void deleteCache(String id) {
		File file = get(id);
		if (file.exists())
			try {
				file.delete();
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), Util.class,e);
			}

	}

	public static void write(String id) {
		try {
			write(org.nextime.ion.framework.business.Section.getInstance(id));
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Util.class,e);
		}

	}

	public static File get(String id) {
		return new File(Config.getInstance().getHtmlCacheDirectory(), "s$" + id
				+ ".cache");
	}

	public static Hashtable generate(
			org.nextime.ion.framework.business.Section s) {
		StringBuffer sb = new StringBuffer();
		Hashtable ht = (Hashtable) s.getMetaData().clone();
		ht.put("path", s.getPath());
		ht.put("child", s.listSubSectionsIds().toString().replaceAll(
				"[\\[| |\\]]", ""));
		return ht;
	}

	public static void generate() {
		if (!check())
			return;
		try {
			Vector roots = org.nextime.ion.framework.business.Section.listAll();
			for (int i = 0; i < roots.size(); i++) {
				try {
					org.nextime.ion.framework.business.Section section = (org.nextime.ion.framework.business.Section) roots
							.get(i);
					write(section);
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), Util.class,e);
				}
			}
		} catch (Exception e) {
			Logger.getInstance().error(
					e.getMessage(), Util.class,e);
		}
	}

	public static void generate(String id) {
		if (!check())
			return;
		try {
			Vector v = new java.util.Vector();
			v.add(org.nextime.ion.framework.business.Section.getInstance(id));
			Vector roots = org.nextime.ion.framework.business.Section
					.listAll(v);
			for (int i = 0; i < roots.size(); i++) {
				try {
					org.nextime.ion.framework.business.Section section = (org.nextime.ion.framework.business.Section) roots
							.get(i);
					write(section);
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(),Util.class,e);
				}
			}
		} catch (Exception e) {
			Logger.getInstance().error(
					e.getMessage(), Util.class,e);
		}
	}

	public synchronized static void write(
			org.nextime.ion.framework.business.Section s) {
		try {
			FileOutputStream fos = new FileOutputStream(get(s));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(generate(s));
			fos.close();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Util.class,e);
		}
	}

	public static File get(org.nextime.ion.framework.business.Section s) {
		return get(generateUniqueId(s));
	}

	public static String generateUniqueId(
			org.nextime.ion.framework.business.Section s) {
		return s.getId();
	}

}