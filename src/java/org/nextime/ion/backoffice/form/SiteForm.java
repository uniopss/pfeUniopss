package org.nextime.ion.backoffice.form;

import org.apache.struts.action.*;

public class SiteForm extends ActionForm {

	private String _id;

	private String _name;

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

	public void reset() {
		_id = null;
		_name = null;
	}

}

