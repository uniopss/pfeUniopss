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

package org.nextime.ion.frontoffice.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

public class BeginTag extends TagSupport {

	public int doStartTag() throws JspException {
		try {
			if (Mapping.getInstance().isTransactionActive()) {
				try {
					Mapping.rollback();
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(), BeginTag.class,ee);
				}
			}

			Mapping.begin();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BeginTag.class,e);
			Mapping.rollback();
		}
		return EVAL_PAGE;
	}
}