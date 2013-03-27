package org.nextime.ion.osworkflow.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowLoader;
import com.opensymphony.workflow.loader.XMLWorkflowFactory;

public class WorkflowFactory extends XMLWorkflowFactory { // AbstractWorkflowFactory {

	private static final Log log = LogFactory.getLog(WorkflowFactory.class);

	private Map workflows;

	class WorkflowConfig {
		public WorkflowConfig(String type, String location) {
			this.type = type;
			this.location = location;
		}

		String type, location;
	}

	public WorkflowDescriptor getWorkflow(String name) {
		WorkflowConfig c = (WorkflowConfig) workflows.get(name);
		if (c == null) {
			throw new RuntimeException("Unknown workflow name");
		}

		File configRoot = new File(Config.getInstance()
				.getConfigDirectoryPath());

		try {
			if (c.type.equalsIgnoreCase("URL")) {
				// todo wf
				// return WorkflowLoader.load(new URL(c.location));
				return WorkflowLoader.load(new URL(c.location), false);
			} else if (c.type.equalsIgnoreCase("file")) {
				String location= new File(configRoot, c.location).getAbsolutePath();
				// todo wf
				// return WorkflowLoader.load(new File(c.location));
				return WorkflowLoader.load(new FileInputStream(location), false);
			} else {
				// return WorkflowLoader.load(c.location);
				return null;
			}
		} catch (Exception e) {
			log.fatal("Error creating workflow descriptor", e);
			throw new RuntimeException("Error in workflow descriptor: "
					+ e.getMessage());
		}
	}

	public void initDone() throws FactoryException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = null;
		try {
			is = new FileInputStream(new File(Config.getInstance()
					.getConfigDirectoryPath(), "workflows.xml"));
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), WorkflowFactory.class,e);
		}

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = null;

			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				log.fatal("Error creating document builder", e);
			}

			Document doc = db.parse(is);

			Element root = (Element) doc.getElementsByTagName("workflows").item(0);
			workflows = new HashMap();
			NodeList list = root.getElementsByTagName("workflow");
			for (int i = 0; i < list.getLength(); i++) {
				Element e = (Element) list.item(i);
				workflows.put(e.getAttribute("name"), new WorkflowConfig(e
						.getAttribute("type"), e.getAttribute("location")));
			}
		} catch (Exception e) {
			log.fatal("Error reading xml workflow", e);
			throw new RuntimeException("Error in workflow config: "
					+ e.getMessage());
		}
	}

	public String[] listAllNames() {
		Set keys = workflows.keySet();
		Object[] array = keys.toArray();
		String[] retour = new String[array.length];
		for (int i = 0; i < retour.length; i++) {
			retour[i] = array[i] + "";
		}
		return retour;
	}
}