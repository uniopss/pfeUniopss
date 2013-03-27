<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/controls.tld" prefix="controls" %>
<html>

<!-- Standard Content -->

<head>
  <title>tree</title>
  <link rel="stylesheet" type="text/css" href="styles/tree.css">
  <link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<!-- Body -->
<body bgcolor="white" background="images/fond.gif" class="text">
<form id=ress name=ress action="resourceManagement.x" target="sub">
<!-- Tree Component -->
<table cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<td valign="top" class="text">
			<img src="images/resource-tile.gif" align="absmiddle">
                        <span class="title" style="font-size:15px">
			<bean:message key="resource.resourceMenu.title"/>
			</span>
                        <br><br>
                        <br><br>
                                        <select name="selectedSite" style="width=150" onChange="document.ress.submit();">
						<c:forEach items="${currentSite}" var="index">
                                                        <option value="<c:out value="${index.id}"/>" <c:if test="${index.id==selectedSite}">selected</c:if>><c:out value="${index.metaData['name']}"/></option>
						</c:forEach>
					</select>
			<br><br>
                        <input type=hidden name=selectedType value="<c:out value="${selectedType}"/>">
                        <c:forEach var="res" items="${resources}">
				<img align="absmiddle" src="images/<c:out value="${res.icon}"/>">
				<c:choose>
                                        <c:when test="${selectedType==res.id}">
                                                <a href="#"  onclick="document.ress.selectedType.value='<c:out value="${res.id}"/>';document.ress.submit();" class="text"><c:out value="${res.label}"/></a><br>
					</c:when>
					<c:otherwise>
                                                <a href="#" onclick="document.ress.selectedType.value='<c:out value="${res.id}"/>';document.ress.submit();" class="tree-text"><c:out value="${res.label}"/></a><br>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</td>
		<td valign="top" class="text">
                        <iframe id="callout" name="callout" src="/backoffice/edx/browser.jsp?action=<c:out value="${param['action']}"/>&selectedSite=<c:out value="${selectedSite}"/>&selectedType=<c:out value="${selectedType}"/>"
                        frameborder="0" marginheight="3"
                        marginwidth="3" height="540" width="830" style="border:1px solid black;"/>
		</td>
	</tr>
</table>
</form>
</body>

<!-- Standard Footer -->

</html>
