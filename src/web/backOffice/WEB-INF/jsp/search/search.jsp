<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>



<html>
<head>
	<title>search</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='setBallon("BallonTip");' background="images/fond.gif">
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
	<img src="images/search-tile.gif" align="absmiddle">
	<span class="title">
		<bean:message key="search.title"/>
	</span>
	<br>
        <form name="lc" action="search.x" method="POST" onsubmit="if (!document.lc.keyWords.value) {alert('Veuillez saisir un mot clé avant de confirmer.');return false;}">
                <table cellpadding="3" cellspacing="3">
                        <tr >
				<td align="right" class="text">
					<b>
                                                Mots clés :&nbsp;
					</b>
				</td>
                                <td class="text" >
                                        <input type="text" value="<c:out value="${param['keyWords']}"/>" name="keyWords" style="width=200" align="absmiddle">
                                        &nbsp;<c:choose><c:when test="${mL=='1'}"><select name="index" style="width=60" align="absmiddle"><c:forEach items="${indexs}" var="index"><option value="<c:out value="${index}"/>" <c:if test="${index==param['index']}">selected</c:if>><c:out value="${index}"/></option></c:forEach></select></c:when><c:otherwise><input type="hidden" name="index" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
                                        &nbsp;dans&nbsp;<select name="selectedSite" style="width=150" valign="absmiddle"><c:forEach items="${currentSite}" var="index"><option value="<c:out value="${index.id}"/>" <c:if test="${index.id==param['selectedSite']}">selected</c:if>><c:out value="${index.metaData['name']}"/></option></c:forEach></select>
                                        &nbsp;<input type="submit" value="<bean:message key="search.search.searchbutton"/>" class="button" align="absmiddle"/>
				</td>
			</tr>
		</table>
	</form>

	<!-- publications list -->
	<span class="title-list"><bean:message key="search.result"/></span>

        <c:if test="${userLogin=='admin'}">
                ( <img src="images/clef.gif" align="absmiddle">&nbsp;<a onclick="return confirm('Voulez-vous reindexer ?')" href="reIndex.x"><bean:message key="search.reindex"/></a> )
        </c:if>

	<hr width="100%" size="1" noshade color="#949494">
	<c:choose>
		<!-- il y a des publications à afficher, articles to show -->
		<c:when test="${result!=null}">
                        <table border="0" cellpadding="3" cellspacing="2" width="100%" style="border:1px solid black;">
                                <tr class=text align="left"><th></th><th>Nom</th><th>Version</th><th>Type</th><th>Section</th><th></th></tr>
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
                                                        <a onclick="parent.frames['banner'].document.location='menu.x?tab=1'" href="highlight.x?id=<c:out value="${r.id}"/>" style="text-decoration:none"><strong style="color:#5e5e5e;">
                                                                <c:out value="${r.name}"/>
                                                        </strong></a>
                                                </td>
                                                <td class="text" align="left"><c:out value="${r.version}"/></td>
                                                <td class="text" align="left"><c:out value="${r.type}"/></td>
                                                <td class="text" align="left">
                                                                <c:out value="${r.sections}"/>
                                                </td>
                                                <!-- panier -->
                                                <c:choose>
                                                <%-- <c:when test="${r.type == panier.type}"> --%>
												<c:when test="true">
                                                        <c:set var="zPanier" value="${r.versionId}"/>
                                                        <% pageContext.setAttribute("zPanier",((org.nextime.ion.backoffice.panier.Panier)session.getAttribute("panier")).isPresent(""+pageContext.getAttribute("zPanier"))?"1":"0"); %>
                                                        <form target="_panier" method="post" action="editPanier.x" name="panier_<c:out value="${r.versionId}"/>">
                                                        <td align="center" width="20" valign="top"><input type=hidden name=id value="<c:out value="${r.versionId}"/>">
                                                        <input type='checkbox' name='etat' value='<c:if test="${zPanier=='1'}">1</c:if>' <c:if test="${zPanier=='1'}">checked</c:if> onClick="this.checked=(this.checked?false:true);document.panier_<c:out value="${r.versionId}"/>.submit()">&nbsp;
                                                        </td>
                                                        </form>
                                                        <c:remove var="zPanier"/>
                                                </c:when>
                                                <c:otherwise>
                                                                <td width="20">&nbsp;</td>
                                                </c:otherwise>
                                                </c:choose>
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
