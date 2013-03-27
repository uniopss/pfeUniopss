<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<ion:begin/>
<ion:security action="canAdminSecurity" user="${userLogin}">
	<c:set value="yes" var="canAdminSecurity"/>
</ion:security>
<ion:close/>
<html>
<head>
        <title>Lien</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" onload='' background="images/fond.gif">
        <c:choose>
        <c:when test="${laction == 'c' && content != null && content != ''}">
                <form id="formu" name="formu" action="listLink.x" method="POST">
                <input type='hidden' id='laction' name='laction' value="c">
                <c:import url="/edx/rep2form.xsl" var="xsl"/>
                <x:transform xslt="${xsl}">
                <c:out value="${content}" escapeXml="false"/>
                </x:transform>
                </form>
                <script language="javascript">document.formu.submit();</script>
        </c:when>
        <c:otherwise>
        <img src="images/link-tile.gif" align="absmiddle">
	<span class="title">
                Référentiel des liens Internet
	</span>
	<br>
                <script language="javascript">
                        function ouvrir(url)  {
                                window.open(url,'edx','width=640,height=480,resizable=yes,scrollbars=yes');
                        }
                </script>
                <form id="formu" name="formu" action="listLink.x" method="POST">
                <input type='hidden' id='laction' name='laction'>
                <input type='hidden' id='lid' name='lid'>
                <table bgcolor="#F5F5F5" border="0" style="border:1px solid black;" width="700">
                <c:set var="j" value="-1"/>
                <c:forEach var="pps" items="${ps}">
                <c:if test="${pps!=null && pps!=''}">
                        <c:set var="j" value="${j+1}"/>
                        <c:set var="_pps" value="${pps}" scope="request"/>
                        <% request.setAttribute("l","l_"+request.getAttribute("_pps"));%>
                        <% request.setAttribute("u","u_"+request.getAttribute("_pps"));%>
                        <% request.setAttribute("t","t_"+request.getAttribute("_pps"));%>
                        <% request.setAttribute("d","d_"+request.getAttribute("_pps"));%>
                        <% request.setAttribute("h","h_"+request.getAttribute("_pps"));%>
                        <% request.setAttribute("w","w_"+request.getAttribute("_pps"));%>
                        <% request.setAttribute("m","m_"+request.getAttribute("_pps"));%>
                        <tr>
                                <td valign="center" align='right' class='text' width='60' rowspan="2"><input type='hidden' name='<c:out value="${_pps}"/>' value='<c:out value="${j}"/>'><b>Lien (<c:out value="${j}"/>): </b></td>
                                <td class="text">URL&nbsp;<input type='text' name='<c:out value="${u}"/>' value='<c:out value="${param[u]}"/>' style="width:250px;">&nbsp;
                                <script language="javascript">
                                        function select_<c:out value="${u}"/>(value) {
                                                document.forms[0].<c:out value="${u}"/>.value=value;
                                        }
                                </script>
                                <c:if test="${canAdminSecurity!=null}">
                                ou choisir une <a href="#link" onclick='ouvrir("/backoffice/listSection.x?action=select_<c:out value="${u}"/>")'>section</a>
                                ou <a href="#link" onclick='ouvrir("/backoffice/listPublication.x?action=select_<c:out value="${u}"/>")' >publication</a>
                                ou <a href="#link" onclick='ouvrir("/backoffice/callout/browser.jsp?btype=_lien&action=select_<c:out value="${u}"/>")' >ressource</a>
                                </c:if></td>
                                <td align="right">
                                <c:if test="${canAdminSecurity!=null}">
                                <c:if test="${j gt 0}">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Monter' src='/backoffice/images/up.gif'
                                        border='0'
                                        onclick="laction.value='u';lid.value='<c:out value="${_pps}"/>'">
                                </c:if>
                                <c:if test="${j lt n-1}">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Descendre' src='/backoffice/images/down.gif'
                                        border='0'
                                        onclick="laction.value='d';lid.value='<c:out value="${_pps}"/>'">
                                </c:if>
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Supprimer' src='/backoffice/images/croix.gif'
                                        border='0'
                                        onclick="if (confirm('Voulez-vous supprimer ?')) { laction.value='s';lid.value='<c:out value="${_pps}"/>'} else return false;">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Ajouter' src='/backoffice/images/etoile.gif'
                                        border='0'
                                        onclick="laction.value='a';lid.value='<c:out value="${_pps}"/>'">
                                </c:if>
                                </td>
                        </tr>
                        <tr>
                                <td colspan="2" class="text">
                                <table>
                                        <tr class="text">
                                                <td>Target</td><td><select style="width:200px;" name="<c:out value="${t}"/>"><option value="_self">Fenetre courante</option><option <c:if test="${param[t] eq '_blank'}">selected</c:if> value="_blank">Nouvelle fenetre</option><option <c:if test="${param[t] eq '_popup'}">selected</c:if> value="_popup">Popup</option></select></td>
                                                <td>Titre</td><td><input name="<c:out value="${l}"/>" style="width:100px;" value="<c:out value="${param[l]}"/>"></td>
                                        </tr>
                                        <tr class="text"><td>Redim</td><td><select style="width:100px;" name="<c:out value="${d}"/>"><option value="no">non</option><option <c:if test="${param[d] eq 'yes'}">selected</c:if> value="yes">oui</option></select></td>
                                                <td>Menu</td><td><select style="width:200px;" name="<c:out value="${m}"/>"><option value="no">non</option><option <c:if test="${param[m] eq 'yes'}">selected</c:if> value="yes">oui</option></select></td></tr>
                                        <tr class="text"><td>Hauteur</td><td><input name="<c:out value="${h}"/>" style="width:100px;" value="<c:out value="${param[h]}"/>"></td>
                                                <td>Largeur</td><td><input name="<c:out value="${w}"/>" style="width:200px;" value="<c:out value="${param[w]}"/>"></td></tr>
                                </table>
                                </td>
                        </tr>
                        <c:if test="${j lt n-1}">
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        </c:if>
                </c:if>
                </c:forEach>
                </table>
                <P>
                                <c:if test="${canAdminSecurity!=null}">
                <table style='border:1px solid black;;background-color:#F5F5F5'>
                        <tr>
                        <c:choose>
                        <c:when test="${n == null || n==0}">
                                        <td width='160' align='right' class='text'><b>Ajouter un nouveau lien </b></td>
                                        <td valign='middle'>
                                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Créer' src='/backoffice/images/add.gif'
                                                border='0'
                                                onclick="laction.value='n';">
                                        </td>
                        </c:when>
                        <c:otherwise>
                                        <td width='260' align='right' class='text'><b>Sauvegarder dans le fichier de configuration </b></td>
                                        <td valign='middle'>
                                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Enregistrer' src='/backoffice/images/valide.gif'
                                                        border='0'
                                                        onclick="if (confirm('Voulez-vous sauvegarder ?')) {laction.value='t';} else return false;">
                                        </td>
                        </c:otherwise>
                        </c:choose>
                        </tr>
                </table>
                </c:if>
                </form>
        </c:otherwise>
        </c:choose>
</body>
</html>



