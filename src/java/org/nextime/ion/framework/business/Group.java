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
import org.nextime.ion.framework.business.impl.GroupImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * L'objet <b>Group </b> permet de rassembler des objets User. Pour le mod�le de
 * s�curit�, il est pr�f�rable d'autoriser les utilisateurs par rapport a leur
 * groupe. Typiquement un groupe <b><i>administrateurs </i> </b>, <b>
 * <i>redacteurs </i> </b>, ... <br>
 * Pour utiliser la s�curit� fournie par votre conteneur web mappez ces groupes
 * aux <b>roles </b> j2ee.
 * 
 * @author gbort
 * @version 0.9
 */
public abstract class Group {

	// m�thodes de fabrication

	/**
	 * Charge une instance d'un objet Group existant correspondant �
	 * l'identifiant pass� en param�tre. <i>(correspond � un SELECT) </i>
	 * 
	 * @param id
	 *            L'id de l'objet Group � charger.
	 * @return Une instance de Group.
	 * @throws MappingException
	 *             si l'objet Group est impossible � charger. En particulier si
	 *             l'id ne correspond � aucun objet Group en base de donn�e.
	 */
	public static Group getInstance(String id) throws MappingException {
		try {
			Group u = (Group) Mapping.getInstance().getDb().load(
					GroupImpl.class, id);
			Logger.getInstance().log(
					"Une instance de l'objet Group pour l'id " + id
							+ " � �t� charg�e.", Group.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de charger une instance de l'objet Group pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Group.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Cr�� un nouvel objet Group avec l'id pass� en param�tre. <i>(correspond �
	 * un INSERT) </i>
	 * 
	 * @param id
	 *            l'id de l'objet Group � cr�er.
	 * @return Une instance de Group.
	 * @throws MappingException
	 *             si l'objet Group est impossible � cr�er. En particulier si un
	 *             objet Group avec le m�me id existe d�j� en base de donn�e.
	 */
	public static Group create(String id) throws MappingException {
		try {
			GroupImpl u = new GroupImpl();
			u.setId(id);
			Mapping.getInstance().getDb().create(u);
			Logger.getInstance().log(
					"Un objet Group pour l'id " + id + " � �t� cr�e.",
					Group.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de cr�er l'objet Group pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Group.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Detruit l'objet Group correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i> Cette m�thode est �quivalente � un
	 * <code>getInstance(id).remove()</code>.
	 * 
	 * @param id
	 *            L'id du Group � detruire.
	 * @throws MappingException
	 *             si le Group est impossible � d�truire. En particulier si le
	 *             getInstance � echou�.
	 */
	public static void remove(String id) throws MappingException {
		try {
			Group u = getInstance(id);
			u.remove();
		} catch (MappingException e) {
			String message = "Etes vous certain qu'un objet Group pour l'id "
					+ id + " existe ?";
			Logger.getInstance().error(message, Group.class, e);
		}
	}

	/**
	 * Detruit l'objet Group correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i>
	 * 
	 * @param id
	 *            L'id du Group � detruire.
	 * @throws MappingException
	 *             si le Group est impossible � d�truire.
	 */
	public void remove() throws MappingException {
		try {
			Mapping.getInstance().getDb().remove(this);
			Logger.getInstance().log(
					"L'objet Group pour l'id " + getId() + " � �t� detruit.",
					Group.class);
		} catch (PersistenceException e) {
			String message = "Impossible de detruire l'objet Group pour l'id "
					+ getId() + ".";
			Logger.getInstance().error(message, Group.class, e);
			throw new MappingException(message);
		}
	}

	// m�thodes business

	/**
	 * Retourne l'identifiant de cet objet Group
	 * 
	 * @return id du Group.
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
	 * Ajoute un utilisateur au groupe.
	 * 
	 * @param l'utilisateur
	 *            � ajouter.
	 */
	public abstract void addUser(User user);

	/**
	 * Retire un utilisateur du groupe.
	 * 
	 * @param l'utilisateur
	 *            � retirer.
	 */
	public abstract void removeUser(User user);

	/**
	 * Verifie si un utilisateur appartient � ce groupe.
	 * 
	 * @param l'utilisateur
	 *            duquel on veut v�rifier l'appartenance
	 * @return true/false
	 */
	public abstract boolean isInThisGroup(User user);

	/**
	 * Liste les utilisateurs appartenant au groupe.
	 * 
	 * @return un Vector d'objet User
	 */
	public abstract Vector listUsers();

	/**
	 * Retourne un tableau des ids des utilisateurs appartenant au groupe. Plus
	 * pratique que listUsers pour cr�er les formulaires.
	 * 
	 * @return un tableau de String
	 */
	public abstract String[] getUsersIds();

	/**
	 * Retire tous les utilisateurs de ce groupe.
	 */
	public abstract void resetUsers();

	/**
	 * renvoi une liste de tous les objets Group
	 * 
	 * @return un Vector d'objet Group
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
							"SELECT p FROM org.nextime.ion.framework.business.impl.GroupImpl p");
			QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
			while (results.hasMore()) {
				v.add(results.next());
			}
		} catch (Exception e) {
			Logger.getInstance().error("erreur lors du listAll de Group",
					Group.class, e);
			throw new MappingException(e.getMessage());
		}
		return v;
	}

	// gestion des �venements

	/**
	 * Ajoute un nouveau listener aux objets du type Group. Il sera averti lors
	 * de la cr�ation/modification/suppression d'un objet du type Group.
	 */
	public static void addListener(WcmListener listener) {
		GroupImpl.addListener(listener);
	}

	/**
	 * Retire un listener
	 * 
	 * @see #addListener(WCMListener)
	 */
	public static void removeListener(WcmListener listener) {
		GroupImpl.removeListener(listener);
	}
}