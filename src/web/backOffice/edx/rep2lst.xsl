<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:template match="/lc">
                <table cellpadding="1" cellspacing="1">
                        <xsl:for-each select="r">
                                <xsl:sort select="format-number(@ordre,'0000')"/>
                                <tr class="text"  valign="top"><td><xsl:if test="c/. = '1'"><img border="0" src="images/choix.gif" align="absmiddle"/></xsl:if>&#160;</td><td ><xsl:if test="l/.!=''"><b><xsl:value-of select="l/."/></b> : </xsl:if></td><td><xsl:choose><xsl:when test="contains(d/.,'&#10;')"><pre class="text"><xsl:value-of select="d/."/></pre></xsl:when><xsl:otherwise><xsl:value-of select="d/."/></xsl:otherwise></xsl:choose></td></tr>
                        </xsl:for-each>
                </table>
        </xsl:template>
</xsl:stylesheet>

