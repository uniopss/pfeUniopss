package org.nextime.ion.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.SectionPublication;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

import sun.security.action.GetLongAction;

// Referenced classes of package org.nextime.ion.commons:
//            MyPublicationComparator

public class PublicationSorter {

    public PublicationSorter() {
    }


    /**
     * 	Récupération d'une liste des publications par ordre naturelle
     * @param s
     * @return
     * @throws MappingException
     */
    public static Collection sortPublications(Section s) throws MappingException {
        Date begindate = new Date();
        List ret = new FastArrayList();
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery(
                            "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 order by sp.index desc ");
            oql.bind(s);
            QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
            while (results.hasMore()) {
                Publication p = (Publication)results.next();
                ret.add(p);
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list des sections root", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        Logger.getInstance().log(" Temps consomé = " + ( new Date().getTime() - begindate.getTime() ) + " ms " , PublicationSorter.class);
        return ret;
    }

    /**
     *	Récupération d'une liste de publications en ordre
     * @param p_Section
     * @param p_StartIndex
     * @param p_StopIndex
     * @param p_IsDescending
     * @return
     * @throws MappingException
     */
    public static Collection sortPublications(Section p_Section, int p_StartIndex, int p_StopIndex, boolean p_IsDescending) throws MappingException {
        String ORDER_DESC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 order by sp.index desc LIMIT $2 OFFSET $3 ";
        String ORDER_ASC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 order by sp.index asc LIMIT $2 OFFSET $3 ";
        Date begindate = new Date();
        List ret = new FastArrayList();
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery(p_IsDescending?ORDER_DESC_REQ:ORDER_ASC_REQ);
            oql.bind(p_Section);
            oql.bind(p_StopIndex-p_StartIndex);
            oql.bind(p_StartIndex);
            QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
            while (results.hasMore()) {
                Publication p = (Publication)results.next();
                ret.add(p);
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list sortPublications", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        Logger.getInstance().log(" Temps consomé = " + ( new Date().getTime() - begindate.getTime() ) + " ms " , PublicationSorter.class);
        return ret;
    }



    public static Vector sortDatePublications(Section s) {
        Collection publications = s.listPublications();
        Vector result = new Vector(publications);
        Collections.sort(result, new PublicationDateComparator());
        Collections.reverse(result);
        return result;
    }

    public static Collection sortPublications(List p_SectionIds, int p_Limit, int p_Offset, boolean is_Descending) throws MappingException {
    	String ids = buildListId(p_SectionIds);
        String DATE_DESC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section.id IN LIST "+ids+ " order by sp.index  LIMIT $1 OFFSET $2 "; //
        String DATE_ASC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE  sp.section.id IN LIST "+ids+ " order by sp.index LIMIT $1 OFFSET $2 ";

        ArrayList<Publication> ret=new ArrayList<Publication>();
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery((is_Descending)?DATE_DESC_REQ:DATE_ASC_REQ);
            oql.bind(p_Limit);
            oql.bind(p_Offset);
            QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
            while (results.hasMore()) {
                Publication p = (Publication)results.next();
                ret.add(p);
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list des sortDatePublications", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }
    /**
     * 	Récupération d'une list des publications par date
     * @param p_Section
     * @param p_Limit
     * @param p_Offset
     * @param is_Descending
     * @return
     * @throws MappingException
     */
    public static Collection sortDatePublications(Section p_Section, int p_Limit, int p_Offset, boolean is_Descending) throws MappingException {

        String DATE_DESC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 order by sp.publication.datePubli desc LIMIT $2 OFFSET $3 ";
        String DATE_ASC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 order by sp.publication.datePubli asc LIMIT $2 OFFSET $3 ";

        ArrayList<Publication> ret=new ArrayList<Publication>();
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery((is_Descending)?DATE_DESC_REQ:DATE_ASC_REQ);
            oql.bind(p_Section);
            oql.bind(p_Limit);
            oql.bind(p_Offset);
            QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
            while (results.hasMore()) {
                Publication p = (Publication)results.next();
                ret.add(p);
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list des sortDatePublications", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }


    /**
     * 	Construction de la liste de ids pour la requete castor
     * @param p_ListId
     * @return
     */
    private static String buildListId(List p_ListId) {
        if (p_ListId==null || p_ListId.size()==0)
                    return StringUtils.EMPTY;
        StringBuffer sb = new StringBuffer("( ");
        int size=p_ListId.size();
        for (int i=0; i<size; i++) {
            String id = (String)p_ListId.get(i);
            sb.append("\"").append(id).append("\"");
            if (i<size-1) {
                sb.append(",");
            }
        }
        sb.append(" ) ");
        return sb.toString();
    }


    private static int GAP = 10;

    public static Collection sortDatePublications(List p_SectionIds, int p_Limit, int p_Offset, boolean is_Descending) throws MappingException {
    		String ids = buildListId(p_SectionIds);
    		String DATE_DESC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp " +
    				" WHERE sp.section.id IN LIST "+ids+ " order by sp.publication.datePubli desc LIMIT $1 OFFSET $2";
    		String DATE_ASC_REQ = "SELECT sp.publication FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp " +
    				" WHERE  sp.section.id IN LIST "+ids+ " order by sp.publication.datePubli LIMIT $1 OFFSET $2";

    		int offset = p_Offset;
    		int limit = offset + GAP;

    		ArrayList<Publication> ret= new ArrayList<Publication>();

    		boolean hasResultat = true;
    		try {
    			while (ret.size()<p_Limit && hasResultat ) {
    				OQLQuery oql = Mapping.getInstance().getDb().getOQLQuery((is_Descending)?DATE_DESC_REQ:DATE_ASC_REQ);
    			    oql.bind(limit);
    			    oql.bind(offset);
    			    QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);

    			    hasResultat = results.hasMore();

    			    while (results.hasMore() && ret.size() < p_Limit) {
	    			    	Publication p = (Publication)results.next();
	    			        if (!ret.contains(p)) {
	    			        	ret.add(p);
	    			        }
	    			}
    			    offset=limit+1;
    			    limit = offset + GAP;
    			}
    		} catch (Exception e) {
    			Logger.getInstance().error("erreur lors du list des sortDatePublications", PublicationSorter.class, e);
    		    throw new MappingException(e.getMessage());
    		}
    		return ret;
    }

    public static Vector sortDpPublications(Section s) {
        Collection publications = s.listPublications();
        Vector result = new Vector(publications);
        Collections.sort(result, new PublicationDpComparator());
        Collections.reverse(result);
        return result;
    }

    /*
    public static Vector sortNamePublications(Section s) {
        Vector publications = s.listPublications();
        Vector result = (Vector) publications.clone();
        Collections.sort(result, new PublicationNameComparator());
        Collections.reverse(result);
        return result;
    }


    public static Vector sortTypePublications(Section s) {
        Vector publications = s.listPublications();
        Vector result = (Vector) publications.clone();
        Collections.sort(result, new PublicationTypeComparator());
        Collections.reverse(result);
        return result;
    }
    */

    public static void upPublication(Publication p, Section s) throws MappingException {
        SectionPublication currentSP=SectionPublication.findSectionPublication(s, p);
        int currentIndex = currentSP.getIndex();
        SectionPublication nextSP=SectionPublication.findNextSectionPublication(s, currentIndex);
        if (nextSP!=null) {
            int nextIndex = nextSP.getIndex();
            nextSP.setIndex(currentIndex);
            currentSP.setIndex(nextIndex);
        }

        /*
        try {
            int currentIndex = Integer.parseInt(p.getMetaData("index_"
                    + s.getId())
                    + "");
            int newIndex = currentIndex + 1;
            Vector publications = s.listPublications();
            if (newIndex > publications.size()) {
                newIndex = publications.size();
            } else {
                for (int i = 0; i < publications.size(); i++) {
                    if (newIndex == Integer
                            .parseInt(((Publication) publications.get(i))
                                    .getMetaData("index_" + s.getId())
                                    + "")) {
                        Publication pac = (Publication) publications.get(i);
                        pac.setMetaData("index_" + s.getId(), (Integer
                                .parseInt(pac.getMetaData("index_" + s.getId())
                                        + "") - 1)
                                + "");
                    }
                }

            }
            p.setMetaData("index_" + s.getId(), newIndex + "");
        } catch (Exception e) {
            //e.printStackTrace();
            org.nextime.ion.framework.logger.Logger.getInstance().error(
                    e.getMessage(), PublicationSorter.class,e);
        } */

    }

    public static void downPublication(Publication p, Section s) throws MappingException {
        SectionPublication currentSP=SectionPublication.findSectionPublication(s, p);
        int currentIndex = currentSP.getIndex();
        SectionPublication previousSP=SectionPublication.findPreviousSectionPublication(s, currentIndex);
        if (previousSP!=null) {
            int previousIndex = previousSP.getIndex();
            currentSP.setIndex(previousIndex);
            previousSP.setIndex(currentIndex);
        }
        /*
        try {
            int currentIndex = Integer.parseInt(p.getMetaData("index_"
                    + s.getId())
                    + "");
            int newIndex = currentIndex - 1;
            Vector publications = s.listPublications();
            if (newIndex < 1) {
                newIndex = 1;
            } else {
                for (int i = 0; i < publications.size(); i++) {
                    if (newIndex == Integer
                            .parseInt(((Publication) publications.get(i))
                                    .getMetaData("index_" + s.getId())
                                    + "")) {
                        Publication pac = (Publication) publications.get(i);
                        pac.setMetaData("index_" + s.getId(), (Integer
                                .parseInt(pac.getMetaData("index_" + s.getId())
                                        + "") + 1)
                                + "");
                    }
                }

            }
            p.setMetaData("index_" + s.getId(), newIndex + "");
        } catch (Exception e) {
            //e.printStackTrace();
            org.nextime.ion.framework.logger.Logger.getInstance().error(
                    e.getMessage(), PublicationSorter.class,e);
        }
        */
    }

    public static void resetPublication(Publication p, Section s) {
        return;
        /*
        try {
            Vector publications = s.listPublications();
            p.setMetaData("index_" + s.getId(), publications.size() + "");
        } catch (Exception e) {
            //e.printStackTrace();
            org.nextime.ion.framework.logger.Logger.getInstance().error(
                    e.getMessage(), PublicationSorter.class,e);
        }*/
    }

    public static void initPublication(Publication p, Section s) throws MappingException {
        return;
        /*
        try {
            Vector publications = s.listPublications();
            if (publications.contains(p)) {
                p.setMetaData("index_" + s.getId(), publications.size() + "");
            } else {
                p.setMetaData("index_" + s.getId(), (publications.size() + 1)
                        + "");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            org.nextime.ion.framework.logger.Logger.getInstance().error(
                    e.getMessage(), PublicationSorter.class,e);
        } */
    }

    /**
     * 	Reinitaliser les position de publication dans la section
     * @param p
     * @param s
     * @throws MappingException
     */
    public static void removePublication(Publication p, Section s) throws MappingException {
    	SectionPublication impl = ((PublicationImpl)p).findSectionPublication(s);
    	try {
			if (impl!=null) {
				// mettre à jour les autres en diminuant
				int currentIndex = impl.getIndex();
	            OQLQuery oql = Mapping
	            .getInstance()
	            .getDb().getOQLQuery("SELECT sp FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp " +
	            		" WHERE sp.section = $1 AND sp.index > $2 ");
	            oql.bind(s.getId());
	            oql.bind(currentIndex);
	            QueryResults results = oql.execute();
	            while (results.hasMore()) {
	                SectionPublication sp = (SectionPublication)results.next();
	                sp.setIndex(sp.getIndex()-1);
	            }

			}
    	} catch (PersistenceException e) {
    		Logger.getInstance().error("Error de mettre à jour les sections_publications la publicaiton ", PublicationSorter.class,e);
    		throw new MappingException("Error de mettre à jour les sections_publications la publicaiton ");
    	}
        return;
        /*
        try {
            Vector publications = s.listPublications();
            int index = Integer.parseInt(p.getMetaData("index_" + s.getId())
                    + "");
            for (int i = 0; i < publications.size(); i++) {
                Publication tp = (Publication) publications.get(i);
                int tindex = Integer.parseInt(tp.getMetaData("index_"
                        + s.getId())
                        + "");
                if (tindex > index) {
                    tp.setMetaData("index_" + s.getId(), (tindex - 1) + "");
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            org.nextime.ion.framework.logger.Logger.getInstance().error(
                    e.getMessage(), PublicationSorter.class);
        }*/
    }
}