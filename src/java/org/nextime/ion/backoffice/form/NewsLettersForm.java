package org.nextime.ion.backoffice.form;

import org.apache.struts.action.ActionForm;

public class NewsLettersForm extends ActionForm {

    private String _siteId;
    private String _job;


    public String getJob() {
        return _job;
    }

    public void setJob(String job) {
        this._job = job;
    }

    public String getSiteId() {
        return _siteId;
    }

    public void setSiteId(String siteId) {
        this._siteId = siteId;
    }

}
