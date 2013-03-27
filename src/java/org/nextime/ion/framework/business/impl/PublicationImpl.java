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

package org.nextime.ion.framework.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Persistent;
import org.jdom.Document;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Category;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.SectionPublication;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.workflow.Workflow;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.helper.IdGenerator;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;


/**
 * Une implementation de Publication
 *
 * @author gbort
 * @version 1.7
 */
public class PublicationImpl extends Publication implements Persistent,
		Comparable {

	// propriétés

	private String _id;

	private Hashtable _metaData;

	private String _name;

	private TypePublication _type;

	private Vector _sectionpublications;

	private Vector _categories;

	private Date _date;

	private Vector _versions;

	private String _workflowType;

	private boolean _canBeModified = false;

	private boolean _modified = false;

	private static Vector _listeners = new Vector();

	// pour que la classe soit constructible par castor

	public PublicationImpl() {
		_name = "";
		_metaData = new Hashtable();
		_sectionpublications = new Vector();
		_categories = new Vector();
		_type = null;
		_date = null;
		_versions = new Vector();
	}

	public boolean isInSection(Section p_Section) {
		SectionPublication impl=findSectionPublication(p_Section);
		return (impl != null);
	}


	/**
	 * 	Recherche la SectionPublication par section
	 * 	Ce n'est pas très couteux car une publication ne lie qu'à quelques section
	 * 	Sinon, on passe par OQL
	 * @param p_Section
	 * @return
	 */
	public SectionPublication findSectionPublication(Section p_Section) {
		SectionPublicationImpl impl = null;
		for (Iterator it=_sectionpublications.iterator(); it.hasNext() && impl==null ;  ) {
			SectionPublicationImpl sectionPublication = (SectionPublicationImpl)it.next();
			if (sectionPublication.getSection().equals(p_Section)) {
				impl=sectionPublication;
			}
		}
		return impl;
	}



	public void removeSection(Section s) throws MappingException {
		SectionPublication impl = findSectionPublication(s);
		if (impl!=null) {
			impl.remove();
			_sectionpublications.remove(impl);
		}
		postChange();
	}

	public void addSection(Section s) throws MappingException {
		try {
			boolean existed = isInSection(s);
			if (! existed ) {
				SectionPublication sectionPublication = SectionPublication.create(s, this);
				_sectionpublications.add(sectionPublication);
				((SectionImpl)s).getSectionPublications().add(sectionPublication);
			}
		}
		catch (PersistenceException e) {
			 Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
			 throw new MappingException("Error d'ajouter une section dans la publication ");
		}
		postChange();
	}

	public Vector listSections() {
		Vector sections = new Vector();
		for (int i = 0; i < _sectionpublications.size(); i++) {
			sections.add(((SectionPublication) _sectionpublications.get(i)).getSection());
		}
		return sections;
	}

	public void setDate(Date date) {
		_date = date;
		postChange();
	}

	public Date getDate() {
		return _date;
	}

	public String getFormatedDate() {
		if (_date == null)
			return "";
		String pattern = Config.getInstance().getDateFormatPattern();
		if (pattern == null)
			return _date.toString();
		return new SimpleDateFormat(pattern).format(_date);
	}

	public TypePublication getType() {
		return _type;
	}

	public void removeMetaData(String key) {
		_metaData.remove(key);
		postChange();
	}

	public Object getMetaData(String key) {
		return _metaData.get(key);
	}

	public void setMetaData(String key, Object value) throws MappingException {
		if (!(value instanceof java.io.Serializable)) {
			throw new MappingException(value.getClass()
					+ " n'implemente pas l'interface java.io.Serializable");
		}
		_metaData.put(key, value);
		postChange();
	}

	public String getId() {
		return _id;
	}

	public Vector listCategories() {
		return (Vector) _categories.clone();
	}

	public void addCategory(Category c) {
		if (!_categories.contains(c)) {
			_categories.add(c);
			c.addPublication(this);
		}
		postChange();
	}

	public void removeCategory(Category c) {
		_categories.remove(c);
		postChange();
	}

	public boolean isInCategory(Category c) {
		return _categories.contains(c);
	}

	public Hashtable getMetaData() {
		return _metaData;
	}

	public String[] getSectionsIds() {
		String[] sections = new String[_sectionpublications.size()];
		for (int i = 0; i < _sectionpublications.size(); i++) {
			sections[i] = ((SectionPublication) _sectionpublications.get(i)).getSection().getId();
		}
		return sections;
	}

	public String[] getSectionsIds(String p_rootSection) {
		ArrayList ret = new ArrayList();
		for (Iterator it= _sectionpublications.iterator(); it.hasNext(); ) {
			SectionPublication sectionpublication = (SectionPublication)it.next();
			if (sectionpublication.getSection().getId().startsWith(p_rootSection)) {
				ret.add(sectionpublication.getSection().getId());
			}
		}
		String[] arr = new String[ret.size()];
		return (String[])ret.toArray(arr);
	}

	public void resetSections() throws MappingException {
		List sectionPubliToRemove = new ArrayList();
		for (Iterator it=_sectionpublications.iterator(); it.hasNext();){
			SectionPublication sectionPublication=(SectionPublication)it.next();
			sectionPubliToRemove.add(sectionPublication);
		}
		for (Iterator it=sectionPubliToRemove.iterator(); it.hasNext();){
			SectionPublication sectionPublication=(SectionPublication)it.next();
			sectionPublication.remove();
		}
		_sectionpublications = new Vector();
		postChange();
	}

	public void resetSections(String p_rootSection) throws MappingException {
		List sectionPubliToRemove = new ArrayList();
		for (int i = 0; i < _sectionpublications.size(); i++) {
			SectionPublication sectionPublication = (SectionPublication) _sectionpublications.get(i);
			if (sectionPublication.getSection().getId().startsWith(p_rootSection) ) {
				sectionPubliToRemove.add(sectionPublication);
			}
		}
		_sectionpublications.removeAll(sectionPubliToRemove);
		for (Iterator it=sectionPubliToRemove.iterator(); it.hasNext();){
			SectionPublication sectionPublication=(SectionPublication)it.next();
			sectionPublication.remove();
		}
		postChange();
	}

	public void resetVersions() {
		for (int i = 1; i <= getVersions().size(); i++) {
			PublicationVersion pv = (PublicationVersion) getVersion(i);
			((PublicationVersionImpl) pv).setVersion(i);
		}
		postChange();
	}

	public String[] getCategoriesIds() {
		String[] categories = new String[_categories.size()];
		for (int i = 0; i < _categories.size(); i++) {
			categories[i] = ((Category) _categories.get(i)).getId();
		}
		return categories;
	}

	public void resetCategories() {
		for (int i = 0; i < _categories.size(); i++) {
			Category g = (Category) _categories.get(i);
			g.removePublication(this);
		}
		_categories = new Vector();
		postChange();
	}

	public void changeType(TypePublication type) {
		setType(type);
	}

	public Vector getSectionPublications() {
		return _sectionpublications;
	}

	public void setSectionPublications(Vector v) {
		_sectionpublications = v;
		postChange();
	}

	public Vector getCategories() {
		return _categories;
	}

	public void setCategories(Vector v) {
		_categories = v;
		postChange();
	}

	public void setType(TypePublication type) {
		_type = type;
	}

	public void setId(String id) {
		_id = id;
		postChange();
	}

	public void setMetaData(Hashtable ht) {
		_metaData = ht;
	}

	public String getDatePubli() {
		if (getDate() == null)
			return null;
		return getDate().getTime() + "";
	}

	public void setDatePubli(String datePubli) {
		setDate(new Date(Long.parseLong(datePubli)));
	}

	// méthodes pour la lecture/écriture du blob de meta données

	public byte[] getMetaDataBytes() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(_metaData);
		byte[] result = os.toByteArray();
		os.close();
		return result;
	}

	public void setMetaDataBytes(byte[] value) throws Exception {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(value);
			ObjectInputStream ois = new ObjectInputStream(is);
			Object o = ois.readObject();
			is.close();
			_metaData = (Hashtable) o;
		} catch (NullPointerException e) {
			_metaData = new Hashtable();
		}
	}

	// méthodes utiles pour le wrapper xml de publications
	// tant que castor ne permet pas de rendre les Hashtables
	// correctement

	public Vector getMetaDataFields() {
		Enumeration keys = _metaData.keys();
		Vector v = new Vector();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			v.add(new DataField(key + "", _metaData.get(key) + ""));
		}
		return v;
	}

	// redefinition des méthodes de Object

	public String toString() {
		return "type[PUBLICATION] properties[" + _id + ";" + _name
				+ "] metaData" + _metaData + " sections" + _sectionpublications;
	}

	public boolean equals(Object o) {
		if (o==null) return false;
		try {
			return ((PublicationImpl) o).getId().equals(getId());
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			return false;
		}
	}

	// implementation de Comparable
	// une publication est inférieure à une
	// autre si sa date est inférieure

	public int compareTo(Object o) {
		try {
			Publication p = (Publication) o;
			int r = p.getDate().compareTo(getDate());
			if (r == 0)
				return -1;
			if (r < 0)
				return -10;
			if (r > 0)
				return 10;
			return 0;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			return 0;
		}
	}

	// heritées de Persitent

	public void jdoPersistent(Database db) {
	}

	public void jdoTransient() {
	}

	public Class jdoLoad(short accessMode) {
		return null;
	}
	public Class jdoLoad(org.exolab.castor.mapping.AccessMode am) {
		return null;
	}

	public void jdoBeforeCreate(Database db) {
	}

	public void jdoAfterCreate() {
		//****
		//WcmEvent event = new WcmEvent(this, getId());
		//for (int i = 0; i < _listeners.size(); i++) {
		//	try {
		//		((WcmListener) _listeners.get(i)).objectCreated(event);
		//	} catch (Exception e) {
		//	}
		//}
	}

	public void jdoStore(boolean modified) {
		if (_modified && modified) {
			WcmEvent event = new WcmEvent(this, getId());
			for (int i = 0; i < _listeners.size(); i++) {
				try {
					((WcmListener) _listeners.get(i)).objectModified(event);
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
				}
			}
		}
	}

	public void jdoBeforeRemove() {
		// remove all versions
		Enumeration v = ((Vector) getVersions().clone()).elements();
		while (v.hasMoreElements()) {
			try {
				PublicationVersionImpl p = (PublicationVersionImpl) v.nextElement();
				p.remove();
			} catch (Exception e) {
				//e.printStackTrace();
				Logger.getInstance().error(
						e.getMessage(), PublicationImpl.class,e);
			}
		}
	}

	public void jdoAfterRemove() {
		// dispatch event
		WcmEvent event = new WcmEvent(this, getId());
		for (int i = 0; i < _listeners.size(); i++) {
			try {
				((WcmListener) _listeners.get(i)).objectDeleted(event);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			}
		}
	}

	public void jdoUpdate() {
	}

	public void postLoad() {
		_canBeModified = true;
	}

	public void postChange() {
		if (_canBeModified) {
			_modified = true;
		}
	}

	// gestion des évenements

	public static void addListener(WcmListener listener) {
		_listeners.add(listener);
	}

	public static void removeListener(WcmListener listener) {
		_listeners.remove(listener);
	}

	// versions

	public void setWorkflowType(String t) {
		_workflowType = t;
	}

	public String getWorkflowType() {
		return _workflowType;
	}

	public PublicationVersion getVersion(int ver) {
		try {
			return (PublicationVersion) getVersions().get(
					getVersions().size() - ver);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			return null;
		}
	}

	public Vector getVersions() {
		Collections.sort(_versions);
		return _versions;
	}

	public void setVersions(Vector versions) {
		_versions = versions;
	}

	public PublicationVersion getLastVersion() {
		try {
			Vector versions=getVersions();
			if (versions.size()>0) {
				return (PublicationVersion)versions.firstElement();
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			return null;
		}
	}

	public void newVersion(User user) throws Exception {

		Document dataToInsert = getType().getModel();
		if (getLastVersion() != null) {
			try {
				dataToInsert = getLastVersion().getData();
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			}
		}

		PublicationVersionImpl version = new PublicationVersionImpl();
		version.setPublication(this);

		Hashtable ht = new Hashtable();
		ht.put("author", user.getLogin());
		ht.put("id", getId());
		ht.put("version", (getVersions().size() + 1) + "");
		Workflow wf = Workflow.create(user, getWorkflowType(), ht);
		version.setWorkflow(wf);

		version.setAuthor(user);
		version.setData(dataToInsert);
		version.setVersion(getVersions().size() + 1);
		version.setId(IdGenerator.nextPublicationVersionId());
		Mapping.getInstance().getDb().create(version);
		_versions.add(version);
		postChange();
	}

}
