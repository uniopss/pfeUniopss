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

package org.nextime.ion.frontoffice.smartCache;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.StringUtils;
import org.nextime.ion.commons.CommonServlet;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.business.impl.SectionImpl;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;

public class SmartCacheManager extends HttpServlet implements WcmListener {

	public static final String ION_LAST_UPDATE_ATT = "ion_lastUpdate";

	public static final String CACHE_APPLICATION_PARAM_NAME = "cacheApplication";
	public static final String BO_PARAM_VALUE = "BO";
	public static final String FO_PARAM_VALUE = "FO";

	private static SmartCacheManager _instance;
/*
	static {
		PublicationImpl.addListener(new SmartCacheManager());
		SectionImpl.addListener(new SmartCacheManager());
	}
*/

	private static ServletContext context;
	private String _cacheApplication = StringUtils.EMPTY;


	public SmartCacheManager() {
		super();
		Logger.getInstance().log("Instancier SmartCache Manager " + this, SmartCacheManager.class);
	}

	public static SmartCacheManager getInstance() {
		return _instance;
	}

	/**
	 * @see org.nextime.ion.framework.event.WcmListener#objectCreated(WcmEvent)
	 */
	public void objectCreated(WcmEvent event) {
		cleanCache();
	}

	/**
	 * @see org.nextime.ion.framework.event.WcmListener#objectDeleted(WcmEvent)
	 */
	public void objectDeleted(WcmEvent event) {
		cleanCache();
	}

	/**
	 * @see org.nextime.ion.framework.event.WcmListener#objectModified(WcmEvent)
	 */
	public void objectModified(WcmEvent event) {
		cleanCache();
	}

	/**
	 * Ne rien faire
	 */
	public void objectLoaded(WcmEvent event) {
			return;
	}

	/**
	 * 	Mise à jour de la cache
	 * 		- Mettre à jour la date de la dernière update quelque soit l'objet : Publication et Section
	 * 		- Notifier le front office de cette date
	 */
	protected void cleanCache() {
		//		try {
		//			Vector sections = Section.listAll();
		//			for (int i = 0; i < sections.size(); i++) {
		//				String id = ((Section) sections.get(i)).getId();
		//				context.removeAttribute("org.apache.taglibs.cache.session.caches.section_"+id);
		//			}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
		// mettre à jour le contexr de cette application ( backoffice)
		String lastUpdate = (System.currentTimeMillis() - 5000) + "";

		// notifier l'application backoffice
		setLastUpdateInBOContext(ION_LAST_UPDATE_ATT, lastUpdate);
		// notifier l'application front office
		setLastUpdateInFOContext(ION_LAST_UPDATE_ATT, lastUpdate);

	}

	public static Object getSection(String id) {
		org.nextime.ion.framework.cache.Cache s = null;
		try {
			s = new org.nextime.ion.framework.cache.Section(id);
			if (s == null || s.getMetaData() == null) {
				s = Section.getInstance(id);
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), SmartCacheManager.class,e);
		}
		return s;
	}

	public static Object getOnlineSection(String id) {
		org.nextime.ion.framework.cache.Cache s = (org.nextime.ion.framework.cache.Cache) getSection(id);
		try {
			if (!"online".equalsIgnoreCase((String) s.getMetaData("status"))) {
				s = null;
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), SmartCacheManager.class,e);
		}
		return s;
	}

	/**
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
	/*
	public void init(ServletConfig arg0) throws ServletException {
		context = arg0.getServletContext();
		cleanCache();
	}*/


	public void init() throws ServletException {
		super.init();
		initListener();

		_instance = this;
		_cacheApplication = getServletConfig().getInitParameter(CACHE_APPLICATION_PARAM_NAME);
		context = getServletContext();
		cleanCache();

	}

	/**
	 * 	Initialisation le listener
	 *
	 */
	private void initListener() {
		PublicationImpl.addListener(this);
		SectionImpl.addListener(this);
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
	/**
	 * 	Mettre à jour les données du FrontOffice dans le cas ou la modif a été effectué
	 * 	côté BackOffice
	 * @param p_FrontOfficeContext
	 * @param p_Data
	 */
	private void setLastUpdateInFOContext(String p_Key, Object p_Data) {
		if (StringUtils.equals(_cacheApplication, BO_PARAM_VALUE)) {
			// get the Context containing the shared session data
		    ServletContext foContext = context.getContext(CommonServlet.FRONT_OFFICE_CONTEXT);
		    if (foContext !=null) {
			    foContext.setAttribute(p_Key, p_Data);
		    }
		}
	}
	/**
	 * 	Mettre à jour les données du BackOffice dans le cas ou la modif a été effectuée au Front Office
	 * @param p_BackOfficeContext
	 * @param p_Data
	 */
	private void setLastUpdateInBOContext( String p_Key, Object p_Data) {
		if (StringUtils.equals(_cacheApplication, FO_PARAM_VALUE)) {
			// get the Context containing the shared session data
		    ServletContext foContext = context.getContext(CommonServlet.BACK_OFFICE_CONTEXT);
		    if (foContext !=null) {
			    foContext.setAttribute(p_Key, p_Data);
		    }
		}
	}

}