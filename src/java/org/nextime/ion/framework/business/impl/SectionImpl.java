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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.SectionPublication;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * Une implementation de Section
 *
 * @author gbort
 * @version 1.9
 */
public class SectionImpl extends Section implements Persistent, Comparable {

    private static Log _log = LogFactory.getLog(SectionImpl.class);

    // propriétés

    private Section _parent;

    private String _id;

    private Hashtable _metaData;

    private String _name;

    private Collection  _sectionpublications;

    private int _index;

    // private boolean _sorted = false;

    private static Vector _listeners = new Vector();

    // pour que la classe soit constructible par castor

    public SectionImpl() {
        _name = "";
        _metaData = new Hashtable();
        _sectionpublications = new Vector();
        _parent = null;
    }

    // implémentation des méthodes abstraites

    public String getId() {
        return _id;
    }

    public String[] getPublicationsIds() {
        ArrayList list = new ArrayList();
        for (Iterator it=_sectionpublications.iterator(); it.hasNext();) {
        	SectionPublication sectionPublication = (SectionPublication)it.next();
        	list.add(sectionPublication.getPublication().getId());
        }
        String[] ret = new String[list.size()];
        ret = (String[])list.toArray( ret);
        return ret;
    }

    public Section getParent() {
        return _parent;
    }

    public void removeMetaData(String key) {
        _metaData.remove(key);
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
    }

