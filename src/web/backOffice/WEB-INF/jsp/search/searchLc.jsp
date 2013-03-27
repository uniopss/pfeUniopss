<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<ion:begin/>
<html>
<head>
	<title>search</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='setBallon("BallonTip");' background="images/fond.gif">
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
	<img src="images/search-tile.gif" align="absmiddle">
	<span class="title">
                rechercher par critère
	</span>
        <br><script>function go(){return false ;}</script>



<c:set var="xsl">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:param name="id"></xsl:param>
        <xsl:param name="valeur"></xsl:param>
        <xsl:template match="/lc">
        <input type="hidden" id="view" name="view" value="{//lc/@view}"/>
        <xsl:choose><xsl:when test="//lc/@multi='y'">
        <table cellpadding="0" cellspacing="0" width="400" style='border:1px solid black;background-color:#F5F5F5'>
        <xsl:for-each select="r">
                <tr valign="center">
                        <td align="left" class="text" width="100%">
                &#160;&#160;<xsl:value-of select="."/>
                </td>
                        <td align="left" class="text" nowrap="1">
                = <xsl:element name="input">
                        <xsl:attribute name="name">lc_<xsl:value-of select="@id"/></xsl:attribute>
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="value"></xsl:attribute>
                </xsl:element>&#160;&#160;
                </td>
                </tr>
        </xsl:for-each></table><br/>
        <script><xsl:text><![CDATA[
        function go()
        {
        document.lc.keyWords.value=(document.lc.akeyWords.value!=""?("+co:"+document.lc.akeyWords.value):"") ]]></xsl:text>
        <xsl:for-each select="r"><xsl:text><![CDATA[
        +(document.lc.lc_]]></xsl:text><xsl:value-of select="@id"/><xsl:text><![CDATA[.value!=""?(" +]]></xsl:text><xsl:value-of select="@id"/><xsl:text><![CDATA[:"+document.lc.lc_]]></xsl:text><xsl:value-of select="@id"/><xsl:text><![CDATA[.value):"")
        ]]></xsl:text></xsl:for-each>
        <xsl:text><![CDATA[;
        if (!document.lc.keyWords.value) {alert('Veuillez saisir un mot clé avant de confirmer.');return false;}
        return true ;
        }
        ]]></xsl:text></script>
        </xsl:when>
        <xsl:otherwise>
        <b>
                Critère :&#160;
        </b>
        &#160;
        <select name="type" style="width=150">
        <xsl:for-each select="r">
                <xsl:element name="option">
                        <xsl:attribute name="value"><xsl:value-of select="@id"/></xsl:attribute>
                        <xsl:if test="@id = $id"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>
                        <xsl:value-of select="."/>
                </xsl:element>
        </xsl:for-each>
        </select>
        =&#160;&#160;
        <input type="text" value="{$valeur}" name="bkeyWords" style="width=150" align="absmiddle"/>
        <script><xsl:text><![CDATA[
        function go()
        {
        ]]></xsl:text>
        <xsl:text><![CDATA[
        document.lc.keyWords.value=(document.lc.akeyWords.value?'+co:'+document.lc.akeyWords.value:'')+(document.lc.type.value && document.lc.bkeyWords.value?' +'+document.lc.type.value+':'+document.lc.bkeyWords.value:'');
        if (!document.lc.keyWords.value) {alert('Veuillez saisir un mot clé avant de confirmer.');return false;
        return true ;}
        }
        ]]></xsl:text></script>
        </xsl:otherwise>
        </xsl:choose>
        </xsl:template>
