<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%@ taglib uri="/WEB-INF/tlds/cache.tld" prefix="cache"%>
<ion:begin/>
<c:if test="${section!=null}">
<c:set var="viewAll" value="1"/>
<html>
<head>
	<title>viewSection</title>
         <link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='setBallon("BallonTip");' background="images/fond.gif" style="cursor:default">
<div id="_chargement" style="visibility:hidden;position:absolute; top:250px; left:200px; z-index:10; background-color: #FFFFEE;"><!--<table width=400 border=0 cellpadding=2 cellspacing=3 style='border:1px solid black;background-color:#F5F5F5' class='text'><tr><td align=center valign=middle><table width=100% border=0 cellpadding=2 cellspacing=3><tr><td align=center valign=middle><b>Veuillez patienter un moment...</b></td></tr></table></td></tr></table>--></div>
<script language="javascript">var _lcChargement=false;function _chargement(){if (!_lcChargement){document.getElementById("_lc").style.visibility = "hidden";document.getElementById("_chargement").style.visibility = "visible";_lcChargement=true;}return _lcChargement;}</script>
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
	<!-- title -->
        <a onclick="return confirm('voulez-vous supprimer le cache de cette section ?');" href="uncacheSection.x?sid=<c:out value="${section.id}"/>" style="cursor:default"><img src="images/content-tile.gif" align="absmiddle" border="0"></a>
                <c:choose>
                        <c:when test="${section.parent != null}">
                                <span class="title">
                                <bean:message key="viewSection.title"/>&nbsp;"&nbsp;<c:out value="${sectionName}" default="${section.id}" escapeXml="false"/>&nbsp;"
                                &nbsp;
                                <c:if test="${section.metaData['status']!='offline'}">
                                        &nbsp;&nbsp;<a href="#site" onclick="window.open('/section/<c:out value="${section.id}"/>.html','site','width=805,height=600,resizable=yes,scrollbars=yes')" title="site"><img src="images/site.gif" border="0" style="position=relative;top=4px"></a>
                                </c:if>
                                </span>
                                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="title-list"><c:out value="${section.parent.pathLabel}" default="" escapeXml="false"/></span>
                                <br><br>
				<table border="0" cellpadding="1" cellspacing="0" width="100%">
                                <tr style="font-size:11px; font-weight:bold; color:#006699"><td width='66%'>
                                <c:out value="${sectionDescription}"/></td><td align='right'>
                                <table style='border:1px solid black;background-color:#F5F5F5' class='text'>
                                <tr><td><b>code</b>&nbsp;</td><td><c:out value="${section.id}"/></td></tr>
                                <tr><td><b>template</b>&nbsp;</td><td><c:out value="${section.metaData['template']}"/></td></tr>
                                <tr><td><b>statut</b>&nbsp;</td><td><c:out value="${section.metaData['status']}" default="online"/></td></tr>
                                <tr><td><b>workflow</b>&nbsp;</td><td><c:out value="${section.metaData['workflow']}"/></td></tr>
                                </table>
                                </td></tr></table>
                        </c:when>
                        <c:otherwise>
                                <span class="title">&nbsp;<c:out value="${sectionName}" default="${section.id}" escapeXml="false"/>&nbsp;</span>
                        </c:otherwise>
                </c:choose>
<ion:security action="canCreateSection" user="${userLogin}">
        <c:set var="canCreateSection" scope="page"/>
