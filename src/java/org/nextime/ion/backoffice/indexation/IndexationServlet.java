package org.nextime.ion.backoffice.indexation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.nextime.ion.framework.helper.Indexer;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public class IndexationServlet extends HttpServlet implements Runnable {

	private int minuteDelay = 60;

	private Thread thread;

	/**
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		super.init();
		try {
			minuteDelay = Integer.parseInt(getServletConfig().getInitParameter(
					"delay"));
		} catch (Exception e) {
			// TODO
			Logger.getInstance().error(e.getMessage(), IndexationServlet.class,e);
		}
		if (minuteDelay > 0) {
			thread = new Thread(this, "Indexation");
			thread.setDaemon(true);
			thread.start();
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			try {
				exec();
				Thread.sleep(minuteDelay * 60 * 1000);
			} catch (Exception e) {
				//e.printStackTrace();
				Logger.getInstance().error(
						e.getMessage(), IndexationServlet.class,e);
			}
		}
	}

	/**
	 * Execute Indexation.
	 *
	 */
	private void exec() {
		try {
			Mapping.begin();
			Indexer.reIndex();
		} catch (Exception e) {
			Logger.getInstance().error(
					e.getMessage(), IndexationServlet.class,e);
		} finally {
			Mapping.rollback();
		}
	}

	/**
	 * Returns the minuteDelay.
	 *
	 * @return int
	 */
	public int getMinuteDelay() {
		return minuteDelay;
	}

	/**
	 * Sets the minuteDelay.
	 *
	 * @param minuteDelay
	 *            The minuteDelay to set
	 */
	public void setMinuteDelay(int minuteDelay) {
		this.minuteDelay = minuteDelay;
	}

}
