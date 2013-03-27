package org.nextime.ion.osworkflow.util;

import java.util.Map;

import org.nextime.ion.framework.logger.Logger;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;

/**
 * @author gbort
 */
public class IonUseThisPermissionAsMetadata implements Condition {
	public boolean passesCondition(Map inputs, Map args, PropertySet  variables) {
		try {
			WorkflowContext context = (WorkflowContext) inputs.get("context");
			//return "smartIonFramework".equals(context.getCaller());
			return "visiteurAnonyme".equals(context.getCaller());

		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IonUseThisPermissionAsMetadata.class,e);
			return false;
		}
	}
}