package org.nextime.ion.backoffice.bean;

import org.nextime.ion.framework.logger.Logger;

public class File {

	private java.io.File file;

	public File(java.io.File file) {
		this.file = file;
	}

	/**
	 * Returns the extension.
	 * 
	 * @return String
	 */
	public String getExtension() {
		try {
			return file.getName()
					.substring(file.getName().lastIndexOf(".") + 1)
					.toLowerCase();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), File.class,e);
			return "NULL";
		}
	}

	/**
	 * Returns the name.
	 * 
	 * @return String
	 */
	public String getName() {
		return file.getName();
	}

}

