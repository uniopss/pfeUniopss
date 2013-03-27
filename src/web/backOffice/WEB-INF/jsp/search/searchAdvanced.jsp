<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%--
		La recherche avancée pour le backfoffice 
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
	function go(){
		return true;
	}
	function sort(p_SortBy){
		var frm=document.searchAdvancedForm;
		frm.sortBy.value=p_SortBy;
		if 	(frm.sortDirection.value=='Asc') {
			frm.sortDirection.value='Desc'
		} else {
			frm.sortDirection.value='Asc'
		};
		frm.submit();
	}
	</script>

	<img src="images/search-tile.gif" align="absmiddle">
	<span class="title">
                recherche avancée
	</span>
        <form name="searchAdvancedForm" action="searchAdvanced.x" method="POST" onSubmit="return go()" >
        <input type="hidden" name="popup" value="<c:out value="${param['popup']}"/>">
		<input type="hidden" name="postBack" value="true">
		<input type="hidden" name="sortBy"  value="<c:out value="${param['sortBy']}"/>" >
		<input type="hidden" name="sortDirection" value="<c:out value="${param['sortDirection']}"/>" >
		
<%--    <logic:messagesPresent property="errorSynonyme">--%>
        <span class="error"><html:errors property="errorSynonyme"/></span>
<%--    </logic:messagesPresent>--%>
<%--    <logic:messagesPresent property="errorFewParam">--%>
        <span class="error"><html:errors property="errorFewParam"/></span>
<%--    </logic:messagesPresent>--%>
<%--    <logic:messagesPresent property="errorParser">--%>
		<span class="error"><html:errors property="errorParser"/></span>
