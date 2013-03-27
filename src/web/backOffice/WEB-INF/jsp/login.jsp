<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
	<title>login</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body background="images/fond.gif" style="cursor:default" class='text'>
<%
String ua = request.getHeader( "User-Agent" );
if ( ua != null && ( ua.indexOf( "MSIE" ) != -1 )) {%>
<html:form action="login">
        <script>
        if (parent && parent.document && parent.document!=document)
                parent.document.title="Interface de contribution CMS";
        if (parent && parent.frames['banner'])
                parent.frames['banner'].document.location.reload();
        </script>
	<br><br>
	<table align="center" border="0" cellpadding="0" cellspacing="0" width="500">
		<tr>
			<td rowspan="3" align=center >
				<img src="images/login_content-fuzion.jpg" align="absmiddle">
			</td>
			<td colspan="3" align=center >
              <span class="title" style="font-size:24px"><span style="color:black">Interface</span> de contribution</span>
				<br><br>
			</td>
		</tr>
		<tr><td></td>
			<td class="text" width="100" align="right"><bean:message key="login.login"/> : &nbsp;</td>
            <td width="350"><html:text property="login" style="height:17px;"/>&nbsp;&nbsp;<span class="error"><html:errors property="login"/></span></td>
		</tr>
		<tr><td></td>
			<td align="right" class="text"><bean:message key="login.password"/> : &nbsp;</td>
			<td><html:password property="password" style="height:17px;"/>&nbsp;&nbsp;<span class="error">&nbsp;<input type="submit" class="button" value="entrer"><html:errors property="password"/></span></td>
		</tr>
	</table>

</html:form>
<%
}
else {
out.println("<center><img src='images/warning.gif' align='absmiddle'> vous devez posséder Microsoft Internet Explorer 6</center>");
}
%>
</body>

</html>
