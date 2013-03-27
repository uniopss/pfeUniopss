package org.nextime.ion.osworkflow.util;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import java.util.Map;

/**
 * @author gbort
 */
public class IonInitWorkflow implements FunctionProvider {

	public void execute(Map inputs, Map properties, PropertySet variables) {

		String id = inputs.get("id") + "";
		int version = Integer.parseInt(inputs.get("version") + "");

		variables.setString("publicationId", id);
		variables.setString("publicationVersion", version + "");

	}

}