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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.exolab.castor.jdo.Database;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.impl.PublicationDocument;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

import sun.util.calendar.CalendarUtils;

/**
 * Indexe les publications
 *
 * @author gbort
 * @version 1.0
 */
public class Indexer implements WcmListener {

	private static Logger _log = Logger.getInstance();

	private static Indexer _instance=new Indexer();

	static {
		if (Config.getInstance().getAutoIndex()) {
			PublicationImpl.addListener(_instance);
		}
	}

    public static Indexer getInstance() {
        return _instance;
    }

	/**
	 * 	Initilisation de tous les indexations de tous les langues
	 */
	private Indexer() {
		_log.log("Instancier la classe Indexer", Indexer.class);

		String[] indexNames = Config.getInstance().getIndexNames();
		for (int i = 0; i < indexNames.length; i++) {
			try {
				File rep = new File(Config.getInstance().getIndexRoot(), indexNames[i]);
				if (!rep.exists()) {
					rep.mkdir();
				}
				if (rep.exists() && rep.listFiles().length == 0) {
					IndexWriter writer = new IndexWriter(rep, new FrenchAnalyzer(), true);
					writer.close();
				}
			} catch (IOException e) {
				Logger.getInstance().error("Initialize error " + e.getMessage(), Indexer.class,e);
			}
		}
	}

	/**
	 * re indexe toutes les publications
	 */
	public static synchronized void reIndex() {
			// flush();
			List<String> ids = new ArrayList();
			try {
				Mapping.begin();
				ids = Publication.listAllIds();
			} catch (MappingException e) {
				Mapping.rollback();
			}
			finally {
				Mapping.rollback();
			}
			Logger.getInstance().log("Commencer à indexer " + ids.size() + " elements " , Indexer.class);
			for(int indexCount=0;indexCount<ids.size();indexCount++) {
				Logger.getInstance().log("Commencer à indexer element No "  + indexCount, Indexer.class);
				String pId = ids.get(indexCount);

				Publication p = null;
				try {
					Mapping.begin();
					p = Publication.getInstance(pId, Database.ReadOnly);

					for (int versionNumber = 1; p!=null && versionNumber <= p.getVersions().size(); versionNumber++) {
						Date date = new Date();

						String[] s = Config.getInstance().getIndexNames();
						for (int i = 0; i < s.length; i++) {
							IndexWriter writer = null;
							try {
								if (IndexReader.indexExists(new File(Config.getInstance().getIndexRoot(), s[i]+ "-work$"))) {
									writer = new IndexWriter(new File(Config.getInstance().getIndexRoot(), s[i]+ "-work$"),
															new FrenchAnalyzer(),
															false);
								} else {
									writer = new IndexWriter(new File(Config.getInstance().getIndexRoot(), s[i]+ "-work$"),
															new FrenchAnalyzer(),
															true);
								}
								writer.addDocument(PublicationDocument.getDocument(p, versionNumber, s[i]));
								writer.close();
							} catch (Exception e) {
								if (writer != null)
									writer.close();
								Logger.getInstance().error("impossible d'indexer la publication " + p.getId() + " dans l'index "+ s[i], Indexer.class, e);
							}
						}
						Logger.getInstance().log(" Temps passé "  + ((new Date()).getTime() - date.getTime()) + " ms " , Indexer.class);
					}

				} catch (MappingException e) {
					Logger.getInstance().error("impossible de récupérer la publication " + pId , Indexer.class, e);
					// on ignore cette erreur et continue pour les restes
				} catch (IOException e) {
					Logger.getInstance().error("Erreur d'indexation " + pId , Indexer.class, e);
					// on ignore cette erreur et continue pour les restes
				} finally {
					Mapping.rollback();
				}

			} // end for

		// restaurer indexation du repertoire work vers le répertoire d'indexation
		String[] indexNames = Config.getInstance().getIndexNames();
		for (int i = 0; i < indexNames.length; i++) {
			File from = new File(Config.getInstance().getIndexRoot(), indexNames[i]+ "-work$");
			File to = new File(Config.getInstance().getIndexRoot(), indexNames[i]);

			File[] files = to.listFiles();
			for (int k = 0; k < files.length; k++) {
				files[k].delete();
			}
			to.delete();
			from.renameTo(to);
		}
	}

