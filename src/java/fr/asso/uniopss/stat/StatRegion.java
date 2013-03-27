package fr.asso.uniopss.stat;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.action.security.CreateGroupAction;
import org.nextime.ion.backoffice.bean.Option;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class StatRegion extends BaseAction {

    private static final String IGNORED_SECION = "trco";

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        StatRegionForm sForm = (StatRegionForm) form;
        // check if user is correctly logged
        if (!checkUser(request))
            return (mapping.findForward("errorUserIon"));

        String userLogin = request.getSession().getAttribute("userLogin") + "";

        // check if this action is allowed
        try {
            Mapping.begin();
            if (!new SecurityManagerImpl().canAdminSecurity(User.getInstance(userLogin))) {
                throw new Exception();
            }
        } catch (Exception e) {
            Mapping.rollback();
            Logger.getInstance().error(e.getMessage(), CreateGroupAction.class, e);
            return (mapping.findForward("errorSecurityIon"));
        } finally {
            Mapping.rollback();
        }

        // récupération les sites courantes de l'utilisateur en session
        Vector sites = (java.util.Vector) request.getSession().getAttribute("currentSite");

        ArrayList availableSites = new ArrayList();
        if (StringUtils.equalsIgnoreCase("admin", userLogin)) {
            availableSites.add(new Option("Global", "global"));
        }

        // on récupère les régions auquels attache l'utilisateur
        for (Iterator it = sites.iterator(); it.hasNext();) {
            Section section = (Section) it.next();
            if (!StringUtils.equals(IGNORED_SECION, section.getId())) {
                availableSites.add(new Option((String) section.getMetaData("name"), section.getId()));
            }
        }

        // vérifie que admin lié à au moins un site

        if (availableSites.isEmpty()) {
            return (mapping.findForward("errorUserIon"));
        }

        if (!sForm.isPostBack()) {
            // on point sur le jour d'avant
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);

            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            currentMonth = currentMonth + 1;

            sForm.setSelectedYear(currentYear + "");
            sForm.setSelectedMonth(StringUtils.leftPad(currentMonth + "", 2, "0"));
            sForm.setSelectedSite(((Option) availableSites.get(0)).getValue());
        }

        initForm(availableSites, form);

        String path = calculatePath(sForm.getSelectedYear(), sForm.getSelectedMonth(), sForm.getSelectedSite());
        sForm.setIframePath(path);

        // Forward to the next page
        return mapping.findForward("ok");

    }

    /**
     * Initialisation des données commun pour le formulaire ( dropdowlist )
     *
     * @param availableSites
     * @param form
     */
    private void initForm(Collection availableSites, ActionForm form)

    {

        StatRegionForm sForm = (StatRegionForm) form;

        // assigner la liste des site au combo
        sForm.setSites(availableSites);

        // on récupère le répertoire qui contient les stats et on construit les 3 combo liste
        File statRootDirectory = Config.getInstance().getStatRootDirectory();
        // on récupère le répetoire type
        File typeDirectory = new File(statRootDirectory, "type");
        // on récupère tous les sous répertoire


        File[] yearsDirectory = typeDirectory.listFiles();

        Collection availableYears = new ArrayList();

        File selectedYearDirectory = null;
        for (int i = 0; yearsDirectory != null && i < yearsDirectory.length; i++) {
            File file = (File) yearsDirectory[i];
            if (file != null && file.isDirectory()) {
                availableYears.add(new Option(file.getName(), file.getName()));
                if (selectedYearDirectory == null && StringUtils.equals(file.getName(), sForm.getSelectedYear())) {
                    selectedYearDirectory = file;
                }
            }
        }
        sForm.setYears(availableYears);

        if (selectedYearDirectory != null) {
            // on récupère les mois selon l'anné
            Collection availableMonths = new ArrayList();
            File[] allMonths = selectedYearDirectory.listFiles();

            for (int i = 0; allMonths != null && i < allMonths.length; i++) {
                File file = (File) allMonths[i];
                if (file != null && file.isDirectory()) {
                    availableMonths.add(new Option(file.getName(), file.getName()));
                }
            }
            Collections.sort((ArrayList)availableMonths);
            sForm.setMonths(availableMonths);
        }
    }

    /**
     * Retourner le path de awstat
     *
     * @param p_CurrentYear
     * @param p_CurrentMonth
     * @param p_Type
     * @return
     */
    private String calculatePath(String p_CurrentYear, String p_CurrentMonth, String p_Type) {
        return "/stats/type/" + p_CurrentYear + "/" + p_CurrentMonth + "/" + p_Type + "/awstats.html";
	}


}
