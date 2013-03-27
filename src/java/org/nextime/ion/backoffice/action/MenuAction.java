package org.nextime.ion.backoffice.action;

import java.io.IOException;
import java.io.File;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.logger.Logger;

public class MenuAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Locale newLocale = request.getLocale();
		String localizedMenu = "";
		if (request.getSession().getAttribute("localizedMenu") == null) {
			java.io.File d = new java.io.File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getConfigDirectoryPath());
			File menu = new File(d, "menu.xml");
			if (newLocale != null) {
				request.getSession().setAttribute(Action.LOCALE_KEY, newLocale);

				Locale locs[] = Locale.getAvailableLocales();
				boolean b = false;
				for (int i = 0; i < locs.length; i++) {
					if (newLocale.equals(locs[i])) {
						b = true;
						break;
					}
				}

				String lMenu = "menu_" + newLocale.getLanguage() + ".xml";
				File f = new File(d, lMenu);
				if (b && f.exists())
					menu = f;
			}
			if (menu != null && menu.exists()) {
				try {
					// read file
					localizedMenu = new String(org.nextime.ion.commons.Util
							.read(menu));
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(), MenuAction.class,e);
				}
			}
			if (localizedMenu != null && !"".equals(localizedMenu))
				request.getSession().setAttribute("localizedMenu",
						localizedMenu);
		}
		// Forward to the next page
		return (mapping.findForward("view"));

	}

}