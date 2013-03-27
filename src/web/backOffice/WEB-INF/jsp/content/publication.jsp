<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='' background="images/fond.gif">
        <script language="JavaScript" src="scripts/calendrier.js"></script>
        <html:form action="publication.x">
             <input type="Hidden" name="postBack" value="true">
             <input type="Hidden" name="sid" value="<c:out value="${section.id}"/>">
             <input type="Hidden" name="id"  value="<c:out value="${publication.id}"/>">
             <input type="Hidden" name="version" value="<c:out value="${version}"/>">
             <input type="Hidden" name="label"  value="<c:out value="${publication.metaData['name']}"/>">
   	<img src="images/publication-edit-tile.gif" align="absmiddle">
	<span class="title">
                Gestion de la publication " <c:out value="${publication.metaData['name']}" default="<i>(undefined)</i>" escapeXml="false"/> "
	</span>
	<br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="title-list"><c:out value="${section.pathLabel}" default="" escapeXml="false"/></span>
	<br>
	<hr width="100%" size="1" noshade color="#949494">
                <table>
                        <tr>
                                <td align="left" valign="top" class="text">ID</td>
                                <td align="left" valign="top" class="text"><input type=text disabled=true value="<c:out value="${publication.id}"/>">
                                &nbsp;&nbsp;
                                <c:if test="${userLogin=='admin'}">
                                <a href="#" onclick="if(confirm('voulez-vous éditer ses méta données ?')) window.open('publicationMd.x?sid=<c:out value="${section.id}"/>&id=<c:out value="${publication.id}"/>&version=<c:out value="${version}"/>','md','width=600,height=400,resizable=yes,scrollbars=yes')"><img src="images/choix.gif" border="0" alt="méta données" align="absmiddle"></a>
                                </c:if>
                                </td>
                        </tr>
                        <tr>
                        <td align="left" valign="top" class="text">Nom</td>
                        <td align="left" valign="top" class="text"><html:text style="width:300px" property="n"/><br><span class="error"><html:errors property="n"/></span></td>
                        </tr>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <tr>
                                <td colspan="2" height="1" >&nbsp;</td>
                        </tr>
                        <tr>
                        <td align="left" class="text">Date de publication (dd/MM/yyyy)</td>
                        <td align="left" class="text">
                        <html:text style="width:260px" name="PublicationForm" property="dp" readonly="true"/>
                        <a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('dp','fr');this.style.cursor='hand'" align="absmiddle" border="0"></a>
                        <a href="" onclick="document.forms[0].dp.value='';return false;"><img src="images/poubelle.gif" style="border=0; background-color:transparent"></a>
                        </td>
                        </tr>
                        <tr>
                        <td align="left" class="text">Date de dépublication (dd/MM/yyyy)</td>
                        <td align="left" class="text"><html:text style="width:260px" property="df" readonly="true"/>
                        <a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('df','fr');this.style.cursor='hand'" align="absmiddle" border="0"></a>
                        <a href="" onclick="document.forms[0].df.value='';return false;"><img src="images/poubelle.gif" style="border=0; background-color:transparent"></a>
                        </td>
                        </tr>
                        <tr>
                        <td align="left" class="text">Date de changement (dd/MM/yyyy)</td>
                        <td align="left" class="text"><html:text style="width:260px" property="dc" readonly="true"/>
                        <a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('dc','fr');this.style.cursor='hand'" align="absmiddle" border="0"></a>
                        <a href="" onclick="document.forms[0].dc.value='';return false;"><img src="images/poubelle.gif" style="border=0; background-color:transparent"></a>
                        </td>
                        </tr>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <tr>
                                <td colspan="2" height="1" >&nbsp;</td>
                        </tr>
                        <c:choose>
                        <c:when test="${publication.type.id == 'objet'}">
                        <tr>
                        <td align="left" class="text">Type d objet</td>
                        <td align="left" class="text">
                                <html:select property="o" style="width:260px">
                                        <html:option value=""></html:option>
                                        <c:forEach var="obj" items="${objets}">
                                                <c:set var="tempVar" value="${obj.id}" scope="request"/>
                                                <% String tempVar2 = request.getAttribute("tempVar")+""; // y a des problémes quelque part ???!! %>
                                                <html:option value="<%=tempVar2%>"><c:out value="${obj.label}"/></html:option>
                                        </c:forEach>
                                </html:select>
                        </td>
                        </tr>
                        <tr>
                        <td align="left" class="text">Modele de présentation objet</td>
                        <td align="left" class="text">
                                <html:text style="width:260px" property="op" />
                        </td>
                        </tr>
                        </c:when>
                        <c:otherwise>
                        <tr>
                        <td align="left" class="text">Modele de presentation</td>
                        <td align="left" class="text">
                                <html:select property="p" style="width:260px" >
                                        <html:option value=""></html:option>
                                        <c:forEach var="style" items="${styles}">
                                                <c:set var="tempVar" value="${style}" scope="request"/>
                                                <% String tempVar2 = request.getAttribute("tempVar")+""; // y a des problémes quelque part ???!! %>
                                                <html:option value="<%=tempVar2%>"><c:out value="${style}"/></html:option>
                                        </c:forEach>
                                </html:select>
                        </td>
                        </tr>
                        </c:otherwise>
                        </c:choose>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <tr>
                                <td colspan="2" height="1" >&nbsp;</td>
                        </tr>

