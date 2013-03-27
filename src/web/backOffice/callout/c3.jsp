<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Les caracteristiques specifiques</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text"  onload="top.resizeTo(400,500)">
        <% if (request.getParameter("nom") != null && !"".equals(request.getParameter("nom")) && request.getParameter("ct")==null) { %>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <textarea id="ct" name="ct" style="visibility:hidden;width:300px;height:1px"></textarea>
                <c:if test="${param['valeur']!=null && param['valeur']!=''}">
                <c:import url="/edx/rep2form.xsl" var="xsl"/>
                <x:transform xslt="${xsl}">
                <c:out value="${param['valeur']}" escapeXml="false"/>
                </x:transform>
                </c:if>
                </form>
                <script language="javascript">
                        document.formu.ct.value=top.opener.get_caracteristiques_techniques();
                        document.formu.submit();
                </script>
        <% } else { %>
                <script language="javascript">
                function lcC() {
                        eval(top.opener.<c:out value="${param['zaction']}"/>(document.formu.xml.value,document.formu.zxml.value));
                        self.close();
                }
                </script>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <input type='hidden' id='laction' name='laction'>
                <input type='hidden' id='lid' name='lid'>
                <table bgcolor="#F5F5F5" border="0" style="border:1px solid black;">
                <%
                        String sid = "";
                        try {
                        String[] s = request.getParameter("sid").toString().split("_");
                        sid = s[0]+"_"+s[1]+"_"+s[2];
                        }catch(Exception e) {}

                        String lid = request.getParameter("lid");
                        String laction = request.getParameter("laction");
                        String sXml = "<r ordre='0'>"
                                                +"<d>"+request.getParameter("d_r_0")+"</d>"
                                                +"</r>\n" ;
                                        if (!"t".equals(laction)) { %>
                        <tr>
                                <td >
                                <textarea name="d_r_0" onfocus="_focus=this" style="width:300px;height:100px"><%=request.getParameter("d_r_0")!=null?request.getParameter("d_r_0"):""%></textarea>
                                <a href="#plusProduit" onclick="window.open('/backoffice/listPP.x?sid=<%=sid%>&zaction=d_r_0','plusproduit','width=750,height=400,resizable=yes,scrollbars=yes')">Choisir</a>
                                </td>
                        </tr>
                                        <%}
                %>
                </table>
                <textarea id="xml" name="xml" style="visibility:hidden;width:300px;height:1px"><lc><%=sXml%></lc></textarea>
                <textarea id="ct" name="ct" style="visibility:hidden;width:300px;height:1px"><c:out value="${param['ct']}" escapeXml="false"/></textarea>
                <% if("t".equals(laction)){%>
                <textarea id="zxml" name="zxml" style="visibility:hidden;width:300px;height:1px">
                <c:import url="/edx/rep2lst.xsl" var="xsl"/>
                <x:transform xslt="${xsl}"><lc><%=sXml%></lc></x:transform>
                </textarea>
                <script language="javascript">
                        lcC();
                </script>
                <%} else {
                                try {
                                String valeur = request.getParameter("ct").toString() ;
                                if (valeur==null || "".equals(valeur))
                                        valeur = "<lc/>" ;
                %>
        <script language="javascript">
        var ca_CT = new Array();
        function goct() {
                var _valeur = "" ;
                for(var x=0;x<document.formu.elements.length;x++){
                        var y = document.formu.elements[x];
                        if (y.checked && ca_CT[y.value])
                                _valeur = _valeur+" "+ca_CT[y.value] ;
                }
                if (_valeur.indexOf(" ")==0)
                        _valeur=_valeur.substring(1);
                if (_valeur) {
                        _focus.focus();
                        sel = document.selection.createRange();
                        sel.text = _valeur;
                }

        }
        </script>
<c:set var="xsl">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:template match="/lc">
        <xsl:if test="count(r) &gt; 0">
        <div style="border:1px solid black;height:180px;overflow:auto;background-color:#F5F5F5'" class="button2">
        <table cellpadding="2" cellspacing="2" border="0" width="320" >
        <th class="text"><a href="#ct" onclick="goct()"><img alt='Choisir' src='/backoffice/images/pucef.gif' border='0' align="absmiddle"/></a></th><th class="text">Caractéristiques techniques du produit</th>
        <tr>
                <td height="1" bgcolor="#949494"></td>
                <td width="100%" height="1" bgcolor="#949494"></td>
        </tr>
        <xsl:for-each select="r">
                <xsl:sort select="format-number(@ordre,'0000')"/>
                <tr class="text">
                        <td ><script language="javascript"><xsl:text>ca_CT["</xsl:text><xsl:value-of select="@ordre"/><xsl:text>"]="</xsl:text><xsl:value-of select="l/."/> : <xsl:value-of select="d/."/><xsl:text>";</xsl:text></script>
                        <input class="button2" style="border:0px" type="checkbox" name="ct_{@ordre}" value="{@ordre}"/>
                        </td>
                        <td class="text">&#160;&#160;<xsl:value-of select="l/."/> : <xsl:value-of select="d/."/></td>
                </tr>
        </xsl:for-each>
        </table>
        </div>
        </xsl:if>
        </xsl:template>
</xsl:stylesheet>
</c:set>
                        <x:transform xslt="${xsl}">
                        <%=valeur%>
                        </x:transform>

                <%      }catch(Exception e) {}
                 } %>
                <P/>
                <table style='border:1px solid black;background-color:#F5F5F5'>
                        <tr><td width='160' align='right' class='text'><b>Terminer </b></td>
                        <td valign='middle' align='center'><input type='image' style='border:0px;background-color:#F5F5F5' alt='Terminer' src='/backoffice/images/valide.gif'
                        border='0'
                        onclick="laction.value='t';">
                                </td>
                        </tr>
                </table>
                <script language="javascript">
                var _focus=document.formu.d_r_0;
                </script>
                </form>
        <% } %>
</body>
</html>



