<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:template match="/lc">
                <table border="0" cellpadding="3" cellspacing="3" style="border:1px solid #000000;">
                <xsl:for-each select="r">
                        <xsl:sort select="l/."/>
                        <tr class="text" style="border:1px solid #000000;background-color:#d6e4f3">
                        <td width="200">
                                <xsl:value-of select="l/."/>
                        </td>
                        <td>
                        <xsl:element name="a">
                                <xsl:attribute name="href">
                                		<!--  desactive le javascript  -->
                                		<!--
                                        <xsl:text>javascript:lcC("javascript:lien('</xsl:text><xsl:value-of select="u/."/><xsl:text>','</xsl:text><xsl:value-of select="l/."/><xsl:text>','</xsl:text><xsl:value-of select="t/."/><xsl:text>','</xsl:text><xsl:value-of select="m/."/><xsl:text>','</xsl:text><xsl:value-of select="d/."/><xsl:text>','</xsl:text><xsl:value-of select="h/."/><xsl:text>','</xsl:text><xsl:value-of select="w/."/><xsl:text>')")</xsl:text>
                                         -->
										<xsl:text>javascript:lcC("</xsl:text><xsl:value-of select="u/."/><xsl:text>")</xsl:text>
                                </xsl:attribute>
                                        <xsl:value-of select="u/."/>
                        </xsl:element>
                        </td>
                        </tr>
                </xsl:for-each>
                </table>
        </xsl:template>
</xsl:stylesheet>
