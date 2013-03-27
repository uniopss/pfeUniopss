package org.nextime.ion.backoffice.tree;

import java.util.*;
import java.io.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionServlet;
import org.nextime.ion.framework.business.*;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.*;
import org.nextime.ion.framework.config.*;

public class WcmTreeBuilder implements TreeBuilder {

	/**
	 * @see org.nextime.ion.backoffice.tree.TreeBuilder#buildTree(TreeControl,
	 *      ActionServlet, HttpServletRequest)
	 */
	public void buildTree(TreeControl treeControl, ActionServlet servlet,
			HttpServletRequest request) {

		TreeControlNode root = treeControl.getRoot();

		try {
			Mapping.begin();
			Vector roots = (java.util.Vector) request.getSession()
					.getAttribute("currentSite");
			for (int i = 0; i < roots.size(); i++) {
				Section section = (Section) roots.get(i);
				String id = section.getId() + ".node";
				TreeControlNode node = null;
				//TreeControlNode node = load(id);
				if (node == null) {
					node = buildNode(section);
					root.addChild(node);
					//if( i==0 ) treeControl.selectNode(section.getId());
					if (org.nextime.ion.framework.cache.Util.check())
						recursive(section.getId(), node);
					else
						recursive(section, node);
					//save(id, node);
				} else
					root.addChild(node);
			}
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), WcmTreeBuilder.class,e);
		} finally {
			Mapping.rollback();
		}
	}

	private static TreeControlNode load(String id) {
		TreeControlNode node = null;
		if (Config.getInstance().getHtmlCacheDirectory() != null) {
			try {
				File possibleCache = new File(Config.getInstance()
						.getHtmlCacheDirectory(), id);
				if (possibleCache.exists()) {
					FileInputStream fis = new FileInputStream(possibleCache);
					ObjectInputStream ois = new ObjectInputStream(fis);
					node = (TreeControlNode) ois.readObject();
					fis.close();
				}
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), WcmTreeBuilder.class,e);
			}
		}
		return node;
	}

	private static synchronized void save(String id, TreeControlNode node) {
		if (Config.getInstance().getHtmlCacheDirectory() != null) {
			try {
				File possibleCache = new File(Config.getInstance()
						.getHtmlCacheDirectory(), id);
				FileOutputStream fos = new FileOutputStream(possibleCache);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(node);
				fos.close();
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), WcmTreeBuilder.class,e);
			}
		}
	}

	public static TreeControlNode buildNode(Section section) {
		return buildNode(section.getId(), section.getMetaData("status"),
				section.getMetaData("name"));
	}

	public static TreeControlNode buildNode(String id, Object status,
			Object name) {
		String img = "section.gif";
		if ("offline".equals(status)) {
			img = "section-offline.gif";
		}
		TreeControlNode node = new TreeControlNode(id, img,
				(name != null && !"".equals(name)) ? name + "" : id,
				"lcTree_2('" + id + "')", "", false);
		return node;
	}

	private void recursive(Section section, TreeControlNode node) {
		Vector childs = section.listSubSections();
		for (int i = 0; i < childs.size(); i++) {
			Section tsection = (Section) childs.get(i);
			TreeControlNode tnode = buildNode(tsection);
			try {
				node.addChild(tnode);
				recursive(tsection, tnode);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), WcmTreeBuilder.class,e);
			}
		}
	}

	private static String[] read(String id) {
		String[] i = new String[2];
		Hashtable res = TreeCache.load(id);
		try {
			i[0] = (String) res.get("status");
			i[1] = (String) res.get("name");
		} catch (Exception ee) {
			// TODO
			Logger.getInstance().error(ee.getMessage(), WcmTreeBuilder.class,ee);
			try {
				if (id != null && !"".equals(id)) {
					Section s = Section.getInstance(id);
					TreeCache.write(s);
					i[0] = (String) s.getMetaData("status");
					i[1] = (String) s.getMetaData("name");
				}
			} catch (Exception eeee) {
				// TODO
				Logger.getInstance().error(eeee.getMessage(), WcmTreeBuilder.class,eeee);
			}
		}
		return i;
	}

	private void recursive(String id, TreeControlNode node) {
		java.util.Hashtable res = TreeCache.load(id);
		try {
			String child = (String) res.get("child");
			if (!"".equals(child.trim())) {
				String[] childs = child.split(",");
				for (int i = 0; i < childs.length; i++) {
					String[] info = read(childs[i]);
					TreeControlNode tnode = buildNode(childs[i], info[0],
							info[1]);
					try {
						node.addChild(tnode);
						recursive(childs[i], tnode);
					} catch (Exception e) {
						// TODO
						Logger.getInstance().error(e.getMessage(), WcmTreeBuilder.class,e);
					}
				}
			}
		} catch (Exception ee) {
			try {
				if (id != null && !"".equals(id)) {
					Section s = Section.getInstance(id);
					TreeCache.write(s);
					Vector childs = s.listSubSections();
					for (int i = 0; i < childs.size(); i++) {
						Section tsection = (Section) childs.get(i);
						TreeControlNode tnode = buildNode(tsection);
						try {
							node.addChild(tnode);
							recursive(tsection.getId(), tnode);
						} catch (Exception eee) {
							// TODO
							Logger.getInstance().error(eee.getMessage(), WcmTreeBuilder.class,eee);

						}
					}
				}
			} catch (Exception eeee) {
				// TODO
				Logger.getInstance().error(eeee.getMessage(), WcmTreeBuilder.class,eeee);
			}
		}

	}
}