</xsl:stylesheet>
</c:set>
        <form name="lc" action="searchLc.x" method="POST" onSubmit="return go()">
        <input type="hidden" name="popup" value="<c:out value="${param['popup']}"/>">
        <input type="hidden" value="<c:out value="${param['keyWords']}"/>" name="keyWords" style="width=200" align="absmiddle">
                <table cellpadding="3" cellspacing="3" width="600">
                        <tr valign="center">
                                <td align="left" class="text" colspan=2>
					<b>Mots clés :&nbsp;</b>
                                        &nbsp;<input type="text" value="<c:out value="${param['akeyWords']}"/>" name="akeyWords" style="width=200" align="absmiddle">&nbsp;
                                        &nbsp;<c:choose><c:when test="${mL=='1'}"><select name="index" style="width=60" align="absmiddle"><c:forEach items="${indexs}" var="index"><option value="<c:out value="${index}"/>" <c:if test="${index==param['index']}">selected</c:if>><c:out value="${index}"/></option></c:forEach></select></c:when><c:otherwise><input type="hidden" name="index" value="<c:out value="${currentLocale}"/>"></c:otherwise></c:choose>
                                        &nbsp;dans&nbsp;<select name="selectedSite" style="width=150" valign="absmiddle"><c:forEach items="${currentSite}" var="index"><option value="<c:out value="${index.id}"/>" <c:if test="${index.id==param['selectedSite']}">selected</c:if>><c:out value="${index.metaData['name']}"/></option></c:forEach></select>
				</td>
			</tr>
                        <tr valign="top" align="left ">
                                <td align="left" class="text" >
                                        <x:transform xslt="${xsl}">
                                        <c:if test="${param['type']!=null && param['type']!=''}">
                                        <x:param name="id" value="${param['type']}"/>
                                        </c:if>
                                        <c:if test="${param['bkeyWords']!=null && param['bkeyWords']!=''}">
                                        <x:param name="valeur" value="${param['bkeyWords']}"/>
                                        </c:if>
                                        <c:out value="${sC}" default="<lc/>" escapeXml="false"/>
                                        </x:transform>
				</td>
                                <td align="left" class="text" valign="bottom">
                                        &nbsp;
                                        <input type="submit" value="<bean:message key="search.search.searchbutton"/>" class="button" align="absmiddle"/>
				</td>
			</tr>
		</table>
        </form><script>
        <%
                java.util.Enumeration e = request.getParameterNames();
                while(e.hasMoreElements()){
                        String tmp = (String)e.nextElement();
                        if( tmp.startsWith("lc_") && request.getParameter(tmp)!=null && !"".equals(request.getParameter(tmp)))
                                out.println("document.lc."+tmp+".value=\""+request.getParameter(tmp)+"\";");
                }
        %></script>
	<!-- publications list -->
	<span class="title-list"><bean:message key="search.result"/></span>
	<hr width="100%" size="1" noshade color="#949494">
	<c:choose>
		<!-- il y a des publications à afficher, articles to show -->
		<c:when test="${result!=null}">
                        <c:if test="${param['view'] !=null && param['view'] != ''}">
                                <c:import url="/xml/${param['view']}.xsl" var="xslResult" />
                        </c:if>
                        <table border="0" cellpadding="3" cellspacing="2" width="100%" style="border:1px solid black;">
                                <tr class=text align="left"><th></th><th>Nom</th><th>Version</th><th>Type</th><th>Section</th><th></th><th></th></tr>
                                <% java.util.Vector v = new java.util.Vector(); %>
				<c:forEach var="r" items="${result}" varStatus="status">
                                        <tr align="center">
                                                <!-- nom (lien) -->
                                                <td class="text">
                                                        <a  href="#infos"
                                                                <c:if test="${r.type != 'objet'}">onclick="window.open('viewPublication.x?id=<c:out value="${r.id}"/>&version=<c:out value="${r.version}"/>&sid=<c:out value="${r.section}"/>','contenu','width=600,height=400,resizable=yes,scrollbars=yes')"</c:if>
                                                                ><img src="images/infos.gif" border="0"></a>
                                                </td>
                                                <td class="text" align="left">
                                                        <c:choose>
                                                        <c:when test="${param['popup'] !='1'}">
                                                                <a onclick="parent.frames['banner'].document.location='menu.x?tab=1'" href="highlight.x?id=<c:out value="${r.id}"/>" style="text-decoration:none"><strong style="color:#5e5e5e;">
                                                                        <c:out value="${r.name}"/>
                                                                </strong></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                                <a href="#" onclick="top.opener.searchLc('<c:out value="${r.name}"/>')"><c:out value="${r.name}"/></a>
                                                        </c:otherwise>
                                                        </c:choose>
                                                </td>
                                                <td class="text" align="left"><c:out value="${r.version}"/></td>
                                                <td class="text" align="left"><c:out value="${r.type}"/></td>
                                                <td class="text" align="left">
                                                        <c:out value="${r.sections}"/>
                                                </td>
                                                <!-- panier -->
                                                <c:choose>
                                                <c:when test="${param['popup'] !='1'"> <%-- && r.type == panier.type} --%>
                                                        <c:set var="zPanier" value="${r.versionId}"/>
                                                        <% pageContext.setAttribute("zPanier",((org.nextime.ion.backoffice.panier.Panier)session.getAttribute("panier")).isPresent(""+pageContext.getAttribute("zPanier"))?"1":"0"); %>
                                                        <form target="_panier" method="post" action="editPanier.x" name="panier_<c:out value="${r.versionId}"/>">
                                                        <td align="center" width="20" valign="top"><input type=hidden name=id value="<c:out value="${r.versionId}"/>">
                                                        <input type='checkbox' name='etat' value='<c:if test="${zPanier=='1'}">1</c:if>' <c:if test="${zPanier=='1'}">checked</c:if> onClick="this.checked=(this.checked?false:true);document.panier_<c:out value="${r.versionId}"/>.submit()">&nbsp;
                                                        </td>
                                                        </form>
                                                        <c:remove var="zPanier"/>
                                                </c:when>
                                                <c:otherwise>
                                                                <td width="20">&nbsp;</td>
                                                </c:otherwise>
                                                </c:choose>
                                                <td class="text" align="left">
                                                        <c:if test="${param['view'] !=null && param['view'] != '' && status.index < 10}">
                                                                <x:transform xslt="${xslResult}"><c:out value="${r.content}" default="<ionPublication/>" escapeXml="false"/></x:transform>
                                                        </c:if>
                                                </td>
                                        </tr>
				</c:forEach>
			</table>
		</c:when>
		<!-- il n'y a pas de publications à afficher, no articles to show -->
		<c:otherwise>
			<i>(<bean:message key="search.noResult"/>)</i>
		</c:otherwise>
	</c:choose>

</body>

</html>
<ion:close/>
