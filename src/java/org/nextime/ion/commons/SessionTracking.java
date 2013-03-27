package org.nextime.ion.commons;

import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionTracking implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Session crée ");

	}


	public void sessionDestroyed (HttpSessionEvent e) {
	 	HttpSession session = e.getSession();
	    String sessionid = session.getId();
	    System.out.println("Essyayer de libérer la session SSO " + sessionid );

	    ServletContext context = session.getServletContext();
	    // look if the session being destroyed is stored in the ServletContext
	    Hashtable<String, String> shareddata = (Hashtable<String, String>)context.getAttribute(CommonServlet.USER_CROSS_APPLICATION_KEY);

	    if (shareddata !=null && shareddata.containsKey(sessionid)) {
	    		System.out.println("Liberer données SSO data pour la session " + sessionid);
	    		shareddata.remove(sessionid);
	    		context.setAttribute(CommonServlet.USER_CROSS_APPLICATION_KEY, shareddata);
	    }
	  }

}
