<%@ taglib uri="/WEB-INF/tlds/controls.tld" prefix="controls" %>
<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>
        Sections
        </title>
        <link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body class=text background="/backoffice/images/fond.gif" onload="top.resizeTo(400,400)" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
<form name=tree method=post action="/backoffice/listSection.x" >
<controls:tree tree="treeControlTest" action="lcTree_1('${name}')" style="tree-control" styleSelected="tree-text" styleUnselected="tree-text" images="images" />
<script language=javascript>
function lcTree_1(tree) {
        document.tree.tree.value=tree ;
        document.tree.submit();
}
function lcTree_2(sid) {
        eval(top.opener.<c:out value="${param['action']}"/>("/section/"+sid+".html"));
        self.close();
}
</script><input type=hidden name=sid><input type=hidden name=tree><input type=hidden name=action value="<c:out value="${param['action']}"/>"></form>
</body>
</html>
