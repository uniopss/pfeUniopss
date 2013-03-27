<%--
	Liste enrichissable par région
	Prendre en compte le paramètre sid et récuperer le fichier des liste enrichissable dans le répertoire de la région 

--%>
<%
		// Il a deux type de callout: par région et commun pour toutes les région 
		
		String xmlFileName=request.getParameter("calloutParam");
		String currentSection=request.getParameter("sid");
		if (currentSection !=null && (currentSection.indexOf("_") != -1) )  {
			xmlFileName = currentSection.substring(0, currentSection.indexOf("_")) +  java.io.File.separator + xmlFileName ;
		}
		String sC = org.nextime.ion.commons.Util.readXMLFile(org.nextime.ion.framework.config.Config.getInstance().getDynamicListXML() ,xmlFileName, "<lc/>");
%>


<%@ include file="listeEnrichissable_inc.jsp" %>