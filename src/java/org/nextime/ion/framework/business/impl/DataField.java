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

package org.nextime.ion.framework.business.impl;

/**
 * Represente un champ de donn�e ( metaData ou data ) c�d une paire clef/valeur.
 * Cet objet est utilis� par le wrapper xml.
 */
public class DataField {

	private String _name;

	private String _value;

	public DataField(String name, String value) {
		_name = name;
		_value = value;
	}

	public DataField() {
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	public void setName(String value) {
		_name = value;
	}

	public void setValue(String value) {
		_value = value;
	}
}