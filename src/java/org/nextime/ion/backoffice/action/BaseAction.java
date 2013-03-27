package org.nextime.ion.backoffice.action;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionServlet;
import org.nextime.ion.backoffice.exception.UserNotLoggedException;
import org.nextime.ion.backoffice.message.MessageBoard;
import org.nextime.ion.backoffice.panier.Panier;
import org.nextime.ion.backoffice.tree.TreeBuilder;
import org.nextime.ion.backoffice.tree.TreeControl;
import org.nextime.ion.backoffice.tree.TreeControlNode;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.frontoffice.smartCache.SmartCacheManager;

public class BaseAction extends Action {



	private static String ION_LAST_UPDATE_ATT_INTERNAL = "ION_LAST_UPDATE_ATT_INTERNAL";

	public static final int INIT_PLUGIN_MAX = 10;

	public static final String TREEBUILDER_KEY = "treebuilders";

	public static final String ROOTNODENAME_KEY = "rootnodename";

	protected boolean checkUser(HttpServletRequest request) throws UserNotLoggedException {
		if (request.getSession().getAttribute("userLogin") == null) {
			//throw new UserNotLoggedException();
			Logger.getInstance().log("Not connected", BaseAction.class);
			//return (mapping.findForward("errorUserIon"));
			return false;
		}
		try {
			if (Mapping.getInstance().isTransactionActive()) {
				try {
					Mapping.rollback();
				} catch (Exception ee) {
					// TODO
					Logger.getInstance().error(ee.getMessage(), BaseAction.class, ee);
				}
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BaseAction.class,e);
		}


		// check panier & message
		checkPanier(request);
		checkMessage(request);




		try {
			// check cache
			cleanCacheIfNeed();
			// check tree
			org.nextime.ion.commons.Util.prepare(request);
			checkTreeNode(request);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BaseAction.class, e);
		}
		return true;
	}

	protected void checkPanier(HttpServletRequest request) {
		if (request.getSession().getAttribute("panier") == null) {
			Panier p = new Panier(request.getSession()
					.getAttribute("userLogin")
					+ "");
			if (p != null)
				request.getSession().setAttribute("panier", p);
		}
	}

	protected void checkMessage(HttpServletRequest request) {
		if (request.getSession().getAttribute("messageBoard") == null) {
			MessageBoard p = new MessageBoard(request.getSession()
					.getAttribute("userLogin")
					+ "");
			if (p != null)
				request.getSession().setAttribute("messageBoard", p);
		}
	}

	protected void checkTreeNode(HttpServletRequest request)
			throws ServletException {

		HttpSession session = request.getSession();
		if (session.getAttribute("treeControlTest") == null) {

			ActionServlet servlet = getServlet();

			// Getting init parms from web.xml

			// Get the string to be displayed as root node while rendering the
			// tree
			String rootnodeName = servlet.getInitParameter("rootNodeName");

			String treeBuildersStr = "org.nextime.ion.backoffice.tree.WcmTreeBuilder";

			// Make the root node and tree control

			// The root node gets rendered only if its value
			// is set as an init-param in web.xml

			TreeControlNode root = new TreeControlNode("ROOT-NODE", "root.gif",
					rootnodeName, "setUpTree.x?select=ROOT-NODE", "content",
					true);

			TreeControl control = new TreeControl(root);

			if (treeBuildersStr != null) {
				Class treeBuilderImpl;
				TreeBuilder treeBuilderBase;

				ArrayList treeBuilders = new ArrayList(INIT_PLUGIN_MAX);
				int i = 0;
				StringTokenizer st = new StringTokenizer(treeBuildersStr, ",");
				while (st.hasMoreTokens()) {
					treeBuilders.add(st.nextToken().trim());
				}

				if (treeBuilders.size() == 0)
					treeBuilders.add(treeBuildersStr.trim());

				for (i = 0; i < treeBuilders.size(); i++) {

					try {
						treeBuilderImpl = Class.forName((String) treeBuilders
								.get(i));
						treeBuilderBase = (TreeBuilder) treeBuilderImpl
								.newInstance();
						treeBuilderBase.buildTree(control, servlet, request);
					} catch (Throwable t) {
						Logger.getInstance().error(t.getMessage(), BaseAction.class,t);
					}
				}
			}

			session.setAttribute("treeControlTest", control);

			String name = request.getParameter("select");
			if (name != null) {
				control.selectNode(name);
			}

		}

	}

	public void cleanCacheIfNeed() throws MappingException {
		ServletContext context = getServlet().getServletContext();
		String lastUpdate = (String)context.getAttribute(SmartCacheManager.ION_LAST_UPDATE_ATT);
		String lastUpdateInternal = (String)context.getAttribute(ION_LAST_UPDATE_ATT_INTERNAL);
		if (lastUpdate==null || lastUpdateInternal==null || !lastUpdate.equals(lastUpdateInternal)) {

			Logger.getInstance().log("Remove cache ", BaseAction.class);

			Mapping.getInstance().expireCache();
			context.setAttribute(ION_LAST_UPDATE_ATT_INTERNAL, lastUpdate);
		}
	}

	protected void saveKeyError(HttpServletRequest request, String p_KeyError) {
	    	ActionErrors errors = new ActionErrors();
	    	ActionError err=new ActionError(p_KeyError);
	    	errors.add(p_KeyError,err);
	        saveErrors(request, errors);
	 }
}