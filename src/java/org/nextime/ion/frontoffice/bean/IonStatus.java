/*
 * �ON content management system. Copyright (C) 2002 Guillaume
 * Bort(gbort@msn.com). All rights reserved.
 * 
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 * 
 * �ON is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * �ON core framework, �ON content server, �ON backoffice, �ON frontoffice and
 * �ON admin application are parts of �ON and are distributed under same terms
 * of licence.
 * 
 * 
 * �ON includes software developed by the Apache Software Foundation
 * (http://www.apache.org/) and software developed by the Exolab Project
 * (http://www.exolab.org).
 * 
 * �ON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

package org.nextime.ion.frontoffice.bean;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

public class IonStatus {

	private String currentSite;

	private Section currentSection;

	private Publication currentPublication;

	private int currentVersion;

	private boolean isStatic;

	public IonStatus() {
	}

	public String getCurrentSite() {
		return currentSite;
	}

	public void setCurrentSite(String value) {
		currentSite = value;
	}

	public Section getCurrentSection() {
		return currentSection;
	}

	public void setCurrentSection(String id) {
		try {
			currentSection = Section.getInstance(id);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IonStatus.class,e);
		}

	}

	public void setCurrentSection(Section value) {
		currentSection = value;
	}

	/**
	 * Returns the currentPublication.
	 * 
	 * @return Publication
	 */
	public Publication getCurrentPublication() {
		return currentPublication;
	}

	/**
	 * Sets the currentPublication.
	 * 
	 * @param currentPublication
	 *            The currentPublication to set
	 */
	public void setCurrentPublication(Publication currentPublication) {
		this.currentPublication = currentPublication;
	}

	/**
	 * Returns the isStatic.
	 * 
	 * @return boolean
	 */
	public boolean getIsStatic() {
		return isStatic;
	}

	/**
	 * Sets the isStatic.
	 * 
	 * @param isStatic
	 *            The isStatic to set
	 */
	public void setIsStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * Returns the currentVersion.
	 * 
	 * @return int
	 */
	public int getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Sets the currentVersion.
	 * 
	 * @param currentVersion
	 *            The currentVersion to set
	 */
	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}

}

