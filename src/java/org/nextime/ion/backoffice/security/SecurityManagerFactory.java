package org.nextime.ion.backoffice.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.nextime.ion.framework.logger.Logger;

public class SecurityManagerFactory extends HttpServlet {

	private static String securityManager;

	private static SecurityManager instance;

	public void init() throws ServletException {
		securityManager = getServletConfig().getInitParameter(
				"securityManagerClass");
	}

	public static SecurityManager getInstance() {
		try {
			if (instance == null) {
				instance = (SecurityManager) Class.forName(securityManager)
						.newInstance();
			}
			return instance;
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(e.getMessage(), SecurityManagerFactory.class,e);
			return null;
		}
	}

}