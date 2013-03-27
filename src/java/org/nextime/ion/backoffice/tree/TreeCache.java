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

package org.nextime.ion.backoffice.tree;

import java.io.*;

import org.nextime.ion.framework.business.*;
import org.nextime.ion.framework.logger.Logger;

public class TreeCache {

	public static String reverse(File f) {
		String id = "";
		if (f.exists())
			id = f.getName().substring(2).replace(".cache", "");
		return id;
	}

	public synchronized static void write(Section s) {
		org.nextime.ion.framework.cache.Util.write(s);
	}

	public static java.util.Hashtable load(String id) {
		return load(org.nextime.ion.framework.cache.Util.get(id));
	}

	public static java.util.Hashtable load(File f) {
		java.util.Hashtable res = null;
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			res = (java.util.Hashtable) ois.readObject();
			fis.close();
		} catch (FileNotFoundException ee) {
			// TODO
			Logger.getInstance().log(" Le fichier cache non trouvé, il sera recréé : " + f.getAbsolutePath(), TreeCache.class);
		} catch (IOException ee) {
			// TODO
			Logger.getInstance().error(ee.getMessage(), TreeCache.class,ee);
		} catch (ClassNotFoundException ee) {
			// 	TODO
			Logger.getInstance().error(ee.getMessage(), TreeCache.class,ee);
		}

		return res;
	}

}