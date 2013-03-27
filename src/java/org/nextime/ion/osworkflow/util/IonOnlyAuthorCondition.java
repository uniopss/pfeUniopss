package org.nextime.ion.osworkflow.util;

import java.util.Map;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.logger.Logger;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;

/**
 * @author gbort
 */
public class IonOnlyAuthorCondition implements Condition {
	public boolean passesCondition(Map inputs, Map args, PropertySet  variables) {
		try {
			WorkflowContext context = (WorkflowContext) inputs.get("context");
			String id = variables.getString("publicationId") + "";
			int version = Integer.parseInt(variables.getString("publicationVersion") + "");

			String author = Publication.getInstance(id).getVersion(version).getAuthor().getLogin();

			return author.equals(context.getCaller());
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IonOnlyAuthorCondition.class,e);
			return false;
		}
	}
}