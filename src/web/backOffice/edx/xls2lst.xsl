<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:template match="/lc">
                <table cellpadding="1" cellspacing="1" border="0" width="100%" style='border:1px solid black;background-color:#648BCB'>
                        <xsl:for-each select="r">
                                <xsl:sort select="format-number(@ordre,'0000')"/>
                                <tr class="text"  valign="top">
                                        <xsl:for-each select="*">
                                                <td style='border:1px solid #f1f1f1;background-color:#d6e4f3'><xsl:value-of select="."/>&#160;</td>
                                        </xsl:for-each>
                                </tr>
                        </xsl:for-each>
                </table>
        </xsl:template>
</xsl:stylesheet>
