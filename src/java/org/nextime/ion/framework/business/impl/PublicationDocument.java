/*
 * ÏON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * ÏON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * ÏON core framework, ÏON content server, ÏON backoffice, ÏON frontoffice
 * and ÏON admin application are parts of ÏON and are distributed under
 * same terms of licence.
 *
 *
 * ÏON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * ÏON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.business.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.nextime.ion.commons.IsOnline;
import org.nextime.ion.commons.Util;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.workflow.WorkflowStep;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * Document lucene représentant une publication
 */
public class PublicationDocument {

	// Format de la date utilisé.
	public static final String DATE_FORMAT = "dd/MM/yyyy";

    // les champs indexés avec Lucene
    public static final String FILED_ID = "id";

    public static final String FIELD_VERSION = "version";
    public static final String FIELD_VERSION_ID = "versionId";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_AUTEUR = "auteur";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_DATE_COMPARABLE = "dateComparable";
    public static final String FIELD_SECTIONS = "sections";
    public static final String FIELD_ION_PUBLICATION = "_ionPublication";
    public static final String FIELD_DATE_CREATION = "dateCreation";

    public static final String FIELD_SITE = "site";
    public static final String FIELD_WORKFLOW = "workflow";

    public static final String FIELD_ION_IS_ONLINE = "_ionIsOnline";
    public static final String FIELD_DATE_FIN = "dateFin";

    public static final String FIELD_LC_DATE_CREATION = "lcDateCreation";

    // Date de publication d'un document de type Publication
    public static final String FIELD_DATE_PUBLICATION = "datePublication";

    // Date de parution d'un document de type texte de droit
    public static final String FIELD_DATE_PARUTION = "dateParution";

    public static final String FIELD_LC_DATE_PUBLICATION = "lcDatePublication";
    public static final String FIELD_LC_DATE_FIN = "lcDateFin";

    // field visibility : specifique uniopss
    public static final String FIELD_VISIBILITY = "visibility";
    public static final String FIELD_DATE_EVENT_BEGIN = "dateEventBegin";
    public static final String FIELD_DATE_EVENT_END = "dateEventEnd";
    public static final String FIELD_DATE_TEXT_DROIT = "dateTextDroit";
    public static final String FILED_NO_FICHE = "noFiche";
    public static final String FIELD_AUTEUR_COMMENTAIRE = "auteurCommentaire";

    public static final String FIELD_SECTIONS_SEPARATOR = "sectionsSeparator";
    public static final String SECTIONS_SEPARATOR_BEGIN = "|";
    public static final String SECTIONS_SEPARATOR_END = "$";

    public PublicationDocument() {

    }

    // les champs date afin d'indexer avec le type date
    private static List<String> DATE_FIELDS = new ArrayList<String>();

    static {
        DATE_FIELDS.add(FIELD_LC_DATE_PUBLICATION);
        DATE_FIELDS.add(FIELD_LC_DATE_FIN);
        DATE_FIELDS.add(FIELD_LC_DATE_CREATION);
        DATE_FIELDS.add(FIELD_DATE_PUBLICATION);
        DATE_FIELDS.add(FIELD_DATE_PARUTION);
        DATE_FIELDS.add(FIELD_DATE_EVENT_BEGIN);
        DATE_FIELDS.add(FIELD_DATE_EVENT_END);
        DATE_FIELDS.add(FIELD_DATE_TEXT_DROIT);
    }


