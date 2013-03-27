package org.nextime.ion.framework.business.impl;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.mapping.AccessMode;

public class IdentityImpl implements Persistent {

	// propriétées

	private String _sequenceName;
	private int _sequenceCount;
	private int _sequenceInc;



	public String getSequenceName() {
		return _sequenceName;
	}

	public void setSequenceName(String name) {
		_sequenceName = name;
	}

	public int getSequenceCount() {
		return _sequenceCount;
	}

	public void setSequenceCount(int count) {
		_sequenceCount = count;
	}

	public int getSequenceInc() {
		return _sequenceInc;
	}

	public void setSequenceInc(int inc) {
		_sequenceInc = inc;
	}

	public void jdoAfterCreate() throws Exception {
		// TODO Auto-generated method stub

	}

	public void jdoAfterRemove() throws Exception {
		// TODO Auto-generated method stub

	}

	public void jdoBeforeCreate(Database arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void jdoBeforeRemove() throws Exception {
		// TODO Auto-generated method stub

	}

	public Class jdoLoad(AccessMode arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void jdoPersistent(Database arg0) {
		// TODO Auto-generated method stub

	}

	public void jdoStore(boolean arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void jdoTransient() {
		// TODO Auto-generated method stub

	}

	public void jdoUpdate() throws Exception {
		// TODO Auto-generated method stub

	}




}
