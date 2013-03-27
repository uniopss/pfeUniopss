<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
        <title>edit Group</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif" style="cursor:default">

        <img src="images/group-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="editGroup.title"/>
	</span>
	<br><br>
                <span class="title-list"><bean:message key="editGroup.properties"/></span>
		<hr width="100%" size="1" noshade color="#949494">
		<table>
                        <html:form action="editGroup.x">
			<tr>
                                <td align="right" class="text"><bean:message key="editGroup.label.id"/> :&nbsp;</td>
                                <td valign="middle"><html:hidden property="id"/><html:text property="id" disabled="true"/>&nbsp;</td>
			</tr>
			<tr>
                                <td align="right" class="text"><bean:message key="editGroup.label.name"/> :&nbsp;</td>
                                <td valign="middle"><html:text property="name"/>&nbsp;<span class="error"><html:errors property="name"/></span></td>
			</tr>
			<%--
			<tr>
                                <td align="right" class="text"><bean:message key="editGroup.userList"/> :&nbsp;</td>
                                <td valign="middle">
                                        <html:select property="users" multiple="true" size="15">
                                                <html:options property="login" labelProperty="login" collection="userList"/>
                                        </html:select>
                                        <c:if test="${userLogin != 'admin'}"><script>
                                        for (i=0 ; i < document.forms[0].users.length ; i++) {
                                               if(document.forms[0].users[i].value == 'admin' || document.forms[0].users[i].value == 'visiteurAnonyme')
                                                     document.forms[0].users[i] = null ;
                                        }
                                        </script
                                        </c:if>
                                </td>
			</tr>
			--%>
                </table>
		<br><br>
                <input onclick="return confirm('Voulez-vous sauvegarder ?')" type="submit" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/>
	</html:form>

</body>

</html>
