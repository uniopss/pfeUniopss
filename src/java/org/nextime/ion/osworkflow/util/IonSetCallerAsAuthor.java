package org.nextime.ion.osworkflow.util;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowContext;

import java.util.Map;

/**
 * @author gbort
 */
public class IonSetCallerAsAuthor implements FunctionProvider {
	public void execute(Map inputs, Map properties, PropertySet  variables) {
		WorkflowContext context = (WorkflowContext) inputs.get("context");
		variables.setString("author", context.getCaller());
	}
}