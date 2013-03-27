<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:my-ext="ext1" extension-element-prefixes="my-ext" version="1.0">
	<xsl:output method="html" indent="yes"/>
        <xsl:template match="/ionPublication"><xsl:choose><xsl:when test="@type='ba_accessoire'">
                <b>Marque</b> : <xsl:for-each select="data/publication/liste_marque/lc/r"><xsl:value-of select="d/."/><xsl:text> </xsl:text></xsl:for-each><br/>
                <b>Désignation</b> : <xsl:value-of select="data/publication/designation/."/></xsl:when></xsl:choose>
        </xsl:template>
</xsl:stylesheet>

