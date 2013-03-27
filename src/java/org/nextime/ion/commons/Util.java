package org.nextime.ion.commons;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.business.Group;


public class Util {

	public static void prepare(HttpServletRequest request)
			throws ServletException, IOException {
		initialize(request);
		if (request.getSession().getAttribute("currentSite") == null) {
			try {
				Mapping.begin();
				User user = User.getInstance((String) request.getSession().getAttribute("userLogin"));
				java.util.Vector vR = Section.listRootSections();
				java.util.Vector v = new java.util.Vector();
				for (int i = 0; i < vR.size(); i++) {
					try {
						if (user.isInGroup(Group.getInstance("_"
								+ ((Section) vR.get(i)).getId())))
							v.add(vR.get(i));
					} catch (Exception e) {
						// TODO
						Logger.getInstance().error(e.getMessage(),Util.class,e);
					}
				}
				request.getSession().setAttribute("currentSite", v);
			} catch (org.nextime.ion.framework.mapping.MappingException e) {

			} finally {
				Mapping.rollback();
			}
		}
		if (request.getSession().getServletContext().getAttribute("mL") == null) {
			request.getSession().getServletContext().setAttribute(
							"mL",	org.nextime.ion.framework.locale.LocaleList.getInstance().getLocales() != null
									&& org.nextime.ion.framework.locale.LocaleList.getInstance().getLocales().size() > 1 ? "1"
									: "0");
		}
	}

	public static void initialize(HttpServletRequest request)
			throws ServletException, IOException {
		// locale setting
		if (request.getSession().getAttribute("currentLocale") == null)
			request.getSession().setAttribute("currentLocale", "fr");
	}

	public static String translate(String src) {
		StringBuffer result = new StringBuffer();
		if (src != null && src.length() != 0) {
			int index = -1;
			char c = (char) 0;
			String chars = "àâéèêîôùû~¨#|`_^@°£$¤µ§[]{}_\\`%!<>?çöüäòëïìÔÖÛÜÙÂÄÀÒÈËÊÎÏÌÃãõ²&ÉæÆÿØ×áíóúñÑªº¿®½¼¡\"\"Á©¦¢¥ğĞÍÓßşŞıİ¯±¾_÷¸·¹³";
			String replace = "AAEEEIOUU       a                  COUAOEIIOOUUUAAAOEEEIIIAAO  E  Y  AIOUNN         A      IO   YY         ";
			for (int i = 0; i < src.length(); i++) {
				c = src.charAt(i);
				if ((index = chars.indexOf(c)) != -1)
					result.append(replace.charAt(index));
				else
					result.append(c);
			}
		}
		;

		return result.toString();
	}

	public static String translateAccent(String src) {
		StringBuffer result = new StringBuffer();
		if (src != null && src.length() != 0) {
			int index = -1;
			char c = (char) 0;
			String chars = "àâéèêîôùûçöüäòëïìÔÖÛÜÙÂÄÀÒÈËÊÎÏÌÃãõÉÿáíóúñÑÁÍÓıİ";
			String replace = "AAEEEIOUUCOUAOEIIOOUUUAAAOEEEIIIAAOEYAIOUNNAIOYY";
			for (int i = 0; i < src.length(); i++) {
				c = src.charAt(i);
				if ((index = chars.indexOf(c)) != -1)
					result.append(replace.charAt(index));
				else
					result.append(c);
			}
		}

		return result.toString();
	}

	/**
	 * Reads a file storing intermediate data into a list. Fast method.
	 *
	 * @param file
	 *            the file to be read
	 * @return a file data
	 */
	public static byte[] read(java.io.InputStream is) throws Exception {
		InputStream in = null;
		byte[] buf = null; // output buffer
		int bufLen = 1024;
		try {
			in = new BufferedInputStream(is);
			buf = new byte[bufLen];
			byte[] tmp = null;
			int len = 0;
			java.util.List data = new java.util.ArrayList(24); // keeps peaces
			// of data
			while ((len = in.read(buf, 0, bufLen)) != -1) {
				tmp = new byte[len];
				System.arraycopy(buf, 0, tmp, 0, len); // still need to do copy
				data.add(tmp);
			}
			len = 0;
			if (data.size() == 1)
				return (byte[]) data.get(0);
			for (int i = 0; i < data.size(); i++)
				len += ((byte[]) data.get(i)).length;
			buf = new byte[len]; // final output buffer
			len = 0;
			for (int i = 0; i < data.size(); i++) { // fill with data
				tmp = (byte[]) data.get(i);
				System.arraycopy(tmp, 0, buf, len, tmp.length);
				len += tmp.length;
			}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					// TODO
					Logger.getInstance().error(e.getMessage(),Util.class,e);
				}
		}
		return buf;
	}

	public static byte[] read(java.io.File file) throws Exception {
		return read(new FileInputStream(file));
	}

	/**
	 * 	Récupération du contenu du fichier xml
	 * 	Retourner une chaine defaut dans le cas d'exception
	 * 	Cette méthode ne lance pas d'exception dans aucun cas
	 * @param p_Directory
	 * @param p_FileName
	 * @return
	 */
	public static String readXMLFile(File p_Directory, String p_XMLFileName, String p_defaultXML) {
		BufferedReader is = null;
		FileInputStream fip=null;
		try {
        		String content="";
        		File f = new File(p_Directory, p_XMLFileName);
                // lit le contenu du fichier
                fip=FileUtils.openInputStream(f);
                String line = "";
                is = new BufferedReader(new InputStreamReader(fip));
                while( line != null ) {
                	content+=line+"\r\n";
                	line = is.readLine();
                }
                is.close();
                fip.close();
                return content;
        } catch (IOException e) {
        	Logger.getInstance().error("Erreur d'entré sortie" + e.getMessage(), Util.class,e);
        } finally {
        		IOUtils.closeQuietly(fip);
        		IOUtils.closeQuietly(is);
        }
        return p_defaultXML;
	}
}