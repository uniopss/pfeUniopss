<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<ion:begin/>

<ion:security action="canAdminSecurity" user="${userLogin}">
	<c:set value="yes" var="canAdminSecurity"/>
</ion:security>

<html>
<head>
        <title>list Groups</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif">

        <img src="images/group-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="listGroups.title"/>
	</span>
	<br><br>
        <span class="title-list"><bean:message key="listGroups.list"/></span>
	<c:if test="${canAdminSecurity!=null}">
                ( <img src="images/create.gif" align="absmiddle" border="0"> <a onclick="return confirm('voulez-vous créer un nouveau groupe ?');" href="createGroup.x" target="sub"><bean:message key="listGroups.create"/></a> )
	</c:if>
        <P>
	<table border="0" cellpadding="0" cellspacing="2" width="100%">
	 	<tr>
                        <td class="text">&nbsp;&nbsp;<b>id</b></td>
                        <td class="text">&nbsp;&nbsp;<b>nom</b></td>
                        <td class="text"></td>
		</tr>
		<tr>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
		</tr>
                <c:forEach var="group" items="${groups}">
                <c:if test="${group.id != 'visitors' }">
                        <c:set var="gid" value="${group.id}" scope="page"/>
			<tr>
				<td width="300" class="text">
					<img src="images/puce.gif">&nbsp;
					<c:choose>
                                                <c:when test="${canAdminSecurity!=null}">
                                                        <a onclick="return confirm('voulez-vous éditer ce groupe ?');" href="editGroup.x?id=<c:out value="${group.id}"/>" target="sub" style="position:relative;top:-2px"><c:out value="${group.id}"/></a>
						</c:when>
						<c:otherwise>
                                                        <c:out value="${group.id}"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td width="300" class="text">
                                        <c:out value="${group.metaData['name']}" default="<em>(sans nom)</em>" escapeXml="false"/>
				</td>
				<!-- supprimer / delete -->
				<td align="right" height="20">
                                        <c:if test="${canAdminSecurity!=null && group.id != 'admins'}">
                                        <% if( "admin".equals(session.getAttribute("userLogin")) || !pageContext.getAttribute("gid").toString().startsWith("_"))  { %>
                                                <a onclick="return confirm('voulez-vous supprimer ce groupe ?');" href="deleteGroup.x?id=<c:out value="${group.id}"/>" target="sub"><img src="images/poubelle.gif" border="0" alt="supprimer"></a>
                                        <%}%>
					</c:if>
				</td>
			</tr>
			<tr>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
			</tr>
                </c:if>
		</c:forEach>
	</table>

</body>

</html>
<ion:close/>