<%--    </logic:messagesPresent>--%>

        <table cellpadding="3" cellspacing="3" width="600">
        	<tr valign="center">
					<td align="left" class="text" colspan=2>
							<b>Mots clés :&nbsp;</b>
                                        &nbsp;<input type="text" value="<c:out value="${param['keyWords']}"/>" name="keyWords" style="width:200" align="absmiddle">&nbsp;
                                        &nbsp;<c:choose><c:when test="${mL=='1'}"><select name="index" style="width:60" align="absmiddle"><c:forEach items="${indexs}" var="index"><option value="<c:out value="${index}"/>" <c:if test="${index==param['index']}">selected</c:if>><c:out value="${index}"/></option></c:forEach></select></c:when><c:otherwise><input type="hidden" name="index" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
                                        &nbsp;dans&nbsp;<select name="selectedSite" style="width:150" valign="absmiddle"><c:forEach items="${currentSite}" var="index"><option value="<c:out value="${index.id}"/>" <c:if test="${index.id==param['selectedSite']}">selected</c:if>><c:out value="${index.metaData['name']}"/></option></c:forEach></select>
					</td>
			</tr>
			<tr valign="top" align="left ">
						<td align="left" class="text" >
								<table style="border:1px solid black;" width="400" cellspacing="0" cellpadding="0" border="0">
									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Auteur création</td>
										<td nowrap="1" class="text" align="left">= <input name="authorId" type="text" value="<c:out value="${param['authorId']}"/>">&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center">
										<td width="100%" class="text" align="left" nowrap="nowrap">&nbsp;&nbsp;Date de publication inférieure&nbsp;</td>
										<td nowrap="nowrap" class="text" align="left">= <input name="datePublicationBeforeStr" type="text" value="<c:out value="${param['datePublicationBeforeStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('datePublicationBeforeStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de publication supérieure </td>
										<td nowrap="1" class="text" align="left">= <input name="datePublicationAfterStr" type="text" value="<c:out value="${param['datePublicationAfterStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('datePublicationAfterStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de création inférieure </td>
										<td nowrap="1" class="text" align="left">= <input name="dateCreationBeforeStr" type="text" value="<c:out value="${param['dateCreationBeforeStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('dateCreationBeforeStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>									
									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Date de création supérieure </td>
										<td nowrap="1" class="text" align="left">= <input name="dateCreationAfterStr" type="text" value="<c:out value="${param['dateCreationAfterStr']}"/>">&nbsp;
										<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('dateCreationAfterStr','fr');this.style.cursor='hand'" align="absmiddle" border="0"/></a>&nbsp;&nbsp;</td>
									</tr>
									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Thésaurus</td>
										<input type="hidden" name="presentation" value='<c:out value="${param['presentation']}" escapeXml="false" />' >
										<td nowrap="nowrap"><textarea style="visibility:hidden;width=1;height=1" readonly="1" name="FIELD_mots_cles"><c:out value="${param['FIELD_mots_cles']}" escapeXml="false" /></textarea>
											<div style="border:1px solid black;width:200px;height:120px;overflow:auto" class="button3" id="lc_mots_cles">
											<c:out value="${param['presentation']}" escapeXml="false" />
											</div>
											&nbsp;<a href="javascript:callout_mots_cles()">Modifier
			                                </a>
			                            </td>
			                		</tr>
			                		
									<script language="javascript">
										function callout_mots_cles() {
											var frm=document.callout;
											frm.valeur.value=document.forms[0].FIELD_mots_cles.value;
    			                            frm.nom.value='listeMotCles.jsp';
                			                frm.zaction.value='select_mots_cles';
                            			    frm.action='/backoffice/callout/listeMotCles.jsp';
			                                frm.submit();
										}
				                        function select_mots_cles(value,rep) {
                		        	        document.forms[0].FIELD_mots_cles.value=value;
                        		    	    document.getElementById('lc_mots_cles').innerHTML=rep;
											document.forms[0].presentation.value=rep;
										}
				                        function get_mots_cles() {
                			                return document.forms[0].FIELD_mots_cles.value;
										}
									</script>
									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Numéro de fiche</td>
										<td nowrap="1" class="text" align="left">= <input name="noFiche" type="text" value="<c:out value="${param['noFiche']}"/>">&nbsp;&nbsp;</td>
									</tr>

									<tr valign="center">
										<td width="100%" class="text" align="left">&nbsp;&nbsp;Type de contenu</td>
										<td nowrap="1" class="text" align="left">=
										<html:select name="searchAdvancedForm" property="typePublicationId" style="width=150" > 
											<option value="">Tous</option>
											 <c:forEach var="type" items="${searchAdvancedForm.typePublications}">
											 	<option value="<c:out value="${type.id}" />" <c:if test="${type.id==param['typePublicationId']}">selected</c:if>>
											 		<c:out value="${type.metaData['name']}" />
											 	</option>
											 </c:forEach>
										</html:select>
										&nbsp;&nbsp;</td>
									</tr>
									<tr>
										<td nowrap="nowrap" class="text" align="left">Ouvrage - Localisation
											<input type="hidden" name="presLocaOuvrage" value='<c:out value="${param['presLocaOuvrage']}" escapeXml="false" />' >
										</td>
										<td nowrap="nowrap"><textarea style="visibility:hidden;width=1;height=1" readonly="1" name="FIELD_ouvrage_local"><c:out value="${param['FIELD_ouvrage_local']}" escapeXml="false" /></textarea>
											<div style="border:1px solid black;width:200px;height:120px;overflow:auto" class="button3" id="lc_ouvrage_local">
											<c:out value="${param['presLocaOuvrage']}" escapeXml="false" />
											</div>
											&nbsp;<a href="javascript:callout_ouvrage_local()">Modifier</a></td><td width="30">&nbsp;
										</td>
										<td align="left" valign="center" width="100%" nowrap="nowrap"></td>
									</tr>
									<script language="javascript">
			                        function callout_ouvrage_local() {
            		                    document.callout.valeur.value=document.forms[0].FIELD_ouvrage_local.value;
                    		            document.callout.nom.value='listeEnrichissable.jsp?calloutParam=localisation_ouvrage.xml';
                            		    document.callout.zaction.value='select_ouvrage_local';
		                                document.callout.action='/backoffice/callout/listeEnrichissable.jsp?calloutParam=localisation_ouvrage.xml';
        		                        document.callout.submit();
									}
									function select_ouvrage_local(value,rep) {
		                                document.forms[0].FIELD_ouvrage_local.value=value;
        		                        document.all['lc_ouvrage_local'].innerHTML=rep;
										document.forms[0].presLocaOuvrage.value=rep;
									}
			                        function get_ouvrage_local() {
            		                    return document.forms[0].FIELD_ouvrage_local.value;
									}
									</script>
								</table>						
								
						</td>
                        <td align="left" class="text" valign="bottom">&nbsp;
							<input type="submit" value="<bean:message key="search.search.searchbutton"/>" class="button" align="absmiddle"/>
						</td>
			</tr>
		</table>
        </form>
        
        <form id="callout" name="callout" action="" target="callout" method="post">
	        <input type=hidden id="nom" name="nom" >
    	    <input type=hidden id="valeur" name="valeur" >
        	<input type=hidden id="zaction" name="zaction" >
    	    <input type="hidden" id="currentLocale" name="currentLocale" value="fr">
    	    
        </form>

       
	<c:set var="titreTemplate">
		<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
			        <xsl:output method="html"/>
			        <xsl:template match="/ionPublication">
			        		<xsl:if test="@type='information_generale'">
									<xsl:value-of select="data/publication/titre_doc_info/text()" />
							</xsl:if>
			        		<xsl:if test="@type='texte_de_droit'">
									<xsl:value-of select="data/publication/titre/text()" />
							</xsl:if>
			        		<xsl:if test="@type='manifestation_formation'">
									<xsl:value-of select="data/publication/intitule/text()" />
							</xsl:if>
			        		<xsl:if test="@type='publication'">
									<xsl:value-of select="data/publication/titre_doc_article/text()" />
							</xsl:if>
							<xsl:if test="@type='offre_emploi'">
									<xsl:value-of select="data/publication/intitule/text()" />
							</xsl:if>
							<xsl:if test="@type='demande_emploi'">
									<xsl:value-of select="data/publication/intitule_poste_recherche/text()" />
							</xsl:if>
			        </xsl:template>
		</xsl:stylesheet>
	</c:set>
	
	<%-- Template d'afficher le resume --%>
	<c:set var="resumeTemplate">
		<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
			        <xsl:output method="xml"/>
			        <xsl:template match="/ionPublication">
			        		<xsl:if test="@type='information_generale'">
					        		<xsl:call-template name="formatResumed">
											<xsl:with-param name="resume">
												<xsl:value-of select="substring(data/publication/resume_txt/text(),0,255)" disable-output-escaping="yes" />
											</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
			        		<xsl:if test="@type='texte_de_droit'">
			        				<xsl:call-template name="formatResumed">
											<xsl:with-param name="resume">
												<xsl:value-of select="substring(data/publication/typeText/text(),0,255)" disable-output-escaping="yes" />
											</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
			        		<xsl:if test="@type='manifestation_formation'">
			        				<xsl:call-template name="formatResumed">
											<xsl:with-param name="resume">
												<xsl:value-of select="substring(data/publication/typeEvent/text(),0,255)" disable-output-escaping="yes" />
											</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
			        		<xsl:if test="@type='publication'">
			        				<xsl:call-template name="formatResumed">
											<xsl:with-param name="resume">
												<xsl:value-of select="substring(data/publication/resume/text(),0,255)" disable-output-escaping="yes" />
											</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
							<xsl:if test="@type='offre_emploi'">
									<xsl:call-template name="formatResumed">
											<xsl:with-param name="resume">
												<xsl:value-of select="substring(data/publication/description/text(),0,255)" disable-output-escaping="yes" />
											</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
							<xsl:if test="@type='enquete'">
									<xsl:call-template name="formatResumed">
											<xsl:with-param name="resume">
												<xsl:value-of select="substring(data/publication/question/text(),0,255)" disable-output-escaping="yes" />
											</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
			        </xsl:template>
			        
			<xsl:template name="subStringBeforeInverse">
				<xsl:param name="inputStr"/>
				<xsl:param name="subStr"/>
				<xsl:if test="$subStr and contains($inputStr, $subStr)">
					<xsl:variable name="temp" select="substring-after($inputStr, $subStr)"/>
					<xsl:value-of select="substring-before($inputStr, $subStr)" disable-output-escaping="yes"/>
					<xsl:if test="contains($temp, $subStr)">
						<xsl:value-of select="$subStr"/>
						<xsl:call-template name="subStringBeforeInverse">
							<xsl:with-param name="inputStr" select="$temp"/>
							<xsl:with-param name="subStr" select="$subStr"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</xsl:template>
			<!--  format A 255 caracteres -->
			<xsl:template name="formatResumed">
				<xsl:param name="resume"/>
				<xsl:variable name="resumeBefore">
					<xsl:call-template name="subStringBeforeInverse">
						<xsl:with-param name="inputStr" select="$resume"/>
						<xsl:with-param name="subStr" >&lt;</xsl:with-param>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="resumeBefore1">
					<xsl:call-template name="subStringBeforeInverse">
						<xsl:with-param name="inputStr" select="$resume"/>
						<xsl:with-param name="subStr" >&lt;/</xsl:with-param>
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($resumeBefore) > 0">
						<xsl:choose>
							<xsl:when test="string-length($resumeBefore) &lt; string-length($resumeBefore1)">
								<!--  OK retourn tout de suite avec la balise ferme-->
								<xsl:call-template name="subStringBeforeInverse">
									<xsl:with-param name="inputStr" select="$resume"/>
									<xsl:with-param name="subStr" >&gt;</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<!--  sinon on coupe avant le <  -->
								<xsl:value-of select="substring($resumeBefore,0, string-length($resumeBefore)+1)" disable-output-escaping="yes"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<!--  il est vide -->
						<xsl:value-of select="$resume" disable-output-escaping="yes"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:template>
		</xsl:stylesheet>
	</c:set>
	
	<%-- Template d'afficher le type --%>
	<c:set var="typeDocumentTemplate">
		<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
			        <xsl:output method="xml"/>
			        <xsl:template match="/ionPublication">
					        <xsl:value-of select="@type" />
			        		<xsl:if test="@type='information_generale'">
			        				/<xsl:value-of select="data/publication/type_INFO" />
							</xsl:if>
			        		<xsl:if test="@type='texte_de_droit'">
			        				/<xsl:value-of select="data/publication/typeText" />
							</xsl:if>
			        		<xsl:if test="@type='manifestation_formation'">
									/<xsl:value-of select="data/publication/typeEvent" />
							</xsl:if>
			        		<xsl:if test="@type='publication'">
									/<xsl:value-of select="data/publication/type_doc" />
							</xsl:if>
							<xsl:if test="@type='offre_emploi'">
									/<xsl:value-of select="data/publication/fonction" />
							</xsl:if>
		        </xsl:template>
		</xsl:stylesheet>
	</c:set>
	
	<!-- publications list -->
	<span class="title-list"><bean:message key="search.result"/></span>
	<hr width="100%" size="1" noshade color="#949494">
	<c:choose>
		<!-- il y a des publications à afficher, articles to show -->
		<c:when test="${!empty(result)}">
                        <c:if test="${param['view'] !=null && param['view'] != ''}">
                                <c:import url="/xml/${param['view']}.xsl" var="xslResult" />
                        </c:if>
                        <table border="0" cellpadding="3" cellspacing="2" width="100%" style="border:1px solid black;">
                                <tr class=text align="left">
                                	<th></th>
                                	<th>Version</th>
                                	<th>Titre</th>
                                	<th><a href="javascript:sort('dateCreation')">Date de création</a></th>
                                	<th>Nom</th>
                                	<th><a href="javascript:sort('type')">Type</a></th>
	                                <th><a href="javascript:sort('auteur')">Auteur de la publication</a></th>
    	                            <th>Résumé</th>
        	                        <th>Section</th><th></th><th></th></tr>
				<c:forEach var="r" items="${result}" varStatus="status">
                                        <tr align="center">
                                        		<%-- info bull --%>
                                                <td class="text">
                                                        <a  href="#infos"
                                                                <c:if test="${r.type != 'objet'}">onclick="window.open('viewPublication.x?id=<c:out value="${r.id}"/>&version=<c:out value="${r.version}"/>&sid=<c:out value="${r.section}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"</c:if>
                                                                ><img src="images/infos.gif" border="0"></a>
                                                </td>
                                                <%-- version --%>
                                                <td class="text" align="left"><c:out value="${r.version}"/></td>
                                                <%-- Titre --%>
												<td class="text" align="left">
                                                	<x:transform xslt="${titreTemplate}"><c:out value="${r.content}" default="<ionPublication/>" escapeXml="false"/></x:transform>&nbsp;
                                                </td>                
                                                <%-- date création --%>
                                                <td class="text" align="left"><c:out value="${r.dateCreation}"/>&nbsp;</td>
                                                <%-- nom --%>
												<td class="text" align="left">
                                                        <c:choose>
                                                        <c:when test="${param['popup'] !='1'}">
                                                                <a onclick="parent.frames['banner'].document.location='menu.x?tab=1'" href="highlight.x?id=<c:out value="${r.id}"/>" style="text-decoration:none"><strong style="color:#5e5e5e;">
                                                                        <c:out value="${r.name}"/>
                                                                </strong></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                                <a href="#" onclick="top.opener.searchLc('<c:out value="${r.name}"/>')"><c:out value="${r.name}"/></a>
                                                        </c:otherwise>
                                                        </c:choose>
                                                </td>
                                                <%-- type --%>
                                                <td class="text" align="left"><x:transform xslt="${typeDocumentTemplate}"><c:out value="${r.content}" default="<ionPublication/>" escapeXml="false"/></x:transform></td>
												<%-- auteur de la publication  --%>
												<td class="text" align="left"><c:out value="${r.author}"/>&nbsp;</td>
												<%-- resume --%>
                                                <td class="text" align="left">
                                                	<x:transform xslt="${resumeTemplate}"><c:out value="${r.content}" default="<ionPublication/>" escapeXml="false"/></x:transform>&nbsp;
                                                </td>
												<%-- section 
												<td class="text" align="left">
                                                        <c:out value="${r.sections}"/>
                                                </td>
                                                --%>
                                                <%-- section name : dans l'occurence domaines intervetion --%>
												<td class="text" align="left">
												<cache:cache scope="application" name="${r.sectionName}" key="${r.sectionName}">
                                                        <c:out value="${r.sectionName}"/>
                                                 </cache>       

                                                </td>                                                
                                                <!-- panier -->
                                                <c:choose>
                                                <c:when test="${param['popup'] !='1' }"> <%-- && r.type == panier.type --%>
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
                                                <td class="text" align="left">
                                                        <c:if test="${param['view'] !=null && param['view'] != '' && status.index < 10}">
                                                                <x:transform xslt="${xslResult}"><c:out value="${r.content}" default="<ionPublication/>" escapeXml="false"/></x:transform>
                                                        </c:if>
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

