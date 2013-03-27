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
 * Objet event utilis� pour le systeme d'evenements du wcm.
 * 
 * @author gbort
 * @version 0.1
 */
public class WcmEvent {

	private Object _source;

	private Class _objectType;

	private String _id;

	public WcmEvent(Object source, String id) {
		_source = source;
		_objectType = source.getClass();
		_id = id;
	}

	/**
	 * Objet ayant d�clench� l'�venement
	 */
	public Object getSource() {
		return _source;
	}

	/**
	 * Classe de l'objet ayant d�clench� l'�venement
	 */
	public Class getObjectType() {
		return _objectType;
	}

	/**
	 * Id de l'objet ayant d�clench� l'�venement
	 */
	public String getId() {
		return _id;
	}
}