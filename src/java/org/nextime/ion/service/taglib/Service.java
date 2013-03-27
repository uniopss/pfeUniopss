package org.nextime.ion.service.taglib;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Service {

	public String getService(Hashtable params, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}