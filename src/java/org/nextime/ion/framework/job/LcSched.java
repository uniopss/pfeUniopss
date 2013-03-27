/*
 * ÏON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * ÏON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * ÏON core framework, ÏON content server, ÏON backoffice, ÏON frontoffice
 * and ÏON admin application are parts of ÏON and are distributed under
 * same terms of licence.
 *
 *
 * ÏON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * ÏON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.job;

import org.nextime.ion.framework.logger.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;

import java.util.Hashtable;

/**
 * Génére le scheduler
 *
 * @version 1.0
 */
public class LcSched {

    private static Scheduler sched = null;

    public static void init() {
        try {
            if (sched == null || sched.isShutdown()) {
                sched = new org.quartz.impl.StdSchedulerFactory().getScheduler();
                sched.addGlobalJobListener(new LcJobListener("LcJobListener"));
                sched.addGlobalTriggerListener(new LcTriggerListener("LcTriggerListener"));
                sched.start();
            }
        } catch (Exception e) {
            Logger.getInstance().fatal("impossible de demarrer le scheduler ", LcSched.class, e);
        }
    }

    public static void reStart() {
        stop();
        init();
    }

    public static void execute(String p_JobName) {
        execute(p_JobName, null);
    }

    public static void execute(String p_JobName, Hashtable params) {
        try {
            if (sched != null) {
                JobDetail jobDetail = sched.getJobDetail(p_JobName, p_JobName + "Group");
                JobDataMap jobDataMap = null;
                if (params != null && !params.isEmpty())
                    jobDataMap = new JobDataMap(params);
                jobDetail.getJobDataMap().put("direct", true);
                sched.addJob(jobDetail, true);
                sched.triggerJob(p_JobName, p_JobName + "Group", jobDataMap);
            }
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), LcSched.class, e);
        }
    }

    public static boolean check() {
        try {
            if (sched != null && !sched.isShutdown())
                return true;
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), LcSched.class, e);
        }
        return false;
    }

    public static void stop() {
        try {
            if (sched != null) {
                sched.shutdown(true);
            }
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), LcSched.class, e);
        }
    }

}