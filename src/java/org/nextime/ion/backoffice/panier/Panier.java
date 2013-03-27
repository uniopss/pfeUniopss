package org.nextime.ion.backoffice.panier;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

import org.nextime.ion.framework.logger.Logger;

public class Panier implements Serializable {

	private String _type = org.nextime.ion.framework.config.Config
			.getInstance().getPanierType();

	private Vector _element;

	private java.io.File _path;

	public String getType() {
		return _type;
	}

	public int getSize() {
		return _element.size();
	}

	public boolean isPresent(String id) {
		return _element.contains(id);
	}

	public Vector getElement() {
		return _element;
	}

	public Vector getElements() {
		Vector r = (Vector) _element.clone();
		Collections.sort(r);
		return r;
	}

	public void addElement(String e) {
		if (!_element.contains(e))
			_element.add(e);
		save();
	}

	public void removeElement(String e) {
		_element.remove(e);
		save();
	}

	public Panier(String id) {
		_element = new Vector();
		_path = new java.io.File(org.nextime.ion.framework.config.Config
				.getInstance().getPanierDirectory(), id);
		if (_path.exists())
			load();
		else
			save();
	}

	private void load() {
		try {
			FileInputStream fis = new FileInputStream(_path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			_element = (java.util.Vector) ois.readObject();
			fis.close();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Panier.class,e);
		}
	}

	private synchronized void save() {
		try {
			FileOutputStream fos = new FileOutputStream(_path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(_element);
			fos.close();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), Panier.class,e);
		}
	}

}