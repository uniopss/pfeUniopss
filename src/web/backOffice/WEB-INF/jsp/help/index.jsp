<%@ page import="java.util.Arrays" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
        <title>Aide en ligne</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" background="images/fond.gif">
        <img src="images/help-tile.gif" align="absmiddle">
	<span class="title">
                Aide en ligne
	</span>
	<br><br>
        <form id="form" name="form" method="post">
        <input type=hidden name=aide value="<c:out value="${param['aide']}"/>">
        <span class="title-list">Thème :</span>&nbsp;&nbsp;<select name="etat" style="font-family:verdana; color:black; font-size:9px; font-weight:bold; border:1px solid black; background-color:#d6e4f3;" onChange="form.submit()">
                <%
                String _etat = null;
                try {
                        java.io.File catalogsDirectory = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getResourcesDirectory(),"aide/"+request.getParameter("aide"));
                        if (catalogsDirectory.exists()) {
                                java.io.File[] catalogFiles = catalogsDirectory.listFiles();
                                        if (catalogFiles!=null && catalogFiles.length>0) {
                                                _etat = (request.getParameter("etat")!=null&&!"".equals(request.getParameter("etat"))?request.getParameter("etat"):catalogFiles[0].getName());
                                                for(int z=0;z<catalogFiles.length;z++){%>
                                                <option value="<%=catalogFiles[z].getName()%>" <%if (_etat.equals(catalogFiles[z].getName())){%>selected<%}%>><%=catalogFiles[z].getName().replaceAll("_"," ")%></option>
                                        <%
                                                }
                                        }
                        }
                }
                catch(Exception e) {
                }
                %>
        </select>
        </form>
        <hr width="100%" size="1" noshade color="#949494">
	<br><br>
<table cellpadding="3" cellspacing="3">
	<tr>
		<% int i=0; int j=0; %>
<%
try {
        String url = "aide/"+request.getParameter("aide")+"/"+_etat ;
        java.io.File c = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getResourcesDirectory(),url);
        if (c.exists()) {
                java.io.FilenameFilter filter = new java.io.FilenameFilter() {
                        public boolean accept(java.io.File dir, String name) {
                                return name.matches(".+(JPG|ppt)");
                        }     } ;
                java.io.File[] catalogFiles = c.listFiles(filter);
                Arrays.sort(catalogFiles);
                if (catalogFiles!=null && catalogFiles.length>0) {
                        for(int z=0;z<catalogFiles.length;z++){
                                if (i>4) { %></tr><tr><% ; i=0; }
                                i++; j++; %>
                                <td valign="top">
                                        <table width="100" height="120" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                        <td align="center" valign="middle" colspan="2"  style="border:1px solid #959595" >
                                                                <a href="#" onclick="window.open('/resources/<%=url%>/<%=catalogFiles[z].getName()%>','aide','width=680,height=505,resizable=yes,scrollbars=yes')" >
                                                                <% if (catalogFiles[z].getName().endsWith(".JPG")) { %>
                                                                <img src="/resources/<%=url%>/<%=catalogFiles[z].getName()%>" height="100" width="150" border="0" style="border:1px solid #ffffff">
                                                                <% } else { %>
                                                                <img src="/backoffice/images/ppt-file.gif"  border="0" style="border:1px solid #ffffff">
                                                                <% } %>
                                                                </a>
                                                        </td>
                                                </tr>
                                                <tr>
                                                        <td class="text" align="center" height="15" style="color:#4e4e4e;">
                                                        <div style="border:1px solid black;width:150px;height:35px;overflow:auto" class="button2"><%=catalogFiles[z].getName().replaceAll("_"," ").replaceAll(".JPG","")%></div>
                                                        </td>
                                                </tr>
                                        </table>
                                </td>
                                <%
                        }
                }
        }
}
catch(Exception e) {
}
%>
</tr>
</table>

</body>

</html>
