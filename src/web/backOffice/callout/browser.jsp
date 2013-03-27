<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head>
        <title>Browser <c:out value="${param['btype']}"/></title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" background="/backoffice/images/fond.gif" onload="top.resizeTo(806,675)">
<%String selectedType="";%>
<c:choose>
<c:when test="${param['btype']!='' && param['btype']!=null && param['btype']!='_download' && param['btype']!='_lien'}">
        <c:set var="selectedType" value="${param['btype']}" scope="session"/>
</c:when>
<c:otherwise>
        <%
        selectedType = request.getParameter("btype")!=null&&request.getParameter("btype")!=""&&!"_download".equals(request.getParameter("btype"))&&!"_lien".equals(request.getParameter("btype"))?request.getParameter("btype"):request.getParameter("selectedType") ;
        try {
                if (selectedType == null)
                        selectedType =
                        ((org.nextime.ion.backoffice.bean.ResourceXmlBean) org.nextime.ion.backoffice.bean.Resources.getResourceXmlBeans().get(0)).getId();
        } catch (Exception e) {}
        request.setAttribute("selectedType",selectedType);
        %>
</c:otherwise>
</c:choose>
<%
String selectedSite = request.getParameter("selectedSite") ;
try {
        if (selectedSite == null)
                selectedSite =
                        ((org.nextime.ion.framework.business.Section)((java.util.Vector) session.getAttribute("currentSite"))
                                .get(0)).getId();
} catch (Exception e) {}
request.setAttribute("selectedSite",selectedSite);
%>
<script language="javascript">
function lcC() {
        id ="" ;
        for(var x=0;x<callout.document.FileList.elements.length;x++){
                var y = callout.document.FileList.elements[x];
                if (y.checked) {
                        id = callout.document.FileList.cwdir.value+"/"+y.value ;
                        break;
                }
	}
        <c:choose>
        <c:when test="${param['btype']=='_download'}">
                if (id!=null && id!='') {
                        var liste = id.split('/');
                        <%-- desactiver le javascript --%>
                        <%--
                        eval(top.opener.<c:out value="${param['action']}"/>("javascript:lien('/viewResources"+id+"?download="+liste[liste.length-1]+"','"+ liste[liste.length-1]+"', '_blank', 'no', 'yes', 640, 480)"));
                        --%>
						eval(top.opener.<c:out value="${param['action']}"/>(id));
                }
                else {
                eval(top.opener.<c:out value="${param['action']}"/>(id));
                }
        </c:when>
        <c:otherwise>
                eval(top.opener.<c:out value="${param['action']}"/>(id));
        </c:otherwise>
        </c:choose>
        self.close();
}
</script>
<form id=ress name=ress action="/backoffice/callout/browser.jsp" >
<input name="action" value="<c:out value="${param['action']}"/>" type="hidden"/>
<table width="100%">
<td align="left" class="text">Site&nbsp;
<select name="selectedSite" style="width=150" onChange="document.ress.submit()">
        <c:forEach items="${currentSite}" var="index">
                <option value="<c:out value="${index.id}"/>" <c:if test="${index.id==selectedSite}">selected</c:if>><c:out value="${index.metaData['name']}"/></option>
        </c:forEach>
</select>
</td>
<td align="left" class="text">
<input name="btype" value="<c:out value="${param['btype']}"/>" type="hidden"/>
<c:if test="${param['btype']=='_download' || param['btype']=='_lien'}">
Type&nbsp;<select name="selectedType" style="width=150" onChange="document.ress.submit()">
<%pageContext.setAttribute("resources", org.nextime.ion.backoffice.bean.Resources.getResourceXmlBeans());%>
<c:forEach var="res" items="${resources}">
        <option value="<c:out value="${res.id}"/>" <c:if test="${selectedType==res.id}">selected</c:if>><c:out value="${res.label}"/></option>
</c:forEach>
</select>
</c:if>
</td>
<td align="right">
<a href="javascript:lcC()">Choisir cet element et terminer</a>
</td>
</tr>
</table>
</form>
<iframe id="callout" name="callout" src="/backoffice/edx/browser.jsp?action=<c:out value="${param['action']}"/>&selectedSite=<c:out value="${selectedSite}"/>&selectedType=<c:out value="${selectedType}"/>"
frameborder="0" marginheight="3"
marginwidth="3" height="530" width="755" style="border:1px solid black;"/>
</body>
</html>
