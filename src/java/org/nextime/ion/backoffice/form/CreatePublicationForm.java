package org.nextime.ion.backoffice.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.logger.Logger;

public class CreatePublicationForm extends ActionForm {

	private boolean _check = false;

	private String name;

	private String type;

	private String workflow;

	public boolean getCheck() {
		return _check;
	}

	public void setCheck(boolean b) {
		_check = b;
	}

	/**
	 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
	 *      HttpServletRequest)
	 */
	public ActionErrors myValidate(HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		try {
			if (getName() == null || "".equals(getName())) {
				ActionError error = new ActionError(
						"error.createPublication.nameMissing");
				errors.add("name", error);
			}
			if (getType() == null || "".equals(getType())) {
				ActionError error = new ActionError(
						"error.createPublication.typeMissing");
				errors.add("type", error);
			} else if (getCheck()
					&& getName() != null
					&& !"".equals(getName())
					&& Searcher.check("+pk:" + getName() + " +type:"
							+ getType(), ""
							+ request.getSession()
									.getAttribute("currentLocale"), "pk") > 0) {
				ActionError error = new ActionError(
						"error.createPublication.pk");
				errors.add("name", error);
			}
			if ("".equals(getWorkflow())) {
				ActionError error = new ActionError(
						"error.general.missingField");
				errors.add("workflow", error);
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), CreatePublicationForm.class,e);
		}
		return errors;
	}

	/**
	 * @see org.apache.struts.action.ActionForm#reset(ActionMapping,
	 *      HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		setName(null);
		setType(null);
		setWorkflow(null);
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
	 * Returns the type.
	 * 
	 * @return String
	 */
	public String getType() {
		return type;
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

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the workflow.
	 * 
	 * @return String
	 */
	public String getWorkflow() {
		return workflow;
	}

	/**
	 * Sets the workflow.
	 * 
	 * @param workflow
	 *            The workflow to set
	 */
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

}

