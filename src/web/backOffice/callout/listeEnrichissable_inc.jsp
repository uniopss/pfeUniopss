<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<%--
	Include de la liste enrcichissable
	ce include est utilisé pour les 2 types : commun pour toutes les région et pour chaque région 
--%>

<html>
<head>
    <title>Liste enrichissable</title>
    <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">

    <script language="javascript">
        var _oldValues = new Array();
    </script>
    <c:set var="transformationDataToArray">
    <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
            <xsl:output method="html"/>
            <xsl:template match="/lc">
            <script language="javascript"><![CDATA[
                    ]]><xsl:for-each select="r"><![CDATA[
                        _oldValues.push("]]><xsl:value-of select="d/."/><![CDATA[");]]>
                    </xsl:for-each>
            </script>
            </xsl:template>
    </xsl:stylesheet>
    </c:set>

    <c:set var="oldXML" value="${param['valeur']}"/>

    <c:if test="${!(empty oldXML )}">
        <x:transform xslt="${transformationDataToArray}" xml="${oldXML}"/>
    </c:if>

    <script language="javascript">
        <%-- Cette fonction commapre des données dans
       la liste originale et rend checked des checked box figure dans cette liste
        --%>
        function checkBoxIfNeed() {
            for (var x = 0; x < document.liste.elements.length; x++) {
                var y = document.liste.elements[x];
                var valeurTexte = y.value;
                if (contains(_oldValues, valeurTexte)) {
                    y.checked = "checked";
                }
            }
        }

        function lcC() {
            sXml = "<lc>";
            sRep = "";
            var i = 0 ;
            var newValues = document.liste.elements;
            for (var x = 0; x < newValues.length; x++) {
                var y = newValues[x];
                if (y.checked) {
                    sXml += "<r ordre='" + (i++) + "'><l></l><d>" + y.value + "</d></r>";
                    sRep += "&nbsp;&nbsp;&nbsp;" + y.value + "<br>";
                }
            }
            sXml += "</lc>";

            eval(top.opener.<c:out value="${param['zaction']}"/>(sXml, sRep));
            self.close();
        }

        function effacer() {
            var newValues = document.liste.elements;
            for (var x = 0; x < newValues.length; x++) {
                var y = newValues[x];
                y.checked = false;
            }
        }

        function contains(p_Array, p_Val) {
            for (var i = 0; i < p_Array.length; i++) {
                if (p_Val == p_Array[i]) {
                    return true;
                }
            }
            return false;
        }

    </script>

</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text"
      onload="top.resizeTo(400,800)">

<c:set var="xsl">
    <c:choose>
        <c:when test="${modeRadio}">
            <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                <xsl:output method="html"/>
                <xsl:template match="/lc">
                    <xsl:for-each select="r">
                        <xsl:sort select="@ordre" data-type="number"/>
                        <tr>
                            <td class="text">&#160;&#160;<input class="button2" style="border:0px" type="radio"
                                                                name="radioName" value="{l/.}"/>&#160;&#160;</td>
                            <td class="text">&#160;&#160;<xsl:value-of select="l/."/></td>
                            <td class="text">&#160;&#160;</td>
                        </tr>
                    </xsl:for-each>
                </xsl:template>
            </xsl:stylesheet>
        </c:when>
        <c:otherwise>
            <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                <xsl:output method="html"/>
                <xsl:template match="/lc">
                    <xsl:for-each select="r">
                        <xsl:sort select="@ordre" data-type="number"/>
                        <tr>
                            <td class="text">&#160;&#160;<input class="button2" style="border:0px" type="checkbox"
                                                                name="{i/.}" value="{l/.}"/>&#160;&#160;</td>
                            <td class="text">&#160;&#160;<xsl:value-of select="l/."/></td>
                            <td class="text">&#160;&#160;</td>
                        </tr>
                    </xsl:for-each>
                </xsl:template>
            </xsl:stylesheet>
        </c:otherwise>
    </c:choose>
</c:set>


<form name="liste" method="POST">
    <table border="0" cellpadding="2" cellspacing="3" width="100%" class="text" bgcolor="#F5F5F5"
           style="border:1px solid black;">
        <tr>
            <td height="1" width="20"></td>
            <td align="right" width="80%"><a href="javascript:effacer()">Effacer les choix</a></td>
            <td align="right" width="20%"><a href="javascript:lcC()">Terminer</a></td>
        </tr>
        <tr>
            <td height="1" bgcolor="#949494"></td>
            <td height="1" bgcolor="#949494"></td>
            <td height="1" bgcolor="#949494"></td>
        </tr>


        <x:transform xslt="${xsl}"><%=sC%>
        </x:transform>
        <script>
            checkBoxIfNeed();
        </script>

    </table>
</form>
</body>
</html>
