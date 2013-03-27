package org.nextime.ion.backoffice.action;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.exception.StandardException;
import org.nextime.ion.backoffice.form.LoginForm;
import org.nextime.ion.commons.CommonServlet;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;

/**
 * 		Login mode SSO back office
 * 		L'identifiant de l'utilisater est renvoyé depuis le front office via les données partagées entre 2 applications
 * 		front office et backoffice
 * 		une fois le login est récupéré, les données sont éffacées de l'espace commun entre 2 applications
 *
 *
 * @author q.nguyen
 *
 */
public class LoginSSOAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {


		String sessionId=request.getParameter("id");
		String login=(String)getDataFromContext(getServlet().getServletContext(), sessionId, CommonServlet.FRONT_OFFICE_CONTEXT );
		clearDataFromContext(getServlet().getServletContext(), sessionId, CommonServlet.FRONT_OFFICE_CONTEXT );

		// vérifier l'utilisateur sur le framework
		try {
			Mapping.begin();
			User user = User.getInstance(login);
			Mapping.rollback();

			Boolean active = (Boolean)user.getMetaData("active");
			if (active==null || !active.booleanValue()) {
				throw new StandardException("User existe mais pas active");
			}

			request.getSession().setAttribute("userLogin", user.getLogin());
			request.getSession().setAttribute("userGroup", user.getGroupsIds());
			request.getSession().setAttribute("userMail",  user.getMetaData("email"));
		} catch (Exception e) {
			Mapping.rollback();
			ActionError error = new ActionError("error.login.badLogin");
			ActionErrors errors = new ActionErrors();
			errors.add("login", error);
			request.setAttribute(ERROR_KEY, errors);
			return mapping.findForward("view");
		} finally {
			Mapping.rollback();
		}

		// Forward to the newt page
		LogoutAction.purge(request);
		// request.setAttribute("reload", "true");
		return (mapping.findForward("ok"));

	}

	/**
	 * 	Récupérer un objet de l'espace  commun entre 2 applications :
	 * 			La clé est définie dans CommonServlet.CROSS_APPLICATION_KEY
	 * @param context
	 * @param ssosessionid
	 * @param p_FrontOfficeContext
	 * @return
	 */
	private static Object getDataFromContext(ServletContext context, String ssosessionid, String p_FrontOfficeContext) {
		// get the Context containing the shared session data
	    ServletContext ssocontext = context.getContext(p_FrontOfficeContext);
	    if (ssocontext !=null) {
		    // read the shareddata
		    Hashtable<String, Object> shareddata = (Hashtable)ssocontext.getAttribute(CommonServlet.USER_CROSS_APPLICATION_KEY);
		    if (shareddata!=null) {
		        // get the right Vector using the ssosessionid
		    	return shareddata.get(ssosessionid);
		    }
	    }
	    return null;
	}

	/**
	 * 	Supression des données identifiées par la clé de l'espace commun des 2 applications
	 * @param context
	 * @param ssosessionid
	 * @param p_FrontOfficeContext
	 */
	private static void clearDataFromContext(ServletContext context, String ssosessionid, String p_FrontOfficeContext) {
		// get the Context containing the shared session data
	    ServletContext ssocontext = context.getContext(p_FrontOfficeContext);

	    if (ssocontext !=null) {
		    // read the shareddata
		    Hashtable<String, String> shareddata = (Hashtable)ssocontext.getAttribute(CommonServlet.USER_CROSS_APPLICATION_KEY);
		    if (shareddata!=null) {
		        // get the right Vector using the ssosessionid
		    	shareddata.remove(ssosessionid);
		    }
	    }
	}

}