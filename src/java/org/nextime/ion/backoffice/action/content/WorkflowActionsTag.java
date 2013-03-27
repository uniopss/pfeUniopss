package org.nextime.ion.backoffice.action.content;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.workflow.Workflow;
import org.nextime.ion.framework.workflow.WorkflowAction;

public class WorkflowActionsTag extends TagSupport {

	protected String var;

	protected String publication;

	protected String user;

	protected String version;

	protected String workflow;

	public int doStartTag() throws JspException {
		evaluateExpressions();
		WorkflowAction[] wa = null;
		try {
			//Mapping.begin();
			Workflow _w = Workflow.getInstance(Long.parseLong(workflow), user);
			wa = _w.getAvailableActions();
			pageContext.setAttribute(var, wa);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), WorkflowActionsTag.class,e);
			return (SKIP_BODY);
		} finally {
			//Mapping.rollback();
		}
		if (wa != null && wa.length > 0)
			return (EVAL_BODY_INCLUDE);
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	private void evaluateExpressions() throws JspException {
		if (user != null) {
			user = ExpressionEvaluatorManager.evaluate("user", user,
					Object.class, this, pageContext)
					+ "";
		}
		if (publication != null) {
			publication = ExpressionEvaluatorManager.evaluate("publication",
					publication, Object.class, this, pageContext)
					+ "";
		}
		if (version != null) {
			version = ExpressionEvaluatorManager.evaluate("version", version,
					Object.class, this, pageContext)
					+ "";
		}
		if (workflow != null) {
			workflow = ExpressionEvaluatorManager.evaluate("workflow",
					workflow, Object.class, this, pageContext)
					+ "";
		}
	}

	/**
	 * Returns the publication.
	 * 
	 * @return String
	 */
	public String getPublication() {
		return publication;
	}

	/**
	 * Returns the user.
	 * 
	 * @return String
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Returns the var.
	 * 
	 * @return String
	 */
	public String getVar() {
		return var;
	}

	/**
	 * Sets the publication.
	 * 
	 * @param publication
	 *            The publication to set
	 */
	public void setPublication(String publication) {
		this.publication = publication;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            The user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Sets the var.
	 * 
	 * @param var
	 *            The var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * Returns the version.
	 * 
	 * @return String
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the wf.
	 * 
	 * @return String
	 */
	public String getWorkflow() {
		return workflow;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            The version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sets the wf.
	 * 
	 * @param id
	 *            The wf to set
	 */
	public void setWorkflow(String id) {
		this.workflow = id;
	}
}