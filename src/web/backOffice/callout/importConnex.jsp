<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>
        Import CONNEX
        </title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text" onload="top.resizeTo(400,500)">
<script language="javascript">
var ca_CT = new Array();
var ca_FM = new Array();
var cu_Import = false ;
var cu_Connex = false ;
function applyBgColor(tableId){
        if(tableId != null && tableId != null) {
                objTable = document.getElementById("tr_"+tableId);
                if(objTable == "" || objTable == null)
                        ca_FM[ca_FM.length] = tableId ;
                else
                        objTable.className = "button4";
        }
}
function lcC() {
        if (!cu_Import)
                alert("Il n'est pas possible d'importer ces informations.");
        else {
                if (confirm("Voulez-vous importer ces données CONNEX dans votre fiche ?")) {
                        sXml = "<lc>" ;
                        sRep = "" ;
                        var i = 0 ;
                        for(var x=0;x<document.liste.elements.length;x++){
                                var y = document.liste.elements[x];
                                if (ca_CT[y.name] != null) {
                                        sXml += "<r ordre='"+(i++)+"'><i>"+y.name+"</i><d>"+y.value+"</d><c>"+ca_CT[y.name]+"</c></r>" ;
                                }
                        }
                        if (sXml != "<lc>") {
                                //for(var x=0;x<ca_FM.length;x++){
                                //        sXml += "<r ordre='"+(i++)+"'><i>"+ca_FM[x]+"</i><d></d><c>"+ca_CT[ca_FM[x]]+"</c></r>" ;
                                //}
                                sXml += "</lc>" ;
                                eval(top.opener.importConnex(sXml));
                                self.close();
                        }
                        else
                                alert("Aucune information CONNEX ne correspond au référentiel des caractéristiques techniques déclarées pour la famille de ce produit.\r\n\r\nL'importation est annulée");
                }
        }
}
</script>
<c:set var="cTxsl">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html" encoding="ISO-8859-1"/>
        <xsl:template match="/lc">
        <xsl:choose>
        <xsl:when test="count(ct)=0">
                Aucune caractéristique technique CONNEX n'est disponible pour ce produit.
        </xsl:when>
        <xsl:otherwise>
                <script language="javascript"> cu_Connex = true ;</script>
                <table cellpadding="0" cellspacing="2" width="100%" class="text" bgcolor="#F5F5F5" border="0" style="border:1px solid black;">
                <tr>
                        <td height="1" width="20"></td>
                        <td align="right" width="100%"><a href="javascript:lcC()">Importer</a></td>
                </tr>
                <tr>
                        <td height="1" bgcolor="#949494"></td>
                        <td height="1" bgcolor="#949494"></td>
                </tr>
                <xsl:for-each select="ct">
                        <xsl:sort select="@name"/>
                        <tr id="tr_{@name}">
                                <td class="text">
                                        <xsl:element name="input">
                                                <xsl:attribute name="name"><xsl:value-of select="@name"  disable-output-escaping="yes"/></xsl:attribute>
                                                <xsl:attribute name="value"><xsl:value-of select="."     disable-output-escaping="yes"/></xsl:attribute>
                                                <xsl:attribute name="type">hidden</xsl:attribute>
                                        </xsl:element>
                                        <xsl:value-of select="@name" disable-output-escaping="yes"/>&#160;
                                </td>
                                <td class="text">&#160;<xsl:value-of select="." disable-output-escaping="yes"/></td>
                        </tr>
                </xsl:for-each>
                </table>
        </xsl:otherwise>
        </xsl:choose>
        </xsl:template>
</xsl:stylesheet>
</c:set>
<c:set var="xsl">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html" encoding="ISO-8859-1"/>
        <xsl:template match="/lc">
        <P/>
        <xsl:choose>
        <xsl:when test="count(r)=0">
                <script language="javascript"> if (cu_Connex)
                        document.write("<i>Aucune caractéristique technique n'a été déclarée dans le référentiel pour la famille de ce produit.Aucune information CONNEX n'est donc importable.</i>");
                </script>
        </xsl:when>
        <xsl:otherwise>
                <script language="javascript"> cu_Import = true ;
                <xsl:for-each select="r">
                        <xsl:text>ca_CT["</xsl:text><xsl:value-of select="i/."/><xsl:text>"]="</xsl:text><xsl:value-of select="c/."/><xsl:text>";</xsl:text>
                        <xsl:text>applyBgColor("</xsl:text><xsl:value-of select="i/."/><xsl:text>");</xsl:text>
                </xsl:for-each>
                </script>
                <i>Les valeurs CONNEX des caractéristiques techniques <b>en surbrillance</b> sont celles qui sont déclarées dans le référentiel de caractéristiques
                techniques correspondant à la famille du produit sélectionné. Ce sont ici les seules données importables dans la fiche produit.</i>
        </xsl:otherwise>
        </xsl:choose>
        </xsl:template>
</xsl:stylesheet>
</c:set>
<form name="liste" method="POST">
<%
        String sC = "";
        String cT = "";
        try {
                java.io.File fc = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getImportDirectory(),"/apps/data/temp/cmp/connex/CT/"+request.getParameter("plid")) ;
                if (!fc.exists())
                        fc = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getImportDirectory(),"/apps/archive/temp/cmp/connex/CT/"+request.getParameter("plid")) ;
                java.io.FileInputStream fis = new java.io.FileInputStream(fc);
                java.util.PropertyResourceBundle res = new java.util.PropertyResourceBundle(fis);
                java.util.Enumeration mdks = res.getKeys();
                while (mdks.hasMoreElements()) {
                        String key = mdks.nextElement() + "";
                        cT += "<ct name=\""+key+"\"><![CDATA["+res.getString(key)+"]]></ct>\r\n" ;
                }
                fis.close();
                cT = "<lc>"+cT+"</lc>";
                try {
                        String[] s = request.getParameter("sid").toString().split("_");
                        java.io.File f = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getProduitDirectory(),s[0]+"_"+s[1]+"_"+s[2]+"/caracteristique.xml");
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

        } catch (Exception e) {
        }
        if (cT==null || "".equals(cT))
                cT = "<lc/>" ;
        if (sC==null || "".equals(sC))
                sC = "<lc/>" ;
%>
<x:transform xslt="${cTxsl}"><%=cT%></x:transform>
<x:transform xslt="${xsl}"><%=sC%></x:transform>
</form>
</body>
</html>
