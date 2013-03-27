<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
	<title><bean:message key="content.historyPublication.title"/></title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body style="cursor:default" background="images/fond.gif">
<a style="cursor:default" onclick="return confirm('voulez-vous reinitialiser l historique de cette publication ?');" href="deleteHistoryPublication.x?sid=<c:out value="${param['sid']}"/>&id=<c:out value="${param['id']}"/>&version=<c:out value="${param['version']}"/>" ><img src="images/history-tile.gif" align="absmiddle" border="0"></a>
<span style="font-family:verdana; color:#5d5d5d; font-size:15px; font-weight:bold; position:relative; top:-4px;">
        Historique de "<c:out value="${pub}"/>" (version <c:out value="${param['version']}"/>)
</span>
<br>
<table width="95%">
	<tr>
		<td class="text">
			<b><bean:message key="content.historyPublication.status"/></b>
		</td>
		<td class="text">
			<b><bean:message key="content.historyPublication.user"/></b>
		</td>
		<td class="text">
			<b><bean:message key="content.historyPublication.begin"/></b>
		</td>
		<td class="text">
			<b><bean:message key="content.historyPublication.end"/></b>
		</td>
	</tr>
	<tr>
		<td height="1" bgcolor="black"></td>
		<td height="1" bgcolor="black"></td>
		<td height="1" bgcolor="black"></td>
		<td height="1" bgcolor="black"></td>
	</tr>
	<c:forEach items="${current}" var="step">
		<tr bgcolor="#F6F5F6">
			<td class="text" style="color:#0078EB">
				<strong><c:out value="${step.name}"/></strong>
			</td>
			<td class="text" style="color:#0078EB" >
				<strong><c:out value="${step.owner}"/></strong>
				<c:set var="author" value="${step.owner}"/>
			</td>
			<td class="text" style="color:#0078EB">
				<strong><c:out value="${step.startDate}"/>&nbsp;(<c:out value="${step.startTime}"/>)</strong>
				<c:set var="creationTime" value="${step.startDate} (${step.startTime})"/>
			</td>
			<td class="text"></td>
		</tr>
	</c:forEach>
	<c:forEach items="${history}" var="step">
		<tr bgcolor="#F6F5F6">
			<td class="text">
				<c:out value="${step.name}"/>
			</td>
			<td class="text">
				<c:out value="${step.owner}"/>
				<c:set var="author" value="${step.owner}"/>
			</td>
			<td class="text">
				<c:out value="${step.startDate}"/>&nbsp;(<c:out value="${step.startTime}"/>)
				<c:set var="creationTime" value="${step.startDate} (${step.startTime})"/>
			</td>
			<td class="text">
				<c:out value="${step.finishDate}"/>&nbsp;(<c:out value="${step.finishTime}"/>)
			</td>
		</tr>
	</c:forEach>
	<tr bgcolor="#F6F5F6">
		<td class="text">
			<bean:message key="content.historyPublication.creation"/>
		</td>
		<td class="text">
			<c:out value="${author}"/>
		</td>
		<td class="text">
			<c:out value="${creationTime}"/>
		</td>
		<td class="text">
			<c:out value="${creationTime}"/>
		</td>
	</tr>
</table>

</body>

</html>
