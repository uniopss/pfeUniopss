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

package org.nextime.ion.commons;

import org.nextime.ion.framework.logger.Logger;

import sun.misc.BASE64Encoder;

public class Authenticator {
	private static BASE64Encoder encoder = new BASE64Encoder();

	public static boolean checkPassword(
			org.nextime.ion.framework.business.User user, String password) {
		try {
			return (user.getPassword().equals(createPassword(password)));
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Authenticator.class,e);
		}
		return false;
	}

	public static String createPassword(String password) {
		return encoder.encodeBuffer(password.getBytes()).trim();
	}
}