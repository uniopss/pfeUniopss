package org.nextime.ion.backoffice.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.xml.XML;

public class EditPublicationForm extends ActionForm {

	private String date;

	private String data;

	/**
	 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
	 *      HttpServletRequest)
	 */
	public ActionErrors myValidate(HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		try {
			XML.getInstance().getDocument(getData());
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), EditPublicationForm.class,e);
			ActionError error = new ActionError("error.general.badXml", e
					.getMessage());
			errors.add("data", error);
		}
		return errors;
	}

	/**
	 * @see org.apache.struts.action.ActionForm#reset(ActionMapping,
	 *      HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		setDate(null);
		setData(null);
	}

	/**
	 * Returns the date.
	 * 
	 * @return Date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            The date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns the data.
	 * 
	 * @return String
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            The data to set
	 */
	public void setData(String data) {
		if (data != null) {
			this.data = data.replaceFirst(".*<\\x3Fxml.*\\x3F>",
					"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			this.data = this.data.replaceAll("ion:lang", "xml:lang");
			this.data = this.data.replaceAll("\\xA0", " ");
			this.data = this.data.replaceAll("<P>\\s</P>", "");
			// hack for euro symbol
		} else {
			data = null;
		}
	}

}