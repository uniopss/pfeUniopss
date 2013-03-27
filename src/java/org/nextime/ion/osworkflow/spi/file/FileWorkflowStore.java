package org.nextime.ion.osworkflow.spi.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nextime.ion.framework.config.Config;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.memory.MemoryWorkflowStore;

/**
 * Simple flat file implementation.
 * 
 * Following properties are <b>required </b>:
 * <ul>
 * <li><b>storeFile </b>- the absolute path to the store file (
 * <i>ex:c:\workflow.store </i>)</li>
 * </ul>
 * 
 * @author <a href="mailto:gbort@msn.com">Guillaume Bort </a>
 */
public class FileWorkflowStore  extends MemoryWorkflowStore implements WorkflowStore {
	protected static final Log log = LogFactory.getLog(FileWorkflowStore.class);

	static String storeFile;

	public void init(Map props) {
		String temp = (String) props.get("storeRelativePath");
		storeFile = new File(Config.getInstance().getConfigDirectoryPath(),
				temp).getAbsolutePath();
		Config.getInstance().setWorkflowStore(storeFile);
		// check whether the file denoted by the storeFile property is a normal
		// file.
		if (!new File(storeFile).isFile()) {
			log.fatal("storePath property should indicate a normal file");
		}
		// check wheter the directory containing the storeFile exist
		if (!new File(storeFile).getParentFile().exists()) {
			log.fatal("directory " + new File(storeFile).getParent()
					+ " not found");
		}
	}

	public WorkflowEntry createEntry(String workflowName) {
		long id = SerializableCache.getInstance().globalEntryId++;
		// todo wf
		// SimpleWorkflowEntry entry = new SimpleWorkflowEntry(id, workflowName, false);
		SimpleWorkflowEntry entry = new SimpleWorkflowEntry(id, workflowName, WorkflowEntry.CREATED); // state 
		SerializableCache.getInstance().entryCache.put(new Long(id), entry);
		SerializableCache.getInstance().store();
		return entry;
	}

	public WorkflowEntry findEntry(long entryId) {
		return (WorkflowEntry) SerializableCache.getInstance().entryCache
				.get(new Long(entryId));
	}

	public WorkflowEntry initialize(WorkflowEntry entry) {
		SimpleWorkflowEntry theEntry = (SimpleWorkflowEntry) entry;
		theEntry.setInitialized(true);
		SerializableCache.getInstance().store();
		return theEntry;
	}

	public List findCurrentSteps(long entryId) {
		List currentSteps = (List) SerializableCache.getInstance().currentStepsCache
				.get(new Long(entryId));
		if (currentSteps == null) {
			currentSteps = new ArrayList();
			SerializableCache.getInstance().currentStepsCache.put(new Long(entryId), currentSteps);
		}

		return currentSteps;
	}

	public List findHistorySteps(long entryId) {
		List historySteps = (List) SerializableCache.getInstance().historyStepsCache.get(new Long(entryId));
		if (historySteps == null) {
			historySteps = new ArrayList();
			SerializableCache.getInstance().historyStepsCache.put(new Long(
					entryId), historySteps);
		}

		return historySteps;
	}

	
	public Step createCurrentStep(long entryId, int stepId, String owner,
			Date startDate, Date dueDate, String status, long[] previousIds) {
		long id = SerializableCache.getInstance().globalStepId++;
		// todo wf
		// SimpleStep step = new SimpleStep(id, entryId, stepId, 0, owner, startDate, null, status, previousIds);
		SimpleStep step = new SimpleStep(id, entryId, stepId, 0, owner, startDate, null, null, status, previousIds, null); // caller 
		// SimpleStep step = SimpleStep(long id, long entryId, int stepId, int actionId, String owner, Date startDate, Date dueDate, Date finishDate, String status, long[] previousStepIds, String caller) {

		List currentSteps = (List) SerializableCache.getInstance().currentStepsCache.get(new Long(entryId));
		if (currentSteps == null) {
			currentSteps = new ArrayList();
			SerializableCache.getInstance().currentStepsCache.put(new Long(entryId), currentSteps);
		}
		currentSteps.add(step);
		SerializableCache.getInstance().store();
		return step;
	}

