package org.nextime.ion.backoffice.bean;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.struts.digester.Digester;

public class ObjetXmlBean {

	private String id;

	private String label;

	private static Hashtable roots = new Hashtable();

	protected static ObjetXmlBean parse(InputStream in) throws Exception {
		ObjetXmlBean bean = new ObjetXmlBean();
		Digester digester = new Digester();
		digester.push(bean);
		digester.setValidating(false);
		digester.addObjectCreate("objets-description/objets",
				"org.nextime.ion.backoffice.bean.ObjetXmlBean");
		digester.addSetProperties("objets-description/objets");
		digester.addSetNext("objets-description/objets", "addObjet");
		digester.parse(in);
		return bean;
	}

	protected static ObjetXmlBean getObjet(String id) {
		return (ObjetXmlBean) roots.get(id);
	}

	protected static Vector getItems() {
		Enumeration elts = roots.elements();
		Vector retour = new Vector();
		while (elts.hasMoreElements()) {
			retour.add(elts.nextElement());
		}
		return retour;
	}

	public static void addObjet(ObjetXmlBean bean) {
		roots.put(bean.getId(), bean);
	}

	/**
	 * Returns the id.
	 * 
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the label.
	 * 
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            The label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}