<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
        <title>edit Site</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif" style="cursor:default">

        <img src="images/site-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="site.edit"/>
	</span>
	<br><br>
                <span class="title-list">Propriétés</span>
		<hr width="100%" size="1" noshade color="#949494">
                <table width="600">
                        <html:form action="editSite.x">
			<tr>
                                <td align="right" class="text">ID :&nbsp;</td>
                                <td valign="middle"><html:hidden property="id"/><html:text property="id" disabled="true"/>&nbsp;</td>
			</tr>
			<tr>
                                <td align="right" class="text">Nom :&nbsp;</td>
                                <td valign="middle"><html:text property="name"/>&nbsp;<span class="error"><html:errors property="name"/></span></td>
			</tr>
			<tr>
                                <td align="right" class="text" colspan="2">&nbsp;</td>
			</tr>
			<tr>
                                <td align="right" class="text" colspan="2">&nbsp;</td>
			</tr>
                </table>
		<br><br>
                <input onclick="return confirm('Voulez-vous sauvegarder ?')" type="submit" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/>
	</html:form>

</body>

</html>
