<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>list Jobs</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body class="text" background="images/fond.gif">
<c:if test="${userLogin=='admin'}">
        <img src="images/work-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="job.title"/>
	</span>
        <table border="0" cellpadding="0" cellspacing="2" width="800" style='border:1px solid black;background-color:#F5F5F5'>
                <tr align="center">
                        <td class="text"><a href="listJobs.x?job=MainJob">&nbsp;Main Job</a></td>
                        <td class="text"><a href="listJobs.x?job=IndexJob">&nbsp;Index Job</a></td>
                        <td class="text"><a href="listJobs.x?job=ServiceJob">&nbsp;Service Job</a></td>
                        <td class="text"><a href="listJobs.x?job=ImportJob">&nbsp;Import Job</a></td>
                <tr>
	</table>
<br/>
<iframe id="callout" name="callout" src="/backoffice/edx/browser.jsp?action=_admin"
frameborder="0" marginheight="3"
marginwidth="3" height="530" width="800" style="border:1px solid black;"/>
</c:if>
</body>

</html>
