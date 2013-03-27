package org.nextime.ion.backoffice.resource;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.activation.FileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextime.ion.backoffice.bean.ResourceXmlBean;
import org.nextime.ion.backoffice.bean.Resources;
import org.nextime.ion.framework.logger.Logger;

public class ResourceServlet extends HttpServlet {

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest,
	 *      HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String qs = request.getPathInfo().substring(1);

		String resourcesId = qs.substring(qs.indexOf("/") + 1);
		String site = resourcesId.substring(0, resourcesId.indexOf("/"));
		resourcesId = resourcesId.substring(resourcesId.indexOf("/") + 1);
		String resourceId = resourcesId.substring(resourcesId.indexOf("/") + 1);
		resourcesId = resourcesId.substring(0, resourcesId.indexOf("/"));

		// retrieve resources selected
		String path = null;
		try {
			ResourceXmlBean bean = Resources.getResourceXmlBean(this,
					resourcesId);
			path = bean.getDirectory();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), ResourceServlet.class,e);
			//throw new ServletException(e);
			return;
		}
		File resources = org.nextime.ion.framework.config.Config.getInstance()
				.getResourcesDirectory();
		File tsresources = new File(resources, site);
		File tresources = new File(tsresources, path);
		File tfile = new File(tresources, resourceId);

		if (request.getParameter("width") != null
				|| request.getParameter("height") != null) {

			int w = Integer
					.parseInt((request.getParameter("width") != null ? request
							.getParameter("width") : request
							.getParameter("height"))
							+ "");
			int h = Integer
					.parseInt((request.getParameter("height") != null ? request
							.getParameter("height") : request
							.getParameter("width"))
							+ "");

			File tcache = new File(resources, site);
			File cache = new File(tcache, "cache");
			File possibleCached = new File(cache, w + "_" + h + "_"
					+ tfile.getName() + ".jpg");

			String mimeType = FileTypeMap.getDefaultFileTypeMap()
					.getContentType(possibleCached);
			response.setContentType(mimeType);

			if (possibleCached.exists()) {
				// send content
				try {
					byte[] buffer = org.nextime.ion.commons.Util
							.read(possibleCached);
					response.getOutputStream().write(buffer);
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), ResourceServlet.class,e);
				}
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {

				BufferedImage image = ImageIO.read(tfile);

				BufferedImage myImage = new BufferedImage(w, h,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = myImage.getGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, w, h);

				int width = image.getWidth();
				int height = image.getHeight();

				if (width > height) {
					Image newImage = image.getScaledInstance(w, -1,
							Image.SCALE_SMOOTH);
					g.drawImage(newImage, 0,
							(h - newImage.getHeight(null)) / 2, null);
				} else {
					Image newImage = image.getScaledInstance(-1, h,
							Image.SCALE_SMOOTH);
					g.drawImage(newImage, (w - newImage.getWidth(null)) / 2, 0,
							null);
				}

				ImageIO.write(myImage, "jpg", possibleCached);
				ImageIO.write(myImage, "jpg", response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} else {

			// retrieve mime type
			String mimeType = FileTypeMap.getDefaultFileTypeMap()
					.getContentType(tfile);
			response.setContentType(mimeType);

			// send content
			try {
				byte[] buffer = org.nextime.ion.commons.Util.read(tfile);
				response.getOutputStream().write(buffer);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), ResourceServlet.class,e);
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();

		}
	}

}