</ion:security>
<!-- section/directory list -->
<script language=javascript>function goS() {
if (!document.cs.nid.value || document.cs.nid.value=="<c:out value="${section.id}"/>_") {
        alert("Veuillez saisir un nouvel ID de la section a créer.");
        return ;
}
if (document.cs.nid.value.indexOf("<c:out value="${section.rootSection}"/>_")!=0) {
        alert("Veuillez saisir un ID qui commence par <c:out value="${section.rootSection}"/>_");
        return ;
}
var lcTest = /[^a-zA-Z0-9\_]/g;
if ((lcTest.test(document.cs.nid.value)))
{
        alert("Veuillez saisir le ID ne contenant que des lettres alphanumériques ou un underscore.");
        return ;
}
if (confirm('Voulez-vous créer une sous section avec le code '+document.cs.nid.value+' ?'))
        document.cs.submit()
}
</script>
<div id="_lc" name="_lc">
        <form id="cs" name="cs" action="createSection.x" target="sub" onsubmit="return false;"><input type=hidden id="id" name="id" value="<c:out value="${section.id}"/>">
                <span class="title-list"><bean:message key="viewSection.sectionTitle"/></span><c:if test="${canCreateSection!=null}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;( <img src="images/create.gif" align="absmiddle" border="0"> &nbsp;<a href="javascript:goS()"><bean:message key="viewSection.createSection"/></a>&nbsp;<input id="nid" name="nid" style="width:300px" value="<c:out value="${section.id}"/>_">&nbsp;)</c:if>
                <hr width="100%" size="1" noshade color="#949494">
		<c:choose>
			<!-- il y a des sections à afficher, directories to show -->
			<c:when test="${sectionsSize>0}">
                                <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                        <tr class=text><th  width='20'>&nbsp;</th><th>&nbsp;</th><th width='50'>Code</th><th width='50'>&nbsp;Template</th><th width='50'>&nbsp;Workflow</th><th width='11'>&nbsp;</th><th width='11'>&nbsp;</th><th width='20'>&nbsp;</th></tr>
                                        <c:forEach var="z" items="${sections}">
						<tr>
							<!-- icone -->
							<td width="20">
								<c:choose>
                                                                        <c:when test="${z.metaData['status']=='offline'}">
										<img src="images/section-offline.gif" style="cursor:help"  align="absmiddle" alt="<bean:message key="general.offlineSection"/>">
									</c:when>
									<c:otherwise>
										<img src="images/section.gif" align="absmiddle">
									</c:otherwise>
								</c:choose>
							</td>
							<!-- name (lien) -->
                                                        <td class=text>
                                                                <c:choose>
                                                                        <c:when test="${canCreateSection!=null}">
                                                                                &nbsp;<a  onclick="return confirm('Voulez-vous éditer cette section ?')" href="editSection.x?sid=<c:out value="${section.id}"/>&id=<c:out value="${z.id}"/>" target="sub"><c:out value="${z.metaData['name']}" default="${z.id}" escapeXml="false"/></a>&nbsp;
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                                &nbsp;<b><c:out value="${z.metaData['name']}" default="${z.id}" escapeXml="false"/></b>&nbsp;
                                                                        </c:otherwise>
                                                                </c:choose>
							</td>
                                                        <td class="text">
                                                                &nbsp;<c:out value="${z.id}"/>&nbsp;
							</td>
                                                        <td class="text">
                                                                &nbsp;<c:out value="${z.metaData['template']}"/>&nbsp;
							</td>
                                                        <td class="text">
                                                                &nbsp;<c:out value="${z.metaData['workflow']}"/>&nbsp;
							</td>
							<!-- move down -->
                                                        <td width="11"><c:if test="${canCreateSection!=null}"><a onclick="return confirm('Voulez-vous faire monter cette section ?')" href="moveSection.x?sid=<c:out value="${section.id}"/>&id=<c:out value="${z.id}"/>&type=down" target="sub"><img src="images/up.gif" border="0" alt="monter la section"></a></c:if></td>
							<!-- move up -->
                                                        <td width="11"><c:if test="${canCreateSection!=null}"><a onclick="return confirm('Voulez-vous faire descendre cette section ?')" href="moveSection.x?sid=<c:out value="${section.id}"/>&id=<c:out value="${z.id}"/>&type=up" target="sub"><img src="images/down.gif" border="0" alt="descendre la section"></a></c:if></td>
							<!-- delete -->
                                                        <td width="20" align="right" valign="center" >
                                                                <c:if test="${canCreateSection!=null}">
                                                                <ion:security action="canDeleteSection" user="${userLogin}" section="${z.id}">
                                                                        <a onclick="return confirm('Voulez-vous supprimer cette section ?')" href="deleteSection.x?sid=<c:out value="${section.id}"/>&id=<c:out value="${z.id}"/>" target="sub" title="supprimer"><img style="cursor:hand;" src="images/poubelle.gif" style="border=0; background-color:transparent" border="0"></a>
                                                                </ion:security></c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				<br><br>
			</c:when>
			<!-- il n'y a pas de sections à afficher, no directories to show -->
			<c:otherwise>
				<i style="color:#222222"><bean:message key="viewSection.noSections"/></i>
				<br><br><br>
			</c:otherwise>
		</c:choose>
                </form>
<br><br>
<!-- publications list -->
<ion:security section="${section.id}" action="canCreatePublication" user="${userLogin}">
	<c:set var="canCreatePublication" scope="page"/>
