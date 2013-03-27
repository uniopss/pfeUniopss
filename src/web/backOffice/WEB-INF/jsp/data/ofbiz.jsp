<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Ofbiz</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" onload='' background="images/fond.gif">
<c:if test="${userLogin=='admin'}">
        <img src="images/data-tile.gif" align="absmiddle">
	<span class="title">
                Ofbiz
	</span>
        <div style="border:1px solid black;;background-color:#F5F5F5;height:400px;overflow:auto">
        <pre class="text"><c:out value="${result}"/></pre>
        </div>
</c:if>
</body>
</html>



