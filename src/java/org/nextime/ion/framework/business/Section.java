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

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.SectionImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * L'objet <b>Section </b> sert � d�finir le rubriquage du site. Chaque section
 * a un parent. Si le parent vaut null alors la section est racine ( il peut y
 * avoir plusieurs racines ). Les sections sont ordonn�es dans chaque noeud.
 *
 * @author gbort
 * @version 0.9
 */
public abstract class Section implements org.nextime.ion.framework.cache.Cache {

	// m�thodes de fabrication

	/**
	 * Charge une instance d'un objet Section existant correspondant �
	 * l'identifiant pass� en param�tre. <i>(correspond � un SELECT) </i>
	 *
	 * @param id
	 *            L'id de l'objet Section � charger.
	 * @return Une instance de Section.
	 * @throws MappingException
	 *             si l'objet Section est impossible � charger. En particulier
	 *             si l'id ne correspond � aucun objet Section en base de
	 *             donn�e.
	 */
	public static Section getInstance(String id) throws MappingException {
		try {
			Section u = (Section) Mapping.getInstance().getDb().load(
					SectionImpl.class, id);
			Logger.getInstance().log(
					"Une instance de l'objet Section pour l'id " + id
							+ " � �t� charg�e.", Section.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de charger une instance de l'objet Section pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Section.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Cr�� un nouvel objet Section avec l'id pass� en param�tre. <i>(correspond
	 * � un INSERT) </i>
	 *
	 * @param id
	 *            l'id de l'objet Section � cr�er.
	 * @param parent
	 *            l'objet Section parent ou null pour la racine
	 * @return Une instance de Section.
	 * @throws MappingException
	 *             si l'objet Section est impossible � cr�er. En particulier si
	 *             un objet Section avec le m�me id existe d�j� en base de
	 *             donn�e.
	 */
	public static Section create(Section parent, String id)
			throws MappingException {
		try {
			SectionImpl u = new SectionImpl();
			u.setId(id);
			u.setParent(parent);
			u.initIndex();
			Mapping.getInstance().getDb().create(u);
			Logger.getInstance().log(
					"Un objet Section pour l'id " + u.getId() + " � �t� cr�e.",
					Section.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de cr�er l'objet Section pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Section.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Cr�� une nouvelle Section sous la section courante.
	 *
	 * @param id
	 *            l'id de la nouvelle section
	 * @return Une instance de Section.
	 * @throws MappingException
	 *             si le Section est impossible � cr�er.
	 */
	public Section createSubSection(String id) throws MappingException {
		try {
			SectionImpl u = new SectionImpl();
			u.setId(id);
			u.setParent(this);
			u.initIndex();
			Mapping.getInstance().getDb().create(u);
			Logger.getInstance().log(
					"Un objet Section pour l'id " + u.getId() + " � �t� cr�e.",
					Section.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de cr�er l'objet Section pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, Section.class, e);
			throw new MappingException(message);
		}
	}

	/**
	 * Detruit l'objet Section correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i> Cette m�thode est �quivalente � un
	 * <code>getInstance(id).remove()</code>.
	 *
	 * @param id
	 *            L'id du Section � detruire.
	 * @throws MappingException
	 *             si le Section est impossible � d�truire. En particulier si le
	 *             getInstance � echou�.
	 */
	public static void remove(String id) throws MappingException {
		try {
			Section u = getInstance(id);
			u.remove();
		} catch (MappingException e) {
			String message = "Etes vous certain qu'un objet Section pour l'id "
					+ id + " existe ?";
			Logger.getInstance().error(message, Section.class, e);
		}
	}

	/**
	 * Detruit l'objet Section correspondant � l'id pass� en param�tre.
	 * <i>(correspond � un DELETE) </i>
	 *
	 * @param id
	 *            L'id du Section � detruire.
	 * @throws MappingException
	 *             si le Section est impossible � d�truire.
	 */
	public void remove() throws MappingException {
		try {
			Mapping.getInstance().getDb().remove(this);
			Vector v;
			if (getParent() == null) {
				v = listRootSections();
			} else {
				v = getParent().listSubSections();
			}
			for (int i = 0; i < v.size(); i++) {
				((SectionImpl) v.get(i)).setIndex(0);
			}
			for (int i = 0; i < v.size(); i++) {
				((SectionImpl) v.get(i)).initIndex();
			}
			//			Vector v2 = listSubSections();
			//			for (int i = 0; i < v2.size(); i++) {
			//				((SectionImpl) v2.get(i)).setParent(getParent());
			//			}
			Logger.getInstance().log(
					"L'objet Section pour l'id " + getId() + " � �t� detruit.",
					Section.class);
		} catch (PersistenceException e) {
			String message = "Impossible de detruire l'objet Section pour l'id "
					+ getId() + ".";
			Logger.getInstance().error(message, Section.class, e);
			throw new MappingException(message);
		}
	}

	// m�thodes business

	/**
	 * Retourne l'identifiant de cet objet Section
	 *
	 * @return id du Section.
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
	 * Retourne la section parente
	 *
	 * @return objet Section ( ou null pour la racine )
	 */
	public abstract Section getParent();

	/**
	 * change le parent de cette Section
	 *
	 * @param nouvelle
	 *            Section parent
	 */
	public abstract void changeParent(Section parent);

	/**
	 * Liste les sous sections. Les sous sections sont list�es dans l'ordre de
	 * leur index.
	 *
	 * @return un Vector contenant des Section
	 */
	public abstract Vector listSubSections();

	public abstract Vector listSubSectionsIds();

	/**
	 * Liste les publications de cette section
	 *
	 * @return un Vector contenant des Publication
	 */
	public abstract Collection listPublications();

	/**
	 * Ajoute un objet publication a cette Section
	 */
	public abstract void addPublication(Publication p)  throws MappingException ;

	/**
	 * Retire un objet publication de cette Section
	 */
	public abstract void removePublication(Publication p) throws MappingException ;

	/**
	 * Verifie si une publication appartient a la Section
	 */
	public abstract boolean isInThisSection(Publication p);

	/**
	 * Retourne un tableau des ids des publications appartenant � la section.
	 * Plus pratique que listPublications pour cr�er les formulaires.
	 *
	 * @return un tableau de String
	 */
	public abstract String[] getPublicationsIds();

	/**
	 * Liste les Section racine.
	 *
	 * @return un Vector de Section
	 */
	public static Vector listRootSections() throws MappingException {
		Vector v = new Vector();
		try {
			OQLQuery oql = Mapping
					.getInstance()
					.getDb()
					.getOQLQuery(
							"SELECT s FROM org.nextime.ion.framework.business.impl.SectionImpl s WHERE is_undefined( parent ) order by s.index");
			QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
			while (results.hasMore()) {
				Section s = (Section) results.next();
				v.add(s);
			}
		} catch (Exception e) {
			Logger.getInstance().error("erreur lors du list des sections root",
					Section.class, e);
			throw new MappingException(e.getMessage());
		}
		return v;
	}

	/**
	 * Donne la section de type SITE
	 *
	 */
	public String getRootSection() {
		String[] s = this.getPath().split("/");
		return s[1];
	}

	/**
	 * Teste si la section est vide d element
	 *
	 * @return un boolean
	 */
	public boolean isEmpty() {
		return ((this.listSubSectionsIds() == null || this.listSubSectionsIds()
				.isEmpty()) && (this.getSectionPublications() == null || this.getSectionPublications().isEmpty()));
	}

	/**
	 * Liste tous les objets Section
	 *
	 * @return un Vector de Section
	 */
	public static Vector listAll() throws MappingException {
		return listAll(listRootSections());
	}

	public static Vector listAll(Vector v2) throws MappingException {
		Vector v = new Vector();
		if (v2 == null || v2.isEmpty())
			return v;

		for (int i = 0; i < v2.size(); i++) {
			Section s = (Section) v2.get(i);
			v.add(s);
			SectionImpl.listAllInternal(v, s);
		}
		return v;
	}

	/**
	 * Renvoi une String repr�sentant le chemin de la Section
	 * /parent0/parent1/parent2/id
	 */
	public abstract String getPath();

	public String getPathLabel() {
		String path = "" + this.getMetaData("name");
		Section parent = this.getParent();
		while (parent != null) {
			path = parent.getMetaData("name") + " > " + path;
			parent = parent.getParent();
		}
		return path;
	}

	/**
	 * R�duit l'index de la section. Elle sera avant dans les listes.
	 */
	public abstract void up();

	/**
	 * Augmente l'index de la section. Elle sera apr�s dans les listes.
	 */
	public abstract void down();

	/**
	 * Retourne l'index de la section
	 */
	public abstract int getIndex();

	/**
	 * Retourne le niveau de la section. c�d le nombre de section qu'il y a
	 * avant d'atteindre la racine.
	 */
	public abstract int getLevel();


    public abstract Collection getSectionPublications();

    public abstract void setSectionPublications(Collection p_SectionPublication);

	// gestion des �venements

	/**
	 * Ajoute un nouveau listener aux objets du type Section. Il sera averti
	 * lors de la cr�ation/modification/suppression d'un objet du type Section.
	 */
	public static void addListener(WcmListener listener) {
		SectionImpl.addListener(listener);
	}

	/**
	 * Retire un listener
	 *
	 * @see #addListener(WCMListener)
	 */
	public static void removeListener(WcmListener listener) {
		SectionImpl.removeListener(listener);
	}

}