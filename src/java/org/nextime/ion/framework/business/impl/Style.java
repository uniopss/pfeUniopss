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

package org.nextime.ion.framework.business.impl;

import org.jdom.Document;

public class Style {

	static final public int XSL = 0;

	static final public int XSL_FO = 1;

	private int _type;

	private Document _doc;

	public Style(int type, Document doc) {
		_type = type;
		_doc = doc;
	}

	public int getType() {
		return _type;
	}

	public Document getDocument() {
		return _doc;
	}

}