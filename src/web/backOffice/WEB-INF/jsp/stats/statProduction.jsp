<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%--
	Statistique de production
--%>
<html>
<head>
	<title>search</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif"  > <%-- onload='setBallon("BallonTip");' --%>
<script language="JavaScript" src="scripts/calendrier.js"></script>

<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
<script language="JavaScript" >
	function sendPostBack(){
		var frm = document.forms[0];
		frm.postBack.value = "false";
		frm.submit();
		
	}
	</script>

	<img src="images/search-tile.gif" align="absmiddle">
	<span class="title">
                Statistiques Production
	</span>
	
		<span class="error"><html:errors property="errorFewParam"/></span>
        <form name="statProdForm" action="statProd.x" method="POST" >
		<input type="hidden" name="postBack" value="true">
		<table cellpadding="3" cellspacing="3" width="600">
        	<tr valign="center">
					<td align="left" class="text" colspan=2>
							<b>Site :&nbsp;</b><select name="selectedSite" style="width=150" valign="absmiddle" onchange="sendPostBack();">
							<option value="">Tous</option>
							<c:forEach items="${currentSite}" var="index"><option value="<c:out value="${index.id}"/>" <c:if test="${index.id==param['selectedSite']}">selected</c:if>><c:out value="${index.metaData['name']}"/></option></c:forEach></select>
					</td>
			</tr>
			<tr valign="top" align="left ">
						<td align="left" class="text" >
								<table style="border:1px solid black;" width="600" cellspacing="0" cellpadding="0" border="0">
									<tr valign="center" colspan="3">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de création inférieure </td>
										<td nowrap="1" class="text" align="left">= <input name="dateCreationBeforeStr" type="text" value="<c:out value="${param['dateCreationBeforeStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('dateCreationBeforeStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>									
									<tr valign="center" colspan="3"> 
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de création supérieure </td>
										<td nowrap="1" class="text" align="left">= <input name="dateCreationAfterStr" type="text" value="<c:out value="${param['dateCreationAfterStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('dateCreationAfterStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center" colspan="3">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de publication inférieure </td>
										<td nowrap="1" class="text" align="left">= <input name="datePublicationBeforeStr" type="text" value="<c:out value="${param['datePublicationBeforeStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('datePublicationBeforeStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>									
									<tr valign="center" colspan="3"> 
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de publication supérieure </td>
										<td nowrap="1" class="text" align="left">= <input name="datePublicationAfterStr" type="text" value="<c:out value="${param['datePublicationAfterStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('datePublicationAfterStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center" colspan="3">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Type de contenu</td>
										<td nowrap="1" class="text" align="left">=
										<html:select name="statProdForm" property="typePublicationId" style="width=150" > 
											<option value="">Tous</option>
											 <c:forEach var="type" items="${statProdForm.typePublications}">
											 	<option value="<c:out value="${type.id}" />" <c:if test="${type.id==param['typePublicationId']}">selected</c:if>>
											 		<c:out value="${type.metaData['name']}" />
											 	</option>
											 </c:forEach>
										</html:select>
										&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center" colspan="3">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;User création</td>
										<td nowrap="1" class="text" align="left">=
										<html:select name="statProdForm" property="profilId" style="width=150" onchange="sendPostBack()" > 
											<option value="">Tous</option>
											 <c:forEach var="profil" items="${statProdForm.profils}">
											 	<option value="<c:out value="${profil.id}" />" <c:if test="${profil.id==param['profilId']}">selected</c:if>>
											 		<c:out value="${profil.metaData['name']}" />
											 	</option>
											 </c:forEach>
										</html:select>
										&nbsp;&nbsp;</td>
										<td nowrap="1" class="text" align="left">=
										<html:select name="statProdForm" property="authorId" style="width=180" > 
											<option value="">Tous</option>
											 <c:forEach var="user" items="${statProdForm.authors}">
											 	<option value="<c:out value="${user.login}" />" <c:if test="${user.login==param['authorId']}">selected</c:if>>
											 		<c:out value="${user.login}" /> / <c:out value="${user.metaData['name']}" />
											 	</option>
											 </c:forEach>
										</html:select>
										&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center" colspan="3">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Secteur d'activité</td>
										<td nowrap="1" class="text" align="left">=
										<html:select name="statProdForm" property="secteurActiviteId" style="width=150" > 
											<option value="">Tous</option>
											 <c:forEach var="secteur" items="${statProdForm.secteurActivites}">
											 	<option value="<c:out value="${secteur.id}" />" <c:if test="${secteur.id==param['secteurActiviteId']}">selected</c:if>>
											 		<c:out value="${secteur.metaData['name']}" />
											 	</option>
											 </c:forEach>
										</html:select>
										&nbsp;&nbsp;</td>
									</tr>
								</table>						
								
						</td>
                        <td align="left" class="text" valign="bottom">&nbsp;
							<input type="submit" value="<bean:message key="search.search.searchbutton"/>" class="button" align="absmiddle"/>
						</td>
			</tr>
		</table>
        </form>

	<!-- publications list -->
	<span class="title-list"><bean:message key="search.result"/></span>
	
	<hr width="100%" size="1" noshade color="#949494"> Nombre de résultats = <c:out value="${resultCount}" />
	<c:choose>
		<!-- il y a des publications à afficher, articles to show -->
		<c:when test="${result!=null && !empty(result)}">
                        <table border="0" cellpadding="3" cellspacing="2" width="100%" style="border:1px solid black;">
                                <tr class=text align="left"><th></th><th>ID</th><th>N Fiche</th><th>Titre</th><th>Date Creation</th><th>Date Publication</th><th>Version</th><th>Type</th><th>Auteur</th><th>Section</th><th>Secteurs d'activités</th><th></th></tr>
                                <% java.util.Vector v = new java.util.Vector(); %>
				<c:forEach var="r" items="${result}" varStatus="status">
                                        <tr align="center">
                                                <!-- nom (lien) -->
                                                <td class="text">
                                                        <a  href="#infos"
                                                                <c:if test="${r.type != 'objet'}">onclick="window.open('viewPublication.x?id=<c:out value="${r.id}"/>&version=<c:out value="${r.version}"/>&sid=<c:out value="${r.section}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"</c:if>
                                                                ><img src="images/infos.gif" border="0"></a>
                                                </td>
                                                <td class="text" align="left"><c:out value="${r.id}"/></td>
                                                <td class="text" align="left"><c:out value="${r.noFiche}"/></td>
                                                <td class="text" align="left">
                                                        <a onclick="parent.frames['banner'].document.location='menu.x?tab=1'" href="highlight.x?id=<c:out value="${r.id}"/>" style="text-decoration:none"><strong style="color:#5e5e5e;">
                                                                <c:out value="${r.name}"/>
                                                        </strong></a>
                                                </td>
                                                <td class="text" align="left"><c:out value="${r.dateCreation}"/></td>
                                                <td class="text" align="left"><c:out value="${r.datePublication}"/></td>
                                                <td class="text" align="left"><c:out value="${r.version}"/></td>
                                                <td class="text" align="left"><c:out value="${r.type}"/></td>
                                                <td class="text" align="left"><c:out value="${r.nameAuteur}"/></td>
                                                 <td class="text" align="left">
                                                                <c:out value="${r.sections}"/>
                                                </td>
                                                 <td class="text" align="left">
                                                                <c:out value="${r.secteurActivites}"/>
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

	</c:choose>
</body>
</html>

