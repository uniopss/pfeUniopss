<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Liens circulaires</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text"  onload="top.resizeTo(400,300)">
                <c:if test="${param['zaction']!=null && param['zaction']!='' && param['plid']!=null && param['plid']!='' }">
<%
pageContext.setAttribute("idLien",request.getParameter("zaction").replaceAll("select_",""));
%>
<script>
var <c:out value="${param['idLien']}"/>P= new Array();
function ajouteElement(list,id,elt,produit){
	var found = false;
	for (var i=0;i<list.length && !found;i++ ){
		var tokens = list[i].split(';');
		if (tokens[0] == elt){
			found = true;
		}
	}
	if (!found && elt != produit)
		list.push(elt+';'+id);
}
function generateList(list){
        var _do = false ;
	if (list.length > 0){
                document.write("<ul >");
		while (list.length > 0){
			var elt = list.pop();
			var tokens = elt.split(';');
                       if (tokens[0]!="<c:out value="${param['plid']}"/>" ) {
                        _do = true ;
                        document.write("<li>");
			document.write(tokens[0]);
                        document.write("</li>"); }
		}
		document.write("</ul>");
	}
        if (!_do)
                document.write("Aucun");
}
for (var i=0;i<top.opener.document.formu.elements.length;i++ )
      if (top.opener.document.formu.elements[i].name.indexOf("d_r_")==0 && top.opener.document.formu.elements[i].value != null && top.opener.document.formu.elements[i].value != '')
        ajouteElement(<c:out value="${param['idLien']}"/>P,top.opener.document.formu.elements[i].value,top.opener.document.formu.elements[i].value,'<c:out value="${param['produit']}"/>');
</script>
                <span class="title-list">Liens circulaires de type "<c:out value="${idLien}"/>" trouvés pour le produit "<c:out value="${param['plid']}"/>"</span>
                <ion:begin/>
                <ion:selectObjects type="org.nextime.ion.frontoffice.objectSelector.SearchOnlinePublications">
                        <ion:param name="queryString" value="${param['plid']}"/>
                        <ion:param name="rootSection" value="bp"/>
                        <ion:param name="index" value="fr"/>
                        <ion:param name="view" value=""/>
                        <ion:param name="type" value="${idLien}"/>
                        <ion:iterateOver var="result">
                                <script>ajouteElement(<c:out value="${param['idLien']}"/>P,'<c:out value="${result.searchResult.name}" escapeXml="false"/>','<c:out value="${result.searchResult.name}" escapeXml="false"/>','<c:out value="${param['produit']}"/>');</script>
                        </ion:iterateOver>
                </ion:selectObjects>
                <ion:close/><P>
                <script>generateList(<c:out value="${param['idLien']}"/>P);</script>
                </c:if>
</body>
</html>



