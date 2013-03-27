<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ion="http://ion-cms.sourceforge.net/ion-ns">
        <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	<xsl:param name="locale">fr</xsl:param>
        <xsl:template match="nextimeSupaXml">
                <publication xmlns:ion="http://ion-cms.sourceforge.net/ion-ns" ion:type="{publication/@ion:type}" xmlns:xml="http://www">
			<xsl:apply-templates select="publication/node()"/>
		</publication>
	</xsl:template>
	<xsl:template match="publication/node()">
                <xsl:if test="not(name()='' or name()='metaDatas')">
			<xsl:element name="{name()}">
				<xsl:if test="@xml:lang">
                                        <xsl:attribute name="ion:lang"><xsl:value-of select="@xml:lang"/></xsl:attribute>
				</xsl:if>
                                <xsl:if test="@ion:callout">
                                        <xsl:attribute name="ion:callout"><xsl:value-of select="@ion:callout"/></xsl:attribute>
				</xsl:if>
                                <xsl:if test="@ion:control">
                                        <xsl:attribute name="ion:control"><xsl:value-of select="@ion:control"/></xsl:attribute>
				</xsl:if>
                                <xsl:if test="@ion:option">
                                        <xsl:attribute name="ion:option"><xsl:value-of select="@ion:option"/></xsl:attribute>
				</xsl:if>
                                <xsl:attribute name="ion:type"><xsl:value-of select="@ion:type"/></xsl:attribute>
                                <xsl:attribute name="ion:label"><xsl:value-of select="@ion:label"/></xsl:attribute>
				<xsl:variable name="theName">
					<xsl:value-of select="name()"/>
				</xsl:variable>
                                <xsl:if test="not(@ion:type='callout')">
                                <xsl:value-of select="string('&lt;![CDATA[')" disable-output-escaping="yes"/>
                                </xsl:if>
                                <xsl:choose>
                                        <xsl:when test="/nextimeSupaXml/data/node()[name()=$theName] and ( @xml:lang=$locale or not(@xml:lang) )">
                                                <xsl:value-of select="/nextimeSupaXml/data/node()[name()=$theName]" disable-output-escaping="yes"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                                <xsl:value-of select="." disable-output-escaping="yes"/>
                                        </xsl:otherwise>
                                </xsl:choose>
                                <xsl:if test="not(@ion:type='callout')">
                                <xsl:value-of select="string(']]&gt;')" disable-output-escaping="yes"/>
                                </xsl:if>
			</xsl:element>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
