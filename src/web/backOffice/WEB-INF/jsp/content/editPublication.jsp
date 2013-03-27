<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<c:set var="currentLocale" scope="page" value="${defaultLocale.locale}"/>
<c:if test="${param['targetLocale']!=null}">
        <c:set var="currentLocale" scope="page" value="${param['targetLocale']}"/>
</c:if>
<html>
<head>
	<title></title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/publication-form.css">
        <script language="JavaScript" src="/backoffice/edx/js/lcControl.js"></script>
</head>
<body class="text" background="images/fond.gif">
        <script language="javascript">
                function lc()  { return true ; }
                function zlc()  {
                        if (lc()) {
                                document.forms(0).target='';
                                document.forms(0).action='edx/merge.jsp';
                                return true;
                        }
                        else
                                alert("Des erreurs existent dans le formulaire");
                                return false ;
                }
                function zLg()  {
                        if (lc()) {
                                document.forms(0).target='';
                                document.forms(0).action='edx/changeLocale.jsp';
                                document.forms(0).submit()
                        }
                        else
                                alert("Des erreurs existent dans le formulaire");
                }
        </script>
	<c:if test="${param['targetLocale']!=null}">
		<c:set var="currentLocale" scope="page" value="${param['targetLocale']}"/>
	</c:if>
        <html:form action="editPublication.x" target="" onsubmit="return zlc();">
	<img src="images/publication-edit-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="editPublication.title"/> " <c:out value="${publication.metaData['name']}" default="<i>(undefined)</i>" escapeXml="false"/> " (version <c:out value="${version}"/>)
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span class="title-list"><c:out value="${section.pathLabel}" default="" escapeXml="false"/>
	                <input type="Hidden" name="sid" value="<c:out value="${section.id}"/>">
					<input type="Hidden" name="currentLocale" value="<c:out value="${currentLocale}"/>">
        	        <c:choose><c:when test="${mL=='1'}"><center>Langue de saisie&nbsp;:&nbsp;<select align="absmiddle" name="targetLocale" onchange="zLg()" ><c:forEach items="${locales}" var="localeItem"><option value="<c:out value="${localeItem.locale}"/>" <c:if test="${localeItem.locale==currentLocale}">selected</c:if>><c:out value="${localeItem.name}"/></option></c:forEach></select></center></c:when><c:otherwise><input type="hidden" name="targetLocale" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
                </span>
	</span>
        <br>
		<input type="Hidden" name="id" value="<c:out value="${publication.id}"/>">
		<input type="Hidden" name="version" value="<c:out value="${version}"/>">
		<input type="Hidden" name="itsOk" value="itsOk">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="text">
			<tr>
                 <td>
                 <%try{%>
					<c:import url="/edx/publi2form.xsl" var="xsl"/>
                                        <c:import url="/edx/getPublicationXml.jsp?id=${publication.id}&version=${version}&sid=${section.id}" var="xml"/>
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

		<textarea name="data" style="position:absolute; visibility:hidden">
			<c:out value="${xml}" escapeXml="false"/>
		</textarea>
		<br><br><br>
		<input onclick="return confirm('Voulez-vous sauvegarder ?')" type=submit name="ok" value="<bean:message key="general.save"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button" onclick="document.forms(0).onsubmit=''"/><br><br><i><bean:message key="general.saveHint"/></i>
	</html:form>
        <form id="callout" name="callout" action="" target="callout" method="post">
        <input type=hidden id="nom" name="nom" >
        <input type=hidden id="valeur" name="valeur" >
        <input type=hidden id="zaction" name="zaction" >
        <input type="hidden" id="sid" name="sid" value="<c:out value="${section.id}"/>">
        <input type="hidden" id="pid" name="pid" value="<c:out value="${publication.id}"/>">
        <input type="hidden" id="plid" name="plid" value="<c:out value="${publication.metaData['name']}"/>">
        <input type="hidden" id="ptype" name="ptype" value="<c:out value="${publication.type.id}"/>">
        <input type="hidden" id="currentLocale" name="currentLocale" value="<c:out value="${currentLocale}"/>">
        </form>
        <script language="JavaScript" src="scripts/calendrier.js"></script>
</body>

</html>
