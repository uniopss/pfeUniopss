<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>
        Marques
        </title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text" onload="top.resizeTo(400,250)">
<script language="javascript">
function lcC(id) {
        sXml = "<lc>" ;
        sRep = "" ;
        var i = 0 ;
        for(var x=0;x<document.liste.elements.length;x++){
                var y = document.liste.elements[x];
                if (y.checked) {
                        sXml += "<r ordre='"+(i++)+"'><l>"+y.name+"</l><d>"+y.value+"</d></r>" ;
                        sRep += "&nbsp;"+y.name + "&nbsp;:&nbsp;" + y.value +"<br>";
                }
	}
        sXml += "</lc>" ;
        eval(top.opener.<c:out value="${param['zaction']}"/>(sXml,sRep));
        self.close();
}
</script>
<c:set var="xsl">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:template match="/lc">
        <xsl:for-each select="r">
                <xsl:sort select="l/."/>
                <tr>
                        <td class="text">&#160;&#160;<input class="button2" style="border:0px" type="checkbox" name="{i/.}" value="{l/.}"/>&#160;&#160;</td>
                        <td class="text">&#160;&#160;<xsl:value-of select="l/."/></td>
                </tr>
        </xsl:for-each>
        </xsl:template>
</xsl:stylesheet>
</c:set>
<form name="liste" method="POST">
<table cellpadding="2" cellspacing="3" width="100%" class="text" bgcolor="#F5F5F5" style="border:1px solid black;">
<tr>
        <td height="1" width="20"></td>
        <td align="right" width="100%"><a href="javascript:lcC()">Terminer</a></td>
</tr>
<tr>
        <td height="1" bgcolor="#949494"></td>
        <td height="1" bgcolor="#949494"></td>
</tr>
<%
        String sC = "";
        try {
                java.io.File f = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getProduitDirectory(),"marque.xml");
                // lit le contenu du fichier
                String line = "";
                java.io.BufferedReader is = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream(f) ) );
                while( line != null ) {
                sC+=line+"\r\n";
                line = is.readLine();
                }
                is.close();

        } catch (Exception e) {
        }
        if (sC==null || "".equals(sC))
                sC = "<lc/>" ;
%>
<x:transform xslt="${xsl}">
<%=sC%>
</x:transform>
</table>
</form>
</body>
</html>
