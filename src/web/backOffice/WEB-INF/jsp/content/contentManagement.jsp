<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head>
        <title>Content Management</title>
</head>
<frameset cols="220,*" frameborder="NO" border="0" framespacing="0">
  <frame name="tree" marginheight="0" marginwidth="0" src="setUpTree.x" scrolling="auto" style="border-right:1px solid #949494">
  <frame name="content" src="viewSection.x?sid=<c:out value="${sid}"/>" scrolling="auto" marginheight="5" marginwidth="10">
</frameset>
</html>
