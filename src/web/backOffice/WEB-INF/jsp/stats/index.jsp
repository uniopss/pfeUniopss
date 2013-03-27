<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ page import="java.util.Calendar" %>
<html>
<head>
        <title>Statistiques</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body class="text" background="images/fond.gif">

        <img src="images/stats-tile.gif" align="absmiddle">
	<span class="title">
                Statistiques
	</span>
	
	<form action="statRegion.x" method="post">
		<html:hidden property="postBack" value="true" />
		<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td width="12%">Region :</td>
					<td>
						<html:select name="statRegionForm" property="selectedSite" styleClass="SelectRechercheLarge" > 
							<c:forEach var="site" items="${statRegionForm.sites}">
									<option value="<c:out value="${site.value}" />"
										<c:if test="${statRegionForm.selectedSite==site.value}">selected</c:if>>
											<c:out value="${site.label}" />
									</option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<td width="12%">Année :</td>
					<td>
						<html:select name="statRegionForm" property="selectedYear" styleClass="SelectRechercheLarge" onchange="document.forms[0].submit()" > 
							<c:forEach var="year" items="${statRegionForm.years}">
									<option value="<c:out value="${year.value}" />" <c:if test="${statRegionForm.selectedYear==year.value}">selected</c:if> >
											<c:out value="${year.label}" />
									</option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<td width="12%">Mois :</td>
					<td>
						<html:select name="statRegionForm" property="selectedMonth" styleClass="SelectRechercheLarge" > 
							<c:forEach var="month" items="${statRegionForm.months}">
									<option value="<c:out value="${month.value}" />" <c:if test="${statRegionForm.selectedMonth==month.value}">selected</c:if> >
											<c:out value="${month.label}" />
									</option>
							</c:forEach>
						</html:select>
					</td>
				</tr>	
				<tr>
					<td width="12%">&nbsp;</td>
					<td>
						<input type="submit" name="Valider" /></td>
					</td>
				</tr>	
		</table>
	</form>
	<br><br>
        <iframe id="stats" name="stats" src="<bean:write name="statRegionForm" property="iframePath"/>" frameborder="0" marginheight="3"
                marginwidth="3" width="100%" height="500" style="border:1px solid black;"/>
</body>

</html>
