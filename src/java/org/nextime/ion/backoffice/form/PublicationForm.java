package org.nextime.ion.backoffice.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;
import org.nextime.ion.framework.helper.Searcher;

public class PublicationForm extends ActionForm {

	private boolean _postBack = false;

	private String _siteId;

	private String[] _sectionsId;

	private String _type = "";

	private String _dp = "";

	private String _df = "";

	private String _dc = "";

	/**
	 * 	Canal de diffusion
	 */
	// private String _d = "";

	private String _o = "";

	private String _n = "";

	private String _p = "";

	private String _oP = "";

	/**  Curseur de visibilité */
	private String _curseurVisibilite;

	public String getType() {
		return _type;
	}

	public void setType(String t) {
		_type = t;
	}
/*
	public String getD() {
		return _d;
	}

	public void setD(String value) {
		_d = value;
	}
*/
	public String[] getSections() {
		return _sectionsId;
	}

	public void setSections(String[] value) {
		_sectionsId = value;
	}

	public void setDp(String z) {
		_dp = z;
	}

	public String getDp() {
		return _dp;
	}

	public void setP(String z) {
		_p = z;
	}

	public String getP() {
		return _p;
	}

	public void setO(String z) {
		_o = z;
	}

	public void setOp(String z) {
		_oP = z;
	}

	public String getOp() {
		return _oP;
	}

	public String getO() {
		return _o;
	}

	public void setN(String z) {
		_n = z;
	}

	public String getN() {
		return _n;
	}

	public void setDf(String z) {
		_df = z;
	}

	public String getDf() {
		return _df;
	}

	public void setDc(String z) {
		_dc = z;
	}

	public String getDc() {
		return _dc;
	}

	/**
	 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
	 *      HttpServletRequest)
	 */
	public ActionErrors myValidate(HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if ("".equals(getN())) {
			ActionError error = new ActionError(
					"error.createPublication.nameMissing");
			errors.add("n", error);
		}
		if (getType() != null
				&& !"".equals(getType())
				&& getN() != null
				&& !"".equals(getN())
				&& !request.getParameter("label").equalsIgnoreCase(getN())
				&& Searcher.check("+pk:" + getN() + " +type:" + getType(), ""
						+ request.getSession().getAttribute("currentLocale"),
						"pk") > 0) {
			ActionError error = new ActionError("error.createPublication.pk");
			errors.add("n", error);
		}
		return errors;
	}


	public void reset() {
		String empty = "";
		_sectionsId = new String[0];
		_dp =  empty;
		_dc = empty;
		_df = empty;
		//_d = "";
		_o = empty;
		_p = empty;
		_curseurVisibilite = empty;
	}

	public String getCurseurVisibilite() {
		return _curseurVisibilite;
	}

	public void setCurseurVisibilite(String visibilite) {
		_curseurVisibilite = visibilite;
	}

	public String getSiteId() {
		return _siteId;
	}

	public void setSiteId(String id) {
		_siteId = id;
	}

	public boolean isPostBack() {
		return _postBack;
	}

	public void setPostBack(boolean back) {
		_postBack = back;
	}

}

