package org.nextime.ion.backoffice.form;

import org.apache.struts.action.*;

public class GroupForm extends ActionForm {

	private String _id;

	private String _name;

	//private String[] _usersId;

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public void setId(String value) {
		_id = value.trim();
	}

	public void setName(String value) {
		_name = value;
	}
/*
	public String[] getUsers() {
		return _usersId;
	}

	public void setUsers(String[] value) {
		_usersId = value;
	}
*/

	public void reset() {
		_id = null;
		_name = null;
	//	_usersId = new String[0];
	}

}

