<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
        <title>myWork</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='setBallon("BallonTip");' background="images/fond.gif">
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
        <img src="images/mywork-tile.gif" align="absmiddle">
	<span class="title">
		<bean:message key="myWork.title"/>
	</span>
	<br><br>
        <form id="form" name="form" method="post">
        <span class="title-list">Etat :</span>&nbsp;
        <select name="etat" style="font-family:verdana; color:black; font-size:9px; font-weight:bold; border:1px solid black; background-color:#d6e4f3;" onChange="form.submit()">
                <option value="" />
                <option value="brouillon" <c:if test="${'brouillon'==param['etat']}">selected</c:if>>En brouillon</option>
                <option value="édition" <c:if test="${'édition'==param['etat']}">selected</c:if>>En édition</option>
                <option value="attente" <c:if test="${'attente'==param['etat']}">selected</c:if>>En attente de validation</option>
                <option value="validé" <c:if test="${'validé'==param['etat']}">selected</c:if>>Validé</option>
        </select>
        &nbsp;dans&nbsp;<select onChange="form.submit()" name="selectedSite" style="width=150" valign="absmiddle"><c:forEach items="${currentSite}" var="index"><option value="<c:out value="${index.id}"/>" <c:if test="${index.id==param['selectedSite']}">selected</c:if>><c:out value="${index.metaData['name']}"/></option></c:forEach></select>
        </form>
        <c:if test="${param['etat']!=null&&param['etat']!=''}">
	<c:choose>
		<!-- il y a des publications à afficher, articles to show -->
		<c:when test="${result!=null}">
                        <img src="images/publication<c:choose><c:when test="${param['etat']==null || param['etat']=='brouillon'}">-create</c:when><c:when test="${param['etat']=='édition'}">-lock</c:when><c:when test="${param['etat']=='attente'}">-wait</c:when><c:when test="${param['etat']=='validé'}">-published</c:when></c:choose>.gif" align="absmiddle"> &nbsp;
                        <hr width="100%" size="1" noshade color="#949494">
                        <table border="0" cellpadding="3" cellspacing="2" width="100%" style="border:1px solid black;">
                                <tr class=text align="left"><th></th><th>Sections</th><th>Nom</th><th>Version</th><th>Type</th><th>Section</th><th></th></tr>
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
												<c:forEach var="sectionName" items="${r.allSectionNames}" varStatus="sectionNamestatus">
														<c:out value="${sectionName}"/>
														<c:if test="${!sectionNamestatus.last}"> <br/> </c:if>
												</c:forEach>
												</td>
                                                <td class="text" align="left">
                                                        <a onclick="parent.frames['banner'].document.location='menu.x?tab=1'" href="highlight.x?id=<c:out value="${r.id}"/>" style="text-decoration:none"><strong style="color:#5e5e5e;">
                                                                <c:out value="${r.name}"/>
                                                        </strong></a>
                                                </td>
                                                <td class="text" align="left"><c:out value="${r.version}"/></td>
                                                <td class="text" align="left"><c:out value="${r.type}"/></td>
                                                <td class="text" align="left">
                                                                <c:forTokens var="sectionName" items="${r.sections}" delims=" ">
                                                               			<c:out value="${sectionName}" /><br/>
                                                                </c:forTokens>

                                                </td>

                                                <!-- history -->
                                                <td width="30" align="center">
                                                        <a href="#null" title="historique" onclick="window.open('historyPublication.x?id=<c:out value="${r.id}"/>&version=<c:out value="${r.version}"/>','history','width=600,height=250,resizable=yes,scrollbars=yes')"><img src="images/historique.gif" border="0"></a>
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
        </c:if>

</body>

</html>




