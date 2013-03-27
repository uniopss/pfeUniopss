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

package org.nextime.ion.frontoffice.servlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.frontoffice.bean.IonStatus;
import org.nextime.ion.frontoffice.bean.SectionTypes;
import org.nextime.ion.frontoffice.smartCache.SmartCacheManager;

public class SectionServlet extends HttpServlet {

	private static final String ION_LAST_UPDATE_ATT_INTERNAL = "ION_LAST_UPDATE_ATT_INTERNAL";
	public static SmartCacheManager cache;


	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (cache == null) {
			cache = SmartCacheManager.getInstance();
		}

		try {
			org.nextime.ion.commons.Util.initialize(request);
			cleanCacheIfNeed();

			// cherche l'identifiant de la section demandée dans
			// la query string
			String requestedId = (request.getPathInfo() != null) ? request.getPathInfo().substring(1) : null;
			if (requestedId != null) {
				if (requestedId.indexOf(".") != -1) {
					requestedId = requestedId.substring(0, requestedId.indexOf("."));
				}
			}

			// rollback dirty transaction ??
			if (Mapping.getInstance().isTransactionActive()) {
				try {
					Mapping.rollback();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), SectionServlet.class,e);
				}
			}
			// demarre une transaction pour accéder aux
			// données du framework wcm
			Mapping.begin();

			// choisis une section par default si aucune n'est
			// spécifiée
			if (requestedId == null) {
				requestedId = getDefaultSectionId();
			}

			// recupére une instance sur la Section
			Section section = null;
			try {
				section = Section.getInstance(requestedId);
				if (section == null)
					throw new Exception();
			} catch (Exception e) {
				try {
					getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
					return;
				} catch (Exception ee) {
					//transmet l'exception a tomcat
					throw new ServletException(e);
				}
			}
			// créé le bean de status
			IonStatus status = new IonStatus();
			status.setCurrentSite(section.getRootSection());
			status.setCurrentSection(section);
			request.getSession().setAttribute("navSection", section);
			if (request.getParameter("static") != null) {
				status.setIsStatic(true);
			}
			request.setAttribute("ionStatus", status);

			// utilise la metaData "template" de la section
			// pour decider vers quelle vue rediriger le flux
			String template = (String) section.getMetaData("template");

			// template
			if (template == null)
				template = "default";
			String jsp = SectionTypes.getSectionBean(this, template).getJsp();
			if (request.getParameter("templateType") != null) {
				jsp = jsp.substring(0, jsp.lastIndexOf(".jsp")) + "-" + request.getParameter("templateType") + ".jsp";
			}
			getServletContext().getRequestDispatcher("/templates/" + jsp).forward(request, response);

		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), SectionServlet.class,e);

			Mapping.rollback();
			try {
				getServletContext().getRequestDispatcher("/error.jsp").forward(
						request, response);
			} catch (Exception ee) {
				//transmet l'exception a tomcat
				throw new ServletException(e);
			}
		} finally {
			Mapping.rollback();

		}
	}

	public void cleanCacheIfNeed() throws MappingException {
		String lastUpdate = (String)getServletContext().getAttribute(SmartCacheManager.ION_LAST_UPDATE_ATT);
		String lastUpdateInternal = (String)getServletContext().getAttribute(ION_LAST_UPDATE_ATT_INTERNAL);
		if (lastUpdate==null || lastUpdateInternal==null || !lastUpdate.equals(lastUpdateInternal)) {
				Mapping.getInstance().expireCache();
				getServletContext().setAttribute(ION_LAST_UPDATE_ATT_INTERNAL, lastUpdate);
		}
	}

	protected String getDefaultSectionId() throws MappingException {
		// selectionne la premiére des sections racines
		Vector rootSections = Section.listRootSections();
		return ((Section) rootSections.get(0)).getId();
	}

}