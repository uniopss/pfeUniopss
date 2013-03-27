<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/cache.tld" prefix="cache"%>
<html>
<head>
        <title>Publications</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='setBallon("BallonTip");' background="images/fond.gif" style="cursor:default">
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
	<!-- title -->
        <img src="images/publication-edit-tile.gif" align="absmiddle">
	<span class="title">
                 <bean:message key="viewSection.title"/>&nbsp;"&nbsp;<c:out value="${section.metaData['name']}" default="<i>(indéfini)</i>" escapeXml="false"/>&nbsp;"
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="title-list"><c:out value="${section.parent.pathLabel}" default="" escapeXml="false"/></span>
	</span>
	<br><br><br>
	<c:choose>
		<!-- il y a des publications à afficher, publications to show -->
                <c:when test="${result!=null}">
                        <table border="0" cellpadding="3" cellspacing="3" width="100%">
                                <tr class='text'>
                                        <td></td>
                                        <td>&nbsp;&nbsp;<b>nom&nbsp;</td>
                                        <td>&nbsp;&nbsp;<b>version</b></td>
                                        <td>&nbsp;&nbsp;<b>auteur</b></td>
                                        <td>&nbsp;&nbsp;<b>type&nbsp;</td>
                                        <td>&nbsp;&nbsp;<b>date&nbsp;</b></td>
                                        <td>&nbsp;&nbsp;<b>section&nbsp;</b></td>
										<td>&nbsp;&nbsp;<b>visibilité&nbsp;</b></td>
                                </tr>
                                <tr>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>
                                        <td height="1" bgcolor="#949494"></td>                                        
                                </tr>
                                <c:forEach var="r" items="${result}">
                                        <tr>
                                                <!-- informations -->
                                                <td width="25" align="left" valign='center'>
                                                        <a href="#infos"
                                                                <c:if test="${r.type != 'objet'}">onclick="window.open('viewPublication.x?id=<c:out value="${r.id}"/>&version=<c:out value="${r.version}"/>&sid=<c:out value="${r.section}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"</c:if>
                                                                ><img src="images/infos.gif" border="0"></a>
                                                </td>
                                                <!-- name -->
                                                <td class="text" style="color:#5e5e5e;cursor:default">
                                                        <strong style="color:#5e5e5e;"><c:out value="${r.name}"/></strong>
                                                </td>
                                                <td class="text" style="color:#5e5e5e;cursor:default">
                                                        <c:out value="${r.version}"/>
                                                </td>
                                                <td class="text" style="color:#5e5e5e;cursor:default">
                                                        <c:out value="${r.author}"/>
                                                </td>
                                                <td class="text">
                                                        <c:out value="${r.type}"/>
                                                </td>
                                                <td class="text">
                                                        <c:out value="${r.date}"/>
                                                </td>
                                                <td class="text">
                                                        <c:out value="${r.sections}"/>
                                                </td>
                                                <td class="text">
                                                        <c:out value="${r.visibility}"/>
                                                </td>                                                
                                        </tr>
					<tr>
                                                <td colspan="8" height="1"></td>
					</tr>
					<tr>
                                                <td colspan="8" height="1" bgcolor="#bdbdbd"></td>
					</tr>
					<tr>
                                                <td colspan="8" height="1"></td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<!-- il n'y a pas de publications à afficher, no publications to show -->
		<c:otherwise>
			<i style="color:#222222"><bean:message key="viewSection.noPublications"/></i>
		</c:otherwise>
	</c:choose>
</body>
</html>
