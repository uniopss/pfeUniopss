<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<ion:begin/>
<html>
<head>
        <title>list Sites</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif">

        <img src="images/site-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="site.title"/>
	</span>
	<br><br>
        <span class="title-list"><bean:message key="site.list"/></span>
        <c:if test="${userLogin=='admin'}">
                ( <img src="images/create.gif" align="absmiddle" border="0"> <a onclick="return confirm('voulez-vous créer un nouveau site ?');" href="createSite.x" target="sub"><bean:message key="site.add"/></a> )
	</c:if>
        <P>
        <table border="0" cellpadding="0" cellspacing="2" width="100%">
	 	<tr>
                        <td class="text">&nbsp;&nbsp;<b>id</b></td>
                        <td class="text">&nbsp;&nbsp;<b>nom</b></td>
                        <td class="text"></td>
                        <td class="text"></td>
                        <td class="text"></td>
                        <td class="text"></td>
		</tr>
		<tr>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
		</tr>
                <c:forEach var="site" items="${sites}">
                        <tr>
                                <td class="text" width="40"><img src="images/puce.gif" align="absmiddle" border="0">&nbsp;
                                                <c:choose>
                                                        <c:when test="${userLogin=='admin'}">
                                                                <a onclick="return confirm('voulez-vous éditer ce site ?');" href="editSite.x?id=<c:out value="${site.id}"/>"><c:out value="${site.id}" /></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                                <c:out value="${site.id}" />
                                                        </c:otherwise>
                                                </c:choose>
                                </td>
                                <td class="text">&nbsp;
                                        <c:out value="${site.metaData['name']}" />
                                </td>
                                <td class="text" width="20" align="center"><a target="site" href="/section/<c:out value="${site.id}"/>_accu.html"><img border=O src="images/pucef.gif" alt="voir le site"></a></td>
				<!-- supprimer / delete -->
                                <td align="center" width="20">
                                        <c:if test="${userLogin=='admin'}">
                                                <a onclick="return confirm('voulez-vous supprimer le cache de ce site ?');" href="uncacheSite.x?id=<c:out value="${site.id}"/>" target="sub"><img src="images/poubelle-fade.gif" border="0" alt="vider le cache"></a>
                                        </c:if>
				</td>
                                <td align="center" width="20">
                                        <c:if test="${userLogin=='admin'}">
                                                <a onclick="return confirm('voulez-vous refaire le cache de ce site ?');" href="uncacheSite.x?rd=1&id=<c:out value="${site.id}"/>" target="sub"><img src="images/choix.gif" border="0" alt="refaire le cache"></a>
                                        </c:if>
				</td>
                                <td align="center" width="20">
                                        <ion:security action="canDeleteSection" user="${userLogin}" section="${site.id}">
                                                <c:if test="${userLogin=='admin'}">
                                                <a onclick="return confirm('voulez-vous supprimer ce site ?');" href="deleteSite.x?id=<c:out value="${site.id}"/>" target="sub"><img src="images/poubelle.gif" border="0" alt="supprimer"></a>
                                                </c:if>
                                        </ion:security>
				</td>
                        </tr>
			<tr>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
			</tr>
		</c:forEach>
	</table>

</body>

</html>
<ion:close/>
