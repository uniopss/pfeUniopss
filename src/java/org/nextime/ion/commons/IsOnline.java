package org.nextime.ion.commons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.logger.Logger;

public class IsOnline {

	static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public static String frontUserLogin = "visiteurAnonyme";

	public static boolean check(PublicationVersion pv) {
		try {
			if (pv.getWorkflow(User.getInstance(frontUserLogin))
					.getPermissions().contains("canDisplay"))
				return true;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
		}
		return false;
	}

	public static int life(Publication p) {
		try {
			Date lcDate = new Date();
			// Date en cours < Date Publication => KO
			String lcDatePub = (String) p.getMetaData("lcDatePublication");
			if (lcDatePub != null && !"".equals(lcDatePub)
					&& formatter.parse(lcDatePub).after(lcDate))
				return 0;
			String lcDateFin = (String) p.getMetaData("lcDateFin");
			// Date en cours > Date Fin => KO
			if (lcDateFin != null && !"".equals(lcDateFin)
					&& formatter.parse(lcDateFin).before(lcDate))
				return 2;
			return 1;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
		}
		return -1;
	}

	/**
	 * 	La publication est KO dans le cas
	 * 				- Date en cours < Date Publication => KO
	 * 				- Date en cours > Date Fin => KO
	 * @param p_Publication
	 * @return true/false
	 */
	public static boolean check(Publication p_Publication) {
		try {
			Date lcDate = new Date();
			// Date en cours < Date Publication => KO
			String lcDatePub = (String) p_Publication.getMetaData("lcDatePublication");
			if (lcDatePub != null && !"".equals(lcDatePub)
					&& formatter.parse(lcDatePub).after(lcDate))
				return false;
			String lcDateFin = (String) p_Publication.getMetaData("lcDateFin");
			// Date en cours > Date Fin => KO
			if (lcDateFin != null && !"".equals(lcDateFin)
					&& formatter.parse(lcDateFin).before(lcDate))
				return false;
			String diffusion = (String) p_Publication.getMetaData("diffusion");
			// Canal de diffusion
			if (diffusion != null && !"".equals(diffusion)
					&& !"internet".equals(diffusion))
				return false;
			return true;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
		}
		return false;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	private static boolean checkChange(Publication p) {
		try {
			Date lcDate = new Date();
			// Date en cours < Date Changement => KO
			String lcDateChg = (String) p.getMetaData("lcDateChange");
			if (lcDateChg != null && !"".equals(lcDateChg)
					&& formatter.parse(lcDateChg).after(lcDate))
				return false;
			return true;
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
		}
		return false;
	}

	/**
	 * 	Vérification du statut de la publicaition
	 * 			KO :  dans le cas où les dates de la publication ne sont pas respectés
	 * 			Si une des version sont OK ( dont le droit "canDisplay" est appliqué )

	 *
	 * @param p_Publication
	 * @return
	 */
	public static boolean getStatus(Publication p_Publication) {
		try {
			if (!check(p_Publication))
				return false;
			Vector v = p_Publication.getVersions();
			for (int i = 1; i <= v.size(); i++) {
				if (check(p_Publication.getVersion(i)))
					return true;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
		}
		return false;
	}

	public static boolean getStatus(Object p) {
		try {
			return getStatus((Publication) p);
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
			return false;
		}
	}

	public static int getMostRecentVersion(Publication p_Publication) {
		int lcVersion = -1;
		try {
			if (!check(p_Publication))
				return lcVersion;
			boolean change = checkChange(p_Publication);
			Vector v = p_Publication.getVersions();
			for (int i = 0; i < v.size(); i++) {
				PublicationVersion ver = (PublicationVersion) v.get(i);
				if (check(ver)) {
					if (change) {
						return ver.getVersion();
					} else {
						lcVersion = ver.getVersion();
						change = true;
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(e.getMessage(), IsOnline.class,e);
		}
		return lcVersion;
	}

}