	/**
	 * détruit une base d'index
	 */
	public static void flush(String indexName) {
		try {
			File rep = new File(Config.getInstance().getIndexRoot(), indexName);
			if (!rep.exists())
				rep.mkdir();
			else {
				IndexWriter writer = new IndexWriter(rep, new FrenchAnalyzer(), true);
				writer.close();
			}
		} catch (IOException e) {
			Logger.getInstance().error("Flush error " + e.getMessage(), Indexer.class,e);
		}
	}

	/**
	 * détruit toues les bases d'index
	 */
	public static void flush() {
		String[] s = Config.getInstance().getIndexNames();
		for (int i = 0; i < s.length; i++) {
			flush(s[i]);
		}
	}

	/**
	 * Indexe la publication passée en paramétre
	 */
	public static void index(Publication p) {
		for (int versionNumber = 1; versionNumber <= p.getVersions().size(); versionNumber++) {
			String[] s = Config.getInstance().getIndexNames();
			for (int indexCounter = 0; indexCounter < s.length; indexCounter++) {
				IndexWriter writer = null;
				try {
					writer = new IndexWriter(new File(Config.getInstance().getIndexRoot(),
												s[indexCounter]), new FrenchAnalyzer(),
													false);
					writer.addDocument(PublicationDocument.getDocument(p, versionNumber, s[indexCounter]));
					writer.close();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), Indexer.class,e);
					try {
						if (writer != null)
							writer.close();
					} catch (Exception ee) {
						// TODO
						Logger.getInstance().error(ee.getMessage(), Indexer.class,ee);
					}
				}
			}
		}
	}


	public static void index(PublicationVersion pv) {
		String[] s = Config.getInstance().getIndexNames();
		for (int i = 0; i < s.length; i++) {
			IndexWriter writer = null;
			try {
				writer = new IndexWriter(new File(Config.getInstance()
						.getIndexRoot(), s[i]), new FrenchAnalyzer(), false);
				writer.addDocument(PublicationDocument.getDocument(pv
						.getPublication(), pv.getVersion(), s[i]));
				writer.close();
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(),Indexer.class,e);
				try {
					if (writer != null)
						writer.close();
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(),Indexer.class,ee);
				}
			}
		}
	}

	/**
	 * Retire la publication passée en paramétre de l'index
	 */
	public static void remove(Publication p) {
		String[] s = Config.getInstance().getIndexNames();
		for (int i = 0; i < s.length; i++) {
			Term term = new Term("id", p.getId());
			IndexReader reader = null;
			try {
				reader = IndexReader.open(new File(Config.getInstance().getIndexRoot(), s[i]));
				reader.deleteDocuments(term);
				reader.close();
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), Indexer.class,e);
				try {
					if (reader != null)
						reader.close();
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(), Indexer.class,ee);
				}
				Logger.getInstance().error(
						"impossible de supprimer la publication " + p.getId()
								+ " de l'index " + s[i], Indexer.class, e);
			}
		}
	}

	public static void refresh(Publication p) {
		if (Config.getInstance().getAutoIndex()) {
			remove(p);
			index(p);
		}
	}

	// implementation de WcmListener

	/**
	 * pour l'indexation automatique
	 */
	public void objectCreated(WcmEvent event) {
		index((Publication) event.getSource());
	}

	/**
	 * pour l'indexation automatique
	 */
	public void objectModified(WcmEvent event) {
		remove((Publication) event.getSource());
		index((Publication) event.getSource());
	}

	/**
	 * pour l'indexation automatique
	 */
	public void objectDeleted(WcmEvent event) {
		remove((Publication) event.getSource());
	}

	/**
	 * Ne rien faire
	 */
	public void objectLoaded(WcmEvent event) {
			return;
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
