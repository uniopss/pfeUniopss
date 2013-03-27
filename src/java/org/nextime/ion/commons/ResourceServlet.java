package org.nextime.ion.commons;

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

import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.frontoffice.bean.ResourceXmlBean;
import org.nextime.ion.frontoffice.bean.Resources;

public class ResourceServlet extends HttpServlet {

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest,
	 *      HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("width") != null
				|| request.getParameter("height") != null
				|| request.getParameter("download") != null) {

			String qs = request.getPathInfo().substring(1);

			String resourcesId = qs.substring(qs.indexOf("/") + 1);
			String site = resourcesId.substring(0, resourcesId.indexOf("/"));
			resourcesId = resourcesId.substring(resourcesId.indexOf("/") + 1);
			String resourceId = resourcesId
					.substring(resourcesId.indexOf("/") + 1);
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
			File resources = org.nextime.ion.framework.config.Config
					.getInstance().getResourcesDirectory();
			File tsresources = new File(resources, site);
			File tresources = new File(tsresources, path);
			File tfile = new File(tresources, resourceId);

			if (request.getParameter("width") != null
					|| request.getParameter("height") != null) {

				int w = Integer
						.parseInt((request.getParameter("width") != null ? request
								.getParameter("width")
								: request.getParameter("height"))
								+ "");
				int h = Integer
						.parseInt((request.getParameter("height") != null ? request
								.getParameter("height")
								: request.getParameter("width"))
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

					/*
					 * BufferedImage myImage = new BufferedImage(w, h,
					 * BufferedImage.TYPE_INT_RGB); Graphics g =
					 * myImage.getGraphics(); g.setColor(Color.WHITE);
					 * g.fillRect(0, 0, w, h);
					 */

					int width = image.getWidth();

					/*
					 * if (width > height) { Image newImage =
					 * image.getScaledInstance(w, -1, Image.SCALE_SMOOTH);
					 * g.drawImage( newImage, 0, (h - newImage.getHeight(null)) /
					 * 2, null); } else { Image newImage =
					 * image.getScaledInstance(-1, h, Image.SCALE_SMOOTH);
					 * g.drawImage( newImage, (w - newImage.getWidth(null)) / 2,
					 * 0, null); }
					 */

					Image newImage = null;
					Image newImage2 = null;

					if (width > w) {
						newImage = image.getScaledInstance(w, -1,
								Image.SCALE_SMOOTH);
					} else {
						newImage = (Image) image;
					}

					int height = newImage.getHeight(null);

					if (height > h) {
						newImage2 = newImage.getScaledInstance(-1, h,
								Image.SCALE_SMOOTH);
					} else {
						newImage2 = newImage;
					}

					BufferedImage myImage = new BufferedImage(newImage2
							.getWidth(null), newImage2.getHeight(null),
							BufferedImage.TYPE_INT_RGB);
					Graphics g = myImage.getGraphics();
					g.drawImage(newImage2, 0, 0, null);

					ImageIO.write(myImage, "jpg", possibleCached);
					ImageIO.write(myImage, "jpg", response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
				}

			} else {
				try {
					String _download = request.getParameter("download");
					if (_download != null && !"".equals(_download)) {
						response.setContentType("application/octet-stream");
						response.setHeader("Content-Disposition",
								"attachment;filename=\"" + _download + "\"");
					} else {
						// retrieve mime type
						String mimeType = FileTypeMap.getDefaultFileTypeMap()
								.getContentType(tfile);
						response.setContentType(mimeType);
					}
					// read content
					byte[] buffer = org.nextime.ion.commons.Util.read(tfile);
					response.getOutputStream().write(buffer);
					response.getOutputStream().flush();
					response.getOutputStream().close();

				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), ResourceServlet.class,e);
				}
			}
		}
	}

	public static String read(String src) {
		String content = "";
		if (src != null && src.length() != 0) {
			try {
				java.io.File f = new java.io.File(
						org.nextime.ion.framework.config.Config.getInstance()
								.getResourcesDirectory(), src);
				// lit le contenu du fichier
				content = new String(org.nextime.ion.commons.Util.read(f));
				if (content == null || "".equals(content.trim()))
					content = "<lc/>";
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), ResourceServlet.class,e);
				return "<lc/>";
			}
		}
		return content;
	}
}