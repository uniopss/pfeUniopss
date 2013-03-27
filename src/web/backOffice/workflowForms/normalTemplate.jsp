<%@ taglib uri="/WEB-INF/tlds/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<html>
<head>
	<title>...</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body class="text" background="images/fond.gif">
	<img src="images/work-tile.gif" align="absmiddle">
	<span class="title">
                 Workflow
	</span>
	<br><br>
        <c:import url="${externalAction}?retour=/viewSection.x"/>
</body>
</html>
