package org.nextime.ion.service.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.nextime.ion.framework.logger.Logger;

import java.util.*;

public class ServiceTag extends BodyTagSupport {

	private Hashtable _params;

	private String _type;

	// methodes héritées de ParentParam

	public void addParameter(String name, String value) {
		_params.put(name, value);
	}

	// accesseurs de propriétés

	public void setType(String type) {
		_type = type;
	}

	// methodes héritées de BodyTagSupport

	public int doStartTag() throws JspException {
		_params = new Hashtable();
		return (EVAL_BODY_INCLUDE);
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
		init();
	}

	private void init() {
		_params = new Hashtable();
	}

	public String getService() throws JspException {
		try {
			Object os = Class.forName(_type).newInstance();
			return ((Service) os).getService(_params,
					(HttpServletRequest) pageContext.getRequest(),
					(HttpServletResponse) pageContext.getResponse());
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), ServiceTag.class,e);
		}
		return null;
	}
}