	// todo : a changer
	public void moveToHistory(int actionId, Date finishDate, Step step,
			String status) {
		List currentSteps = (List) SerializableCache.getInstance().currentStepsCache.get(new Long(step.getEntryId()));

		List historySteps = (List) SerializableCache.getInstance().historyStepsCache.get(new Long(step.getEntryId()));
		if (historySteps == null) {
			historySteps = new ArrayList();
			SerializableCache.getInstance().historyStepsCache.put(new Long(step.getEntryId()), historySteps);
		}

		SimpleStep simpleStep = (SimpleStep) step;
		for (Iterator iterator = currentSteps.iterator(); iterator.hasNext();) {
			Step currentStep = (Step) iterator.next();
			if (simpleStep.getId() == currentStep.getId()) {
				iterator.remove();
				simpleStep.setActionId(actionId);
				simpleStep.setFinishDate(finishDate);
				historySteps.add(simpleStep);
				break;
			}
		}
		SerializableCache.getInstance().store();
	}
	public void moveToHistory(Step step) {
		moveToHistory(step.getActionId(), step.getFinishDate(), step, step.getStatus());
	}

	public PropertySet getPropertySet(long entryId) {
		PropertySet ps = (PropertySet) SerializableCache.getInstance().propertySetCache.get(new Long(entryId));
		if (ps == null) {
			ps = PropertySetManager.getInstance("serializable", null);
			SerializableCache.getInstance().propertySetCache.put(new Long(entryId), ps);
		}
		return ps;
	}

	
	public Step markFinished(Step step, int actionId, Date finishDate,
			String status) {
		List currentSteps = (List) SerializableCache.getInstance().currentStepsCache.get(new Long(step.getEntryId()));
		for (Iterator iterator = currentSteps.iterator(); iterator.hasNext();) {
			SimpleStep theStep = (SimpleStep) iterator.next();
			if (theStep.getId() == step.getId()) {
				theStep.setStatus(status);
				theStep.setActionId(actionId);
				theStep.setFinishDate(finishDate);
				return theStep;
			}
		}
		SerializableCache.getInstance().store();
		return null;
	}


	public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller)  {
		return this.markFinished(step, actionId, finishDate, status);
	}
	
	public void setEntryState(long entryId, int state) throws StoreException {
        SimpleWorkflowEntry theEntry = (SimpleWorkflowEntry) findEntry(entryId);
        theEntry.setState(state);
        SerializableCache.getInstance().store();
        
    }
	
	/*
	public List query(WorkflowQuery query)  {
		return null;

	}

	public List query(WorkflowExpressionQuery query)  {
		return null; 
	}
	*/
}

class SerializableCache implements Serializable {
	HashMap entryCache;

	HashMap currentStepsCache;

	HashMap historyStepsCache;

	HashMap propertySetCache;

	long globalEntryId = 1;

	long globalStepId = 1;

	private static transient SerializableCache instance;

	private SerializableCache() {
		entryCache = new HashMap();
		currentStepsCache = new HashMap();
		historyStepsCache = new HashMap();
		propertySetCache = new HashMap();
	}

	static SerializableCache getInstance() {
		if (instance == null) {
			instance = load();
		}
		return instance;
	}

	static SerializableCache load() {
		try {
			FileInputStream fis = new FileInputStream(new File(
					FileWorkflowStore.storeFile));
			ObjectInputStream ois = new ObjectInputStream(fis);
			SerializableCache o = (SerializableCache) ois.readObject();
			fis.close();
			return o;
		} catch (Exception e) {
			FileWorkflowStore.log.fatal("cannot store in file "
					+ FileWorkflowStore.storeFile
					+ ". Create a new blank store.");
		}
		return new SerializableCache();
	}

	static synchronized void store() {
		try {
			FileOutputStream fos = new FileOutputStream(new File(
					FileWorkflowStore.storeFile));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(getInstance());
			fos.close();
		} catch (Exception e) {
			FileWorkflowStore.log.fatal("cannot store in file "
					+ FileWorkflowStore.storeFile + ".");
		}
	}
}