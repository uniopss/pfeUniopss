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

package org.nextime.ion.framework.job;

import java.io.*;
import java.util.Date;

import org.nextime.ion.commons.Mail;
import org.nextime.ion.framework.helper.Indexer;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class IndexJob implements StatefulJob {

	public IndexJob() {
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("-- " + context.getJobDetail().getFullName() + " ["
				+ new Date() + "]");

		String rc = "";
		// reindexation

		rc += "-- ReIndexation ";
		Indexer.reIndex();
		rc += "\n@@ Traitement termine";

		// cache
		try {
			if (org.nextime.ion.framework.cache.Util.check()) {
				Mapping.begin();
				rc += "\n-- Cache ";
				org.nextime.ion.framework.cache.Util.generate();
				rc += "\n@@ Traitement termine";

			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(),  IndexJob.class,e);
			rc += "\n!! Erreur : " + e;
		} finally {
			Mapping.rollback();
		}
		try {
			FileOutputStream fot = new FileOutputStream(new File(new File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath(), "../exploit/log"),
					context.getJobDetail().getName()
							+ "_"
							+ new java.text.SimpleDateFormat("yyyyMMdd")
									.format(new java.util.Date()) + ".log"));
			fot.write(rc.getBytes());
			fot.close();
		} catch (Exception erreur) {
			// TODO
			Logger.getInstance().error(erreur.getMessage(),  IndexJob.class, erreur);
		}
	}
}