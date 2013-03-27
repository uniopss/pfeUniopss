package org.nextime.ion.framework.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Persistent;
import org.jdom.Document;
import org.jdom.Element;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.framework.workflow.Workflow;
import org.nextime.ion.framework.workflow.WorkflowStep;
import org.nextime.ion.framework.xml.XML;

/**
 * @author gbort
 */
public class PublicationVersionImpl extends PublicationVersion implements
		Persistent, Comparable {

	private long _workflowId = -1;

	private Publication _publication;

	private int _version;

	private Document _data;

	private String _id;

	private User _author;

	private static Vector _listeners = new Vector();

	// l'object sert uniquement aux traitements, et ne fait pas partie des objects persistant
	private Object _transientObject=null;

	public PublicationVersionImpl() {
	}

	public void remove() throws MappingException {
		try {
			Mapping.getInstance().getDb().remove(this);
			Logger.getInstance().log(
					"L'objet PublicationVersion pour l'id " + getId()
							+ " à été detruit.", Publication.class);
		} catch (PersistenceException e) {
			String message = "Impossible de detruire l'objet PublicationVersion pour l'id "
					+ getId() + ".";
			Logger.getInstance().error(message, this, e);
			throw new MappingException(message);
		}
	}

	public byte[] getDataBytes() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(_data);
		byte[] result = os.toByteArray();
		os.close();
		return result;
	}

	public void setDataBytes(byte[] value) throws Exception {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(value);
			ObjectInputStream ois = new ObjectInputStream(is);
			Object o = ois.readObject();
			is.close();
			_data = (Document) o;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationVersionImpl.class,e);
			try {
				_data = getPublication().getType().getModel();
			} catch (Exception ee) {
				// TODO
				Logger.getInstance().error(ee.getMessage(), PublicationVersionImpl.class,ee);
			}
		}
	}

	public void setData(Document newData) {
		beforeChange();
		_data = newData;
		((PublicationImpl) getPublication()).postChange();
		postChange();
	}

	public void setDataAsString(String newData) throws Exception {
		beforeChange();
		try {
			try {
				_data = XML.getInstance().getDocument(newData);
			} catch (Exception ee) {
				// TODO
				Logger.getInstance().error(ee.getMessage(), PublicationVersionImpl.class,ee);
				// dans le cas d'erreur, on change le data avec un modèle vide
				_data = getPublication().getType().getModel();
			}
			((PublicationImpl) getPublication()).postChange();
			postChange();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationVersionImpl.class,e);
		}
	}

	public void resetData() {
		_data = getPublication().getType().getModel();
		((PublicationImpl) getPublication()).postChange();
	}

	public Document getData() {
		return _data;
	}

	public String getDataAsString() {
		String s = "";
		try {
			s = XML.getInstance().getStringWithoutFormat(_data);
		} catch (Exception e) {
			s = XML.getInstance().getStringWithoutFormat(getPublication().getType().getModel());
		}
		return s;
	}

	public int getVersion() {
		return _version;
	}

	public void setVersion(int ver) {
		_version = ver;
	}

	public Workflow getWorkflow(User user) {
		if (getWorkflowId() == -1) {
			return null;
		}
		return Workflow.getInstance(getWorkflowId(), user);
	}

	public void setWorkflow(Workflow wf) {
		setWorkflowId(wf.getId());
	}

	// méthodes non publiées

	public Workflow getWorkflow() {
		if (getWorkflowId() == -1) {
			return null;
		}
		return Workflow.getInstance(getWorkflowId(), getAuthor());
	}

	public long getWorkflowId() {
		return _workflowId;
	}

	public void setWorkflowId(long id) {
		_workflowId = id;
	}

	public Publication getPublication() {
		return _publication;
	}

	public void setPublication(Publication p) {
		_publication = p;
	}

	public String getId() {
		return _id;
	}

	public void setId(String p) {
		_id = p;
	}

	public User getAuthor() {
		return _author;
	}

	public void setAuthor(User p) {
		_author = p;
		((PublicationImpl) getPublication()).postChange();
	}

	public String toString() {
		return "version " + getVersion() + " de la publication "
				+ getPublication().getId();
	}

	public int compareTo(Object o) {
		try {
			PublicationVersion p = (PublicationVersion) o;
			if (!p.getPublication().equals(this.getPublication())) {
				throw new Exception();
			}
			if (p.getVersion() < this.getVersion())
				return -1;
			return 1;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationVersionImpl.class,e);
			return 0;
		}
	}

	public String getXml() {
		return XML.getInstance().getString(getXmlDoc());
	}

	public String getXml(String language) {
		try {
			Document multiLanguage = getXmlDoc();
			multiLanguage.getRootElement().setAttribute("xml:lang", language);
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream(
							"org/nextime/ion/framework/tools/langFilter.xsl");
			Document filter = XML.getInstance().readWithoutValidation(is);
			Hashtable ht = new Hashtable();
			ht.put("lang", language);
			return XML.getInstance().transform(multiLanguage, filter, ht);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), PublicationVersionImpl.class,e);
			return e.getMessage();
		}
	}

	public Document getXmlDoc(String language) {
		try {
			return XML.getInstance().getDocument(getXml(language));
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), this, e);
			return null;
		}
	}

	public Document getXmlDoc() {

		//************* AUTHOR ********************//
		Element author = new Element("author");
		// set the login attribute
		author.setAttribute("login", getAuthor().getLogin());
		// list metaData
		Enumeration mdks = getAuthor().getMetaData().keys();
		while (mdks.hasMoreElements()) {
			String key = mdks.nextElement() + "";
			Element metaData = new Element("metaData");
			metaData.setAttribute("name", key);
			metaData.addContent(getAuthor().getMetaData(key).toString());
			author.addContent(metaData);
		}

		//************* DATA ********************//
		Element data = new Element("data");
		// set the data content
		data.addContent((Element) (getData().getRootElement().clone()));

		//************* METADATA ********************//
		Element metaDatas = new Element("metaDatas");
		// list metaData
		mdks = getPublication().getMetaData().keys();
		while (mdks.hasMoreElements()) {
			String key = mdks.nextElement() + "";
			Element metaData = new Element("metaData");
			metaData.setAttribute("name", key);
			metaData.addContent(getPublication().getMetaData(key).toString());
			metaDatas.addContent(metaData);
		}

		//************* SECTIONS ********************//
		Element sections = new Element("sections");
		// list sections
		Vector v = getPublication().listSections();
		for (int i = 0; i < v.size(); i++) {
			Section s = (Section) v.get(i);
			Element section = new Element("section");
			section.setAttribute("id", s.getId());
			if (s.getParent() != null) {
				section.setAttribute("pid", s.getParent().getId());
				section.setAttribute("lid", ""
						+ s.getParent().getMetaData("name"));
				section.setAttribute("path", s.getPath());
				section.setAttribute("pathLabel", s.getPathLabel());
			}
			// list metaData
			mdks = s.getMetaData().keys();
			while (mdks.hasMoreElements()) {
				String key = mdks.nextElement() + "";
				Element metaData = new Element("metaData");
				metaData.setAttribute("name", key);
				metaData.addContent(s.getMetaData(key).toString());
				section.addContent(metaData);
			}
			sections.addContent(section);
		}

		//************* PUBLICATION ********************//
		Element root = new Element("ionPublication");
		// set the id attribute
		root.setAttribute("id", getPublication().getId());
		// set the version attribute
		root.setAttribute("version", getVersion() + "");
		// set the date attribute
		root.setAttribute("date", getPublication().getFormatedDate());
		// set the type attribute
		root.setAttribute("type", getPublication().getType().getId());
		// set the root attribute
		root.setAttribute("root", getPublication().getRootSection());


		WorkflowStep currentStep = (WorkflowStep) (getWorkflow().getCurrentSteps().firstElement());

		// set the State attribute
		root.setAttribute("workflowState", currentStep.getName());

		Vector histories = getWorkflow().getHistorySteps();
		WorkflowStep creationStep = currentStep;
		if (histories !=null && histories.size()>0) {
			creationStep = (WorkflowStep)histories.lastElement();
		}
		// set the create date attribute
		root.setAttribute("dateCreation", creationStep.getStartDate());

		// set the author element
		root.addContent(author);
		// set the data element
		root.addContent(data);
		// set the metadata element
		root.addContent(metaDatas);
		// set the sections element
		root.addContent(sections);

		Document doc = new Document(root);
		return doc;
	}

	// heritées de Persitent

	public void jdoPersistent(Database db) {
	}

	public void jdoTransient() {
	}

	private void callObjectLoad() {
		WcmEvent event = new WcmEvent(this, getId());
		for (int i = 0; i < _listeners.size(); i++) {
			try {
				((WcmListener) _listeners.get(i)).objectLoaded(event);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			}
		}
	}

	public Class jdoLoad(short accessMode) {
		callObjectLoad();
		return null;
	}
	public Class jdoLoad(org.exolab.castor.mapping.AccessMode am) {
		callObjectLoad();
		return null;
	}

	public void jdoBeforeCreate(Database db) {
	}

	public void jdoAfterCreate() {
		if (getPublication() != null)
			((PublicationImpl) getPublication()).jdoAfterCreate();
	}


	private void beforeChange() {
		WcmEvent event = new WcmEvent(this, getId());
		for (int i = 0; i < _listeners.size(); i++) {
			try {
				((WcmListener) _listeners.get(i)).objectBeforeChange(event);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			}
		}
	}

	private void postChange() {
		WcmEvent event = new WcmEvent(this, getId());
		for (int i = 0; i < _listeners.size(); i++) {
			try {
				((WcmListener) _listeners.get(i)).objectPostChange(event);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			}
		}
	}

	public void jdoStore(boolean modified) {
	}

	public void jdoBeforeRemove() {
		WcmEvent event = new WcmEvent(this, getId());
		for (int i = 0; i < _listeners.size(); i++) {
			try {
				((WcmListener) _listeners.get(i)).objectBeforeRemove(event);
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), PublicationImpl.class,e);
			}
		}
	}

	public void jdoAfterRemove() {
	}

	public void jdoUpdate() {
	}

	public static void addListener(WcmListener listener) {
		_listeners.add(listener);
	}

	public static void removeListener(WcmListener listener) {
		_listeners.remove(listener);
	}

	public Object getTransientObject() {
		return _transientObject;
	}

	public void setTransientObject(Object p_Object) {
		_transientObject = p_Object;
	}



}
