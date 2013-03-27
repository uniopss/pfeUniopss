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

import javax.servlet.*;
import javax.servlet.http.*;

import org.nextime.ion.framework.business.*;
import org.nextime.ion.framework.business.impl.*;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;

public class TreeManager extends HttpServlet implements WcmListener {

	static {
		if (org.nextime.ion.framework.cache.Util.check()) {
			SectionImpl.addListener(getInstance());
		}
	}

	private static TreeManager instance;

	private static ServletContext context;

	public static TreeManager getInstance() {
		if (instance == null) {
			instance = new TreeManager();
		}
		return instance;
	}

	/**
	 * Ne rien faire
	 */
	public void objectLoaded(WcmEvent event) {
			return;
	}

	/**
	 * @see org.nextime.ion.framework.event.WcmListener#objectCreated(WcmEvent)
	 */
	public void objectCreated(WcmEvent event) {
		cleanCache(event, true);
	}

	/**
	 * @see org.nextime.ion.framework.event.WcmListener#objectDeleted(WcmEvent)
	 */
	public void objectDeleted(WcmEvent event) {
		cleanCache(event);
	}

	/**
	 * @see org.nextime.ion.framework.event.WcmListener#objectModified(WcmEvent)
	 */
	public void objectModified(WcmEvent event) {
		cleanCache(event, true);
	}

	private void cleanCache(WcmEvent event) {
		cleanCache(event, false);
	}

	private void cleanCache(WcmEvent event, boolean rebuild) {
		try {
			Section s = (Section) event.getSource();
			File possibleCache = org.nextime.ion.framework.cache.Util.get(s);
			if (possibleCache.exists())
				possibleCache.delete();
			if (rebuild)
				TreeCache.write(s);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), TreeManager.class,e);
		}
	}

	public void objectBeforeRemove(WcmEvent event) {
		// TODO Auto-generated method stub

	}

	public void objectPostChange(WcmEvent event) {
		// TODO Auto-generated method stub

	}

	public void objectBeforeChange(WcmEvent event) {
		// TODO Auto-generated method stub

	}

}