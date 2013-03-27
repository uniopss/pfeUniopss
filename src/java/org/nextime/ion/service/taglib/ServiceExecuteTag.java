package org.nextime.ion.service.taglib;

import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class ServiceExecuteTag extends BodyTagSupport {

	private String _var;

	private String _service;

	private Iterator _iterator;

	public ServiceExecuteTag() {
		super();
		init();
	}

	public void setVar(String var) {
		_var = var;
	}

	public int doStartTag() throws JspException {

		Tag t = findAncestorWithClass(this, ServiceTag.class);
		if (t == null)
			throw new JspTagException(
					"Le tag execute doit etre dans un tag Service");

		pageContext.removeAttribute(_var);
		_service = ((ServiceTag) t).getService();

		if (_service != null) {
			pageContext.setAttribute(_var, _service);
			return (EVAL_BODY_INCLUDE);
		}
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
		init();
	}

	private void init() {
		_var = null;
		_service = null;
	}

}