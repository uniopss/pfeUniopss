<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
        <xsl:output method="html"/>
        <xsl:template match="/lc">
                <xsl:for-each select="r">
                        <xsl:sort select="format-number(@ordre,'0000')"/>
                        <xsl:variable name="nom"><xsl:value-of select="name()"/><xsl:text>_</xsl:text><xsl:value-of select="@ordre"/></xsl:variable>
                        <xsl:element name="input">
                                <xsl:attribute name="style"><xsl:text>visibility : hidden;</xsl:text>
                                </xsl:attribute>
                                <xsl:attribute name="name">
                                        <xsl:value-of select="$nom"/>
                                </xsl:attribute>
                                <xsl:attribute name="value">
                                        <xsl:value-of select="@ordre"/>
                                </xsl:attribute>
                        </xsl:element>
                        <xsl:for-each select="*">
                                <xsl:element name="textarea">
                                        <xsl:attribute name="style"><xsl:text>visibility : hidden;</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="name">
                                                <xsl:value-of select="name()"/><xsl:text>_</xsl:text><xsl:value-of select="$nom"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="."/>
                                </xsl:element>
                        </xsl:for-each>
                </xsl:for-each>
        </xsl:template>
</xsl:stylesheet>
