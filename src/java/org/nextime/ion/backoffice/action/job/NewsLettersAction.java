package org.nextime.ion.backoffice.action.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.action.security.CreateGroupAction;
import org.nextime.ion.backoffice.bean.Option;
import org.nextime.ion.backoffice.form.NewsLettersForm;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.job.LcSched;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * T�che d'envoi les news letters
 * Pour les 3 t�che diff�nrents : grand puiblic, adh�nrent et locale
 *
 * @author q.nguyen
 */
public class NewsLettersAction extends BaseAction {

    private static final Log _log = LogFactory.getLog(NewsLettersAction.class);

    // Map repr�sent le code entr� et le nom du job
    private static final Map<String, String> CODE_JOBNAMES = new HashMap<String, String>();

    static {
        CODE_JOBNAMES.put("GP", "NewsLettersGPJob");
        CODE_JOBNAMES.put("AD", "NewsLettersADJob");
        CODE_JOBNAMES.put("RE", "NewsLettersREJob");
        CODE_JOBNAMES.put("VO", "NewsLettersVOJob");
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // check if user is correctly logged
        if (!checkUser(request))
            return (mapping.findForward("errorUserIon"));

        // check if this action is allowed
        try {
            Mapping.begin();
            if (!new SecurityManagerImpl().canAdminSecurity(User.getInstance(request.getSession().getAttribute("userLogin") + ""))) {
                throw new Exception();
            }
        } catch (Exception e) {
            Mapping.rollback();
            Logger.getInstance().error(e.getMessage(), CreateGroupAction.class, e);
            return (mapping.findForward("errorSecurityIon"));
        } finally {
            Mapping.rollback();
        }

        NewsLettersForm frm = (NewsLettersForm) form;

        String jobCode = frm.getJob();
        String jobName = CODE_JOBNAMES.get(jobCode);

        // r�cup�ration les sites courantes de l'utilisateur en session

        Vector sites = (java.util.Vector) request.getSession().getAttribute("currentSite");
        request.setAttribute("sites", generateBeanSites(sites));

        // TODO

        if (jobName != null) {
            try {
                // ajouter la r�gion comme param�tre
                Hashtable<String, String> params = new Hashtable<String, String>();
                params.put("parm_0", frm.getSiteId());
                LcSched.execute(jobName, params);
            } catch (Exception e) {
                _log.error("Erreur d'ex�cure", e);
                return (mapping.findForward("errorIon"));
            }
        } else {
            _log.info("Pas trouver le job avec comme code " + jobCode);
        }

        // Forward to the next page
        return (mapping.findForward("ok"));
    }

    /**
     * G�n�ration des sites
     *
     * @param p_Sections
     * @return
     */
    private Collection generateBeanSites(Vector p_Sections) {
        ArrayList ret = new ArrayList();
        for (Iterator it = p_Sections.iterator(); it.hasNext();) {
            Section section = (Section) it.next();
            Option option = new Option((String) section.getMetaData("name"), section.getId());
            ret.add(option);
        }
        return ret;
    }

}