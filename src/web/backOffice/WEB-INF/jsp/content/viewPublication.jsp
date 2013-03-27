<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<c:set var="currentLocale" scope="page" value="${defaultLocale.locale}"/>
<c:if test="${param['locale']!=null}">
        <c:set var="currentLocale" scope="page" value="${param['locale']}"/>
</c:if>
<html>
<head>
        <title>Contenu</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body>
<table width="95%">
	<tr>
		<td valign="top">
                        <form action="viewPublication.x" method="post" id="form">
                                <input type="Hidden" name="sid" value="<c:out value="${param['sid']}"/>">
				<input type="Hidden" name="id" value="<c:out value="${param['id']}"/>">
				<input type="Hidden" name="version" value="<c:out value="${param['version']}"/>">
                                <a style="cursor:default" href="#contenu" style="text-decoration:none" onclick="window.open('viewPublicationXml.x?id=<c:out value="${param['id']}"/>&version=<c:out value="${param['version']}"/>&sid=<c:out value="${param['sid']}"/>','contenuXml','width=600,height=400,resizable=yes,scrollbars=yes')"><img src="images/publication-edit-tile.gif" border="0" align="absmiddle" style="text-decoration:none"></a>
                                <span style="font-family:verdana; color:#5d5d5d; font-size:15px; font-weight:bold; position:relative; top:-4px;">
                                        Publication "<c:out value="${publication.metaData['name']}"/>" (version <c:out value="${param['version']}"/>)
				</span>
				<table width="100%">
					<tr>
                                                <td valign="top">
                                                        <c:choose><c:when test="${mL=='1'}"><center><span style="font-family:verdana; color:#0148B2; font-size:10px; font-weight:bold;">Langue : <select name="locale" style="font-family:verdana; color:black; font-size:9px; font-weight:bold; border:1px solid black; background-color:#d6e4f3;" onChange="form.submit()"><c:forEach var="localeItem" items="${locales}"><option value="<c:out value="${localeItem.locale}"/>" <c:if test="${localeItem.locale==param['locale']}">selected</c:if>><c:out value="${localeItem.name}"/></option></c:forEach></select></span></center></c:when><c:otherwise><input type="hidden" name="locale" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td valign="top">
                        <hr width="100%" size="1" noshade color="#949494">
		</td>
	</tr>
	<tr>
		<td valign="top">
                                        <%try{%>
                                        <c:import url="/edx/publi2view.xsl" var="xsl"/>
					<x:transform xslt="${xsl}">
						<nextimeSupaXml>
							<data><c:out value="${xml}" escapeXml="false"/></data>
						</nextimeSupaXml>
						<x:param name="locale" value="${currentLocale}"/>
					</x:transform>
                                        <%}catch(Exception e) {out.println("<center><img src='images/warning.gif' align='absmiddle'> Publication corrompue</center>");}%>
		</td>
	</tr>
</table>

</body>

</html>
