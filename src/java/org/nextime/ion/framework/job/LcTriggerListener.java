/*
 * �ON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * �ON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * �ON core framework, �ON content server, �ON backoffice, �ON frontoffice
 * and �ON admin application are parts of �ON and are distributed under
 * same terms of licence.
 *
 *
 * �ON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * �ON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.job;

import org.quartz.*;

/**
 * G�n�re le listener
 * 
 * @version 1.0
 */
public class LcTriggerListener implements org.quartz.TriggerListener {

	String name;

	public LcTriggerListener(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @see org.quartz.TriggerListener#triggerFired(org.quartz.Trigger,
	 *      org.quartz.JobExecutionContext)
	 */
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
	}

	/**
	 * @see org.quartz.TriggerListener#vetoJobExecution(org.quartz.Trigger,
	 *      org.quartz.JobExecutionContext)
	 */
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}

	/**
	 * @see org.quartz.TriggerListener#triggerMisfired(org.quartz.Trigger)
	 */
	public void triggerMisfired(Trigger trigger) {
		System.err.println("!! Trigger '" + trigger.getFullName()
				+ "' non lance");
	}

	/**
	 * @see org.quartz.TriggerListener#triggerComplete(org.quartz.Trigger,
	 *      org.quartz.JobExecutionContext, int)
	 */
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			int triggerInstructionCode) {
	}

}

