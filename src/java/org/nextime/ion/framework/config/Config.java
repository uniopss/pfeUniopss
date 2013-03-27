/*
 * �ON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * �ON is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * �ON core framework, �ON content server, �ON backoffice, �ON frontoffice
 * and �ON admin application are parts of �ON and are distributed under
 * same terms of licence.
 *
 *
 * �ON includes software developed by the Apache Software Foundation (http://www.apache.org/)
 * and software developed by the Exolab Project (http://www.exolab.org).
 *
 * �ON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.nextime.ion.framework.config;

import fr.asso.uniopss.publication.TextDroitListener;
import org.apache.fop.apps.Driver;
import org.nextime.ion.framework.helper.Indexer;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.locale.LocaleList;
import org.nextime.ion.framework.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * Apporte les informations de configuration contenues dans le config.properties
 * au reste du framework. Si un champ REQUIRED n'est pas pr�sent dans le fichier
 * de conf une fatal error est d�clench�e.
 *
 * @author gbort
 * @version 1.0
 */
public class Config {

    private String configDirectoryPath;

    private static Config instance;

    private String _merchantId;

    private int _mailPort = -1;

    private String _mailHost;

    private String _mailFrom;

    private String _mailToList;


    private File _dataBaseConfigurationFile;

    private URL _publicationMappingUrl;

    private String _databaseName;

    private String _dateFormatPattern;

    private File _typePublicationDirectory;

    private File _workflowDirectory;

    private File _resourcesDirectory;

    private File _panierDirectory;

    private File _messageDirectory;

    private File _lienDirectory;

    private File _produitDirectory;

    private File _dynamicListXML;

    private File _mailDirectory;

    private File _htmlCacheDirectory;

    private File _thesaurusDirectory;

    private String _saxDriver;

    private String _indexRoot;

    private String _panierType;

    private String _workflowStore;

    private String _nbJrExp;

    private String[] _indexNames;

    private String[] _indexTypes;

    private int _foRenderType = -2112;

    private long _lockTimeout = -2112;

    public static String LUCENE_DATE_FORMAT = "yyyyMMdd";

    private String _synonymeFileName;

    private File _statRootDirectory;

    private PropertyResourceBundle res;

    private Config() {
        configDirectoryPath = new File(((PropertyResourceBundle) ResourceBundle
                .getBundle("ion-locate")).getString("confDirectory"))
                .toString();
        try {
            FileInputStream fis = new FileInputStream(new File(configDirectoryPath, "config.properties"));
            res = new PropertyResourceBundle(fis);
            fis.close();
        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
            init();
        }
        return instance;
    }

    /**
     * Returns the configDirectoryPath.
     *
     * @return String
     */
    public String getConfigDirectoryPath() {
        return configDirectoryPath;
    }

    public static void init() {
        Indexer.getInstance();
        Viewer.getInstance();
        TextDroitListener.getInstance();
    }

    /**
     * Returns the storeFile.
     *
     * @return String
     */
    public String getWorkflowStore() {
        return _workflowStore;
    }

    public void setWorkflowStore(String s) {
        _workflowStore = s;
    }

