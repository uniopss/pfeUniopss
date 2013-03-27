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
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.UserImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * L'objet <b>User </b> sert � la gestion de la s�curit�. Un objet User peut
 * �galement �tre d�finit comme auteur d'une publication.
 *
 * @author gbort
 * @version 1.0
 */
public abstract class User {

	// m�thodes de fabrication

	/**
	 * Charge une instance d'un objet User existant correspondant �
	 * l'identifiant pass� en param�tre. <i>(correspond � un SELECT) </i>
	 *
	 * @param id
	 *            L'id de l'objet User � charger.
	 * @return Une instance de User.
	 * @throws MappingException
	 *             si l'objet User est impossible � charger. En particulier si
	 *             l'id ne correspond � aucun objet User en base de donn�e.
	 */
	public static User getInstance(String login) throws MappingException {
		try {
			User u = (User) Mapping.getInstance().getDb().load(UserImpl.class, login);
			Logger.getInstance().log("Une instance de l'objet User pour l'id " + login
							+ " � �t� charg�e.", User.class);
			return u;
		} catch (Exception e) {
			String message = "Impossible de charger une instance de l'objet User pour l'id " + login + ".";
			Logger.getInstance().error(message, User.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Cr�� un nouvel objet User avec l'id pass� en param�tre. <i>(correspond �
	 * un INSERT) </i>
	 *
	 * @param id
	 *            l'id de l'objet User � cr�er.
	 * @return Une instance de User.
	 * @throws MappingException
	 *             si l'objet User est impossible � cr�er. En particulier si un
	 *             objet User avec le m�me id existe d�j� en base de donn�e.
	 */
	public static User create(String login) throws MappingException {
		try {
			UserImpl u = new UserImpl();
			u.setLogin(login);
			Mapping.getInstance().getDb().create(u);
			Logger.getInstance().log("Un objet User pour l'id " + login + " � �t� cr�e.",
					User.class);
			return u;
		} catch (Exception e) {
			String message = "Impossible de cr�er l'objet User pour l'id " + login + ".";
			Logger.getInstance().error(message, User.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Detruit l'objet User correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i> Cette m�thode est �quivalente � un
	 * <code>getInstance(id).remove()</code>.
	 *
	 * @param id
	 *            L'id du User � detruire.
	 * @throws MappingException
	 *             si le User est impossible � d�truire. En particulier si le
	 *             getInstance � echou�.
	 */
	public static void remove(String login) throws MappingException {
		try {
			User u = getInstance(login);
			u.remove();
		} catch (Exception e) {
			String message = "Etes vous certain qu'un objet User pour l'id " + login + " existe ?";
			Logger.getInstance().error(message, User.class, e);
		}
	}

	/**
	 * Detruit l'objet User correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i>
	 *
	 * @param id
	 *            L'id du User � detruire.
	 * @throws MappingException
	 *             si le User est impossible � d�truire.
	 */
	public void remove() throws MappingException {
		try {
			Mapping.getInstance().getDb().remove(this);
			Logger.getInstance().log( "L'objet User pour l'id " + getLogin() + " � �t� detruit.", User.class);
		} catch (Exception e) {
			String message = "Impossible de detruire l'objet User pour l'id " + getLogin() + ".";
			Logger.getInstance().error(message, User.class, e);
			throw new MappingException(message);
		}
	}

	// m�thodes business

	/**
	 * Retourne le login de l'utilisateur. Le login est utilis� comme
	 * identifiant pour l'objet.
	 *
	 * @return login du User
	 */
	public abstract String getLogin();

	/**
	 * Retourne le password de l'utilisateur.
	 *
	 * @return password de User
	 */
	public abstract String getPassword();

	/**
	 * D�finit un nouveau password pour le User
	 *
	 * @param value
	 *            Nouveau password de l'utilisateur.
	 */
	public abstract void setPassword(String value);

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
	 * Ajoute un groupe � l'utilisateur
	 *
	 * @param le
	 *            groupe � ajouter
	 */
	public abstract void addGroup(Group group);

	/**
	 * Retire un groupe � l'utilisateur
	 *
	 * @param le
	 *            groupe � retirer
	 */
	public abstract void removeGroup(Group group);

	/**
	 * Verifie l'appartenance � un groupe
	 *
	 * @param le
	 *            groupe auquel on veut v�rifier l'appartenance
	 * @return vrai/faux
	 */
	public abstract boolean isInGroup(Group group);

	/**
	 * Liste les groupes de l'utilisateur.
	 *
	 * @return un Vector d'objet Group
	 */
	public abstract Vector listGroups();

	/**
	 * Retire tous les groupes de cet utilisateur.
	 */
	public abstract void resetGroups();

	/**
	 * Retourne un tableau des ids des groupes de ce user. Plus pratique que
	 * listGroups pour cr�er les formulaires.
	 *
	 * @return un tableau de String
	 */
	public abstract String[] getGroupsIds();

	/**
	 * Liste les publications de l'utilisateur ( dont il est l'auteur )
	 *
	 * @return un Vector d'objet Publication
	 */
	public abstract Vector listPublications();


	/**
	 * Liste les identifiant de publications de l'utilisateur ( dont il est l'auteur )
	 *
	 * @return un Vector d'objet <code>String</code>
	 */
	public abstract Vector listPublicationIds();

	/**
	 * renvoi une liste de tous les utilisateurs
	 */
	public static Vector listAll() {
		Vector v = new Vector();
		try {
			OQLQuery oql = Mapping
					.getInstance()
					.getDb()
					.getOQLQuery(
							"SELECT p FROM org.nextime.ion.framework.business.impl.UserImpl p");
			QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
			while (results.hasMore()) {
				v.add(results.next());
			}
		} catch (Exception e) {
			Logger.getInstance().error("erreur lors du listAll de User",
					User.class, e);
			return v;
		}
		return v;
	}

	// gestion des �venements

	/**
	 * Ajoute un nouveau listener aux objets du type User. Il sera averti lors
	 * de la cr�ation/modification/suppression d'un objet du type User.
	 */
	public static void addListener(WcmListener listener) {
		UserImpl.addListener(listener);
	}

	/**
	 * Retire un listener
	 *
	 * @see #addListener(WCMListener)
	 */
	public static void removeListener(WcmListener listener) {
		UserImpl.removeListener(listener);
	}

}