<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<%
		// Il a deux type de callout: par r�gion et commun pour toutes les r�gion 
		
		String xmlFileName=request.getParameter("calloutParam");
		String sC = org.nextime.ion.commons.Util.readXMLFile(org.nextime.ion.framework.config.Config.getInstance().getDynamicListXML() ,xmlFileName, "<lc/>");
%>

<c:set var="modeRadio" value="true" />

<%@ include file="listeEnrichissable_inc.jsp" %>