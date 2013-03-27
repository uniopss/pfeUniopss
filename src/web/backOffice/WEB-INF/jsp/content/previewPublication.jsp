<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title><bean:message key="content.previewPublication.title"/></title>
	<link rel="stylesheet" type="text/css" href="/resources/commons/css/style.css">
</head>

<body background="images/fond.gif">

<table width="95%">
	<tr>
		<td valign="top">
			<form action="previewPublication.x" method="post" id="form">
				<input type="Hidden" name="sid" value="<c:out value="${param['sid']}"/>">
				<input type="Hidden" name="id" value="<c:out value="${param['id']}"/>">
				<input type="Hidden" name="version" value="<c:out value="${param['version']}"/>">
				<a style="cursor:default" onclick="return confirm('voulez-vous supprimer le cache de cette publication ?');" href="uncachePublication.x?sid=<c:out value="${param['sid']}"/>&id=<c:out value="${param['id']}"/>&version=<c:out value="${param['version']}"/>" ><img src="images/preview-tile.gif" align="absmiddle" border="0"></a>
				<span style="font-family:verdana; color:#5d5d5d; font-size:15px; font-weight:bold; position:relative; top:-4px;">Preview de "<c:out value="${publication.metaData['name']}"/>" (version <c:out value="${param['version']}"/>)</span>
				<table width="100%">
					<tr>
						<td valign="top">
                        	<span style="font-family:verdana; color:#0148B2; font-size:10px; font-weight:bold;">&nbsp;Choisissez le modèle de présentation pour cet aperçu :&nbsp;</span>
						</td>
						<td valign="top" width="60%">
							<select name="style" style="font-family:verdana; color:black; font-size:9px; font-weight:bold; border:1px solid black; background-color:#d6e4f3;" onChange="form.submit()">
								<c:forEach var="style" items="${styles}">
									<option value="<c:out value="${style}"/>" <c:if test="${style==param['style']}">selected</c:if>><c:out value="${style}"/></option>
								</c:forEach>
							</select>
							&nbsp;&nbsp;
							<c:choose><c:when test="${mL=='1'}"><span style="font-family:verdana; color:#0148B2; font-size:10px; font-weight:bold;">Langue : <select name="locale" style="font-family:verdana; color:black; font-size:9px; font-weight:bold; border:1px solid black; background-color:#d6e4f3;" onChange="form.submit()"><c:forEach var="localeItem" items="${locales}"><option value="<c:out value="${localeItem.locale}"/>" <c:if test="${localeItem.locale==param['locale']}">selected</c:if>><c:out value="${localeItem.name}"/></option></c:forEach></select></span></c:when><c:otherwise><input type="hidden" name="locale" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td valign="top">
				<hr width="100%" size="1" noshade color="#949494">
		</td>
	</tr>
	<tr>
		<td class="blocBlanc">
			<table width="100%" cellspacing="0" cellpadding="2">																					
					<c:out value="${view}" escapeXml="false"/>
			</table>
		</td>
	</tr>
</table>

</body>

</html>
