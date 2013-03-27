<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
	<title>delete user</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif" style="cursor:default">

	<img src="images/deleteUser-tile.gif" align="absmiddle">
	<span class="title">
		<bean:message key="security.deleteUser.title"/>
	</span>
	<br><br>

	<form action="deleteUser.x">
		<input type="hidden" name="id" value="<c:out value="${param['id']}"/>">
		<span class="title-list"><bean:message key="security.deleteUser.warning"/>
                L'utilisateur "<c:out value="${param['id']}"/>" va être supprimé</span>
		<hr width="100%" size="1" noshade color="#949494">

		<span class="text">
                        Choississez un autre utilisateur qui reprendra ses contenus :
			<select size="1" name="newUser">
				<c:forEach items="${users}" var="user">
                                        <c:if test="${user.login!=param['id'] && user.login != 'visiteurAnonyme' && (userLogin == 'admin' || user.login != 'admin') }">
						<option value="<c:out value="${user.login}"/>"><c:out value="${user.login}"/></option>
					</c:if>
				</c:forEach>
			</select>
		</span>
		<br><br><br><br>
                <input onclick="return confirm('Voulez-vous supprimer cet utilisateur ?')" type="submit" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/>
	</form>

</body>

</html>
