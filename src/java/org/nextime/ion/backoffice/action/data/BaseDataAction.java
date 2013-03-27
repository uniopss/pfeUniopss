package org.nextime.ion.backoffice.action.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.logger.Logger;

public abstract class BaseDataAction extends BaseAction {

	public abstract java.io.File getFile(HttpServletRequest request);

	public abstract String getXml(HttpServletRequest request, String name);

	public void execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String laction = request.getParameter("laction");
		String lid = request.getParameter("lid");
		java.util.Vector ps = new java.util.Vector();
		java.util.Enumeration ep = request.getParameterNames();
		int max = 0;
		while (ep.hasMoreElements()) {
			String name = ep.nextElement() + "";
			if (name.startsWith("r_")
					&& !(name.equals(lid) && "s".equals(laction))) {
				int zN = Integer.parseInt(request.getParameter(name));
				while (zN >= ps.size())
					ps.add("");
				ps.set(zN, name);
				int n = Integer.parseInt(name.substring(2));
				if (n >= max)
					max = n + 1;
			}
		}
		int zId = 0;
		if (lid != null && !"".equals(lid))
			zId = ps.indexOf(lid);
		try {
			if ("n".equals(laction)) {
				ps.add("r_" + max);
			} else if ("a".equals(laction)) {
				ps.add(1 + zId, "r_" + max);
			} else if ("u".equals(laction) && zId > 0) {
				ps.removeElementAt(zId);
				ps.add(zId - 1, lid);
			} else if ("d".equals(laction) && zId < ps.size() - 1) {
				ps.removeElementAt(zId);
				ps.add(zId + 1, lid);
			} else if ("t".equals(laction)) {
				int j = -1;
				String sXml = "";
				for (int i = 0; i < ps.size(); i++) {
					String name = ps.get(i) + "";
					if (name != null && !"".equals(name)) {
						j++;
						sXml += "<r ordre='" + j + "'>" + getXml(request, name)
								+ "</r>\n";
					}
				}
				save(request, sXml);
			}

		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BaseDataAction.class,e);
		}
		request.setAttribute("n", "" + ps.size());
		request.setAttribute("ps", ps);

	}

	public synchronized void save(HttpServletRequest request, String x) {
		try {
			java.io.File f = getFile(request);
			java.io.File d = new java.io.File(f.getParent());
			if (!d.exists())
				d.mkdir();
			java.io.PrintStream os = new java.io.PrintStream(
					new java.io.FileOutputStream(f));
			os.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><lc>"
					+ x + "</lc>");
			os.close();

		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BaseDataAction.class,e);
		}
	}

	public String load(HttpServletRequest request) {
		String content = "";
		try {
			java.io.File f = getFile(request);
			content = new String(org.nextime.ion.commons.Util.read(f));
			if (content == null || "".equals(content.trim()))
				content = "<lc/>";

		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BaseDataAction.class,e);
			return "<lc/>";
		}
		return content;
	}

	public void prepare(HttpServletRequest request) {
	}

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));
		prepare(request);
		// fill data
		String laction = request.getParameter("laction");
		if (laction == null || "".equals(laction)) {
			try {
				String content = load(request);
				request.setAttribute("content", content.trim());
				request.setAttribute("laction", "c");

			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), BaseDataAction.class,e);
				request.setAttribute("content", "<lc/>");
			}
			return (mapping.findForward("view"));
		}

		execute(mapping, form, request, response);

		// Forward to the next page
		return (mapping.findForward("view"));

	}

}