<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Gestion du thésaurus</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text" onload="top.resizeTo(800,500)">
<%
	String code=request.getParameter("code");
	if (code==null || code.length() ==0 ) {
		code="motcles";
	}
	String xpath= "/entry[@code='"+code+"']";
%>


<script language="javascript">
function naviguer(p_Code) {
	var frm = document.thesaurusFrm;
	frm.code.value=p_Code;
	frm.submit();
	return false;
}
function choisirMotCle(p_Code) {
	var frm = document.thesaurusFrm;
	frm.code.value=p_Code;
	frm.actioncode.value='c';
	frm.submit();
	return false;
}
function callbackList() {
	top.opener.callbackFromThesaurus(_listeretourne);
	self.close();
}
</script>

<c:set var="xslRenduArbre">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
		<xsl:output method="html"/>
        <xsl:template match="/"> <!-- standard match to include all child elements -->
        <!-- this is the child table -->
        <table class="datatable" border="0">
        <colgroup class="center" />
        <colgroup width="400" />
	      <thead>
	        <tr>
	          <th width="200">&#160;</th>
	          <th width="200">&#160;</th>
	        </tr>
        </thead>
	    <tbody>
	        <xsl:apply-templates select="/<%=xpath%>">
	        </xsl:apply-templates>		
	    </tbody>
      </table>
</xsl:template>
<xsl:template match="entry">
	<tr>
	    <td><span class="text">Position Courante:</span></td>
	    <td>
	    <xsl:for-each select="ancestor-or-self::*">
		<xsl:call-template name="tree_node">
			<xsl:with-param name="code" select="@code"/>
			<xsl:with-param name="desc" select="text()"/>
		</xsl:call-template>
		</xsl:for-each>
		</td>
	</tr> 
	<xsl:for-each select="child::entry" >     
	  <tr>
		<td>&#160;</td>
	    <td>
	    <img src="/backoffice/images/linemiddlenode.gif" border="0" align="absmiddle"/>
		<xsl:call-template name="tree_node">
			<xsl:with-param name="code" select="@code"/>
			<xsl:with-param name="desc" select="text()"/>
		</xsl:call-template>
		</td>
	</tr> 
     </xsl:for-each> 
</xsl:template>
<xsl:template name="tree_node">
  <xsl:param name="code"/>
  <xsl:param name="desc"/>
	<a onclick="return naviguer('{$code}');"><xsl:value-of select="$desc" /></a>&#160;
	<a href="javascript:;" onclick="return choisirMotCle('{$code}');" ><img style='border:0px;background-color:#F5F5F5' alt='Choisir ce mot clé' src='/backoffice/images/pucef.gif' border='0'/></a>
	&#160;
</xsl:template> <!-- tree_node -->
</xsl:stylesheet>
</c:set>

<script>
	var _listeretourne = new Array();
</script>

<c:set var="xslRecupererEtSubmit">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
		<xsl:output method="text"/>
		<xsl:strip-space elements="*" />
		

        <xsl:template match="/">
	        <xsl:apply-templates select="/<%=xpath%>" />
			<![CDATA[<script>callbackList();</script>]]>
		</xsl:template>
		<xsl:template match="entry">
		    <xsl:for-each select="ancestor-or-self::*">
		    		<xsl:if test="position() > 1"> <!-- ignore le premier element -->
						<![CDATA[<script> _listeretourne.push(]]>"<xsl:value-of  select="translate(normalize-space(text()),'&quot;','')" />"<![CDATA[);</script>]]>
					</xsl:if>
			</xsl:for-each>
		</xsl:template>

		
</xsl:stylesheet>
</c:set> 

		<%
		String thesaurusContent = org.nextime.ion.commons.Util.readXMLFile(org.nextime.ion.framework.config.Config.getInstance().getThesaurusDirectory() ,"structure.xml", "");
		%>
<form name="thesaurusFrm" action="/backoffice/callout/thesaurus.jsp">
		<input name="code" value="" type="hidden"/>
		<input name="actioncode" value="" type="hidden"/>
		<x:transform  xslt="${xslRenduArbre}"><%=thesaurusContent%></x:transform>
		<c:if test="${param['actioncode'] == 'c'}">	
			<x:transform  xslt="${xslRecupererEtSubmit}"><%=thesaurusContent%></x:transform>
		</c:if>	
</form>
</body>
</html>



