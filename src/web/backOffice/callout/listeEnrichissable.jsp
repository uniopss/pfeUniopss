<%
		// Il a deux type de callout: par r�gion et commun pour toutes les r�gion 
		
		String xmlFileName=request.getParameter("calloutParam");
		String sC = org.nextime.ion.commons.Util.readXMLFile(org.nextime.ion.framework.config.Config.getInstance().getDynamicListXML() ,xmlFileName, "<lc/>");
%>

<%@ include file="listeEnrichissable_inc.jsp" %>