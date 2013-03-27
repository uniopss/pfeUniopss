<%@ taglib uri="/WEB-INF/tlds/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/cache.tld" prefix="cache"%>
<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="0">
        <title>ion menu</title>
	<link rel="stylesheet" type="text/css" href="styles/menu.css">
</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
        <c:if test="${userLogin!=null}">
                <c:set var="tab" value="${param['tab']}"/>
                <c:if test="${tab==null}">
                        <c:set var="tab" value="NULL"/>
                </c:if>
                <!-- smart menu from menu.xml with cache ... -->
                <c:if test="${localizedMenu!=null}">
                        <cache:cache scope="application" name="menus" key="${tab}">
                        <c:import url="/xml/menu.xsl" var="xsl"/>
                        <x:transform xslt="${xsl}">
                                <backoffice-menu selectedTab="<c:out value="${tab}"/>">
                                        <c:out value="${localizedMenu}" escapeXml="false"/>
                                </backoffice-menu>
                        </x:transform>
                        </cache:cache>
                </c:if>
	</c:if>
</body>
</html>