    /**
     * @return
     */
    public String getMerchantId() {
        if (_merchantId != null)
            return _merchantId;
        String name = "";
        try {

            name = res.getString("merchantId");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( merchantId introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _merchantId = name;
        return name;
    }

    /**
     * @return
     */
    public String getMailHost() {
        if (_mailHost != null)
            return _mailHost;
        String name = "";
        try {

            name = res.getString("mailHost");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( mailHost introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _mailHost = name;
        return name;
    }

    /**
     * @return
     */
    public String getMailFrom() {
        if (_mailFrom != null)
            return _mailFrom;
        String name = "";
        try {

            name = res.getString("mailFrom");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( mailFrom introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _mailFrom = name;
        return name;
    }

    /**
     * @return
     */
    public String getMailToList() {
        if (_mailToList != null)
            return _mailToList;
        String name = "";
        try {

            name = res.getString("mailToList");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( mailToList introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _mailToList = name;
        return name;
    }


    public int getMailPort() {
        if (_mailPort > -1)
            return _mailPort;
        String name = "";
        try {

            name = res.getString("mailPort");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( mailPort introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _mailPort = Integer.parseInt(name);
        return _mailPort;
    }

    public String getPanierType() {
        if (_panierType != null)
            return _panierType;
        String name = "";
        try {

            name = res.getString("panierType");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( panierType introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _panierType = name;
        return name;
    }

    /**
     * @return
     */
    public String getNbJrExp() {
        if (_nbJrExp != null)
            return _nbJrExp;
        String name = "";
        try {

            name = res.getString("nbJrExp");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( nbJrExp introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _nbJrExp = name;
        return name;
    }

    /**
     * Fichier de configuration database.xml pour castor ( Required )
     */
    public File getDatabaseConfigurationFile() {
        if (_dataBaseConfigurationFile != null)
            return _dataBaseConfigurationFile;
        _dataBaseConfigurationFile = new File(configDirectoryPath, "database.xml");
        if (!_dataBaseConfigurationFile.exists()) {
            Logger
                    .getInstance()
                    .log("Le fichier WCMConf/database.xml est introuvable dans le classpath courant.", this);
            return null;
        }
        return _dataBaseConfigurationFile;
    }

    /**
     * Format de rendu pour les styles xsl-fo ( PDF, SVG, TXT, PS ) ( Optional )
     */
    public int getFoRenderType() {
        if (_foRenderType != -2112)
            return _foRenderType;
        String s = "";
        int r = Driver.RENDER_PDF;
        try {

            s = res.getString("foRenderType");

            if (s.equalsIgnoreCase("PDF"))
                r = Driver.RENDER_PDF;
            if (s.equalsIgnoreCase("SVG"))
                r = Driver.RENDER_SVG;
            if (s.equalsIgnoreCase("TXT"))
                r = Driver.RENDER_TXT;
            if (s.equalsIgnoreCase("PS"))
                r = Driver.RENDER_PS;
        } catch (Exception e) {
            r = Driver.RENDER_PDF;
        }
        _foRenderType = r;
        return r;
    }

    public long getLockTimeout() {
        if (_lockTimeout != -2112)
            return _foRenderType;
        _lockTimeout = 60000;
        return _lockTimeout;
    }

    /**
     * Fichier de mapping utilis� par le wrapper xml ( Required )
     */
    public URL getPublicationMappingUrl() {
        if (_publicationMappingUrl != null)
            return _publicationMappingUrl;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        File path = new File(configDirectoryPath, "publication-wrapper.xml");
        if (!path.exists()) {
            Logger
                    .getInstance()
                    .log("Le fichier WCMConf/publication-wrapper.xml est introuvable dans le classpath courant.", this);
            return null;
        }
        try {
            _publicationMappingUrl = path.toURL();
        } catch (Exception e) {
            Logger
                    .getInstance()
                    .log("Le fichier WCMConf/publication-wrapper.xml est introuvable dans le classpath courant.", this);
            return null;
        }
        return _publicationMappingUrl;
    }

    /**
     * Nom de la base de donn�e ( Required )
     */
    public String getDatabaseName() {
        if (_databaseName != null)
            return _databaseName;
        String name = "";
        try {

            name = res.getString("databaseName");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( databaseName introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _databaseName = name;
        return name;
    }

    /**
     * Pattern a utiliser pour formatter les dates des publications lors de
     * rendus ( Optional ) ... voir SimpleDateFormat pour les patterns.
     */
    public String getDateFormatPattern() {
        if (_dateFormatPattern != null)
            return _dateFormatPattern;
        String name = "";
        try {

            name = res.getString("dateFormatPattern");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
            name = null;
        }
        _dateFormatPattern = name;
        return name;
    }

    /**
     * Repertoire contenant les d�finitions des types de publication ( Required )
     */
    public File getTypePublicationDirectory() {
        if (_typePublicationDirectory != null)
            return _typePublicationDirectory;
        String path = "";
        try {

            path = res.getString("typesPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( typesPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� typesPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _typePublicationDirectory = f;
        return f;
    }

    /**
     * Repertoire contenant les d�finitions des workflows( Required )
     */
    public File getWorkflowDirectory() {
        if (_workflowDirectory != null)
            return _workflowDirectory;
        String path = "";
        try {

            path = res.getString("workflowsPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( typesPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� workflowsPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _workflowDirectory = f;
        return f;
    }

    /**
     * Repertoire de travail pour le cache html ( Optional ) Si aucun repertoire
     * n'est pr�cis�, le cache est inactif
     */
    public File getHtmlCacheDirectory() {
        if (_htmlCacheDirectory != null)
            return _htmlCacheDirectory;
        String path = "";
        try {

            path = res.getString("htmlCache");

        } catch (Exception e) {
            return null;
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� htmlCache est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _htmlCacheDirectory = f;
        return f;
    }

    /**
     * Driver SAX � utiliser pour parser les xmls ( Optional ) Si aucun driver
     * n'est sp�cifi�, un jeu de driver standard est essay�.
     */
    public String getSaxDriver() {
        if (_saxDriver != null)
            return _saxDriver;
        String s = "";
        try {

            s = res.getString("saxDriver");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
            s = "_AUTO_";
        }
        _saxDriver = s;
        return s;
    }

    /**
     * Repertoire de travail pour l'indexation ( Required )
     */
    public String getIndexRoot() {
        if (_indexRoot != null)
            return _indexRoot;
        String s = "";
        try {

            s = res.getString("indexRoot");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( indexRoot introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        _indexRoot = new File(configDirectoryPath, s).toString();
        return s;
    }

    /**
     * Noms des bases d'index � utiliser pour l'indexation (Required)
     */
    public String[] getIndexNames() {
        if (_indexNames != null)
            return _indexNames;
        String[] s = null;
        try {

            String sl = res.getString("indexNames");

            if (sl == null || "".equals(sl)) {
                s = LocaleList.getInstance().getLocale();
            } else {
                StringTokenizer st = new StringTokenizer(sl, ";");
                s = new String[st.countTokens()];
                int i = -1;
                while (st.hasMoreTokens()) {
                    s[++i] = st.nextToken();
                }
            }
        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( indexNames introuvable(s) ).", this, new NullPointerException());
            System.exit(0);
        }
        _indexNames = s;
        return s;
    }

    /**
     * Noms des types d'index � utiliser pour l'indexation (Required)
     */
    public String[] getIndexTypes() {
        if (_indexTypes != null)
            return _indexTypes;
        String[] s = null;
        try {

            String sl = res.getString("indexTypes");

            StringTokenizer st = new StringTokenizer(sl, ";");
            s = new String[st.countTokens()];
            int i = -1;
            while (st.hasMoreTokens()) {
                s[++i] = st.nextToken();
            }
        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( indexTypes introuvable(s) ).", this, new NullPointerException());
            System.exit(0);
        }
        _indexTypes = s;
        return s;
    }

    /**
     * Pr�cise si les fichiers xml doivent �tre valid�s � leur chargement
     * (Optional) Default:true;
     */
    public boolean getXmlValidationState() {
        boolean r = true;
        try {

            r = res.getString("xmlValidation").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    /**
     * Pr�cise si l'auto indexation des publications est activ�e (Optional)
     * Default:false;
     */
    public boolean getAutoIndex() {
        boolean r = false;
        try {

            r = res.getString("autoIndex").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    /**
     * Pr�cise si l'auto indexation des views est activ�e (Optional)
     * Default:false;
     */
    public boolean getIndexPublication() {
        boolean r = false;
        try {

            r = res.getString("indexPublication").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    /**
     * Desactive les logs pour la mise en production (Optional) Default:false;
     */
    public boolean getDisableLog() {
        boolean r = false;
        try {

            r = res.getString("disableLog").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    public boolean getReloadTpl() {
        boolean r = false;
        try {

            r = res.getString("reloadTpl").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    public boolean getTreeCache() {
        boolean r = false;
        try {

            r = res.getString("treeCache").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    public boolean getSectionCache() {
        boolean r = false;
        try {

            r = res.getString("sectionCache").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    /**
     * Desactive le cache pour les TypePublication
     */
    public boolean getDisableTypeCache() {
        boolean r = false;
        try {

            r = res.getString("disableTypeCache").equals("true");

        } catch (Exception e) {
            // TODO
            Logger.getInstance().error(e.getMessage(), Config.class, e);
        }
        return r;
    }

    public File getMailDirectory() {
        if (_mailDirectory != null)
            return _mailDirectory;
        String path = "";
        try {

            path = res.getString("mailPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( mailPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� mailPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _mailDirectory = f;
        return f;
    }

    public File getPanierDirectory() {
        if (_panierDirectory != null)
            return _panierDirectory;
        String path = "";
        try {

            path = res.getString("panierPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( panierPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� panierPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _panierDirectory = f;
        return f;
    }

    public File getMessageDirectory() {
        if (_messageDirectory != null)
            return _messageDirectory;
        String path = "";
        try {

            path = res.getString("messagePath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( messagePath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� messagePath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _messageDirectory = f;
        return f;
    }

    public File getProduitDirectory() {
        if (_produitDirectory != null)
            return _produitDirectory;
        String path = "";
        try {

            path = res.getString("produitPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( produitPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� produitPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _produitDirectory = f;
        return f;
    }

    public File getDynamicListXML() {
        if (_dynamicListXML != null)
            return _dynamicListXML;
        String path = "";
        try {
            path = res.getString("dynamiclistxml");
        } catch (Exception e) {
            Logger.getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( dynamicXML introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� dynamiclistxml est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _dynamicListXML = f;
        return f;
    }

    public File getThesaurusDirectory() {
        if (_thesaurusDirectory != null)
            return _thesaurusDirectory;
        String path = "";
        try {
            path = res.getString("thesaurusDirectory");
        } catch (Exception e) {
            Logger.getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( thesaurusDirectory introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� thesaurusDirectory est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _thesaurusDirectory = f;
        return f;
    }


    public File getResourcesDirectory() {
        if (_resourcesDirectory != null)
            return _resourcesDirectory;
        String path = "";
        try {

            path = res.getString("resourcesPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( resourcesPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� resourcesPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _resourcesDirectory = f;
        return f;
    }


    public File getLienDirectory() {
        if (_lienDirectory != null)
            return _lienDirectory;
        String path = "";
        try {

            path = res.getString("lienPath");

        } catch (Exception e) {
            Logger
                    .getInstance()
                    .fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( lienPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� lienPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _lienDirectory = f;
        return f;
    }

    public String getSynonymeFileName() {
        if (_synonymeFileName != null)
            return _synonymeFileName;
        String str = "";
        try {
            str = res.getString("synonymeFileName");
        } catch (Exception e) {
            Logger.getInstance().fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( synonymeFileName introuvable ).", this, new NullPointerException());
        }
        File f = new File(configDirectoryPath, str);
        if (!f.exists() || !f.isFile()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� synonymeFileName est incorrect.", this, new NullPointerException());
        } else {
            return f.getAbsolutePath();
        }
        return str;
    }

    /**
     * R�cup�ration le r�pertoire de stats
     *
     * @return
     */
    public File getStatRootDirectory() {
        if (_statRootDirectory != null)
            return _statRootDirectory;
        String path = "";
        try {
            path = res.getString("statsPath");
        } catch (Exception e) {
            Logger.getInstance().fatal("Le fichier WCMConf/config.properties est introuvable dans le classpath courant ou est mal formatt� ( statsPath introuvable ).", this, new NullPointerException());
            System.exit(0);
        }
        File f = new File(configDirectoryPath, path);
        if (!f.exists() || !f.isDirectory()) {
            Logger.getInstance().fatal("Le path indiqu� par la cl� statsPath est incorrect.", this, new NullPointerException());
            System.exit(0);
        }
        _statRootDirectory = f;
        return f;
	}

}