</ion:security>
<c:if test="${canCreatePublication!=null || publicationsSize>0}">
        <span class="title-list"><bean:message key="viewSection.publicationTitle"/></span>
	<c:if test="${canCreatePublication!=null}">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;( <img src="images/create.gif" align="absmiddle" border="0"> <a onclick="return confirm('Voulez-vous créer une nouvelle publication ?')"  href="createPublication.x?id=<c:out value="${section.id}"/>" ><bean:message key="viewSection.createPublication"/></a> )
                <%pageContext.setAttribute("_index","index_"+request.getAttribute("sid"));%>
	</c:if>
	<hr width="100%" size="1" noshade color="#949494">
	<c:choose>
		<!-- il y a des publications à afficher, publications to show -->
		<c:when test="${publicationsSize>0}">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<c:forEach var="publication" items="${publications}">
                                        <!-- pour chaque version, for each version -->
                                        <c:set var="doEdit" value="false"/>
                                        <c:if test="${canCreatePublication!=null}">
                                                <ion:security action="canDeletePublication" user="${userLogin}" publication="${publication.id}">
                                                <c:set var="canDelete" value="yes"/>
                                                </ion:security>
                                        </c:if>
					<c:forEach var="publicationVersion" items="${publication.versions}">
						<c:if test="${publicationVersion.version==publication.lastVersion.version || versionDisplayInfos[publication.id]=='true'}">
                                                        <ion:workflowActions var="actions" publication="${publication.id}" user="${userLogin}" version="${publicationVersion.version}" workflow="${publicationVersion.workflowId}">
							<c:set var="canAction" value="yes"/>
							</ion:workflowActions>
                                                        <c:if test="${canAction != null || viewAll=='1'}">
                                                        <ion:security action="canEditPublication" user="${userLogin}" publication="${publicationVersion.publication.id}" version="${publicationVersion.version}">
								<c:set var="canEditPublication" value="yes"/>
							</ion:security>
							<c:set var="resume" scope="page" value="true"/>
							<c:set var="separator" scope="page" value="true"/>
							<tr <c:if test="${highlightId==publication.id}">bgcolor="#fce4e6"</c:if>>
                                                                <!-- informations -->
                                                                <td width="25" align="left">
                                                                        <c:if test="${publication.lastVersion.version==publicationVersion.version}">
                                                                        <a style="cursor:help" href="#infos"
                                                                                <c:if test="${canCreatePublication!=null && publication.type.id != 'objet'}">onclick="window.open('viewPublication.x?id=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>&sid=<c:out value="${section.id}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"</c:if>
                                                                                onmouseover="showBallon('<span class=text><b>- type de contenu : </b> <c:out value="${publication.type.metaData['name']}"/><br><b>- id : </b> <c:out value="${publication.id}"/><c:if test="${publication.metaData['lcPresentation']!=null && publication.metaData['lcPresentation']!=''}"><br><b>- modèle de présentation : </b> <c:out value="${publication.metaData['lcPresentation']}"/></c:if><c:if test="${publication.metaData['lcDatePublication']!=null && publication.metaData['lcDatePublication']!=''}"><br><b>- date de publication : </b> <c:out value="${publication.metaData['lcDatePublication']}"/></c:if><c:if test="${publication.metaData['lcDateFin']!=null && publication.metaData['lcDateFin']!=''}"><br><b>- date de dépublication : </b> <c:out value="${publication.metaData['lcDateFin']}"/></c:if><c:if test="${publication.metaData['lcDateChange']!=null && publication.metaData['lcDateChange']!=''}"><br><b>- date de changement : </b> <c:out value="${publication.metaData['lcDateChange']}"/></c:if></span>', 1, 300, '#f0f0f0');" onmouseout="hideBallon()"><img src="images/infos.gif" border="0"></a>
                                                                        </c:if>
								</td>
                                                                <!-- vie -->
                                                                <td width="35" valign="top" align="left" nowrap=1>
                                                                <c:if test="${publication.lastVersion.version==publicationVersion.version}">
                                                                <c:choose>
                                                                <c:when test="${publication.life==2}">
                                                                        <img style="cursor:help" src="images/v2.gif" border="0" alt="En fin de vie (depuis <c:out value="${publication.metaData['lcDateFin']}"/>)">
                                                                </c:when>
                                                                <c:when test="${publication.life==1}">
                                                                        <img style="cursor:help" src="images/v1.gif" border="0" alt="Actif">
                                                                </c:when>
                                                                <c:when test="${publication.life==0}">
                                                                        <img style="cursor:help" src="images/v0.gif" border="0" alt="Inactif (actif à partir de <c:out value="${publication.metaData['lcDatePublication']}"/>)">
                                                                </c:when>
                                                                </c:choose>
                                                                <c:if test="${publication.metaData['diffusion']!=null && publication.metaData['diffusion']!='' && publication.metaData['diffusion']!='internet'}">
                                                                        <img style="cursor:help" src="images/export.gif" border="0" alt="diffusion pour <c:out value="${publication.metaData['diffusion']}"/>">
                                                                </c:if>
                                                                </c:if>
                                                                </td>
								<!-- depliant -->
								<td valign="top" width="13">
								<c:choose>
									<c:when test="${publicationVersion.version==publication.lastVersion.version}">
										<c:choose>
											<c:when test="${publication.lastVersion.version==1}">
												<img src="images/plus-fade.gif" align="absmiddle">
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${versionDisplayInfos[publication.id]=='true'}">
                                                                                                                <a href="viewSection.x?sid=<c:out value="${section.id}"/>&collapse=<c:out value="${publication.id}"/>" title="cacher les anciennes versions"><img src="images/moins.gif" align="absmiddle" border="0"></a>
													</c:when>
													<c:otherwise>
                                                                                                                <a href="viewSection.x?sid=<c:out value="${section.id}"/>&expand=<c:out value="${publication.id}"/>" title="afficher toutes les versions"><img src="images/plus-normal.gif" align="absmiddle" border="0"></a>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${publicationVersion.version==1}">
												<img src="images/line-end.gif" align="absmiddle" border="0">
											</c:when>
											<c:otherwise>
												<img src="images/line-middle.gif" align="absmiddle" border="0">
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
								</td>
								<!-- icon -->
								<td width="22" height="25" align="left">
                                                                        <a href="#contenu"
                                                                        <c:choose><c:when test="${canCreatePublication!=null && publication.type.id != 'objet'}">
                                                                        style="cursor:hand" title="contenu de la publication" onclick="window.open('viewPublication.x?id=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>&sid=<c:out value="${section.id}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"
                                                                        </c:when><c:otherwise>style="cursor:default"</c:otherwise></c:choose>
                                                                        >
                                                                        <c:choose>
										<c:when test="${publicationVersion.workflow.metadatas['publicationIcon']!=null}">
                                                                                        <img src="<c:out value="${publicationVersion.workflow.metadatas['publicationIcon']}"/>" align="absmiddle" border=0>
										</c:when>
										<c:otherwise>
                                                                                        <img src="images/publication.gif" align="absmiddle" border=0>
										</c:otherwise>
									</c:choose>
                                                                        </a>
								</td>
								<!-- name (lien) -->
                                                                <td nowrap=1 class="text" style="color:#5e5e5e;cursor:default">
									<c:choose>
                                                                                <c:when test="${canEditPublication!=null && publication.type.id != 'objet'}">
                                                                                        <a title="formulaire d'édition" href="editPublication.x?id=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>&sid=<c:out value="${section.id}"/>" >
											<c:out value="${publication.metaData['name']}"/>
											</a>
										</c:when>
										<c:otherwise>
											<strong style="color:#5e5e5e;"><c:out value="${publication.metaData['name']}"/></strong>
										</c:otherwise>
									</c:choose>
                                                                        <c:choose>
                                                                        <c:when test="${publicationVersion.version==publication.lastVersion.version && publication.type.id != 'objet' && canCreatePublication != null && publication.lastVersion.version<5}">
										(version <c:out value="${publicationVersion.version}"/>)
                                                                                <a onclick="return confirm('Voulez-vous créer une nouvelle version de cette publication ?')" href="newVersion.x?nid=<c:out value="${publication.id}"/>&sid=<c:out value="${section.id}"/>"  title="créer la version <c:out value="${publicationVersion.version+1}"/> de cette publication"><img src="images/newVersion.gif" align="absmiddle" border="0"></a>
                                                                        </c:when>
                                                                        <c:when test="${publication.type.id == 'objet'}">
                                                                                <img style="cursor:help" src="images/objet.gif" border="0" align="absmiddle" alt="objet de type <c:choose><c:when test="${publication.metaData['objet']!=''}"><c:out value="${publication.metaData['objet']}" default="indéfini"/></c:when><c:otherwise>indéfini</c:otherwise></c:choose>">
                                                                        </c:when>
									</c:choose>
								</td>
								<!-- publication -->
								<c:choose>
                                                                <c:when test="${canEditPublication!=null&&doEdit!='true'}">
                                                                                <c:set var="doEdit" value="true"/>
                                                                                <!-- gestion -->
                                                                                <td width="11"><a title="gerer la publication" onclick="return confirm('Voulez-vous gérer cette publication ?')"  href="publication.x?id=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>&sid=<c:out value="${section.id}"/>&index=<c:out value="${publication.metaData[_index]}"/>"><img src="images/moveSection.gif" border="0"></a></td>
										<!-- move down -->
                                                                                <td width="11">
																				<a title="monter la publication" onclick="return confirm('Voulez-vous faire monter cette publication ?')" href="movePublication.x?pId=<c:out value="${publication.id}"/>&id=<c:out value="${section.id}"/>&type=up"><img src="images/up.gif" border="0"></a>
                                                                                </td>
										<!-- move up -->
                                                                                <td width="11">
                                                                                <a title="descendre la publication" onclick="return confirm('Voulez-vous faire descendre cette publication ?')" href="movePublication.x?pId=<c:out value="${publication.id}"/>&id=<c:out value="${section.id}"/>&type=down"><img src="images/down.gif" border="0"></a>
                                                                                </td>
								</c:when>
								<c:otherwise>
										<td width="11"></td>
										<td width="11"></td>
										<td width="11"></td>
								</c:otherwise>
								</c:choose>
								<!-- history -->
                                                                <td width="30" align="center">
                                                                        <a href="#historique" title="afficher l'historique" onclick="window.open('historyPublication.x?id=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>&sid=<c:out value="${section.id}"/>','history','width=600,height=250,resizable=yes,scrollbars=yes')"><img src="images/historique.gif" border="0"></a>
								</td>
								<!-- preview -->
                                                                <td width="30" align="left">
                                                                <c:if test="${publication.type.id != 'objet'}">
                                                                <a href="#preview" title="prévisualisation" onclick="window.open('previewPublication.x?id=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>&sid=<c:out value="${section.id}"/>','preview','width=600,height=400,resizable=yes,scrollbars=yes')"><img src="images/preview.gif" border="0"></a>
                                                                </c:if>
                                                                </td>
                                                                <!-- panier -->
                                                                <c:choose>
                                                                <%-- <c:when test="${publication.type.id == panier.type}"> --%>
                                                                <c:when test="true">
                                                                        <c:set var="zPanier" value="${publicationVersion.id}"/>
                                                                        <% pageContext.setAttribute("zPanier",((org.nextime.ion.backoffice.panier.Panier)session.getAttribute("panier")).isPresent(""+pageContext.getAttribute("zPanier"))?"1":"0"); %>
                                                                        <form target="_panier" method="post" action="editPanier.x" name="panier_<c:out value="${publicationVersion.id}"/>">
                                                                        <td align="center" width="20" valign="top"><input type=hidden name=id value="<c:out value="${publicationVersion.id}"/>">
                                                                        <input type='checkbox' name='etat' value='<c:if test="${zPanier=='1'}">1</c:if>' <c:if test="${zPanier=='1'}">checked</c:if> onClick="this.checked=(this.checked?false:true);document.panier_<c:out value="${publicationVersion.id}"/>.submit()">&nbsp;
                                                                        </td>
                                                                        </form>
                                                                        <c:remove var="zPanier"/>
                                                                </c:when> 
                                                                <c:otherwise>
                                                                                <td width="20">&nbsp;</td>
                                                                </c:otherwise>
                                                                </c:choose>
								<!-- actions -->
                                                                <form  method="post" action="actions.x" id="form_<c:out value="${publicationVersion.id}"/>">
								<td align="right" width="160" valign="top"><c:if test="${canAction!=null}">
                                                                        <input type="hidden" name="retour" value="/viewSection.x">
									<input type="hidden" name="externalTemplate" value="/workflowForms/normalTemplate.jsp">
									<input type="hidden" name="id" value="<c:out value="${publication.id}"/>">
                                                                        <input type="hidden" name="sid" value="<c:out value="${section.id}"/>">
									<input type="hidden" name="version" value="<c:out value="${publicationVersion.version}"/>">
                                                                        <select name="action" onchange="if (_chargement()) form_<c:out value="${publicationVersion.id}"/>.submit()">
										<option value="nothing">workflow .......................................</option>
											<c:forEach items="${actions}" var="action">
												<option value="<c:out value="${action.id}"/>"><c:out value="${action.name}"/></option>
											</c:forEach>
									</select></c:if>
								</td>
								</form>
                                                                <!-- copy -->
                                                                <td width="20" align="right" valign="top">
                                                                <c:if test="${canCreatePublication!=null&&publication.type.id != 'objet'}">
                                                                <a onclick="return confirm('Voulez-vous créer une nouvelle copie de cette publication ?')" href="copyPublication.x?id=<c:out value="${section.id}"/>&pId=<c:out value="${publication.id}"/>&version=<c:out value="${publicationVersion.version}"/>"  title="copier la publication"><img src="images/copy.gif" border="0"></a>
                                                                </c:if>
                                                                </td>
								<!-- supprimer -->
								<c:choose>
                                                                        <c:when test="${canDelete!=null}">
                                                                                <form action="deletePublicationVersion.x"  onsubmit="return confirm('Voulez-vous supprimer cette version ?')">
                                                                                <td width="20" align="right" valign="top">
                                                                                        <c:if test="${publication.lastVersion.version!=1}">
                                                                                                <input type="hidden" name="sid" value="<c:out value="${section.id}"/>">
                                                                                                <input type="hidden" name="did" value="<c:out value="${publication.id}"/>">
                                                                                                <input type="hidden" name="version" value="<c:out value="${publicationVersion.version}"/>">
                                                                                                <input style="cursor:hand;" type="image" title="supprimer la version de cette publication" src="images/poubelle-fade.gif" style="border=0; background-color:transparent">
                                                                                        </c:if>
                                                                                </td>
                                                                                </form>
                                                                                <c:choose>
                                                                                        <c:when test="${publication.lastVersion.version==publicationVersion.version}">
                                                                                                <form action="deletePublication.x"  onsubmit="return confirm('Voulez-vous supprimer cette publication ?')">
                                                                                                <td width="20" align="right" valign="top">
                                                                                                        <input type="hidden" name="sid" value="<c:out value="${section.id}"/>">
                                                                                                        <input type="hidden" name="did" value="<c:out value="${publication.id}"/>">
                                                                                                        <input style="cursor:hand;" type="image" title="supprimer la publication de l'ensemble des sections" src="images/poubelle.gif" style="border=0; background-color:transparent">
                                                                                                </td>
                                                                                                </form>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                                        <td width="20"></td>
                                                                                        </c:otherwise>
                                                                                </c:choose>

                                                                        </c:when>
                                                                        <c:otherwise>
                                                                                        <td width="20"></td>
                                                                                        <td width="20"></td>
                                                                        </c:otherwise>
                                                                </c:choose>
							</tr>
							</c:if>
						</c:if>
                                                <c:remove var="canEditPublication"/><c:remove var="canAction"/>
					</c:forEach>
                                        <c:remove var="canDelete" scope="page"/>
                                        <c:remove var="doEdit"/>
					<c:if test="${separator=='true'}">
					<tr>
                                                <td colspan="15" height="1"></td>
					</tr>
					<tr>
                                                <td colspan="15" height="1" bgcolor="#bdbdbd"></td>
					</tr>
					<tr>
                                                <td colspan="15" height="1"></td>
					</tr>
					</c:if><c:remove var="separator"/>
				</c:forEach>
			</table>
			<br>

                        <P><center>
			<span class="text" align="left">
                                <a href="#liste" title="liste complète" onclick="window.open('viewSectionPublication.x?id=<c:out value="${section.id}"/>&sort=n','liste','width=600,height=400,resizable=yes,scrollbars=yes')"><img src="images/pucef.gif" align="absmiddle" border="0"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${start>0}">
                                        <a href="viewSection.x?sid=<c:out value="${section.id}"/>&start=<c:out value="${start-pageSize}"/>" style="text-decoration:none">&lt;&lt;Précédent</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
                                <b style="cursor:default; color:#444444">publications <c:out value="${start+1}"/> à <c:out value="${stop+1}"/> sur <c:out value="${publicationsSize}"/></b>
				<c:if test="${(stop+1)<publicationsSize}">
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="viewSection.x?sid=<c:out value="${section.id}"/>&start=<c:out value="${stop+1}"/>" style="text-decoration:none">Suivant&gt;&gt;</a>
                                </c:if>
                        </span></center>

		</c:when>
		<!-- il n'y a pas de publications à afficher, no publications to show -->
		<c:otherwise>
			<i style="color:#222222"><bean:message key="viewSection.noPublications"/></i>
		</c:otherwise>
	</c:choose>
</c:if>
</div>
</body>

</html>
</c:if>
<ion:close/>
