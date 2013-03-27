package org.nextime.ion.framework.workflow;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.osworkflow.loader.WorkflowFactory;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.spi.Step;

public class Workflow {

	private AbstractWorkflow aworkflow;

	private long id;

	private static String[] types;

	private Workflow(long id, AbstractWorkflow aw) {
		setId(id);
		setOSWorkflow(aw);
	}

	public static void init() {
		try {
			//BasicWorkflow bwf = new BasicWorkflow("smartIonFramework");
			BasicWorkflow bwf = new BasicWorkflow("visiteurAnonyme");
			bwf.getCurrentSteps(0);
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), Workflow.class,e);
		}
	}

	public static void reInitV(String l, String m) {
		reInitV(l, m, 13);
	}

	public static void reInitV(String l, String m, int action) {
		try {
			String s = Config.getInstance().getWorkflowStore();
			if (s != null && !"".equals(s)) {
				if (!"3".equals(m)) {
					java.io.File f = new java.io.File(s);
					if (f.exists()) {
						if (f.isFile())
							f.delete();
						else {
							java.io.FilenameFilter filter = new java.io.FilenameFilter() {
								public boolean accept(java.io.File dir,
										String name) {
									return name.matches(".+\\.wf$");
								}
							};
							File[] files = f.listFiles();
							if (files != null) {
								for (int i = 0; i < files.length; i++) {
									files[i].delete();
								}
							}
						}
					}
					org.nextime.ion.framework.business.PublicationVersion
							.reInitWfAll(action);
				}
				if (!"1".equals(m)) {
					try {
						java.util.Vector result = org.nextime.ion.framework.helper.Searcher
								.search("brouillon édition attente", l,
										"workflow");
						if (result != null) {
							for (int i = 0; i < result.size(); i++) {
								try {
									org.nextime.ion.framework.helper.SearchResult r = (org.nextime.ion.framework.helper.SearchResult) result
											.get(i);
									org.nextime.ion.framework.business.PublicationVersion pv = r
											.getPublicationVersion();
									pv.reInitWf(-1);
								} catch (Exception ee) {
									// TODO
									Logger.getInstance().error(ee.getMessage(), Workflow.class,ee);
								}

							}
						}
					} catch (Exception e) {
						// TODO
						Logger.getInstance().error(e.getMessage(), Workflow.class,e);
					}

				}
			}
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), e, e.getCause());
		}
	}

	public static Workflow getInstance(long id, User user) {
		BasicWorkflow bwf = new BasicWorkflow(user.getLogin());
		Workflow wf = new Workflow(id, bwf);
		return wf;
	}

	public static Workflow getInstance(long id, String user) {
		BasicWorkflow bwf = new BasicWorkflow(user);
		Workflow wf = new Workflow(id, bwf);
		return wf;
	}

	public static Workflow create(User user, String type) throws Exception {
		return create(user, type, null);
	}

	public static Workflow create(User user, String type, Map inputs)
			throws Exception {
		BasicWorkflow bwf = new BasicWorkflow(user.getLogin());
		// todo wf
		/*
		long id = bwf.createEntry(type);
		Workflow wf = new Workflow(id, bwf);
		bwf.initialize(id, 1, inputs);
		*/
		long id=bwf.initialize(type, 1, inputs);
		Workflow wf = new Workflow(id, bwf);
		return wf;
	}

	public static void remove(long id) {
	}

	public String getWorkflowType() {
		return aworkflow.getWorkflowName(id);
	}

	public WorkflowAction[] getAvailableActions() {

		HashMap inputMap=new HashMap();
		int[] actions = aworkflow.getAvailableActions(getId(), inputMap);
		WorkflowAction[] wactions = new WorkflowAction[actions.length];
		for (int i = 0; i < actions.length; i++) {
			wactions[i] = new WorkflowAction(this, actions[i]);
		}
		return wactions;
	}

	public WorkflowAction getAction(int id) {
		return new WorkflowAction(this, id);
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	protected void setOSWorkflow(AbstractWorkflow wf) {
		this.aworkflow = wf;
	}

	protected AbstractWorkflow getOSWorkflow() {
		return aworkflow;
	}

	public static String[] listTypes() {
		try {
			if (types == null) {
				WorkflowFactory wff = new WorkflowFactory();
				wff.initDone();
				types = wff.listAllNames();
			}
			return types;
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), Workflow.class,e);
			return null;
		}
	}

	public Vector getPermissions() {
		List list = aworkflow.getSecurityPermissions(getId());
		Vector retour = new Vector();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			retour.add(it.next());

		}
		return retour;
	}

	public Hashtable getMetadatas() {
		Hashtable ht = new Hashtable();
		// todo
		// BasicWorkflow mbwf = new BasicWorkflow("smartIonFramework");
		BasicWorkflow mbwf = new BasicWorkflow("visiteurAnonyme");
		List list = mbwf.getSecurityPermissions(getId());
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String p = it.next() + "";
			if (p.indexOf(":") != -1) {
				String key = p.substring(0, p.indexOf(":"));
				String value = p.substring(p.indexOf(":") + 1);
				ht.put(key, value);
			}
		}
		return ht;
	}

	public PropertySet getVariables() {
		// todo wf
		// return aworkflow.getVariableMap(getId());
		return aworkflow.getPropertySet(getId());
	}

	public Vector getCurrentSteps() {
		Vector retour = new Vector();
		List list = aworkflow.getCurrentSteps(getId());
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Step step = (Step) it.next();
			retour.add(new WorkflowStep(this, step));
		}
		return retour;
	}

	public Vector getHistorySteps() {
		Vector retour = new Vector();
		List list = aworkflow.getHistorySteps(getId());
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Step step = (Step) it.next();
			retour.add(new WorkflowStep(this, step));
		}
		return retour;
	}

	public Vector getSteps() throws FactoryException {
		Vector retour = new Vector();

		// todo wf
		//List list = ConfigLoader.getWorkflow(getWorkflowType()).getSteps();
		List list = DefaultConfiguration.INSTANCE.getWorkflow(getWorkflowType()).getSteps();

		Iterator it = list.iterator();
		while (it.hasNext()) {
			Step step = (Step) it.next();
			retour.add(new WorkflowStep(this, step));
		}
		return retour;
	}

}