    /**
     * Indexation d'une Publicaion afin de renvoyer un Document Lucene
     *
     * @param p
     * @param version
     * @param indexName
     * @return
     */
    public static Document getDocument(Publication p, int version, String indexName) {
        try {

            String DATE_FORMAT = Config.getInstance().getDateFormatPattern();

            // recuppere les sites d'une publication : @see getSites(Publication p)
            String sitesT = getSites(p);

            // créé un nouveau document
            Document doc = new Document();

            // ajoute le champ id, name et index


            // doc.add(Field. Keyword(FILED_ID, p.getId()));
            doc.add(new Field(FILED_ID, p.getId(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            doc.add(new Field(FIELD_NAME, p.getMetaData("name") + "", Field.Store.YES, Field.Index.UN_TOKENIZED));

            // ajoute le champ version
            doc.add(new Field(FIELD_VERSION, version + "", Field.Store.YES, Field.Index.UN_TOKENIZED));
            doc.add(new Field(FIELD_VERSION_ID, p.getVersion(version).getId(), Field.Store.YES, Field.Index.UN_TOKENIZED));

            // ajoute le champ type
            try {
                doc.add(new Field(FIELD_TYPE, p.getType().getId(), Field.Store.YES, Field.Index.TOKENIZED));
            } catch (Exception e) {
                // TODO
                Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
                doc.add(new Field(FIELD_TYPE, "error", Field.Store.YES, Field.Index.TOKENIZED));
            }


            // ajoute le champ site et section
            doc.add(new Field(FIELD_SITE, sitesT, Field.Store.YES, Field.Index.TOKENIZED));

            // section : champs section est une chaine avec des identifiants séparés par des espaces
            String sect = "";
            String[] sectionId = p.getSectionsIds();
            for (int i = 0; i < sectionId.length; i++) {
                sect += sectionId[i] + " ";
            }
            doc.add(new Field(FIELD_SECTIONS, sect, Field.Store.YES, Field.Index.UN_TOKENIZED));

            // ce champ sert à faire des requete sur les sections d'appartenance de publication
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < sectionId.length; i++) {
                sb.append(SECTIONS_SEPARATOR_BEGIN).append(sectionId[i]).append(SECTIONS_SEPARATOR_END).append(" ");
            }
            doc.add(new Field(FIELD_SECTIONS_SEPARATOR, sb.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));


            // ajoute le champ date : date de la publication
            doc.add(new Field(FIELD_DATE, p.getFormatedDate(), Field.Store.YES, Field.Index.TOKENIZED));

            // ajouter la date comparable
            Date datePublication = p.getDate();
            if (datePublication != null) {
                doc.add(new Field(FIELD_DATE_COMPARABLE, new SimpleDateFormat("yyyyMMdd").format(datePublication), Field.Store.YES, Field.Index.TOKENIZED));
            }

            // date : dateFin
            if (StringUtils.isNotEmpty((String) p.getMetaData("lcDateFin"))) {
                try {
                    Date date = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(p.getMetaData("lcDateFin") + "");
                    doc.add(new Field(FIELD_DATE_FIN, new SimpleDateFormat("yyyyMMdd").format(date), Field.Store.YES, Field.Index.TOKENIZED));
                } catch (Exception e) {
                    // TODO
                    Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
                }
            }

            // date : date création
            try {
                WorkflowStep creationStep = null;
                Vector history = p.getVersion(version).getWorkflow().getHistorySteps();
                if (history != null && history.size() > 0) {
                    creationStep = (WorkflowStep) history.lastElement();
                }
                if (creationStep == null) {
                    creationStep = (WorkflowStep) p.getVersion(version).getWorkflow().getCurrentSteps().firstElement();
                }
                Date date = new SimpleDateFormat(DATE_FORMAT).parse(creationStep.getStartDate());
                doc.add(new Field(FIELD_DATE_CREATION, new SimpleDateFormat("yyyyMMdd").format(date), Field.Store.YES, Field.Index.TOKENIZED));
            } catch (Exception e) {
                // TODO
                Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
            }

            // ajoute le champ auteur
            try {
                doc.add(new Field(FIELD_AUTEUR, p.getVersion(version).getAuthor().getLogin(), Field.Store.YES, Field.Index.TOKENIZED));
            } catch (Exception e) {
                // TODO
                Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
                doc.add(new Field(FIELD_AUTEUR, "error", Field.Store.YES, Field.Index.TOKENIZED));
            }

            // ajoute le champ workflow
            try {
                doc.add(new Field(FIELD_WORKFLOW, ((WorkflowStep) (p.getVersion(version).getWorkflow().getCurrentSteps().firstElement())).getName(), Field.Store.YES, Field.Index.TOKENIZED));
            } catch (Exception e) {
                // TODO
                Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
                doc.add(new Field(FIELD_WORKFLOW, "error", Field.Store.YES, Field.Index.TOKENIZED));
            }

            // ajoute le champ visibility
            String visibility = "GDPL";
            if (p.getMetaData("visibility") != null) {
                visibility = (String) p.getMetaData("visibility");
            }
            doc.add(new Field(FIELD_VISIBILITY, visibility, Field.Store.YES, Field.Index.TOKENIZED));

            String[] indexType = org.nextime.ion.framework.config.Config.getInstance().getIndexTypes();
            for (int i = 0; i < indexType.length; i++) {
                // cherche le style correspondant pour l'indexation
                Style style = ((TypePublicationImpl) p.getType()).getStyleSheet("indexation_" + indexName + "_" + indexType[i]);
                // recupere le contenu de la publication
                String c = "";
                if (style != null) {
                    c = new String(Viewer.getView(p, version, "indexation_" + indexName + "_" + indexType[i]));
                    if ("lc".equals(indexType[i])) {
                        try {
                            String[] lc = c.split("@@__@@");
                            if (lc != null && lc.length > 0)
                                for (int j = 0; j < lc.length; j++) {
                                    if (lc[j] != null && !"".equals(lc[j])) {
                                        String[] rc = lc[j].split("__@@__");
                                        if (rc != null && rc.length > 1 && rc[0] != null && !"".equals(rc[0].trim()) && rc[1] != null && !"".equals(rc[1].trim())) {

                                            String key = rc[0];
                                            String val = rc[1] + "";
                                            boolean isTypeDate = DATE_FIELDS.contains(key);
                                            if (isTypeDate) {
                                                String luceneDateStr = StringUtils.EMPTY;
                                                try {
                                                    luceneDateStr = generateDateFiled(val);
                                                } catch (ParseException e) {
                                                    // TODO
                                                    Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
                                                }
                                                doc.add(new Field(key, luceneDateStr, Field.Store.YES, Field.Index.TOKENIZED));
                                            } else {// dans tous les autres cas, c'est un champ texte
                                                doc.add(new Field(key, val, Field.Store.YES, Field.Index.TOKENIZED));
                                            }
                                        }

                                    }
                                }
                        } catch (Exception ee) {
                            // TODO
                            Logger.getInstance().error(ee.getMessage(), PublicationDocument.class, ee);
                        }
                    } else {
                        // ajoute le contenu
                        String normalizeString = Util.translate(c);
                        normalizeString = StringUtils.lowerCase(normalizeString);
                        doc.add(new Field(indexType[i], normalizeString, Field.Store.YES, Field.Index.TOKENIZED));
                    }
                }
            }

            // Ajoute view predefinie et indic de la derniere version online
            if (Config.getInstance().getIndexPublication()) {
                try {
                    if (version == IsOnline.getMostRecentVersion(p)) {
                        doc.add(new Field(FIELD_ION_IS_ONLINE, "1", Field.Store.YES, Field.Index.TOKENIZED));
                    }
                    doc.add(new Field(FIELD_ION_PUBLICATION, p.getVersion(version).getXml(indexName), Field.Store.YES, Field.Index.NO));
                } catch (Exception e) {
                    // TODO
                    Logger.getInstance().error(e.getMessage(), PublicationDocument.class, e);
                }
            }

            return doc;
        } catch (Exception e) {
            Logger.getInstance().error("Impossible d'indexer la publication " + p.getId() + ".", PublicationDocument.class, e);
            return null;
        }
    }

    /**
     * renvoie les sites aux quels appartient une publication passée en paramètre, elle est utile pour la recherche et l'indexation
     *
     * @param publication : la publication en question
     * @return sitesT : sites contenant la publication en paramètre
     */
    private static String getSites(Publication publication) {
        String sitesT = "";
        Vector v = publication.listSections();
        String sectionTemp = "";
        if (v != null && !v.isEmpty())
            for (int k = 0; k < v.size(); k++) {
                sectionTemp = ((Section) v.get(k)).getRootSection();
                sitesT += sectionTemp + " ";
            }
        return sitesT;
    }

    /**
     * Conversion la chaine date sous forme  dd/MM/yyyy en une chaine lucene yyyyMMdd
     *
     * @param p_DateStr
     * @return
     */
    private static String generateDateFiled(String p_DateStr) throws ParseException {
        Date date = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(p_DateStr);
        String luceneDateStr = new SimpleDateFormat("yyyyMMdd").format(date);
		return luceneDateStr;
	}


}