    public Vector listSubSections() {
        Vector v = new Vector();
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery(
                            "SELECT s FROM org.nextime.ion.framework.business.impl.SectionImpl s WHERE parent = $1 order by s.index");
            oql.bind(this);
            QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
            while (results.hasMore()) {
                try {
                    Section s = (Section) results.next();
                    v.add(s);

                } catch (Exception ee) {
                    // TODO
                    Logger.getInstance().error(ee.getMessage(), SectionImpl.class,ee);
                }

            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list des sous sections",
                    Section.class, e);
            return v;
        }
        return v;
    }

    public Vector listSubSectionsIds() {
        Vector v = new Vector();
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery("SELECT s.id FROM org.nextime.ion.framework.business.impl.SectionImpl s WHERE parent = $1 order by s.index");
            oql.bind(this);

            QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
            while (results.hasMore()) {
                v.add((String)results.next());
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list des sous sections", SectionImpl.class, e);
            return v;
        }
        return v;
    }

    public Collection listPublications() {
        // to do : ajouter les traitements : traitemnt par OQL plus tard
        Collection sectionpublications = getSectionPublications();
        Collection ret = new ArrayList();
        for (Iterator it=sectionpublications.iterator(); it.hasNext();) {
        	SectionPublication sectionPublication = (SectionPublication)it.next();
        	ret.add(sectionPublication.getPublication());
        }
        return ret;
    }

    public void addPublication(Publication p) throws MappingException {
		try {
		        boolean isInSection = isInThisSection(p);
		        if (! isInSection) {
		            SectionPublication sectionPublication =  SectionPublication.create(this, p);
		            _sectionpublications.add(sectionPublication);
		            ((PublicationImpl)p).getSectionPublications().add(sectionPublication);
		        }
		} catch (PersistenceException e) {
			 Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
			 throw new MappingException("Error d'ajouter une publication dans la section ");
		}
    }

    public void removePublication(Publication p) throws MappingException {
        SectionPublicationImpl foundSectionPublication = null;

        for (Iterator it=_sectionpublications.iterator(); it.hasNext() && foundSectionPublication==null;) {
            SectionPublicationImpl sectionPublication = (SectionPublicationImpl)it.next();
            if (sectionPublication.getPublication().equals(p)) {
                foundSectionPublication = sectionPublication;
            }
        }
        if (foundSectionPublication!=null) {
        	foundSectionPublication.remove();

        	// remove la section publication
            _sectionpublications.remove(foundSectionPublication);
        }
    }

    /**
     * 	Tester si la publication est dans la liste de la section
     */
    public boolean isInThisSection(Publication p) {

    	/*
        SectionPublicationImpl foundSectionPublication = null;
        for (Iterator it=_sectionpublications.iterator(); it.hasNext() && foundSectionPublication==null;) {
            SectionPublicationImpl sectionPublication = (SectionPublicationImpl)it.next();
            if (sectionPublication.getPublication().equals(p)) {
                foundSectionPublication = sectionPublication;
            }
        }
        */
    	SectionPublication foundSectionPublication = null;

    	try {
    		foundSectionPublication = SectionPublication.findSectionPublication(this, p);
    	} catch (MappingException e) {
    		_log.info("La publication " + p + "introuvable dans la section " + this);
    	}
        return foundSectionPublication!=null;
    }

    public String getPath() {
        String path = "/" + getId();
        Section parent = this.getParent();
        while (parent != null) {
            path = "/" + parent.getId() + path;
            parent = parent.getParent();
        }
        return path;
    }

    public Hashtable getMetaData() {
        return _metaData;
    }

    public int getIndex() {
        return _index;
    }

    public void up() {
        try {
            Vector v;
            Section parent = getParent();
            if (parent == null) {
                v = listRootSections();
            } else {
                v = parent.listSubSections();
            }
            int nindex = getIndex() + 1;
            if (nindex > v.size()) {
                nindex = v.size();
            } else {
                for (int i = 0; i < v.size(); i++) {
                    if (nindex == ((Section) v.get(i)).getIndex()) {

                    	Section nextSection = Section.getInstance(((Section) v.get(i)).getId());
                        /*((SectionImpl) v.get(i)).setIndex(((Section) v.get(i))
                                .getIndex() - 1);*/
                    	((SectionImpl) nextSection).setIndex(((Section) v.get(i))
                                .getIndex() - 1);
                    }
                }
            }
            setIndex(nindex);
        } catch (Exception e) {
            Logger.getInstance().error(
                    "Erreur lors du changement d'index : " + e.getMessage(),
                    this, e);
        }
    }

    public void down() {
        try {
            Vector v;
            Section parent = getParent();
            if (parent == null) {
                v = listRootSections();
            } else {
                v = parent.listSubSections();
            }
            int nindex = getIndex() - 1;
            if (nindex < 1) {
                nindex = getIndex();
            } else {
                for (int i = 0; i < v.size(); i++) {
                    if (nindex == ((Section) v.get(i)).getIndex()) {
                    	Section previousSection = Section.getInstance(((Section) v.get(i)).getId());
                        /*((SectionImpl) v.get(i)).setIndex(((Section) v.get(i))
                                .getIndex() + 1);*/
                    	((SectionImpl) previousSection).setIndex(((Section) v.get(i))
                                .getIndex() + 1);
                    }
                }
            }
            setIndex(nindex);
        } catch (Exception e) {
            Logger.getInstance().error(
                    "Erreur lors du changement d'index : " + e.getMessage(),
                    this, e);
        }
    }

    public void changeParent(Section parent) {
        setParent(parent);
        initIndex();
    }

    public int getLevel() {
        int level = 0;
        Section parent = this.getParent();
        while (parent != null) {
            level++;
            parent = parent.getParent();
        }
        return level;
    }

    // méthodes non publiées

    public void setParent(Section parent) throws IllegalArgumentException {
        if (this.equals(parent))
            throw new IllegalArgumentException(
                    "La section ne peut pas être son propre parent");
        _parent = parent;
    }

    public void setId(String value) throws MappingException {
        _id = value;
    }

    public Collection getSectionPublications() {
        return _sectionpublications;
    }

    public void setSectionPublications(Collection v) {
        _sectionpublications = v;
    }

    public void setMetaData(Hashtable ht) {
        _metaData = ht;
    }

    public void initIndex() {
        try {
            Vector v;
            Section parent = getParent();
            if (parent == null) {
                v = listRootSections();
            } else {
                v = parent.listSubSections();
            }
            int max = 0;
            for (int i = 0; i < v.size(); i++) {
                if (max < ((Section) v.get(i)).getIndex()) {
                    max = ((Section) v.get(i)).getIndex();
                }
            }
            setIndex(max + 1);
        } catch (Exception e) {
            Logger.getInstance().error(
                    "Erreur lors du changement d'index : " + e.getMessage(),
                    this, e);
        }
    }

        public void setIndex(int value) {
        _index = value;
    }

    public static void listAllInternal(Vector v, Section s)
            throws MappingException {
        try {
            Vector v2 = s.listSubSections();
            for (int i = 0; i < v2.size(); i++) {
                Section s2 = (Section) v2.get(i);
                v.add(s2);
                listAllInternal(v, s2);
            }
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
        }
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

    // héritées de Object

    public String toString() {
        return "type[SECTION] properties[" + _id + ";" + _name + "] parent["
                + _parent + "] metaData" + _metaData;
    }

    public boolean equals(Object o) {
        if (o==null) return false;
        try {
            return ((SectionImpl) o).getId().equals(getId());
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
            return false;
        }
    }

    // implementation de Comparable
    // une section est inférieure à une
    // autre si son idex est inférieure

    public int compareTo(Object o) {
        if (o==null) return -1;
        try {
            Section s = (Section) o;
            int indexSection = s.getIndex();
            if (indexSection == getIndex())
                return 0;
            if (indexSection < getIndex())
                return -1;
            if (indexSection > getIndex())
                return 1;
            return 0;
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
            return 0;
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
        WcmEvent event = new WcmEvent(this, getId());
        for (int i = 0; i < _listeners.size(); i++) {
            try {
                ((WcmListener) _listeners.get(i)).objectCreated(event);
            } catch (Exception e) {
                // TODO
                Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
            }
        }
    }

    public void jdoStore(boolean modified) {
        if (modified) {
            WcmEvent event = new WcmEvent(this, getId());
            for (int i = 0; i < _listeners.size(); i++) {
                try {
                    ((WcmListener) _listeners.get(i)).objectModified(event);
                } catch (Exception e) {
                    // TODO
                    Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
                }
            }
        }
    }

    public void jdoBeforeRemove() {
    }

    public void jdoAfterRemove() {
        WcmEvent event = new WcmEvent(this, getId());
        for (int i = 0; i < _listeners.size(); i++) {
            try {
                ((WcmListener) _listeners.get(i)).objectDeleted(event);
            } catch (Exception e) {
                // TODO
                Logger.getInstance().error(e.getMessage(), SectionImpl.class,e);
            }
        }
    }

    public void jdoUpdate() {
    }

    // gestion des évenements

    public static void addListener(WcmListener listener) {
        _listeners.add(listener);
    }

    public static void removeListener(WcmListener listener) {
        _listeners.remove(listener);
    }

}