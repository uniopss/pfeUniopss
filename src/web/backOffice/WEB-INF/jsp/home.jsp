<%@ taglib uri='/WEB-INF/tlds/struts-template.tld' prefix='template' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>


<html>
<head>
	<title>home</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body onload='' background="images/fond.gif" style="cursor:default">
<table width="100%" height="170" cellpadding="0" cellspacing="0">
	<tr>
                <td width="63%" valign="center" class="text">
			<img src="images/login-tile.gif" align="absmiddle">
                        <span class="title" style="font-size:24px"><span style="color:black">Interface</span> de contribution</span>
                        <br><br><br><br>
			<table width="95%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="text">
                                                <!--b style="font-size:10px"><bean:message key="home.headline"/></b>
                                                <br><br>
                                                <bean:message key="home.welcometext"/>-->
                                                <c:if test="${userLogin=='admin'}">
                                                <template:insert template='message/liste.jsp'/>
                                                </c:if>
                                        </td>
				</tr>
			</table>
		</td>
		<td width="2%" align="left">
			<img src="images/gris.gif" height="100%" width="1">
		</td>
		<td width="35%" valign="top">
			<a href="contentManagement.x" onclick="parent.frames['banner'].document.location='menu.x?tab=1'"><img src="images/content-tile.gif" align="middle" border="0"><bean:message key="home.createContent"/></a><br>
			<a href="resourceManagement.x" onclick="parent.frames['banner'].document.location='menu.x?tab=1'"><img src="images/resource-tile.gif" align="middle" border="0"><bean:message key="home.resources"/></a><br>
			<a href="search.x" onclick="parent.frames['banner'].document.location='menu.x?tab=2'"><img src="images/search-tile.gif" align="middle" border="0"><bean:message key="home.searchContent"/></a><br>
			<a href="myProfile.x"><img src="images/user-tile.gif" align="middle" border="0"><bean:message key="home.myProfil"/></a><br>
		</td>
	</tr>
</table>
</body>

</html>
