/*
 * �ON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 * 
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 * 
 * �ON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * �ON core framework, �ON content server, �ON backoffice, �ON frontoffice
 * and �ON admin application are parts of �ON and are distributed under
 * same terms of licence.
 * 
 * 
 * �ON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 * 
 * �ON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.business;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.jdom.Document;
import org.nextime.ion.framework.business.impl.TypePublicationImpl;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.framework.xml.XML;

/**
 * L'objet <b>TypePublication </b> contient les informations sur les champs de
 * contenu des publications ainsi que sur les differents styles de pr�sentation.
 * Chaque Publication appartient forc�ment � un et un seul TypePublication.
 * 
 * @author gbort
 * @version 1.1
 */
public abstract class TypePublication {

	// m�thodes de fabrication

	/**
	 * Charge une instance d'un objet TypePublication existant correspondant �
	 * l'identifiant pass� en param�tre. <i>(correspond � un SELECT) </i>
	 * 
	 * @param id
	 *            L'id de l'objet TypePublication � charger.
	 * @return Une instance de TypePublication.
	 * @throws MappingException
	 *             si l'objet TypePublication est impossible � charger. En
	 *             particulier si l'id ne correspond � aucun objet
	 *             TypePublication en base de donn�e.
	 */
	public static TypePublication getInstance(String id)
			throws MappingException {
		try {
			TypePublication u = (TypePublication) Mapping.getInstance().getDb()
					.load(TypePublicationImpl.class, id);
			Logger.getInstance().log(
					"Une instance de l'objet TypePublication pour l'id " + id
							+ " � �t� charg�e.", TypePublication.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de charger une instance de l'objet TypePublication pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, TypePublication.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Cr�� un nouvel objet TypePublication avec l'id pass� en param�tre.
	 * <i>(correspond � un INSERT) </i>
	 * 
	 * @param id
	 *            l'id de l'objet TypePublication � cr�er.
	 * @return Une instance de TypePublication.
	 * @throws MappingException
	 *             si l'objet TypePublication est impossible � cr�er. En
	 *             particulier si un objet TypePublication avec le m�me id
	 *             existe d�j� en base de donn�e.
	 */
	public static TypePublication create(String id) throws MappingException {
		try {
			TypePublicationImpl u = new TypePublicationImpl();
			u.setId(id);
			Mapping.getInstance().getDb().create(u);
			Logger.getInstance()
					.log(
							"Un objet TypePublication pour l'id " + id
									+ " � �t� cr�e.", Group.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de cr�er l'objet TypePublication pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, TypePublication.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Detruit l'objet TypePublication correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i> Cette m�thode est �quivalente � un
	 * <code>getInstance(id).remove()</code>.
	 * 
	 * @param id
	 *            L'id du TypePublication � detruire.
	 * @throws MappingException
	 *             si le TypePublication est impossible � d�truire. En
	 *             particulier si le getInstance � echou�.
	 */
	public static void remove(String id) throws MappingException {
		try {
			TypePublication u = getInstance(id);
			u.remove();
		} catch (MappingException e) {
			String message = "Etes vous certain qu'un objet TypePublication pour l'id "
					+ id + " existe ?";
			Logger.getInstance().error(message, TypePublication.class, e);
		}
	}

	/**
	 * Detruit l'objet TypePublication correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i>
	 * 
	 * @param id
	 *            L'id du TypePublication � detruire.
	 * @throws MappingException
	 *             si le TypePublication est impossible � d�truire.
	 */
	public void remove() throws MappingException {
		try {
			Mapping.getInstance().getDb().remove(this);
			Logger.getInstance().log(
					"L'objet TypePublication pour l'id " + getId()
							+ " � �t� detruit.", Group.class);
		} catch (PersistenceException e) {
			String message = "Impossible de detruire l'objet TypePublication pour l'id "
					+ getId() + ".";
			Logger.getInstance().error(message, TypePublication.class, e);
			throw new MappingException(message);
		}
	}

	// m�thodes business

	/**
	 * Retourne l'identifiant de cet objet TypePublication
	 * 
	 * @return id du Publication.
	 */
	public abstract String getId();

	/**
	 * Ajoute une nouvelle meta donn�e � l'objet. <br>
	 * Une m�ta donn�e est une paire cl�/valeur ( String / Object )
	 * 
	 * @param key
	 *            cl� sevant � identifier la meta donn�e.
	 * @param value
	 *            la valeur de la meta donn�e. Peut �tre tout objet impl�mentant
	 *            java.io.Serializable.
	 * @throws MappingException
	 *             si l'objet que vous tentez d'ajouter comme meta donn�e n'est
	 *             pas Serializable
	 */
	public abstract void setMetaData(String key, Object value)
			throws MappingException;

	/**
	 * Retrouve une meta donn�e de l'objet.
	 * 
	 * @see #setMetaData(String,Object)
	 * @param key
	 *            cl� sevant � identifier la meta donn�e.
	 * @return la meta donn�e ou null si il n'y en a pas.
	 */
	public abstract Object getMetaData(String key);

	/**
	 * Retourne la Hashtable de meta donn�e. A utiliser avec pr�caution en
	 * �criture. Pr�f�rez largement setMetaData()
	 * 
	 * @return l'objet Hashtable contenant les meta donn�es;
	 */
	public abstract Hashtable getMetaData();

	/**
	 * Detruit une meta donn�e de l'objet. <br>
	 * Notez que cette m�thode ne fait rien si il n'y avais pas de meta donn�e.
	 * 
	 * @see #setMetaData(String,Object)
	 * @param key
	 *            cl� sevant � identifier la meta donn�e.
	 */
	public abstract void removeMetaData(String key);

	/**
	 * Renvoi la liste des styles disponibles pour ce type
	 * 
	 * @return un Vetor de String
	 */
	public abstract Vector listStyles();

	/**
	 * renvoi une liste de toutes les objets Publication.
	 * 
	 * @return un Vector d'objet Publication
	 * @throws MappingException
	 *             en cas d'erreur lors du SELECT
	 */
	public static Vector listAll() throws MappingException {
		Vector v = new Vector();
		try {
			OQLQuery oql = Mapping
					.getInstance()
					.getDb()
					.getOQLQuery(
							"SELECT p FROM org.nextime.ion.framework.business.impl.TypePublicationImpl p");
			QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
			while (results.hasMore()) {
				v.add(results.next());
			}
		} catch (Exception e) {
			Logger.getInstance().error(
					"erreur lors du listAll de TypePublication",
					TypePublication.class, e);
			throw new MappingException(e.getMessage());
		}
		return v;
	}

	public abstract Document getModel();

	public static Document getDocument(String typeId, String styleId) {
		Document style = null;
		File stylesPath = new File(Config.getInstance()
				.getTypePublicationDirectory(), "styles" + File.separator
				+ typeId + File.separator + styleId + ".xsl");
		if (stylesPath.exists() && stylesPath.isFile()) {
			try {
				style = XML.getInstance().readWithoutValidation(stylesPath);
			} catch (Exception e) {
				Logger.getInstance().error(
						e.getMessage(), TypePublication.class,e);
			}
		}
		return style;
	}

	// gestion des �venements

	/**
	 * Ajoute un nouveau listener aux objets du type TypePublication. Il sera
	 * averti lors de la cr�ation/modification/suppression d'un objet du type
	 * TypePublication.
	 */
	public static void addListener(WcmListener listener) {
		TypePublicationImpl.addListener(listener);
	}

	/**
	 * Retire un listener
	 * 
	 * @see #addListener(WCMListener)
	 */
	public static void removeListener(WcmListener listener) {
		TypePublicationImpl.removeListener(listener);
	}

}