package org.nextime.ion.framework.business.impl;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.mapping.AccessMode;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.SectionPublication;

public class SectionPublicationImpl extends SectionPublication implements Persistent {

	private String _id;
	private Section _section;
	private Publication _publication;
	private int _index;

	public int getIndex() {
		return _index;
	}
	public void setIndex(int p_Index) {
		this._index = p_Index;
	}
	public Publication getPublication() {
		return _publication;
	}
	public void setPublication(Publication p_Publication) {
		this._publication = p_Publication;
	}
	public Section getSection() {
		return _section;
	}
	public void setSection(Section p_Section) {
		this._section = p_Section;
	}


	public void jdoAfterCreate() throws Exception {
		// TODO Auto-generated method stub

	}
	public void jdoAfterRemove() throws Exception {
		// TODO Auto-generated method stub

	}
	public void jdoBeforeCreate(Database db) throws Exception {
		// TODO Auto-generated method stub

	}
	public void jdoBeforeRemove() throws Exception {
		// TODO Auto-generated method stub

	}
	public Class jdoLoad(AccessMode accessMode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public void jdoPersistent(Database db) {
		// TODO Auto-generated method stub

	}
	public void jdoStore(boolean modified) throws Exception {
		// TODO Auto-generated method stub

	}
	public void jdoTransient() {
		// TODO Auto-generated method stub

	}
	public void jdoUpdate() throws Exception {
		// TODO Auto-generated method stub

	}
	public String getId() {
		return _id;
	}
	public void setId(String p_Id) {
		this._id = p_Id;
	}

    public String toString() {
        return "type[SECTION_PUBLICATION] properties[" + _id + ";" + _index
                + "] section " + _section;
    }

}
