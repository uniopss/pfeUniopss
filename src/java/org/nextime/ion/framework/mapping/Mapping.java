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

package org.nextime.ion.framework.mapping;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;

import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;

/**
 * Couche de persistence utilisée par le framework
 *
 * @author gbort
 * @version 0.7
 */
public class Mapping {


	public static final String CLASS_NAME = "org.nextime.ion.framework.mapping";

	private JDOManager jdo;

	private static Mapping instance = new Mapping();

	protected Hashtable databases;

	private Mapping() {
		try {
			File f = Config.getInstance().getDatabaseConfigurationFile();
			if (f == null) {
				throw new MappingException(
						"Le fichier database.xml est nécessaire au demarrage de la couche mapping");
			}
			//if (!Config.getInstance().getDisableLog()) {
			//	jdo.setLogWriter(
			//			new org.exolab.castor.util.Logger(System.out)
			//					.setPrefix("WCM mapping"));
			//}

			String content = new String(org.nextime.ion.commons.Util.read(f));
			while (content.indexOf("%conf%") != -1) {
				content = doReplace(content, "%conf%", new File(Config.getInstance().getConfigDirectoryPath()).toURI().toString());
			}
			File temp = File.createTempFile("ion", "database.xml");
			FileOutputStream fos = new FileOutputStream(temp);
			fos.write(content.getBytes());
			fos.close();
			JDOManager.loadConfiguration(temp.getAbsolutePath());
			jdo = JDOManager.createInstance(Config.getInstance().getDatabaseName());
			databases = new Hashtable();
			Logger.getInstance().log("La couche mapping est prête.", this);
		} catch (Exception e) {
			Logger.getInstance().fatal("Erreur lors de la création de la couche mapping.",this, e);
			System.exit(0);
		}
	}

	public void reset() {
		instance = null;
	}

	/**
	 * @@return Une instance du mapping utilisé par le framework WCM.
	 */
	public static Mapping getInstance() {
		return instance;
	}

	/**
	 * @@return instances du mapping utilisé par le framework WCM.
	 * @deprecated
	 */
	private Hashtable getDatabases() {
		return databases;
	}

	/**
	 * Demmare une nouvelle transaction.
	 *
	 * @@throws MappingException
	 *             si la transaction est impossible à démarrer.
	 */
	public static void begin() throws MappingException {
		try {
			getInstance().getDb().begin();
		} catch (org.exolab.castor.jdo.PersistenceException e) {
			expireCacheIfNeed(e);
			Logger.getInstance().error("Impossible de démarrer une transaction.",getInstance(), e);
			throw new MappingException(e.getMessage());
		}
	}

	/**
	 * Valide la transaction.
	 *
	 * @@throws MappingException
	 *             si la transaction est impossible à valider.
	 */
	public static void commit() throws MappingException {
		try {
			getInstance().getDb().commit();
		} catch (org.exolab.castor.jdo.PersistenceException e) {
			Logger.getInstance().error("Impossible de valider la transaction.", Mapping.class , e);
			expireCacheIfNeed(e);
			throw new MappingException(e.getMessage());
		} finally {
			try {
				getInstance().closeDatabase();
			} catch (PersistenceException e) {
				Logger.getInstance().error("[commit]IMPOSSIBLE DE FERMER LA BASE ", Mapping.class,e);
			}
		}
	}
	/**
	 * Annule la transaction.
	 */
	public static void rollback()  {
		try {
			getInstance().getDb().rollback();
		} catch (MappingException e) {
			try {
				expireCacheIfNeed(e);
			} catch (MappingException mappingException ) {
				Logger.getInstance().error(mappingException.getMessage(), Mapping.class,mappingException);
			}
		} catch (Exception e) {
			// Logger.getInstance().error(e.getMessage(), Mapping.class,e);
		} finally {
			try {
				getInstance().closeDatabase();
			} catch (PersistenceException e) {
				Logger.getInstance().error("[rollback]IMPOSSIBLE DE FERMER LA BASE ", Mapping.class,e);
			}
		}

	}
	private static void expireCacheIfNeed(org.exolab.castor.jdo.PersistenceException e) throws MappingException {
		Throwable source = e.getCause();
		if ((source!=null) && (source instanceof LockNotGrantedException)) {
			// libérer les caches
			Logger.getInstance().error("[expireCacheIfNeed] LIBERER LES CACHES ", Mapping.class,e);
			Mapping.getInstance().expireCache();
		}
	}

	/**
	 * @@return La connection à la base de donnée utilisée pour mapper les
	 *         objets. renvoi une instance par thread
	 */
	public Database getDb() throws MappingException {
		try {
			Thread t = Thread.currentThread();
			Database db = (Database) databases.get(t);
			if (db != null) {
				return db;
			}
			db = jdo.getDatabase();
			databases.put(t, db);
			return db;
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), Mapping.class,e);
			throw new MappingException(e.getMessage());
		}
	}

	/***
	 * 	Fermermeture de la connexion courantte ( database )
	 * 	@throws MappingException : dans le cas d'erreur technique
	 */
    public void closeDatabase() throws MappingException {
        try {
    		Thread t = Thread.currentThread();
			Database db = (Database) databases.get(t);
			if (db != null) {
				db.close();
				databases.remove(t);
			}
        } catch (PersistenceException e) {
        	Logger.getInstance().error(e.getMessage(), Mapping.class,e);
			throw new MappingException(e.getMessage());
        }
    }

	/**
	 * @@return true/false selon qu'une transaction est en cours dans le thread
	 *         courant
	 */
	public boolean isTransactionActive() throws MappingException {
		try {
			Thread t = Thread.currentThread();
			Database db = (Database) databases.get(t);
			if (db == null)
				return false;
			return db.isActive();
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), Mapping.class,e);
			throw new MappingException(e.getMessage());
		}
	}

	private static String doReplace(String source, String from, String to) {
		String ret = "";
		int i = source.indexOf(from);
		if (i == -1)
			return source;
		ret = source.substring(0, i);
		ret = ret + to;
		ret = ret + source.substring(i + from.length());
		return ret;
	}

	public void expireCache() throws MappingException {
		try {
			Database db = jdo.getDatabase();
			CacheManager manager = db.getCacheManager();
			manager.expireCache();
		} catch (PersistenceException e ) {
			Logger.getInstance().error(e.getMessage(), Mapping.class,e);
			throw new MappingException(e.getMessage());
		}
	}

	/**
	 * Nettoyage des mappings.
	 * @deprecated
	 */
	public void clean() {
		java.util.Enumeration threads = databases.keys();

		while (threads.hasMoreElements()) {
			Thread thread = (Thread) threads.nextElement();
			if (!thread.isAlive()) {
				try {
					Database db = (Database) databases.get(thread);
					db.close();
					databases.remove(thread);
					Logger.getInstance().log("Le thread "  + thread + " est mort. Destruction de son instance de Database",
									this);
				} catch (Exception e) {
					Logger.getInstance().error(e.getMessage(), Mapping.class,e);
				}
			}
		}

	}
}