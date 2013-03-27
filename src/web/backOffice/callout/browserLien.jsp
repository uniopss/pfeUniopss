<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Browser de liens</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" background="/backoffice/images/fond.gif" onload="top.resizeTo(600,500)">
<script language="javascript">
function lcC(id) {
        eval(top.opener.<c:out value="${param['action']}"/>(id));
        self.close();
}
</script>
<%
        String id = request.getParameter("idLien") ;
        if (id == null || "".equals(id))
                id = "link" ;
        String sXml = "<lc/>";
        try {
                java.io.File f = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getLienDirectory(), id+".xml");
                if (f.exists()) {
                        java.io.BufferedReader reader =
                                new java.io.BufferedReader(
                                        new java.io.InputStreamReader(new java.io.FileInputStream(f)));
                        sXml = "";
			String line = "";
			while (line != null) {
				line = reader.readLine();
                                if (line != null)
                                        sXml += line;
			}
                        reader.close();
                }
        } catch (Exception e) {
                sXml = "<error>Erreur</error>";
        }
%>
<form id=lien name=lien action="/backoffice/callout/browserLien.jsp" method="POST">
<input type=hidden id=action name=action value="<c:out value="${param['action']}"/>">
Type : <select id="idLien" name="idLien" style="width=150" onChange="document.lien.submit();">
<option value="link" <c:if test="${'link'==param['idLien']}">selected</c:if>>Lien Internet</option>
<option value="mail" <c:if test="${'mail'==param['idLien']}">selected</c:if>>Mail</option>
</select>
</form>
<c:choose>
<c:when test="${'mail'==param['idLien']}">
        <c:import url="/edx/mail2lst.xsl" var="xsl"/>
</c:when>
<c:otherwise>
        <c:import url="/edx/link2lst.xsl" var="xsl"/>
</c:otherwise>
</c:choose>
<x:transform xslt="${xsl}">
<%=sXml%>
</x:transform>
</body>
</html>
