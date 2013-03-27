package org.nextime.ion.backoffice.message;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

import org.nextime.ion.framework.logger.Logger;

public class MessageBoard implements Serializable {

	private Vector messages;

	private java.io.File _path;

	public Vector getMessages() {
		Collections.sort(messages);
		return messages;
	}

	public void addMessage(Message m) {
		messages.add(m);
		save();
	}

	public void removeMessage(Message m) {
		messages.remove(m);
		save();
	}

	public void removeMessage(int nb) {
		removeMessage((Message) messages.get(nb));
	}

	public MessageBoard(String id) {
		messages = new Vector();
		_path = new java.io.File(org.nextime.ion.framework.config.Config
				.getInstance().getMessageDirectory(), id);
		if (_path.exists())
			load();
		else
			save();
	}

	private void load() {
		try {
			FileInputStream fis = new FileInputStream(_path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			messages = (java.util.Vector) ois.readObject();
			fis.close();
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), MessageBoard.class,e);
		}
	}

	private synchronized void save() {
		try {
			FileOutputStream fos = new FileOutputStream(_path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(messages);
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.getInstance().error(
					e.getMessage(), MessageBoard.class,e);
		}
	}

}