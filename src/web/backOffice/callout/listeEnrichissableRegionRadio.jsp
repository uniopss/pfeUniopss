<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%--
	Liste enrichissable par r�gion
	Prendre en compte le param�tre sid et r�cuperer le fichier des liste enrichissable dans le r�pertoire de la r�gion 

--%>
<%
		// Il a deux type de callout: par r�gion et commun pour toutes les r�gion 
		
		String xmlFileName=request.getParameter("calloutParam");
		String currentSection=request.getParameter("sid");
		if (currentSection !=null && (currentSection.indexOf("_") != -1) )  {
			xmlFileName = currentSection.substring(0, currentSection.indexOf("_")) +  java.io.File.separator + xmlFileName ;
		}
		String sC = org.nextime.ion.commons.Util.readXMLFile(org.nextime.ion.framework.config.Config.getInstance().getDynamicListXML() ,xmlFileName, "<lc/>");
%>
<c:set var="modeRadio" value="true" />

<%@ include file="listeEnrichissable_inc.jsp" %>