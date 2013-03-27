<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Panier</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body class="text" background="images/fond.gif" style="cursor:default" onload="top.resizeTo(350,250)" >
        <img src="images/mywork-tile.gif" align="absmiddle">
	<span class="title">
                Mon panier
	</span>
	<br><br>
        <c:choose>
                <c:when test="${param['etat']=='1'}">
                        <!--span class="title-list">Voulez-vous supprimer cet élément de votre panier ?</span-->
                        <span class="title-list">Suppression de cet élément de votre panier ?</span>
                </c:when>
                <c:otherwise>
                        <!--span class="title-list">Voulez-vous ajouter cet élément à votre panier ?</span-->
                        <span class="title-list">Ajout de cet élément à votre panier ?</span>
                </c:otherwise>
        </c:choose>
        <form action="editPanier.x" method="post" name="panier" id="panier">
        <input type=hidden name=id value="<c:out value="${param['id']}"/>">
        <input type=hidden name=etat value="<c:out value="${param['etat']}"/>">
        <!--input type="submit" name="ok" value="<bean:message key="general.ok"/>" class="button"/>&nbsp;<input type="submit" name="cancel" value="<bean:message key="general.cancel"/>" class="button"/-->
        <input type=hidden name=ok value="1">
        </form>
        <script>document.panier.submit();</script>
</body>
</html>
