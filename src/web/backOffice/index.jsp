<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<html>
<head>
        <title>Interface de contribution CMS [<c:out value="${userLogin}" default=""/>]</title>
</head>

<!-- Body -->
<frameset rows="73,*" cols="*" frameborder="NO" border="0" framespacing="0">
  <frame name="banner" src="menu.x" scrolling="no" noresize scrolling="no">
  <frame name="sub" src="indexSub.jsp" scrolling="auto" marginheight="5" marginwidth="10" noresize>
</frameset>

</html>
