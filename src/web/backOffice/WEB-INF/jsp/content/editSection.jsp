<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif">

	<img src="images/section-tile.gif" align="absmiddle">
	<span class="title">
                <bean:message key="editSection.title"/> " <c:out value="${section.metaData['name']}" default="${section.id}" escapeXml="false"/> "
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="title-list"><c:out value="${section.parent.pathLabel}" default="" escapeXml="false"/></span>
        </span>
	<br>
	<hr width="100%" size="1" noshade color="#949494">
	<html:form action="editSection.x">
                <input type="Hidden" name="sid" value="<c:out value="${sid}"/>">
		<input type="Hidden" name="id" value="<c:out value="${section.id}"/>">
                <table cellpadding="3" cellspacing="3">
                        <tr>
                                <td align="right" class="text">ID :&nbsp;</td>
                                <td valign="middle"><input type=text disabled=true value="<c:out value="${section.id}"/>">&nbsp;</td>
                        </tr>
                        <tr>
                                <td align="right" class="text">Nom :&nbsp;</td>
                                <td valign="middle"><html:text property="name" style="width:350px"/>&nbsp;</td>
                        </tr>
			<tr>
                                <td align="right" class="text">Workflow :&nbsp;</td>
				<td class="text">
					<html:select property="workflow" size="1" style="height:17px;">
						<html:options name="workflows" labelName="workflows"/>
					</html:select>
					&nbsp;
				</td>
			</tr>
			<tr>
                                <td align="right" class="text">Statut :&nbsp;</td>
				<td>
					<html:select property="status" size="1" style="height:17px;">
                                                <html:option value="online">online</html:option>
                                                <html:option value="offline">offline</html:option>
					</html:select>
					&nbsp;<span class="error"><html:errors property="status"/></span>
				</td>
			</tr>
			<tr>
                                <td align="right"  class="text">Template :&nbsp;</td>
				<td>
                                        <c:set var="lcSite" value="${section.rootSection}" scope="request"/>
                                        <html:select property="template" size="1" style="height:17px;" onchange="document.forms[0].submit()">
                                                <html:option value=""></html:option>
                                                <c:forEach var="template" items="${types}">
                                                                        <c:set var="tempVar" value="${template.template}" scope="request"/>
                                                                        <% String tempVar2 = request.getAttribute("tempVar")+""; // y a des problémes quelque part ???!! %>
                                                                        <html:option value="<%=tempVar2%>"><c:out value="${template.name}"/> [<%=tempVar2%>]</html:option>
                                                </c:forEach>                                                
                                                
                                        </html:select>&nbsp;<span class="error"><html:errors property="template"/></span>
                                </td>
			</tr>
                        <c:if test="${type.propertiesNumber>0}">
                        <tr >
                                <td align="right" valign="top" class="text">Propriétés :&nbsp;</td>
				<td>
                                        <table style="border:1px solid black;font-size:11px; font-weight:bold; color:#006699">
                                                <c:forEach var="property" items="${type.properties}">
                                                        <tr>
                                                                <td align="right" class="text">
                                                                        <c:out value="${property.label}"/>&nbsp;:&nbsp;
                                                                </td>
                                                                <td class="text" align="left">
                                                                        <c:choose>
                                                                                <c:when test="${copyMeta==null}">
                                                                                        <input type="text" style="width:300px" name="META_<c:out value="${property.name}"/>" value="<c:out value="${section.metaData[property.name]}"/>">
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                        <c:set var="temp_temp" scope="request" value="${property.name}"/>
                                                                                        <input type="text" style="width:300px" name="META_<c:out value="${property.name}"/>" value="<%=request.getAttribute("META_"+request.getAttribute("temp_temp"))%>">&nbsp;
                                                                                        <span class="error">
                                                                                                <c:set var="temp2" value="META_${property.name}" scope="request"/>
                                                                                                <% String temp23 = request.getAttribute("temp2")+""; %>
                                                                                                <html:errors property="<%=temp23%>"/>
                                                                                        </span>
                                                                                </c:otherwise>
                                                                        </c:choose>
                                                                </td>
                                                        </tr>
                                                </c:forEach>
                                        </table>
                                        &nbsp;<span class="error"><html:errors property="template"/></span>
                                </td>
			</tr>
                        </c:if>
		</table>
		<br><br>
                <input type="submit" onclick="return confirm('Voulez-vous sauvegarder ?')" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/>
	</html:form>

</body>

</html>
