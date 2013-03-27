/*
 * ÏON content management system. Copyright (C) 2002 Guillaume
 * Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * ÏON is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * ÏON core framework, ÏON content server, ÏON backoffice, ÏON frontoffice and
 * ÏON admin application are parts of ÏON and are distributed under same terms
 * of licence.
 *
 *
 * ÏON includes software developed by the Apache Software Foundation
 * (http://www.apache.org/) and software developed by the Exolab Project
 * (http://www.exolab.org).
 *
 * ÏON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

package org.nextime.ion.frontoffice.objectSelector;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.impl.PublicationImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PublicationResult implements Comparable {

    protected String view;

    protected Map extendedView = new HashMap();

    protected Publication publication;

    protected int version;

    // Use it only if publication is not set
    protected String id;

    /**
     * Returns the publication.
     *
     * @return Publication
     */
    public Publication getPublication() {
        return publication;
    }

    /**
     * Returns the version.
     *
     * @return int
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns the view.
     *
     * @return String
     */
    public String getView() {
        return view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the publication.
     *
     * @param publication The publication to set
     */
    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    /**
     * Sets the version.
     *
     * @param version The version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Sets the view.
     *
     * @param view The view to set
     */
    public void setView(String view) {
        this.view = view;
    }

    public Map getExtendedView() {
        return extendedView;
    }

    public void setExtendedView(Map extendedView) {
        this.extendedView = extendedView;
    }

    public static Date stringToDate(String pattern, String dateString) {
        Date date = null;

        /* Parsing String -> Date */
        try {
            date = (new SimpleDateFormat(pattern)).parse(dateString);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public int compareTo(Object o) {
        try {

            PublicationResult p = (PublicationResult) o;

            Date DATETMP1 = stringToDate("dd/MM/yyyy", ((PublicationImpl) p.publication).getMetaData().get("lcDatePublication").toString());
            Date DATETMP2 = stringToDate("dd/MM/yyyy", ((PublicationImpl) this.publication).getMetaData().get("lcDatePublication").toString());

            return DATETMP1.compareTo(DATETMP2);


        } catch (Exception e) {
            // TODO
            // Logger.getInstance().error(e.getMessage(),
            // PublicationImpl.class,e);
            return 0;
        }
    }

}

