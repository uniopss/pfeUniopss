package org.nextime.ion.frontoffice.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

import org.nextime.ion.commons.Util;

import java.util.Hashtable;

import org.nextime.ion.framework.business.Publication;

import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.helper.Viewer;

/**
 * @author gbort
 */
public class PdfPublicationServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String file = request.getParameter("file");
		if (file != null && !"".equals(file)) {
			response.setContentType("application/pdf");
			Util.initialize(request);
			String requestedId = (request.getPathInfo() != null) ?
						request.getPathInfo().substring(1) : null;
			if (requestedId != null) {
				if (requestedId.indexOf(".") != -1) {
					requestedId = requestedId.substring(0, requestedId.indexOf("."));
				}
			}
			int version = -1;
			String view = request.getParameter("view");
			if (view != null && !"".equals(view)) {
				try {
					if (Mapping.getInstance().isTransactionActive()) {
						try {
							Mapping.rollback();
						} catch (Exception ee) {
							// TODO
							Logger.getInstance().error(ee.getMessage(), PdfPublicationServlet.class,ee);
						}
					}
					Mapping.begin();
					Publication p = Publication.getInstance(requestedId);
					if (p == null)
						throw new Exception();
					if (request.getParameter("version") != null
							&& !"".equals(request.getParameter("version")))
						version = Integer.parseInt(request.getParameter("version"));
					else
						version = p.getLastVersion().getVersion();
					byte[] buffer = null;
					if (version > 0) {
						Hashtable params = new Hashtable();
						Enumeration ps = request.getParameterNames();
						while (ps.hasMoreElements()) {
							String name = ps.nextElement() + "";
							if (name.startsWith("parm_"))
								params.put(name, request.getParameter(name));
						}
						params.put("parm_resources",
								Config.getInstance().getResourcesDirectory());
						buffer = Viewer.getView(p, version, view,
								request.getSession().getAttribute("currentLocale")+ "",
								params);
						if (buffer != null) {
							response.setContentLength(buffer.length);
							response.getOutputStream().write(buffer);
						}
					}
					response.getOutputStream().flush();
					//response.getOutputStream().close();

				} catch (Exception e) {
					Mapping.rollback();
					try {
						getServletContext().getRequestDispatcher("/error.jsp")
								.forward(request, response);
					} catch (Exception ee) {
						//transmet l'exception a tomcat
						throw new ServletException(e);
					}
				} finally {
					Mapping.rollback();
				}
			}
		}
	}

}