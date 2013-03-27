<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>list Types</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif">

        <img src="images/type-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="type.listType"/>
	</span>
	<br><br>
        <span class="title-list"><bean:message key="type.listType.headline"/></span>
        <c:if test="${userLogin=='admin'}">
                ( <img src="images/create.gif" align="absmiddle" border="0"> <a onclick="return confirm('voulez-vous créer un nouveau type de contenu ?');" href="createType.x" target="sub"><bean:message key="type.listType.newtype"/></a> )
	</c:if>
        <P>
        <table border="0" cellpadding="0" cellspacing="2" width="100%">
	 	<tr>
                        <td class="text">&nbsp;&nbsp;<b>id</b></td>
                        <td class="text">&nbsp;&nbsp;<b>nom</b></td>
                        <td class="text">&nbsp;&nbsp;<b>modele</b></td>
                        <td class="text">&nbsp;&nbsp;<b>styles</b></td>
			<td width="20"></td>
		</tr>
		<tr>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
		</tr>
                <c:forEach var="type" items="${types}">
                <tr>
                        <td class="text"><img src="images/puce.gif" align="absmiddle" border="0">&nbsp;
					<c:choose>
                                                <c:when test="${userLogin=='admin'}">
                                                        <a onclick="return confirm('voulez-vous éditer ce type de contenu ?');"  href="editType.x?id=<bean:write name="type" property="id"/>"><bean:write name="type" property="id"/></a>
                                                </c:when>
						<c:otherwise>
                                                        <bean:write name="type" property="id"/>
						</c:otherwise>
					</c:choose>
                        </td>
                        <td class="text" width="200">&nbsp;&nbsp;<c:out value="${type.metaData['name']}"/>&nbsp;&nbsp;</td>
                        <td class="text" width="50">&nbsp;&nbsp;<bean:write name="type" property="modelState" filter="false"/>&nbsp;&nbsp;</td>
                        <td class="text" width="50">&nbsp;&nbsp;<bean:write name="type" property="stylesState" filter="false"/>&nbsp;&nbsp;</td>
                        <td align="center">
                                        <c:if test="${userLogin=='admin'}">
                                                <a onclick="return confirm('voulez-vous supprimer ce type de contenu ?');" href="deleteType.x?id=<bean:write name="type" property="id"/>" target="sub"><img src="images/poubelle.gif" border="0" alt="supprimer"></a>
					</c:if>
                        </td>
                        </tr>
			<tr>
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
