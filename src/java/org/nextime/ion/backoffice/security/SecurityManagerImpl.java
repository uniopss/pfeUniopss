package org.nextime.ion.backoffice.security;

import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;

import com.opensymphony.workflow.basic.*;

public class SecurityManagerImpl implements SecurityManager {

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canAdminSecurity(User)
	 */
	public boolean canAdminSecurity(User user) {
		try {

			return user.isInGroup(Group.getInstance("admins"));
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(e.getMessage(), SecurityManagerImpl.class,e);
		}
		return false;
	}

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canCreatePublication(Section,
	 *      User)
	 */
	public boolean canCreatePublication(Section section, User user) {
		try {
			String workflowType = section.getMetaData("workflow") + "";
			BasicWorkflow bw = new BasicWorkflow(user.getLogin());
			return bw.canInitialize(workflowType, 1);
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canCreateSection(Section,
	 *      User)
	 */
	public boolean canCreateSection(Section section, User user) {
		try {

			return user.isInGroup(Group.getInstance("admins"));
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canDeletePublication(Publication,
	 *      User)
	 */
	public boolean canDeletePublication(Publication publication, User user) {
		try {

			return user.isInGroup(Group.getInstance("admins"))
					|| user.isInGroup(Group.getInstance("gValideurs"));
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canDeleteSection(Section,
	 *      User)
	 */
	public boolean canDeleteSection(Section section, User user) {
		try {

			return section.isEmpty()
					&& user.isInGroup(Group.getInstance("admins"));
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canEditPublication(Publication,
	 *      User)
	 */
	public boolean canEditPublication(Publication publication, int version,
			User user) {
		try {

			if (user.getLogin().equals("admin"))
				return true;
			if (user.isInGroup(Group.getInstance("admins")))
			  return true;
			return publication.getVersion(version).getWorkflow(user)
					.getPermissions().contains("canEdit");
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

	/**
	 * @see org.nextime.ion.backoffice.security.SecurityManager#canEditSection(Section,
	 *      User)
	 */
	public boolean canEditSection(Section section, User user) {
		try {

			return user.isInGroup(Group.getInstance("admins"));
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

	public boolean canSuperAdminSecurity(User user) {
		try {
			if (user.getLogin().equals("admin"))
				return true;
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), SecurityManagerFactory.class,e);
		}
		return false;
	}

}