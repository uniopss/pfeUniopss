<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<ion:begin/>

<html>
<head>
        <title>Panier</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif" style="cursor:default" >

        <img src="images/mywork-tile.gif" align="absmiddle">
	<span class="title">
                Mon panier
	</span>
        <P/>
        <c:choose>
                <c:when test="${panier.size>0}">
                        <script language="javascript">
                                function deselectRadio(radioId){
                                        var objForm = document.panier;
                                        objRadio = document.panier.elements[radioId];
                                        if(!objRadio)
                                                return;
                                        if (objRadio.length)
                                                for(i=0; i<objRadio.length; i++)
                                                        objRadio[i].checked=false;
                                        else
                                                objRadio.checked=false;
                                }
                        </script>
                        <span class="title-list">Les éléments que vous avez sélectionnés sont les suivants :</span>
                                <form name="panier" method="post" action="exportPanier.x" target="_export">
                                <input name="type" type="hidden">
                                <table border=0 cellpadding=3 cellspcacing=3 width="700" style='border:1px solid black;;background-color:#eeeeee'>
                                <tr class=text><th></th><th></th><th>Nom</th><th>Version</th><th>Section</th><th></th></tr>
                                <%int i=0;%>
                                <c:set var="id" />
                                <c:forEach var="pp" items="${panier.elements}" >
                                        <% try { pageContext.setAttribute("pv",org.nextime.ion.framework.business.PublicationVersion.getInstance(pageContext.getAttribute("pp")+"")); %>
                                        <c:if test="${id!='' && id!=pv.publication.id}">
                                        <tr>
                                                <td height="1" bgcolor="#949494"></td>
                                                <td height="1" bgcolor="#949494"></td>
                                                <td height="1" bgcolor="#949494"></td>
                                                <td height="1" bgcolor="#949494"></td>
                                                <td height="1" bgcolor="#949494"></td>
                                                <td height="1" bgcolor="#949494"></td>
                                        </tr>
                                        </c:if>
                                        <tr valign="top">
                                                <td class="text" width="20">
                                                        <input type=hidden name="sc_<c:out value="${pv.publication.id}"/>" value="<c:out value="${pv.publication.sections[0].id}"/>">
                                                        <input type=hidden name="rf_<c:out value="${pv.publication.id}"/>" value="<c:out value="${pv.publication.metaData['name']}"/>">
                                                        <input class="button2" style="border:0px" type=radio name="<c:out value="${pv.publication.id}"/>" value="<c:out value="${pv.version}"/>">
                                                </td>
                                                <td class="text" width="40" align="center">
                                                        <c:if test="${id!=pv.publication.id}">
                                                                <c:set var="id" value="${pv.publication.id}"/>
                                                                <a onclick="return confirm('voulez-vous déselectionner cet élément ?');" href="javascript:deselectRadio('<c:out value="${pv.publication.id}"/>');"><img alt="déselectionner" align="absmiddle" src="images/poubelle-mini.gif" border="0"></a>&nbsp;
                                                        </c:if>&nbsp;
                                                </td>
                                                <td style="font-size:10px; font-family:verdana;" align="left" nowrap>
                                                        <a onclick="if (confirm('voulez-vous aller vers cette fiche ?')){parent.frames['banner'].document.location='menu.x?tab=1'} else return false;" href="highlight.x?id=<c:out value="${pv.publication.id}"/>" style="text-decoration:none">
                                                        <img title="aller vers la fiche" border=0 src="images/pucef.gif" align="absmiddle" >
                                                        </a>&nbsp;
                                                        <a  href="#contenu" style="text-decoration:none"
                                                                onclick="window.open('viewPublication.x?id=<c:out value="${pv.publication.id}"/>&version=<c:out value="${pv.version}"/>&sid=<c:out value="${pv.publication.sections[0].id}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')">
                                                        <strong style="color:#5e5e5e;"><c:out value="${pv.publication.metaData['name']}"/></strong></a>
                                                </td>
                                                <td style="font-size:10px; font-family:verdana;" align="center">
                                                                <c:out value="${pv.version}"/>
                                                </td>
                                                <td class="text" align="left">
                                                        <c:forEach var="z" items="${pv.publication.sections}">
                                                                <c:out value="${z.pathLabel}"/><br>
                                                        </c:forEach>
                                                </td>
                                                <td width="10" style="font-size:10px; font-family:verdana;" height="15">
                                                        <a onclick="return confirm('voulez-vous retirer cet élément du panier ?');" href="deletePanier.x?id=<c:out value="${pp}"/>"><img alt="retirer cet élément du panier" align="absmiddle" src="images/poubelle.gif" border="0"></a>
                                                </td>
                                        </tr>
                                        <% } catch(Exception e) {} %>
                                </c:forEach>
                                </table>
                                <%--
                                <P>
                                <table style='border:1px solid black;;background-color:#F5F5F5'>
                                        <tr>
                                                <td width='260' align='right' class='text'><b>Exporter au format PDF</b></td>
                                                <td valign='middle'>
                                                        <input type='image' style='border:0px;background-color:#F5F5F5'
                                                                alt='PDF' src='/backoffice/images/valide.gif'
                                                                onclick="if (confirm('Voulez-vous exporter ?')) {document.forms(0).type.value='pdf'}else return false;"
                                                                border='0'>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td width='260' align='right' class='text'><b>Exporter au format PowerPoint</b></td>
                                                <td valign='middle'>
                                                        <input type='image' style='border:0px;background-color:#F5F5F5'
                                                                alt='PowerPoint' src='/backoffice/images/valide.gif'
                                                                onclick="if (confirm('Voulez-vous exporter ?')) {document.forms(0).type.value='ppt'}else return false;"
                                                                border='0'>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td width='260' align='right' class='text'><b>Exporter au format RTF</b></td>
                                                <td valign='middle'>
                                                        <input type='image' style='border:0px;background-color:#F5F5F5'
                                                                alt='RTF' src='/backoffice/images/valide.gif'
                                                                onclick="if (confirm('Voulez-vous exporter ?')) {document.forms(0).type.value='rtf'}else return false;"
                                                                border='0'>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td width='260' align='right' class='text'><b>Exporter au format XML</b></td>
                                                <td valign='middle'>
                                                        <input type='image' style='border:0px;background-color:#F5F5F5'
                                                                alt='XML' src='/backoffice/images/valide.gif'
                                                                onclick="if (confirm('Voulez-vous exporter ?')) {document.forms(0).type.value='xml'}else return false;"
                                                                border='0'>
                                                </td>
                                        </tr>
                                </table>
                                --%>
                                </form>
                </c:when>
                <c:otherwise>
                        Votre panier est vide.
                </c:otherwise>
        </c:choose>
</body>

</html>
<ion:close/>
