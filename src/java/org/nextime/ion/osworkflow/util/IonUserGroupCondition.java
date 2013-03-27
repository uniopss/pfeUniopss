package org.nextime.ion.osworkflow.util;

import java.util.Map;

import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;

/**
 * @author gbort
 */
public class IonUserGroupCondition implements Condition {
	public boolean passesCondition(Map inputs, Map args, PropertySet variables) {
		try {
			if (args.get("group").toString() == null
					|| "".equals(args.get("group").toString())
					|| "X".equals(args.get("group").toString()))
				return false;
			WorkflowContext context = (WorkflowContext) inputs.get("context");
			return User.getInstance(context.getCaller()).isInGroup( Group.getInstance(args.get("group") + ""));
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage(), IonUserGroupCondition.class,e);
			return false;
		}
	}
}