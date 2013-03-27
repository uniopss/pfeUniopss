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

import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.CategoryImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * L'objet <b>Category </b> permet de qualifier le contenu de la publication. Un
 * objet <b>Publication </b> peut appartenir � plusieurs Category et �videmment
 * un objet <b>Category </b> peut regrouper plusieurs Publication. Grace � cet
 * objet vous pouvez proposer au visiteur de consulter d'autres publications de
 * la m�me categorie. <br>
 * Cet objet peut �galement servir de lien entre un framework de
 * personnalisation et le framework WCM.
 * 
 * @author gbort
 * @version 0.9
 */
public abstract class Category {

	// m�thodes de fabrication

	/**
	 * Charge une instance d'un objet Category existant correspondant �
	 * l'identifiant pass� en param�tre. <i>(correspond � un SELECT) </i>
	 * 
	 * @param id
	 *            L'id de l'objet Category � charger.
	 * @return Une instance de Category.
	 * @throws MappingException
	 *             si l'objet Category est impossible � charger. En particulier
	 *             si l'id ne correspond � aucun objet Category en base de
	 *             donn�e.
	 */
	public static Category getInstance(String id) throws MappingException {
		try {
			Category u = (Category) Mapping.getInstance().getDb().load(
					CategoryImpl.class, id);
			Logger.getInstance().log(
					"Une instance de l'objet Category pour l'id " + id
							+ " � �t� charg�e.", Category.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de charger une instance de l'objet Category pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Category.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Cr�� un nouvel objet Category avec l'id pass� en param�tre.
	 * <i>(correspond � un INSERT) </i>
	 * 
	 * @param id
	 *            l'id de l'objet Category � cr�er.
	 * @return Une instance de Category.
	 * @throws MappingException
	 *             si l'objet Category est impossible � cr�er. En particulier si
	 *             un objet Category avec le m�me id existe d�j� en base de
	 *             donn�e.
	 */
	public static Category create(String id) throws MappingException {
		try {
			CategoryImpl u = new CategoryImpl();
			u.setId(id);
			Mapping.getInstance().getDb().create(u);
			Logger.getInstance().log(
					"Un objet Category pour l'id " + id + " � �t� cr�e.",
					Category.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de cr�er l'objet Category pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Category.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Detruit l'objet Category correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i> Cette m�thode est �quivalente � un
	 * <code>getInstance(id).remove()</code>.
	 * 
	 * @param id
	 *            L'id du Category � detruire.
	 * @throws MappingException
	 *             si le Category est impossible � d�truire. En particulier si
	 *             le getInstance � echou�.
	 */
	public static void remove(String id) throws MappingException {
		try {
			Category u = getInstance(id);
			u.remove();
		} catch (MappingException e) {
			String message = "Etes vous certain qu'un objet Category pour l'id "
					+ id + " existe ?";
			Logger.getInstance().error(message, Category.class, e);
		}
	}

	/**
	 * Detruit l'objet Category correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i>
	 * 
	 * @param id
	 *            L'id du Category � detruire.
	 * @throws MappingException
	 *             si le Category est impossible � d�truire.
	 */
	public void remove() throws MappingException {
		try {
			Mapping.getInstance().getDb().remove(this);
			Logger.getInstance()
					.log(
							"L'objet Category pour l'id " + getId()
									+ " � �t� detruit.", Category.class);
		} catch (PersistenceException e) {
			String message = "Impossible de detruire l'objet Category pour l'id "
					+ getId() + ".";
			Logger.getInstance().error(message, Category.class, e);
			throw new MappingException(message);
		}
	}

	// m�thodes business

	/**
	 * Retourne l'identifiant de cet objet Category
	 * 
	 * @return id du Category.
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
	 * Detruit une meta donn�e de l'objet. <br>
	 * Notez que cette m�thode ne fait rien si il n'y avais pas de meta donn�e.
	 * 
	 * @see #setMetaData(String,Object)
	 * @param key
	 *            cl� sevant � identifier la meta donn�e.
	 */
	public abstract void removeMetaData(String key);

	/**
	 * Retourne la Hashtable de meta donn�e. A utiliser avec pr�caution en
	 * �criture. Pr�f�rez largement setMetaData()
	 * 
	 * @return l'objet Hashtable contenant les meta donn�es;
	 */
	public abstract Hashtable getMetaData();

	/**
	 * Ajoute une publication � la cat�gorie.
	 * 
	 * @param la
	 *            publication � ajouter.
	 */
	public abstract void addPublication(Publication p);

	/**
	 * Retire une publication de la cat�gorie.
	 * 
	 * @param la
	 *            publication � retirer.
	 */
	public abstract void removePublication(Publication p);

	/**
	 * Verifie si une publication appartient a cette cat�gorie.
	 * 
	 * @param publication
	 *            de laquelle on veut v�rifier l'appartenance
	 * @return true/false
	 */
	public abstract boolean isInThisCategory(Publication p);

	/**
	 * Liste les publications appartenant � la cat�gorie.
	 * 
	 * @return un Vector d'objets Publication
	 */
	public abstract Vector listPublications();

	/**
	 * Retourne un tableau des ids des publications appartenant � la cat�gorie.
	 * Plus pratique que listPublications pour cr�er les formulaires.
	 * 
	 * @return un tableau de String
	 */
	public abstract String[] getPublicationsIds();

	/**
	 * Retire tous les utilisateurs de ce groupe.
	 */
	public abstract void resetPublications();

	/**
	 * Liste toutes les objet Category.
	 * 
	 * @return un Vector d'objets Category
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
							"SELECT p FROM org.nextime.ion.framework.business.impl.CategoryImpl p");
			QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
			while (results.hasMore()) {
				v.add(results.next());
			}
		} catch (Exception e) {
			Logger.getInstance().error("erreur lors du listAll de Category",
					Section.class, e);
			throw new MappingException(e.getMessage());
		}
		return v;
	}

	// gestion des �venements

	/**
	 * Ajoute un nouveau listener aux objets du type Category. Il sera averti
	 * lors de la cr�ation/modification/suppression d'un objet du type Category.
	 */
	public static void addListener(WcmListener listener) {
		CategoryImpl.addListener(listener);
	}

	/**
	 * Retire un listener
	 * 
	 * @see #addListener(WCMListener)
	 */
	public static void removeListener(WcmListener listener) {
		CategoryImpl.removeListener(listener);
	}

}