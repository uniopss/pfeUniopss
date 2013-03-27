<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ion="http://ion-cms.sourceforge.net/ion-ns">
	<xsl:param name="locale"></xsl:param>
	<xsl:output method="html"/>
        <xsl:template match="/nextimeSupaXml">
                <table cellpadding="3" cellspacing="3">
                        <xsl:for-each select="data/publication/node()[@ion:type and not(name()='' or name()='metaDatas')]">
                                <xsl:if test="@xml:lang=$locale or not(@xml:lang)">
                                        <tr>
                                                <td align="right" class="text" nowrap="nowrap" valign="top">
                                                        <b>
                                                                <xsl:value-of select="@ion:label"/> :
                                                        </b>
                                                </td>
                                                <td  class="text" valign="top">
                                                        <xsl:choose>
                                                                <xsl:when test="@ion:type='import'">
                                                                        <xsl:variable name="extid" select="name()"/>
                                                                        <xsl:value-of select="//metaDatas/metaData[@name=$extid]/." disable-output-escaping="yes"/>
                                                                </xsl:when>
                                                                <xsl:when test="@ion:type='callout'">
                                                                        <xsl:call-template name="presenter" />
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                        <xsl:value-of select="." disable-output-escaping="yes"/>
                                                                </xsl:otherwise>
                                                        </xsl:choose>
                                                </td>
                                        </tr>
                                </xsl:if>
                        </xsl:for-each>
                </table>
        </xsl:template>
        <xsl:template name="presenter">
                <xsl:for-each select="lc">
                        <table cellpadding="1" cellspacing="1" >
                                <xsl:for-each select="r">
                                        <xsl:sort select="format-number(@ordre,'0000')"/>
                                        <tr class="text"  valign="top"><td><xsl:if test="c/. = '1'"><img border="0" src="images/choix.gif" align="absmiddle"/></xsl:if>&#160;</td><td><xsl:if test="l/.!=''"><b><xsl:value-of select="l/."/></b> : </xsl:if></td><td><xsl:choose><xsl:when test="contains(d/.,'&#10;')"><pre class="text"><xsl:value-of select="d/."/></pre></xsl:when><xsl:otherwise><xsl:value-of select="d/."/></xsl:otherwise></xsl:choose></td></tr>
                                </xsl:for-each>
                        </table>
                </xsl:for-each>
        </xsl:template>
	<xsl:template match="text()">
	</xsl:template>
</xsl:stylesheet>
