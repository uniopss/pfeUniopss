package org.nextime.ion.backoffice.action.data;

import javax.servlet.http.HttpServletRequest;

import org.nextime.ion.framework.logger.Logger;

public abstract class BaseDataProduitAction extends BaseDataAction {

	public abstract String getType();

	public void prepare(HttpServletRequest request) {
		String fm = request.getParameter("fm");
		try {
			if (request.getParameter("fm_add") != null
					&& !"".equals(request.getParameter("fm_add"))) {
				java.io.File _d = new java.io.File(
						org.nextime.ion.framework.config.Config.getInstance()
								.getProduitDirectory(), request
								.getParameter("fm_add"));
				_d.mkdir();
				fm = request.getParameter("fm_add");
			} else if (request.getParameter("fm_ren") != null
					&& !"".equals(request.getParameter("fm_ren"))) {
				java.io.File _d = new java.io.File(
						org.nextime.ion.framework.config.Config.getInstance()
								.getProduitDirectory(), fm);
				_d.renameTo(new java.io.File(
						org.nextime.ion.framework.config.Config.getInstance()
								.getProduitDirectory(), request
								.getParameter("fm_ren")));
				fm = request.getParameter("fm_ren");
			} else if (request.getParameter("fm_del") != null
					&& !"".equals(request.getParameter("fm_del"))) {
				java.io.File _d = new java.io.File(
						org.nextime.ion.framework.config.Config.getInstance()
								.getProduitDirectory(), request
								.getParameter("fm_del"));
				java.io.File _f = getFile(request.getParameter("fm_del"));
				if (_f.exists())
					_f.delete();
				java.io.File[] _fs = _d.listFiles();
				if (_fs.length == 0)
					_d.delete();
				fm = null;
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), BaseDataProduitAction.class,e);
		}
		java.io.File[] _f = org.nextime.ion.framework.config.Config
				.getInstance().getProduitDirectory().listFiles();
		java.util.Vector v = new java.util.Vector();
		for (int i = 0; i < _f.length; i++)
			if (_f[i].isDirectory())
				v.add(_f[i].getName());
		if (!v.isEmpty()) {
			request.setAttribute("famille", v);
			if (fm == null || "".equals(fm))
				fm = (String) v.get(0);
			request.setAttribute("fm", fm);
		}
	}

	public java.io.File getFile(HttpServletRequest request) {
		if (request.getAttribute("fm") != null
				&& !"".equals(request.getAttribute("fm")))
			return getFile(request.getAttribute("fm") + "");
		return null;
	}

	public java.io.File getFile(String f) {
		if (f != null && !"".equals(f))
			return new java.io.File(new java.io.File(
					org.nextime.ion.framework.config.Config.getInstance()
							.getProduitDirectory(), f), getType() + ".xml");
		return null;
	}

	public String getXml(HttpServletRequest request, String name) {
		return "<d>" + request.getParameter("d_" + name) + "</d>";
	}

}