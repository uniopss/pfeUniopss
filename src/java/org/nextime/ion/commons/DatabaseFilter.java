package org.nextime.ion.commons;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;



/**
 * 		Cette classe sert à fermer proprement les connexions castor à la base de données
 * 		Elle fonction comme un filtre pour le front-office et la back-office
 * 		Elle est utilisée commme un filtre sortant pour toutes les servlet ( struts, publication, .... )
 * 		Elle doit être déclarée dans le(s) fichiers web.xml
 * 		@author q.nguyen
 */
public class DatabaseFilter implements Filter {

	private static Log _log = LogFactory.getLog(DatabaseFilter.class);

	public void init(FilterConfig p_FilterConfig) throws ServletException {

	}
	public void doFilter(ServletRequest p_Request, ServletResponse p_Response, FilterChain p_FilterChain) throws IOException, ServletException {
		try {
			p_FilterChain.doFilter(p_Request,p_Response);
		} finally {
			try {
				Mapping.getInstance().closeDatabase();
			} catch (MappingException e) {
				_log.error("[DatabaseFilter]Impossible de fermer la base de données ", e);
			}
		}

	}

	public void destroy() {


	}








}
