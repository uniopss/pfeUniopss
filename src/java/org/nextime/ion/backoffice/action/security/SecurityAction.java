package org.nextime.ion.backoffice.action.security;

import javax.servlet.http.HttpServletRequest;

import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;


/**
 * 	Action de base fournit des fonctionnalités communes
 * 	pour la récupération des infos nécéssaire à la côntrôle ..
 *
 * @author q.nguyen
 *
 */
public abstract class SecurityAction extends BaseAction {


	/**
	 * 	Récupération l'objet l'utilisateur de login de l'utilisateur de session
	 * @param request
	 * @return l'objet l'utilisateur
	 * Cette action doit appeler après la fonction checkUser de BaseAction
	 * @param p_Request
	 */
	protected User getCurrentUser(HttpServletRequest p_Request) throws MappingException {
		try {
			Mapping.begin();
			User user = User.getInstance((String) p_Request.getSession().getAttribute("userLogin"));
			return user;
		} finally {
			Mapping.rollback();
		}
	}


	/**
	 * 	Déterminer si l'utilisateur courante est super admin
	 * Cette fonnction utilise le login admin en dur comme super-admin
	 * @param request
	 * @return
	 */
	protected boolean isSuperAdmin(HttpServletRequest request ) {
		String userLogin=(String)request.getSession().getAttribute("userLogin");
		if (userLogin!=null && userLogin.equals("admin")) {
			return true;
		} else {
			return false;
		}
	}


}
