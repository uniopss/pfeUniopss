package org.nextime.ion.backoffice.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class UploadResourceForm extends ActionForm {

	private String name;

	private FormFile file;

	/**
	 * @see org.apache.struts.action.ActionForm#reset(ActionMapping,
	 *      HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		setName(null);
		setFile(null);
	}

	/**
	 * Returns the file.
	 * 
	 * @return File
	 */
	public FormFile getFile() {
		return file;
	}

	/**
	 * Returns the name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the file.
	 * 
	 * @param file
	 *            The file to set
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}