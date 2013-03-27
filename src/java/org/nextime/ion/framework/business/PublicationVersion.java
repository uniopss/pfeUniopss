package org.nextime.ion.framework.business;

import java.util.Hashtable;
import java.util.*;
import org.jdom.Document;
import org.nextime.ion.framework.workflow.Workflow;
import org.nextime.ion.framework.workflow.WorkflowAction;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.PublicationVersionImpl;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;


/**
 * @author gbort
 */
public abstract class PublicationVersion {

	public static PublicationVersion getInstance(String id)
			throws MappingException {
		try {
			PublicationVersion u = (PublicationVersion) Mapping.getInstance()
					.getDb().load(PublicationVersionImpl.class, id);
			Logger.getInstance().log(
					"Une instance de l'objet PublicationVersion pour l'id "
							+ id + " à été chargée.", Publication.class);
			return u;
		} catch (PersistenceException e) {
			String message = "Impossible de charger une instance de l'objet PublicationVersion pour l'id "
					+ id + ".";
			Logger.getInstance().error(message, PublicationVersion.class, e);
			throw new MappingException(message);
		}
	}

	public abstract void setData(Document newData);

	public abstract void setDataAsString(String newData) throws Exception;

	public abstract void resetData();

	public abstract Document getData();

	public abstract String getDataAsString();

	public abstract Workflow getWorkflow(User user);

	public abstract Workflow getWorkflow();

	public abstract void setWorkflow(Workflow wf);

	public abstract Publication getPublication();

	public abstract int getVersion();

	public abstract String getXml();

	public abstract String getXml(String language);

	public abstract User getAuthor();

	public abstract void setAuthor(User author);

	public abstract String getId();

	public static void reInitWfAll() throws Exception {
		reInitWfAll(-1);
	}

	public static void reInitWfAll(int action) throws Exception {
		Vector v = listAll();
		for (int i = 0; i < v.size(); i++) {
			PublicationVersion pv = (PublicationVersion) v.get(i);
			pv.reInitWf(action);
		}
	}

	public void reInitWf(int action) throws Exception {
		User u = this.getAuthor();
		Hashtable ht = new Hashtable();
		ht.put("author", u.getLogin());
		ht.put("id", this.getPublication().getId());
		ht.put("version", this.getVersion() + "");
		Workflow wf = Workflow.create(u, this.getPublication()
				.getWorkflowType(), ht);
		this.setWorkflow(wf);
		if (action > 0) {
			WorkflowAction wA = this.getWorkflow(u).getAction(action);
			wA.execute();
		}
	}

	public static Vector listAll() throws MappingException {
		Vector v = new Vector();
		try {
			OQLQuery oql = Mapping
					.getInstance()
					.getDb()
					.getOQLQuery(
							"SELECT p FROM org.nextime.ion.framework.business.impl.PublicationVersionImpl p ");
			QueryResults results = oql.execute(org.exolab.castor.jdo.Database.ReadOnly);
			while (results.hasMore()) {
				v.add(results.next());
			}
		} catch (Exception e) {
			Logger.getInstance().error(
					"erreur lors du listAll de PublicationVersion",
					Publication.class, e);
			throw new MappingException(e.getMessage());
		}
		return v;
	}
}