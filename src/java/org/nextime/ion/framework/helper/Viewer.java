/*
 * ÏON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * ÏON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * ÏON core framework, ÏON content server, ÏON backoffice, ÏON frontoffice
 * and ÏON admin application are parts of ÏON and are distributed under
 * same terms of licence.
 *
 *
 * ÏON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * ÏON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.fop.apps.Driver;
import org.jdom.Document;
import org.nextime.ion.commons.Util;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.business.impl.PublicationVersionImpl;
import org.nextime.ion.framework.business.impl.Style;
import org.nextime.ion.framework.business.impl.TypePublicationImpl;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.xml.XML;

/**
 * Génére les vues pour les publications
 *
 * @author gbort
 * @version 1.0
 */
public class Viewer implements WcmListener {

	private static Logger _log = Logger.getInstance();

	private static Viewer _instance=new Viewer();


	static {
		PublicationImpl.addListener(_instance);
	}


	public static Viewer getInstance() {
	        return _instance;
	}

	private Viewer() {
		_log.log("Instancier la classe Viewer", Viewer.class);
	}

	private static ConsoleLogger _dl = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);

	/**
	 * Retourne la vue pour le style spécifié
	 */
	public static byte[] getView(Publication p, int version, String styleId) {
		return getView(p, version, styleId, null, null);
	}

	/**
	 * Retourne la vue pour le style spécifié
	 */
	public static byte[] getView(Publication p, int version, String styleId,
			String locale) {
		return getView(p, version, styleId, locale, null);
	}

	/**
	 * Retourne la vue pour le style spécifié
	 */
	public static byte[] getView(Publication p, int version, String styleId,
			String locale, java.util.Hashtable params) {
		styleId = changeView(p, styleId);
		if (!styleId.startsWith("indexation_")
				&& !styleId.startsWith("special_")
				&& Config.getInstance().getHtmlCacheDirectory() != null) {

			String uniqueId = generateUniqueId(p, version, styleId, locale, params);
			File possibleCache = new File(Config.getInstance().getHtmlCacheDirectory(),uniqueId );
			if (possibleCache.exists()) {
				try {
					byte[] b = Util.read(possibleCache);
					return b;
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), Viewer.class,e);
				}
			}
		}
		Style style = ((TypePublicationImpl) p.getType()).getStyleSheet(styleId);
		if (style == null)
			return null;
		Document xsl = style.getDocument();
		Document xml = null;
		if (locale == null) {
			xml = ((PublicationVersionImpl) p.getVersion(version)).getXmlDoc();
		} else {
			xml = ((PublicationVersionImpl) p.getVersion(version)).getXmlDoc(locale);
		}

		// TODO TODO TODO

		// XML xmlConversion=org.nextime.ion.framework.xml.XML.getInstance();
		//Logger.getInstance().log("Contenu de la version" , Viewer.class);
		// Logger.getInstance().log(xmlConversion.getString(xml) , Viewer.class);




		if (style.getType() == Style.XSL) {
			String html = XML.getInstance().transform(xml, xsl, params);
			if (html == null)
				html = "";
			if (!styleId.startsWith("indexation_")
					&& !styleId.startsWith("special_")
					&& Config.getInstance().getHtmlCacheDirectory() != null
					&& html != null && html.length() > 0) {

				String uniqueId = generateUniqueId(p, version,styleId, locale, params);
				File cache = new File(Config.getInstance().getHtmlCacheDirectory(), uniqueId);
				try {
					FileOutputStream fos = new FileOutputStream(cache);
					fos.write(html.getBytes());
					fos.close();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), Viewer.class,e);
				}
			}
			return html.getBytes();
		} else {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Driver driver = new Driver();
				driver.setLogger(_dl);
				org.apache.fop.messaging.MessageHandler.setScreenLogger(_dl);
				driver.setOutputStream(out);
				driver.setRenderer(Config.getInstance().getFoRenderType());
				org.nextime.ion.framework.xml.XML.getInstance().transformDriver(driver, xml, xsl, params);

				if (Config.getInstance().getHtmlCacheDirectory() != null
										&& out != null && out.size() > 0) {
					String uniqueId = generateUniqueId(p,version, styleId, locale, null);
					File cache = new File(Config.getInstance().getHtmlCacheDirectory(), uniqueId);
					try {
						FileOutputStream fos = new FileOutputStream(cache);
						fos.write(out.toByteArray());
						fos.close();
					} catch (Exception e) {
						// TODO
						Logger.getInstance().error(e.getMessage(), Viewer.class,e);
					}
				}
				return out.toByteArray();
			} catch (Exception e) {
				//e.printStackTrace();
				Logger.getInstance().error(e.getMessage(), Viewer.class,e);
			}
		}
		return new byte[0];
	}

	/**
	 * Retourne la vue pour le style spécifié
	 */
	public static byte[] getView(String id, int version, String type,
			String publication, String p_styleId, String locale,
			java.util.Hashtable params) {

		String styleId = p_styleId;

		if (styleId.startsWith("-"))
			styleId = styleId.substring(1);

		if (!styleId.startsWith("indexation_")
				&& !styleId.startsWith("special_")
				&& Config.getInstance().getHtmlCacheDirectory() != null) {
			String uniqueId = generateUniqueId(id, version, styleId, locale, params);
			File possibleCache = new File(Config.getInstance().getHtmlCacheDirectory(), uniqueId);
			if (possibleCache.exists()) {
				try {
					byte[] b = org.nextime.ion.commons.Util.read(possibleCache);
					return b;
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), Viewer.class,e);
				}
			}
		}

		try {
			Document xml = XML.getInstance().getDocument(publication);
			if (xml == null)
				return null;

			Document xsl = TypePublication.getDocument(type, styleId);
			if (xsl == null)
				return null;

			String html = XML.getInstance().transform(xml, xsl, params);
			if (html == null)
				html = "";

			if (!styleId.startsWith("indexation_")
					&& !styleId.startsWith("special_")
					&& Config.getInstance().getHtmlCacheDirectory() != null
					&& html != null && html.length() > 0) {
				String uniqueId = generateUniqueId(id, version, styleId, locale, params);
				File cache = new File(Config.getInstance().getHtmlCacheDirectory(), uniqueId);
				try {
					FileOutputStream fos = new FileOutputStream(cache);
					fos.write(html.getBytes());
					fos.close();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), Viewer.class,e);
				}
			}
			return html.getBytes();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Viewer.class,e);
		}
		return new byte[0];
	}

	public static byte[] getView(String id, int version, String type,
			String publication, String styleId, String locale) {
		return getView(id, version, type, publication, styleId, locale, null);
	}

	private static String changeView(Publication p, String styleId) {
		// Le changement est conditionne par le prefixe
		if (!styleId.startsWith("-"))
			return styleId;
		// Vue donnee par la publication
		String lcVue = (String) p.getMetaData("lcPresentation");
		if (lcVue != null && !"".equals(lcVue))
			return lcVue;
		// Sinon vue donnee en parametre (nettoyee a cause du prefixe)
		return styleId.replaceFirst("-", "");
	}

	/**
	 * 	Génération d'un numéro unique pour le cache de publication
	 * @param id
	 * @param version
	 * @param styleId
	 * @param locale
	 * @return
	 */
	private static String generateUniqueId(String id, int version,
			String styleId, String locale, Map<String, String> p_Params) {
		StringBuffer sb = new StringBuffer();
		sb.append(id).append("$").append(styleId).append("$").append("version").append(version);
		if (locale != null) {
			sb.append("_").append(locale);
		}
		if (p_Params!=null && !p_Params.isEmpty() ) {
			sb.append("$");
			for (Iterator it=p_Params.keySet().iterator(); it.hasNext();) {
				String key=(String)it.next();
				sb.append(key).append("_").append(p_Params.get(key));
			}
		}
		sb.append(".cache");
		return sb.toString();

	}

	/**
	 * 		Génération d'un numéro unique pour la publication
	 * @param p
	 * @param version
	 * @param styleId
	 * @param locale
	 * @return
	 */
	private static String generateUniqueId(Publication p, int version, String styleId, String locale, Map<String, String> p_Params) {
		return generateUniqueId(p.getId(), version, styleId, locale, p_Params);
	}

	// implementation de WcmListener

	/**
	 * Ne rien faire
	 */
	public void objectLoaded(WcmEvent event) {
			return;
	}

	public void objectCreated(WcmEvent event) {
	}

	/*
	public void objectModified(WcmEvent event) {
		Publication p = (Publication) event.getSource();
		if (Config.getInstance().getHtmlCacheDirectory() != null) {
			for (int i = 1; i <= p.getVersions().size(); i++) {
				Vector v = p.getType().listStyles();
				for (int t = 0; t < v.size(); t++) {
					File possibleCache = new File(Config.getInstance().getHtmlCacheDirectory(),
								generateUniqueId(p, i, v.get(t)+ "", null));
					possibleCache.delete();
					Iterator it = LocaleList.getInstance().getLocales().iterator();
					while (it.hasNext()) {
						possibleCache = new File(Config.getInstance().getHtmlCacheDirectory(),
									generateUniqueId(p,i, v.get(t) + "", ((Locale) it.next()).getLocale()));
						possibleCache.delete();
					}
				}
			}
		}
	}
	*/
	public void objectModified(WcmEvent event) {
		Publication p = (Publication) event.getSource();
		if (Config.getInstance().getHtmlCacheDirectory() != null) {
			cleanCache(p.getId());

		}

	}


	public void objectDeleted(WcmEvent event) {
		objectModified(event);
	}

	public static void cleanCache() {
		if (Config.getInstance().getHtmlCacheDirectory() != null) {
			java.io.FilenameFilter filter = new java.io.FilenameFilter() {
				public boolean accept(java.io.File dir, String name) {
					return name.matches("^p.+\\.cache$");
				}
			};
			File[] files = Config.getInstance().getHtmlCacheDirectory()
					.listFiles(filter);
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	public static void cleanCache(String id) {
		if (Config.getInstance().getHtmlCacheDirectory() != null) {
			final String _filter = id;
			java.io.FilenameFilter filter = new java.io.FilenameFilter() {
				public boolean accept(java.io.File dir, String name) {
					return name.matches("^" + _filter + ".+\\.cache$");
				}
			};
			File[] files = Config.getInstance().getHtmlCacheDirectory()
					.listFiles(filter);
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
		}
	}

	public void objectBeforeRemove(WcmEvent event) {
		// TODO Auto-generated method stub

	}

	public void objectPostChange(WcmEvent event) {
		// TODO Auto-generated method stub

	}

	public void objectBeforeChange(WcmEvent event) {
		// TODO Auto-generated method stub

	}
}