<%--  TODO : SPEC  --%>
                        <tr>
                        <td align="left" class="text">Curseur de visibilité</td>
                        <td align="left" class="text">
                                <html:select property="curseurVisibilite" style="width:260px">
                                        <html:option value="GDPL">Grand Public</html:option>
                                        <html:option value="PART">Partenaire</html:option>
                                        <html:option value="ADHE">Adhérent</html:option>
                                        <html:option value="RESE">Réseau</html:option>
                                        <html:option value="LOCA">Local</html:option>
                                </html:select>
                                
                        </td>
                        </tr>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <tr>
                                <td colspan="2" height="1" >&nbsp;</td>
                        </tr>
<%--                        
			dev spécifique
                        <tr>
                        <td align="left" class="text">Canal de diffusion</td>
                        <td align="left" class="text">
                                <html:select property="d" style="width:260px">
                                        <html:option value=""></html:option>
                                        <html:option value="internet">Internet</html:option>
                                        <html:option value="export">Export</html:option>
                                </html:select>
                        </td>
                        </tr>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <tr>
                                <td colspan="2" height="1" >&nbsp;</td>
                        </tr>
--%>                        
                        <tr>
                        <td align="left" class="text">Site</td>
                        <td align="left" class="text">
                        	<html:select property="siteId" style="width:260px"  onchange="document.forms[0].submit()">
										<html:options collection="sites" labelProperty="label" property="value" />
				            </html:select>
                        </td>
                        </tr>
                        <tr>
                        <td align="left" class="text">Sections d appartenance</td>
                        <td align="left" class="text">

                                        <div style="border:1px solid black;height:300px;overflow:auto" class="button2">
                                        <table cellspacing=0 cellpadding=0>
                                        <c:forEach var="z" items="${sectionList}">
                                                <c:set var="tempVar" value="${z.id}" scope="request"/>
                                                <% String tempVar2 = request.getAttribute("tempVar")+""; %>
                                                <tr class="text"><td><html:multibox property="sections" value="<%=tempVar2%>"  style="border:0px;background-color:transparent"/></td><td><c:out value="${z.metaData['name']}" default="${z.id}"/></td><td>&nbsp;<c:out value="${z.id}"/></td></tr>
                                        </c:forEach>
                                        </table>
                                        </div>
                        </td>
                        </tr>
                </table>
                <input type="submit" onclick="return confirm('Voulez-vous sauvegarder ?')" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/>
        </html:form>
</body>

</html>



