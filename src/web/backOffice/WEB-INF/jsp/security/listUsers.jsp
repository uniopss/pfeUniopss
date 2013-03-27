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
	<title>list Users</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif">

        <img src="images/user-tile.gif" align="absmiddle">
	<span class="title">
		<bean:message key="listUsers.title"/>
	</span>
	<br><br>
	<span class="title-list"><bean:message key="listUsers.list"/></span>
	<c:if test="${canAdminSecurity!=null}">
                ( <img src="images/create.gif" align="absmiddle" border="0"> <a onclick="return confirm('voulez-vous créer un nouvel utilisateur ?');" href="createUser.x" target="sub"><bean:message key="listUsers.create"/></a> )
	</c:if>
        <P>
	<table border="0" cellpadding="0" cellspacing="2" width="100%">
	 	<tr>
                        <td class="text"><b>login</b></td>
                        <td class="text"><b>nom</b></td>
                        <td class="text"><b>email</b></td>
                        <td class="text"><b>groupes</b></td>
						<td class="text"><b>état</b></td>
                        <td class="text"></td>
		</tr>
		<tr>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
			<td height="1" bgcolor="#949494"></td>
		</tr>
		<c:forEach var="user" items="${users}">
                <c:if test="${user.login != 'visiteurAnonyme' && (userLogin == 'admin' || user.login != 'admin') }">
			<tr>
				<!-- login (link) -->
				<td width="300" class="text">
					<img src="images/puce.gif">&nbsp;
					<c:choose>
                                                <c:when test="${canAdminSecurity!=null}">
                                                        <a onclick="return confirm('voulez-vous éditer cet utilisateur ?');" href="editUser.x?id=<c:out value="${user.login}"/>" target="sub" style="position:relative;top:-2px"><c:out value="${user.login}"/></a>
						</c:when>
						<c:otherwise>
							<c:out value="${user.login}"/>
						</c:otherwise>
					</c:choose>
				</td>
				<!-- nom complet / complete name -->
				<td width="300" class="text">
					<c:out value="${user.metaData['name']}" default="<em>(sans nom)</em>" escapeXml="false"/>
				</td>
				<!-- email -->
				<td class="text" width="300">
					<c:choose>
						<c:when test="${user.metaData['email']!=null}">
							<a href="mailto:<c:out value="${user.metaData['email']}"/>"><c:out value="${user.metaData['email']}" default="<em>(sans email)</em>" escapeXml="false"/></a>
						</c:when>
						<c:otherwise>
							<c:out value="${user.metaData['email']}" default="<em>(sans email)</em>" escapeXml="false"/>
						</c:otherwise>
					</c:choose>
				</td>
				<!-- groupes -->
				<td class="text" width="400">
					<% boolean virgule=false; %>
					<c:forEach var="group" items="${user.groups}">
                                                <% if( virgule ) { %>,&nbsp;<%}%><c:out value="${group.metaData['name']}" default="${group.id}"/><%virgule=true;%>
					</c:forEach>
				</td>
				<td class="text" align="right" height="20">
					<c:choose>
						<c:when test="${user.metaData['active']!=null && user.metaData['active']}">
							<bean:message key="listUsers.actif"/>
						</c:when>
						<c:otherwise>
							<bean:message key="listUsers.inActif"/>
						</c:otherwise>
					</c:choose>
				</td>

				<!-- supprimer / delete -->
				<%--
				<td align="right" height="20">
                     <c:if test="${canAdminSecurity!=null&&user.login!=userLogin&& user.login != 'admin'}">
                           				<a onclick="return confirm('voulez-vous supprimer cet utilisateur ?');" href="deleteUser.x?id=<c:out value="${user.login}"/>" target="sub"><img src="images/poubelle.gif" border="0" alt="supprimer"></a>
					</c:if>
				</td>
				--%>
			</tr>
			<tr>
				<td height="1" bgcolor="#dddddd"></td>
				<td height="1" bgcolor="#dddddd"></td>
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
