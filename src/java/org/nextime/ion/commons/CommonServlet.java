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
package org.nextime.ion.commons;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.nextime.ion.framework.job.LcSched;

public class CommonServlet extends HttpServlet   {

	// Données partagée entre 2 applications : BackOffice et FrontOffice

	// les données conernant l'utilisateur pour la connexion SSO
	public static String USER_CROSS_APPLICATION_KEY = "USER_CROSS_APPLICATION_KEY";

	public static String FRONT_OFFICE_CONTEXT = "/";
	public static String BACK_OFFICE_CONTEXT = "/backoffice/";

	private static boolean _init = false;

	/**
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		if (!_init) {
			org.nextime.ion.framework.config.Config.getInstance().getIndexRoot();
			org.nextime.ion.framework.workflow.Workflow.init();
			// init log4j

			String prefix = getServletContext().getRealPath("/");
			PropertyConfigurator.configure(prefix+"WEB-INF/classes/log4j.properties");
			LcSched.init();
			_init = true;
		}
	}

	/**
	 * @see javax.servlet.Servlet#destroy
	 */
	public void destroy() {
		LcSched.stop();
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest,
	 *      HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("sched") != null
				|| request.getParameter("sched") != null) {
			response.getWriter().println(
					LcSched.check() ? "CHECKISOK" : "CHECKISKO");
		} else
			response.getWriter().println("CHECKISOK");
	}



}