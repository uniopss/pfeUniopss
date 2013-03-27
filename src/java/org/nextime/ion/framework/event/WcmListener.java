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

package org.nextime.ion.framework.event;

/**
 * Les objets souhaitant etres notifi�s des �venements du framework WCM doivent
 * impl�menter cette interface
 *
 * @author gbort
 * @version 0.1
 */
public interface WcmListener {


	/**
	 * chaque fois qu'un nouvel objet est charg�
	 */
	public void objectLoaded(WcmEvent event);

	/**
	 * chaque fois qu'un nouvel objet est cr��
	 */
	public void objectCreated(WcmEvent event);

	/**
	 * chaque fois qu'un objet � �t� modifi� ( d�clench� au moment de faire
	 * persister l'objet )
	 */
	public void objectModified(WcmEvent event);

	/**
	 * chaque fois qu'un nouvel objet � �t� supprim�
	 */
	public void objectDeleted(WcmEvent event);


	/**
	 * devrait appell� avant que l'objet change de ses valeur
	 */
	public void objectBeforeChange(WcmEvent event);

	/**
	 * chaque fois qu'un nouvel objet � �t� modifi� mais pas encore update
	 */
	public void objectPostChange(WcmEvent event);

	/**
	 * chaque fois qu'un nouvel objet a �t� marqu� supprim� mais pas encore delete de la base
	 */
	public void objectBeforeRemove(WcmEvent event);
}