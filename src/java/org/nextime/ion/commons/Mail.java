package org.nextime.ion.commons;

import java.util.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;

/**
 * @author gbort
 */
public class Mail {

	public static void send(String s, String b) {

		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", org.nextime.ion.framework.config.Config
					.getInstance().getMailHost());
			props.put("mail.smtp.port", ""
					+ org.nextime.ion.framework.config.Config.getInstance()
							.getMailPort());

			Session sendMailSession = Session.getInstance(props, null);
			Transport transport = sendMailSession.getTransport("smtp");
			Message message = new MimeMessage(sendMailSession);

			message.setFrom(new InternetAddress(
					org.nextime.ion.framework.config.Config.getInstance()
							.getMailFrom(),
					org.nextime.ion.framework.config.Config.getInstance()
							.getMailFrom()));

			Set toSet = new HashSet();

			// adding users
			StringTokenizer st = new StringTokenizer(
					org.nextime.ion.framework.config.Config.getInstance()
							.getMailToList(), ", ");

			while (st.hasMoreTokens()) {
				String user = st.nextToken();
				toSet.add(new InternetAddress(user, user));
			}

			message.setRecipients(Message.RecipientType.TO,
					(InternetAddress[]) toSet.toArray(new InternetAddress[toSet
							.size()]));

			message.setSubject(s);
			message.setSentDate(new Date());
			message.setContent(b, "text/html");
			message.saveChanges();
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), Mail.class,e);
		}

	}

	public static void send(String toUsers, String toGroups, String cc,
			String ccGroups, String subject, String m) {
		send(null, toUsers, toGroups, cc, ccGroups, subject, m);
	}

	public static void send(String fromEmail, String toUsers, String toGroups,
			String cc, String ccGroups, String subject, String m) {
		String smtpHost = org.nextime.ion.framework.config.Config.getInstance()
				.getMailHost();
		if (fromEmail == null || "".equals(fromEmail))
			fromEmail = org.nextime.ion.framework.config.Config.getInstance()
					.getMailFrom();
		//String fromName =
		// org.nextime.ion.framework.config.Config.getInstance()
		//    .getMailFrom();
		try {
			Set toSet = new HashSet();
			// adding users
			if (toUsers == null)
				toUsers = "";
			StringTokenizer st = new StringTokenizer(toUsers, ", ");

			while (st.hasMoreTokens()) {
				String userLogin = st.nextToken();
				User user = User.getInstance(userLogin);
				if (user.getMetaData("email") != null
						&& !"".equals(user.getMetaData("email")))
					toSet.add(new InternetAddress(user.getMetaData("email")
							+ "", user.getMetaData("name") + ""));
			}
			// adding groups
			if (toGroups == null)
				toGroups = "";
			st = new StringTokenizer(toGroups, ", ");

			while (st.hasMoreTokens()) {
				String groupName = st.nextToken();
				Group group = Group.getInstance(groupName);
				Vector users = group.listUsers();
				for (int i = 0; i < users.size(); i++) {
					User user = (User) users.get(i);
					if (user.getMetaData("email") != null
							&& !"".equals(user.getMetaData("email")))
						toSet.add(new InternetAddress(user.getMetaData("email")
								+ "", user.getMetaData("name") + ""));
				}
			}
			if (cc == null)
				cc = "";
			Set ccSet = new HashSet();
			st = new StringTokenizer(cc, ", ");
			while (st.hasMoreTokens()) {
				String user = st.nextToken();
				ccSet.add(new InternetAddress(user));
			}
			if (ccGroups == null)
				ccGroups = "";
			st = new StringTokenizer(ccGroups, ", ");

			while (st.hasMoreTokens()) {
				String groupName = st.nextToken();
				Group group = Group.getInstance(groupName);
				Vector users = group.listUsers();
				for (int i = 0; i < users.size(); i++) {
					User user = (User) users.get(i);
					if (user.getMetaData("email") != null
							&& !"".equals(user.getMetaData("email")))
						ccSet.add(new InternetAddress(user.getMetaData("email")
								+ "", user.getMetaData("name") + ""));
				}
			}

			if (!toSet.isEmpty() || !ccSet.isEmpty()) {
				Properties props = new Properties();
				props.put("mail.smtp.host", smtpHost);

				Session sendMailSession = Session.getInstance(props, null);
				Transport transport = sendMailSession.getTransport("smtp");
				Message message = new MimeMessage(sendMailSession);
				message.setFrom(new InternetAddress(fromEmail, fromEmail));

				if (!toSet.isEmpty())
					message
							.setRecipients(Message.RecipientType.TO,
									(InternetAddress[]) toSet
											.toArray(new InternetAddress[toSet
													.size()]));
				if (!ccSet.isEmpty())
					message
							.setRecipients(Message.RecipientType.CC,
									(InternetAddress[]) ccSet
											.toArray(new InternetAddress[ccSet
													.size()]));
				message.setSubject(subject);
				message.setSentDate(new Date());
				message.setContent(m, "text/plain");
				message.saveChanges();

				transport.connect();
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), Mail.class,e);
		}

	}

	/**
	 * Alerte Expiration
	 */
	public static String alerteExpire(String _index) {
		if ("0".equals(org.nextime.ion.framework.config.Config.getInstance()
				.getNbJrExp()))
			return "";
		String rc = "-- Alerte pour expiration prochaine des contenus";
		if (_index != null) {
			try {
				Calendar d = Calendar.getInstance();
				d.setTime(new Date());
				d.add(Calendar.DAY_OF_MONTH, 7);
				rc += "\n@@ Deadline : "
						+ org.nextime.ion.framework.config.Config.getInstance()
								.getNbJrExp()
						+ " jours ["
						+ new java.text.SimpleDateFormat("yyyyMMdd").format(d
								.getTime()) + "]";
				Vector results = org.nextime.ion.framework.helper.Searcher
						.searchExpire(_index, new java.text.SimpleDateFormat(
								"yyyyMMdd").format(d.getTime()));
				if (results != null && !results.isEmpty()) {
					String subject = "CMS : expiration de contenu J-"
							+ org.nextime.ion.framework.config.Config
									.getInstance().getNbJrExp();
					String m = "";
					String toUsers = "";
					Vector v = new Vector();
					for (int i = 0; i < results.size(); i++) {
						String[] result = results.get(i).toString().split(";");
						if (result != null && result.length > 0) {
							// id version nom auteur
							if (!v.contains(result[0])) {
								v.add(result[0]);
								m += "\n" + result[2] + " (id=" + result[0]
										+ ")";
								//toUsers += result[3] + ", ";
							}
						}
					}
					if (m != null && !"".equals(m)) {
						m = "Les publications suivantes vont expirer dans "
								+ org.nextime.ion.framework.config.Config
										.getInstance().getNbJrExp()
								+ " jours :" + m;
						rc += "\n@@ Resultats : " + m;
						send(toUsers, null, null, "admins,gValideurs", subject,
								m);
					} else
						rc += "\n@@ Aucun element ne va expirer prochainement";

				} else
					rc += "\n@@ Aucun element ne va expirer prochainement";
			} catch (Exception e) {
				// TODO
				Logger.getInstance().error(e.getMessage(), Mail.class,e);
				rc += "\n!! Erreur : " + e;
			}
		}
		rc += "\n@@ Traitement termine";
		return rc;
	}

}