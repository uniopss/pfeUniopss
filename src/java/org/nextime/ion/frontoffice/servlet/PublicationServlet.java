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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.frontoffice.bean.IonStatus;

public class PublicationServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			// récupération l'indentifiant de la publicaiton
			org.nextime.ion.commons.Util.initialize(request);

			// cherche l'identifiant de la publication demandée dans
			// la query string
			String requestedId = (request.getPathInfo() != null) ? request.getPathInfo().substring(1) : null;
			if (requestedId != null) {
				if (requestedId.indexOf(".") != -1) {
					requestedId = requestedId.substring(0, requestedId
							.indexOf("."));
				}
			}

			// rollback dirty transaction ??
			if (Mapping.getInstance().isTransactionActive()) {
				try {
					Mapping.rollback();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), PublicationServlet.class,e);
				}
			}

			// demarre une transaction pour accéder aux
			// données du framework
			Mapping.begin();

			// recupére la section de cette publication
			Section section = null;

			if (request.getSession().getAttribute("navSection") != null) {
					section = (Section) request.getSession().getAttribute("navSection");
			}
			if (section == null) {

				// recupére une instance sur la Publication
				Publication publication = null;
				try {
					publication = Publication.getInstance(requestedId);
					if (publication == null)
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

				section = (Section) publication.listSections().firstElement();
				request.getSession().setAttribute("navSection", section);
			}
			// dans le cas du tronc commun, on reste sur le tronc commun

			// si non on prend la section de détail du site courant
			String currentSite=section.getRootSection();

			// créé le bean de status
			IonStatus status = new IonStatus();
			status.setCurrentSection(section);
			status.setCurrentSite(section.getRootSection());

			if (request.getParameter("static") != null)
				status.setIsStatic(true);
			request.setAttribute("ionStatus", status);
			// utilise la metaData "template" de la section
			// pour decider vers quelle vue rediriger le flux
			String jsp = "/section/"+currentSite + "_detail.jsp?publicationId=" +  requestedId;
			getServletContext().getRequestDispatcher(jsp).forward(request, response);
		} catch (Exception e) {
			Mapping.rollback();
			try {
				getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
			} catch (Exception ee) {
				//transmet l'exception a tomcat
				throw new ServletException(e);
			}
		} finally {
			Mapping.rollback();

		}
	}

	protected String getDefaultSectionId() throws MappingException {
		// selectionne la premiére des sections racines
		Vector rootSections = Section.listRootSections();
		return ((Section) rootSections.get(0)).getId();
	}

}