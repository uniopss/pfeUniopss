<%@ taglib uri="/WEB-INF/tlds/controls.tld" prefix="controls" %>
<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="0">
  <title>tree</title>
  <link rel="stylesheet" type="text/css" href="styles/tree.css">
  <link rel="stylesheet" type="text/css" href="styles/global.css">
</head>
<body background="images/fond.gif" bgcolor="white" style="cursor:default" class="text" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
<form name=tree method=post action="" target="">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
<tr>
        <td valign="top" rowspan="2">
                <controls:tree tree="treeControlTest" action="lcTree_1('${name}')" style="tree-control" styleSelected="tree-text" styleUnselected="tree-text" images="images" />
        </td>
        <td valign="top"><a onclick="return confirm('Voulez-vous rafraichir l arborescence ?')"  href="setUpTree.x?clean=yes"><img alt="rafraichir l'arborescence" border=0 src="images/refresh.gif"></a></td></tr>
</table><script language=javascript>
function lcTree_1(tree) {
        document.tree.tree.value=tree ;
        document.tree.action="/backoffice/treeControl.x";
        document.tree.target="";
        document.tree.submit();
}
function lcTree_2(sid) {
        document.tree.sid.value=sid ;
        document.tree.action="/backoffice/viewSection.x";
        document.tree.target="content";
        document.tree.submit();
}
</script><input type=hidden name=sid><input type=hidden name=tree></form>
</body>
</html>
