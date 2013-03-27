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

import org.nextime.ion.framework.logger.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class RunJob implements StatefulJob {

    public boolean getTrace() {
        return true;
    }

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (getTrace())
            System.out.println("-- " + context.getJobDetail().getFullName() + " [" + new Date() + "]");
        String content = "";
        String type = (String) context.getJobDetail().getJobDataMap().get("type");
        try {
            java.io.File d = new java.io.File(org.nextime.ion.framework.config.Config.getInstance()
                    .getConfigDirectoryPath(), "../bin");
            java.io.File f = new java.io.File(d, type);
            if (f.exists()) {
                String script = f.getAbsolutePath() + " " + d.getAbsolutePath();
                String[] parm = context.getMergedJobDataMap().getKeys();
                for (int i = 0; i < parm.length; i++) {
                    if (parm[i].startsWith("parm_"))
                        script += " " + parm[i] + "=" + context.getMergedJobDataMap().get(parm[i]);
                }
                if (getTrace())
                    System.out.println("@@ Script=" + script);
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(script);
                String line = "";
                java.io.BufferedReader is = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
                while (line != null) {
                    content += line + "\r\n";
                    line = is.readLine();
                }
                is.close();
                if (getTrace())
                    System.out.println("@@ CR=" + p.exitValue());
                p.destroy();
                r.freeMemory();
            }
        } catch (Exception e) {
            String host = "LC";
            try {
                host = java.net.InetAddress.getLocalHost().getHostName();
            } catch (Exception ee) {
                // TODO
                Logger.getInstance().error(ee.getMessage(), RunJob.class, e);
            }
            content += "\nErreur:" + e;
            System.err.println("!! Erreur : " + e);
            e.printStackTrace(System.err);
            org.nextime.ion.commons.Mail.send(formatter.format(new Date()) + ":CMS:ERROR:" + host + ":" + type, content);
        }
    }
}
