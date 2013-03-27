package org.nextime.ion.framework.business;

import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.SectionPublicationImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * Réprésente la relation entre une section et une publication
 *
 * @author q.nguyen
 */
public abstract class SectionPublication implements Comparable {

    public static SectionPublication create(Section p_Section, Publication p_Publication) throws MappingException {
        String id = p_Section.getId() + "_" + p_Publication.getId();
        try {
            SectionPublication last = findLastSectionPublication(p_Section);
            int lastCount = 0;
            if (last != null) {
                lastCount = last.getIndex();
            }
            SectionPublicationImpl impl = new SectionPublicationImpl();
            impl.setSection(p_Section);
            impl.setPublication(p_Publication);
            impl.setId(id);
            impl.setIndex(lastCount + 1);
            Mapping.getInstance().getDb().create(impl);
            Logger.getInstance().log("Un objet SectionPublication pour l'id " + impl.getId() + " à été crée.", Section.class);
            return impl;
        } catch (PersistenceException e) {
            String message = "Impossible de créer l'objet SectionPublication pour l'id " + id + ".";
            Logger.getInstance().error(message, Section.class, e);
            throw new MappingException(message);
        }
    }


    /**
     * Rercherche SectionPublication par l'identifiant de la section et publication
     *
     * @param p_Section
     * @param p_Publication
     * @return
     * @throws MappingException
     */
    public static SectionPublication findSectionPublication(Section p_Section, Publication p_Publication) throws MappingException {
        SectionPublication ret = null;
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery("SELECT sp FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 AND sp.publication = $2 ");
            oql.bind(p_Section);
            oql.bind(p_Publication);
            QueryResults results = oql.execute();
            while (results.hasMore() && (ret == null)) {
                SectionPublication sectionPublication = (SectionPublication) results.next();
                ret = sectionPublication;
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du list des sections root", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }

    /**
     * Rercherche SectionPublication par l'identifiant de la section et l'index de la publicaiton
     *
     * @param p_Section
     * @param p_Publication
     * @return
     * @throws MappingException
     */
    public static SectionPublication findSectionPublication(Section p_Section, int p_Index) throws MappingException {
        SectionPublication ret = null;
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery("SELECT sp FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 AND sp.index = $2 ");
            oql.bind(p_Section);
            oql.bind(p_Index);
            QueryResults results = oql.execute();
            while (results.hasMore() && (ret == null)) {
                SectionPublication sectionPublication = (SectionPublication) results.next();
                ret = sectionPublication;
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors findSectionPublication ", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }


    /**
     * Recherche la dernère section publication
     *
     * @param p_Section
     * @param p_Index
     * @return
     * @throws MappingException
     */
    public static SectionPublication findLastSectionPublication(Section p_Section) throws MappingException {
        SectionPublication ret = null;
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery("SELECT sp FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl sp WHERE sp.section = $1 order by sp.index desc LIMIT $2 ");
            oql.bind(p_Section);
            oql.bind(1);
            QueryResults results = oql.execute();
            while (results.hasMore() && (ret == null)) {
                SectionPublication sectionPublication = (SectionPublication) results.next();
                ret = sectionPublication;
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du findLastSectionPublication", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }


    public static SectionPublication findNextSectionPublication(Section p_Section, int p_Index) throws MappingException {
        SectionPublication ret = null;
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery("SELECT sp FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl " + " sp WHERE sp.section = $1 AND sp.index > $2 order by sp.index asc LIMIT $3 ");
            oql.bind(p_Section);
            oql.bind(p_Index);
            oql.bind(1);
            QueryResults results = oql.execute();
            while (results.hasMore() && (ret == null)) {
                SectionPublication sectionPublication = (SectionPublication) results.next();
                ret = sectionPublication;
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du findLastSectionPublication", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }

    public static SectionPublication findPreviousSectionPublication(Section p_Section, int p_Index) throws MappingException {
        SectionPublication ret = null;
        try {
            OQLQuery oql = Mapping
                    .getInstance()
                    .getDb()
                    .getOQLQuery("SELECT sp FROM org.nextime.ion.framework.business.impl.SectionPublicationImpl " + " sp WHERE sp.section = $1 AND sp.index < $2 order by sp.index desc LIMIT $3 ");
            oql.bind(p_Section);
            oql.bind(p_Index);
            oql.bind(1);
            QueryResults results = oql.execute();
            while (results.hasMore() && (ret == null)) {
                SectionPublication sectionPublication = (SectionPublication) results.next();
                ret = sectionPublication;
            }
        } catch (Exception e) {
            Logger.getInstance().error("erreur lors du findLastSectionPublication", Section.class, e);
            throw new MappingException(e.getMessage());
        }
        return ret;
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Logger.getInstance().log("L'objet SectionPublication pour l'id " + getId() + " à été detruit.", Section.class);
        } catch (PersistenceException e) {
            String message = "Impossible de detruire l'objet SectionPublication pour l'id " + getId() + ".";
            Logger.getInstance().error(message, Section.class, e);
            throw new MappingException(message);
        }
    }

    /**
     * @return la section
     */
    public abstract Section getSection();


    public abstract Publication getPublication();

    /**
     * Retourne l'index de la publication dans la section
     */
    public abstract int getIndex();

    public abstract void setIndex(int p_NewIndex);

    /**
     * @return la section
     */
    public abstract void setSection(Section p_Section);


    public abstract void setPublication(Publication p_Publication);

    /**
     * Retourne l'index de la publication dans la section
     */
    public abstract String getId();

	public abstract void setId(String p_Id);

    /**
     * Comparaison par index décroissant
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        SectionPublication other = (SectionPublication) o;
        return other.getIndex() - getIndex();
    }
}
