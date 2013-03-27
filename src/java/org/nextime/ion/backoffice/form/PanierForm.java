package org.nextime.ion.backoffice.form;

import org.apache.struts.action.*;
import java.util.*;

public class PanierForm extends ActionForm {

	private String _id;

	private Vector _panier;

	public String getId() {
		return _id;
	}

	public void setId(String value) {
		_id = value.trim();
	}

	public Vector getPanier() {
		return _panier;
	}

	public void setPanier(Vector value) {
		_panier = value;
	}

	public void reset() {
		_id = null;
		_panier = new Vector();
	}

}

