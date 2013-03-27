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
        <title>Reference produit</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" onload='' background="images/fond.gif">
        <img src="images/data-tile.gif" align="absmiddle">
	<span class="title">
                Reference produit
	</span>
        <P/>
<form name="lc" action="/backoffice/produit.x" method="POST" onsubmit="if (!document.lc.produit.value) {alert('Veuillez saisir une référence produit avant de confirmer.');return false;}">
Référence produit : <input name=produit value="<c:out value="${param['produit']}"/>">&nbsp;<input type="submit" value="Exporter en PDF" class="button" align="absmiddle"/>
</form>
<c:if test="${param['produit']!=null&&param['produit']!=''}">
        <c:choose>
        <c:when test="${result!=null}">
                <c:set var="iresult" scope="page"><c:out value="${result.id}" escapeXml="false"/></c:set>
                <c:set var="sresult" scope="page"><c:out value="${result.sections}" escapeXml="false"/></c:set>
                <%pageContext.setAttribute("sresult",pageContext.getAttribute("sresult").toString().substring(3,5));%>
                <center><iframe id="pdf" name="pdf" src="/pdfPublication/<c:out value="${iresult}"/>.html?view=<c:out value="${sresult}"/>_fiche_pdf&file=pdf.pdf"
                frameborder="0" marginheight="3"
                marginwidth="3" height="440" width="800" style="border:1px solid black;"/></center>
        </c:when>
        <c:otherwise>
                <center><img src='images/warning.gif' align='absmiddle'> Aucun produit trouvé !</center>
        </c:otherwise>
        </c:choose>
</c:if>
</body>
</html>



