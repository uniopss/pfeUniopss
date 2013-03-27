<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Liste de publications</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" onload='top.resizeTo(600,400);setBallon("BallonTip");' class="text">
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
<script language="javascript">
function lcC(id) {
        eval(top.opener.<c:out value="${param['action']}"/>(id));
        self.close();
}
</script>
        <form name="lc" action="listPublication.x" method="POST" onsubmit="if (!document.lc.keyWords.value) {alert('Veuillez saisir un mot clé avant de confirmer.');return false;}">
        <input type="hidden" name="action" value="<c:out value="${param['action']}"/>">
                <table cellpadding="3" cellspacing="3">
                        <tr valign="center">
				<td align="right" class="text">
					<b>
                                                Mots clés :&nbsp;
					</b>
				</td>
                                <td class="text"  valign="center">
                                        <input type="text" value="<c:out value="${param['keyWords']}"/>" name="keyWords" style="width=100" align="absmiddle">
                                        &nbsp;<c:choose><c:when test="${mL=='1'}"><select name="index" align="absmiddle" style="width=50"><c:forEach items="${indexs}" var="index"><option value="<c:out value="${index}"/>" <c:if test="${index==param['index']}">selected</c:if>><c:out value="${index}"/></option></c:forEach></select></c:when><c:otherwise><input type="hidden" name="index" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
                                        &nbsp;dans&nbsp;<select name="selectedSite" style="width=150" valign="absmiddle"><c:forEach items="${currentSite}" var="index"><option value="<c:out value="${index.id}"/>" <c:if test="${index.id==param['selectedSite']}">selected</c:if>><c:out value="${index.metaData['name']}"/></option></c:forEach></select>
                                        &nbsp;<input type="submit" align="absmiddle" value="<bean:message key="search.search.searchbutton"/>" class="button" align="absmiddle"/>
				</td>
			</tr>
		</table>
	</form>
	<!-- publications list -->
	<span class="title-list"><bean:message key="search.result"/></span>
	<hr width="100%" size="1" noshade color="#949494">
	<c:choose>
		<!-- il y a des publications à afficher, articles to show -->
		<c:when test="${result!=null}">
                        <table border="0" cellpadding="3" cellspacing="2" width="100%" style="border:1px solid black;">
                                <tr class=text align="left"><th></th><th>Nom</th><th>Version</th><th>Type</th><th>Section</th></tr>
                                <% java.util.Vector v = new java.util.Vector(); %>
				<c:forEach var="r" items="${result}" varStatus="status">
                                        <tr align="center">
                                                <!-- nom (lien) -->
                                                <td class="text">
                                                        <a  href="#infos"
                                                                <c:if test="${r.type != 'objet'}">onclick="window.open('viewPublication.x?id=<c:out value="${r.id}"/>&version=<c:out value="${r.version}"/>&sid=<c:out value="${r.section}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"</c:if>
                                                                ><img src="images/infos.gif" border="0"></a>
                                                </td>
                                                <td class="text" align="left">
                                                        <a href="javascript:lcC('/publication/<c:out value="${r.id}"/>.html')">
                                                                <c:out value="${r.name}"/>
                                                        </strong></a>
                                                </td>
                                                <td class="text" align="left"><c:out value="${r.version}"/></td>
                                                <td class="text" align="left"><c:out value="${r.type}"/></td>
                                                <td class="text" align="left">
                                                                <c:out value="${r.sections}"/>
                                                </td>
                                        </tr>
				</c:forEach>
			</table>
		</c:when>
		<!-- il n'y a pas de publications à afficher, no articles to show -->
		<c:otherwise>
			<i>(<bean:message key="search.noResult"/>)</i>
		</c:otherwise>
	</c:choose>

</body>
</html>
