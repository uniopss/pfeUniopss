<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%@ taglib uri="/WEB-INF/tlds/struts-template.tld" prefix="template" %>
<ion:begin/>
<export>
<%
        try {
        java.util.Enumeration e = request.getParameterNames();
	while(e.hasMoreElements()){
		String tmp = (String)e.nextElement();
                if( tmp.indexOf("sc_") == 0 && request.getParameter(tmp.replaceAll("sc_",""))!=null) {
                        pageContext.setAttribute("pId",tmp.replaceAll("sc_",""));
                        pageContext.setAttribute("pVe",request.getParameter(tmp.replaceAll("sc_","")));
                        request.setAttribute("pRef",request.getParameter(tmp.replaceAll("sc_","rf_")));
%>
        <c:set var="xml" scope="request">
	<ion:selectObjects type="org.nextime.ion.frontoffice.objectSelector.GetPublicationView">
		<ion:param name="publication" value="${pId}"/>
		<ion:param name="version" value="${pVe}"/>
                <ion:param name="view" value="special_export_xml"/>
		<ion:param name="max" value="1"/>
		<ion:iterateOver var="lc">
                        <c:out value="${lc.view}" escapeXml="false"/>
		</ion:iterateOver>
	</ion:selectObjects>
	</c:set>
        <c:set var="type" scope="request" value="equivalent"/><c:set var="xml" scope="request"><template:insert template="util.jsp" /></c:set>
        <c:set var="type" scope="request" value="variante"/><c:set var="xml" scope="request"><template:insert template="util.jsp" /></c:set>
        <c:set var="type" scope="request" value="option"/><c:set var="xml" scope="request"><template:insert template="util.jsp" /></c:set>
        <c:out value="${xml}" escapeXml="false"/>
<%		}
	}
        } catch(Exception e) {out.println("<center><img src='images/warning.gif' align='absmiddle'> Erreur a la generation du XML</center>");
        }
%>
</export>
<ion:close/>
