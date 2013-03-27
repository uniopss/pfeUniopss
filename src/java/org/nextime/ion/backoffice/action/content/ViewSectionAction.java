package org.nextime.ion.backoffice.action.content;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.bean.SectionTypes;
import org.nextime.ion.backoffice.bean.TypeBean;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.impl.SectionImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class ViewSectionAction extends BaseAction {

    static public final int pageSize = 20;

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Date dateBeginAction = new Date();
        Date dateBegin = new Date();

        // check if user is correctly logged
        if (!checkUser(request))
            return (mapping.findForward("errorUserIon"));

        String highlightId = request.getSession().getAttribute("highlightId") + "";
        request.getSession().removeAttribute("highlightId");
        request.setAttribute("highlightId", highlightId);

        if (request.getSession().getAttribute("pageInfos") == null) {
            request.getSession().setAttribute("pageInfos", new Hashtable());
        }
        if (request.getSession().getAttribute("versionDisplayInfos") == null) {
            request.getSession().setAttribute("versionDisplayInfos", new Hashtable());
        }

        if (request.getParameter("expand") != null) {
            Hashtable ht = (Hashtable) request.getSession().getAttribute("versionDisplayInfos");
            ht.put(request.getParameter("expand"), "true");
        }
        if (request.getParameter("collapse") != null) {
            Hashtable ht = (Hashtable) request.getSession().getAttribute("versionDisplayInfos");
            ht.remove(request.getParameter("collapse"));
        }

        // fill data
        try {
            Vector sections = null;
            //Collection publications=null;
            Section section = null;

            String id = request.getParameter("sid") != null && !"".equals(request.getParameter("sid")) ? (String) request.getParameter("sid") : (String) request.getAttribute("sid");

            // dans le cas vient du browser
            if ((id == null || "".equals(id)) && (request.getSession().getAttribute("currentSection") != null))
                id = (String) request.getSession().getAttribute("currentSection");
            Mapping.begin();
            if (id == null || "".equals(id)) {
                Vector roots = (java.util.Vector) request.getSession().getAttribute("currentSite");
                if (roots != null && !roots.isEmpty())
                    section = (Section) roots.get(0);
            } else {
                dateBegin = new Date();
                section = Section.getInstance(id);
                mesureTemps("Temps de recharge section ", dateBegin);
            }

            if (section == null) {
                // Forward to the next page
                return (mapping.findForward("view"));
            }

            dateBegin = new Date();
            sections = section.listSubSections();
            int publicationSize = ((SectionImpl) section).getSectionPublications().size();
            Mapping.rollback();
            mesureTemps("Temps list sous section =  ", dateBegin);

            request.setAttribute("sid", section.getId());
            request.getSession().setAttribute("currentSection", section.getId());
            int startPublications;
            try {
                startPublications = Integer.parseInt(request.getParameter("start"));
                ((Hashtable) (request.getSession().getAttribute("pageInfos"))).put(section.getId(), new Integer(startPublications));
            } catch (NumberFormatException e) {
                try {
                    startPublications = ((Integer) (((Hashtable) (request.getSession().getAttribute("pageInfos"))).
                            get(section.getId()))).intValue();
                } catch (Exception ex) {
                    startPublications = 0;
                    ((Hashtable) (request.getSession().getAttribute("pageInfos"))).put(section.getId(), new Integer(startPublications));
                }
            }
            // ------------------------------
            try {
                request.setAttribute("section", section);
                request.setAttribute("sectionName", section.getMetaData("name"));
                String description = StringUtils.EMPTY;
                TypeBean typeBean = SectionTypes.getSectionBean(servlet, section.getMetaData("template") + "", request);
                if (typeBean != null) {
                    description = typeBean.getDescription();
                }
                request.setAttribute("sectionDescription", description);
            } catch (Exception e) {
                //e.printStackTrace();
                Logger.getInstance().error(e.getMessage(), ViewSectionAction.class, e);
            }

            // list subsections
            request.setAttribute("sections", sections);
            request.setAttribute("sectionsSize", new Integer(sections.size()));

            int stopPublications = -1;
            if (startPublications >= publicationSize) {
                startPublications = 0;
            }

            stopPublications = startPublications + pageSize;
            if (stopPublications > publicationSize) {
                stopPublications = publicationSize;
            }
            stopPublications = stopPublications - 1;
            if (stopPublications < 0) {
                stopPublications = 0;
            }

            ///////
            Mapping.begin();
            Collection publicationPage = PublicationSorter.sortPublications(section, startPublications, stopPublications + 1, true);
            Mapping.rollback();

            // list publications
            request.setAttribute("pageSize", Integer.valueOf(pageSize));
            request.setAttribute("start", Integer.valueOf(startPublications));
            request.setAttribute("stop", Integer.valueOf(stopPublications));
            request.setAttribute("publications", publicationPage);
            request.setAttribute("publicationsSize", Integer.valueOf(publicationSize));
        } catch (Exception e) {
            Mapping.rollback();
            Logger.getInstance().error(e.getMessage(), ViewSectionAction.class, e);
            return (mapping.findForward("errorIon"));
        } finally {
            Mapping.rollback();
        }

        mesureTemps("Temps d'éxécuter action=  ", dateBeginAction);

        // Forward to the next page
        return (mapping.findForward("view"));

    }

    private void mesureTemps(String p_Message, Date p_DateBegin) {
        Logger.getInstance().log(p_Message + (new Date().getTime() - p_DateBegin.getTime()) + " ms ", PublicationSorter.class);
    }

}