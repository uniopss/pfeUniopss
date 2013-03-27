<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
        <title>create Group</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif" style="cursor:default">

        <img src="images/group-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="createGroup.title"/>
	</span>
	<br><br>
                <span class="title-list"><bean:message key="createGroup.properties"/></span>
		<hr width="100%" size="1" noshade color="#949494">
		<table>
                        <html:form action="createGroup.x">
			<tr>
                                <td align="right" class="text"><bean:message key="createGroup.label.id"/> :&nbsp;</td>
                                <td valign="middle"><html:text property="id"/>&nbsp;<span class="error"><html:errors property="id"/></span></td>
			</tr>
			<tr>
                                <td align="right" class="text"><bean:message key="createGroup.label.name"/> :&nbsp;</td>
                                <td valign="middle"><html:text property="name"/>&nbsp;<span class="error"><html:errors property="name"/></span></td>
			</tr>
			<%--
			<tr>
                                <td align="right" class="text"><bean:message key="createGroup.userList"/> :&nbsp;</td>
                                <td valign="middle">
                                        <html:select property="users" multiple="true" size="15">
                                        <c:forEach var="user" items="${userList}">
                                                <c:if test="${user.login != 'visiteurAnonyme' && (userLogin == 'admin' || user.login != 'admin') }">
                                                        <c:set var="tempVar" value="${user.login}" scope="request"/>
                                                        <% String tempVar2 = request.getAttribute("tempVar")+""; // y a des problémes quelque part ???!! %>
                                                        <html:option value="<%=tempVar2%>"><c:out value="${user.login}"/></html:option>
                                                </c:if>
                                        </c:forEach>
                                        </html:select>
                                </td>
			</tr>
			--%>
                </table>
		<br><br>
                <input onclick="return confirm('Voulez-vous sauvegarder ?')" type="submit" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/>
	</html:form>

</body>

</html>
