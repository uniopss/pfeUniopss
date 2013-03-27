package org.nextime.ion.osworkflow.util;

import java.util.Map;

import org.nextime.ion.framework.logger.Logger;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;

/**
 * @author gbort
 */
public class IonSendEmail implements FunctionProvider {

	public void execute(Map inputs, Map args, PropertySet variables) {

		try {
			String from = (String) args.get("from");
			String toUsers = (String) args.get("toUsers");
			String toGroups = (String) args.get("toGroups");
			String cc = (String) args.get("cc");
			String subject = (String) args.get("subject");
			String m = (String) args.get("message");
			org.nextime.ion.commons.Mail.send(from, toUsers, toGroups, cc, null, subject, m);
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(e.getMessage(), IonSendEmail.class,e);
		}

	}
}