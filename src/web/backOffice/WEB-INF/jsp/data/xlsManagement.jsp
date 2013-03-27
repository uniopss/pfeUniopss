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
        <title>Bases importées (XML)</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" onload='' background="images/fond.gif">
        <img src="images/data-tile.gif" align="absmiddle">
	<span class="title">
                Bases importées (XML)
	</span>
        <P/>
       <form id="upFile" name="upFile" action="/backoffice/xlsManagement.x" enctype="multipart/form-data" method="POST" onsubmit="return false">
                Importer <input type="file" name="file" class="button4">
                dans <select id=name name=name>
                        <option value="b1">Distributeurs de pièces détachées</option>
                        <option value="b2">Réparateurs agréés</option>
                        <option value="b3">Revendeurs</option>
                </select>
                <input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0'
                        onclick="goU();">
	</form>
        <script>
        function goU() {
                v =document.upFile.file.value;
                if (!v) {
                        alert("Veuillez choisir un fichier XML à charger.");
                        return ;
                }
                if (confirm('Voulez-vous exécuter cette opération ?')) {
                        document.upFile.submit() ;
                }
        }
        </script>
<c:choose>
<c:when test="${result=='ok'}">Fichier correctement chargé</c:when>
<c:when test="${result=='ko'}"><img src='images/warning.gif' align='absmiddle'> Erreur au chargement</c:when>
</c:choose>
</body>
</html>



