<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ion="http://ion-cms.sourceforge.net/ion-ns">
	<xsl:param name="locale"></xsl:param>
	<xsl:output method="html"/>
	<xsl:template match="/nextimeSupaXml/data/publication">
		<table>
                        <xsl:apply-templates select="/nextimeSupaXml/data/publication/node()[@xml:lang=$locale] | node()[not(@xml:lang)]"/>
		</table>
                <script language="javascript"><xsl:text>
                        function lc()  {
                                </xsl:text><xsl:call-template name="controler" /><xsl:text>
                                return true ;
                        }
                        function ouvrir(url)  {
                                window.open(url,'edx','width=640,height=480,resizable=yes,scrollbars=yes');
                        }
                        var ca_MD = new Array();
                </xsl:text>
                <xsl:for-each select="//metaDatas/*">
                        <xsl:text>ca_MD["</xsl:text><xsl:value-of select="@name"/><xsl:text>"]="</xsl:text><xsl:value-of select="."/><xsl:text>";
                        </xsl:text>
                </xsl:for-each>
                </script>
        </xsl:template>
        <xsl:template name="controler" >
                <xsl:for-each select="/nextimeSupaXml/data/publication/*[@xml:lang=$locale] | node()[not(@xml:lang)]">
                        <xsl:if test="@ion:control">
                                <xsl:text>if (!control(document.forms[0].FIELD_</xsl:text><xsl:value-of select="name()"/><xsl:text>,"</xsl:text><xsl:value-of select="@ion:control"/><xsl:text>")) return false;</xsl:text>
                        </xsl:if>
                </xsl:for-each>
        </xsl:template>
        <xsl:template name="copier" >
                <xsl:for-each select="lc">
                        <xsl:copy>
                                <xsl:for-each select="r">
                                        <xsl:copy>
                                                <xsl:attribute name="ordre"><xsl:value-of select="@ordre"/></xsl:attribute>
                                                <xsl:for-each select="*">
                                                        <xsl:copy>
                                                                <xsl:value-of select="."/>
                                                        </xsl:copy>
                                                </xsl:for-each>
                                        </xsl:copy>
                                </xsl:for-each>
                                </xsl:copy>
                </xsl:for-each>
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
        <!-- ============= texte =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='text']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
                                <xsl:variable name="lcValeur"><xsl:choose><xsl:when test=".='' and @ion:option"><xsl:value-of select="@ion:option"/></xsl:when><xsl:otherwise><xsl:value-of select="."/></xsl:otherwise></xsl:choose></xsl:variable>
                                <input type="text" value="{$lcValeur}" name="FIELD_{name()}" style="width=300" maxlength="255" />
			</td>

		</tr>
	</xsl:template>
	<!-- ============= texte long =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='large-text']">
		<tr>
                        <td align="right" class="text" valign="top" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
                                <textarea name="FIELD_{name()}" style="width=300;height=50">
					<xsl:value-of select="."/>
				</textarea>
			</td>

		</tr>
	</xsl:template>
        <!-- ============= date =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='date']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
                                <input type="text" value="{.}" name="FIELD_{name()}" style="width=100" />
                                &#160;<xsl:text disable-output-escaping="yes"><![CDATA[<a href="#calendar" style="text-decoration:none"><img src="images/calendar.gif" onclick="pop('FIELD_]]></xsl:text><xsl:value-of select="name()"/><xsl:text disable-output-escaping="yes"><![CDATA[',']]></xsl:text><xsl:value-of select="$locale"/><xsl:text disable-output-escaping="yes"><![CDATA[');this.style.cursor='hand'" align="absmiddle" border="0"/></a>]]></xsl:text>
			</td>
		</tr>
	</xsl:template>
        <!-- ============= import =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='import']">
                <xsl:variable name="extid" select="name()"/>
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td class="text">
                                <xsl:value-of select="//metaDatas/metaData[@name=$extid]/." disable-output-escaping="yes"/>
			</td>
		</tr>
	</xsl:template>
        <!-- =============callout ===============
                <varCallout
                        ion:type="callout"
                        ion:label="Exemple appel Callout"
                        ion:callout="listSection"
                />
        -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='callout']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
                                <textarea name="FIELD_{name()}" readonly="1" style="visibility:hidden;width=1;height=1">
                                <xsl:call-template name="copier" />
                                </textarea>
                                <div id="lc_{name()}" class="button3" style="border:1px solid black;width:300px;height:120px;overflow:auto">
                                <xsl:call-template name="presenter" />
                                </div>
                                &#160;<xsl:element name="a">
                                <xsl:attribute name="href"><![CDATA[javascript:callout_]]><xsl:value-of select="name()"/><![CDATA[()]]></xsl:attribute>
                                Modifier
                                </xsl:element>
                        </td>
		</tr>
		<script language="javascript"><![CDATA[
                        function callout_]]><xsl:value-of select="name()"/><![CDATA[() {
                                document.callout.valeur.value=document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value;
                                document.callout.nom.value=']]><xsl:value-of select="@ion:callout"/><![CDATA[';
                                document.callout.zaction.value='select_]]><xsl:value-of select="name()"/><![CDATA[';
                                document.callout.action='/backoffice/callout/]]><xsl:value-of select="@ion:callout"/><![CDATA[';
                                document.callout.submit();
			}
                        function select_]]><xsl:value-of select="name()"/><![CDATA[(value,rep) {
                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=value;
                                document.all['lc_]]><xsl:value-of select="name()"/><![CDATA['].innerHTML=rep;
			}
                        function get_]]><xsl:value-of select="name()"/><![CDATA[() {
                                return document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value;
			}
		]]></script>
	</xsl:template>
        <!-- =============link =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='link']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap" class="text">
                                <input type="text" name="FIELD_{name()}" value="{.}" class="text-field" style="width:270px"/>&#160;Choisir
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/listSection.x?action=select_{name()}')">section</a>&#160;ou
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/listPublication.x?action=select_{name()}')">publication</a>&#160;ou
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browserLien.jsp?action=select_{name()}')">lien</a>&#160;ou
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=_download&amp;action=select_{name()}')">ressource</a>
			</td>
		</tr>
		<script language="javascript"><![CDATA[
                        function select_]]><xsl:value-of select="name()"/><![CDATA[(value) {
                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=value;
			}
		]]></script>
	</xsl:template>
        <!-- =============image =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='image']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
                        </td>
                        <td nowrap="nowrap">
                                <input type="text" name="FIELD_{name()}" onkeypress="return false" value="{.}" class="text-field" style="width:270px"/>
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=images&amp;action=select_{name()}')"  >Choisir</a>
			</td>
		</tr>
		<script language="javascript"><![CDATA[
			function select_]]><xsl:value-of select="name()"/><![CDATA[(value) {
                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=value;
			}
		]]></script>
	</xsl:template>
        <!-- =============flash =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='flash']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
                                <input type="text" name="FIELD_{name()}" onkeypress="return false" value="{.}" class="text-field" style="width:270px"/>
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=flashs&amp;action=select_{name()}')">Choisir</a>
			</td>
		</tr>
		<script language="javascript"><![CDATA[
			function select_]]><xsl:value-of select="name()"/><![CDATA[(value) {
                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=value;
			}
		]]></script>
	</xsl:template>
        <!-- =============file =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='file']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap" class="text">
                                <input type="text" name="FIELD_{name()}" onkeypress="return false" value="{.}" class="text-field" style="width:270px"/>&#160;Choisir
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=documents&amp;action=select_{name()}')"  >documents</a>&#160;ou
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=pdfs&amp;action=select_{name()}')"  >PDFs</a>&#160;ou
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=audios&amp;action=select_{name()}')"  >audios</a>&#160;ou
                                &#160;<a href="#null" onclick="javascript:ouvrir('/backoffice/callout/browser.jsp?btype=videos&amp;action=select_{name()}')"  >videos</a>
			</td>
		</tr>
		<script language="javascript"><![CDATA[
			function select_]]><xsl:value-of select="name()"/><![CDATA[(value) {
                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=value;
			}
		]]></script>
	</xsl:template>
        <!-- =============choice =============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='choice']">
		<tr>
                        <td align="right" class="text" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap" class="text">
                                <select name="FIELD_{name()}" style="width:300px">
                                </select>
			</td>
		</tr>
		<script language="javascript"><![CDATA[
                        var va_Option = new String("]]><xsl:value-of select="@ion:option"/><![CDATA[").split(";");
                        for (vu_I = 0 ; vu_I < va_Option.length ;vu_I++) {
                                var lcOption = va_Option[vu_I].split(",");
                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.options[vu_I]
                                        = new Option (lcOption[1],lcOption[0]);
                                if (lcOption[0] == "]]><xsl:value-of select="."/><![CDATA[")
                                        document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.options[vu_I].selected=true ;
                        }
		]]></script>
	</xsl:template>
	<!-- ============= texte html=============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='formated-text']">
		<tr>
                        <td align="right" class="text" valign="top" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
				<textarea name="FIELD_{name()}" style="visibility : hidden; position:absolute; width:1; height:1">
					<xsl:value-of select="."/>
				</textarea>
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
                                                <td rowspan="6">
                                                        <iframe  style="border:1px solid black;" frameborder="0" id="iframe_{name()}" marginheight="1" marginwidth="1" width="530" height="120" onblur="document.forms[0].FIELD_{name()}.value=iframe_{name()}.document.all.tags('body')[0].innerHTML"/>
						</td>
						<td>
                                                        <a href="#fastedit" title="Mettre en gras" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('bold');">
								<img src="images/bold.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Mettre en italique" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('italic');">
								<img src="images/italic.gif" border="0"/>
							</a>
						</td>
					</tr>
                                        <tr>
						<td>
                                                        <a href="#fastedit" title="Liste a puces" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('InsertUnorderedList');">
								<img src="images/puces.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Indentation" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('Indent');">
								<img src="images/indent.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Supprimer l'indentation" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('Outdent');">
								<img src="images/outdent.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
                                                <td align="center">
                                                        <a href="#fastedit" title="Vider le champ" onClick="iframe_{name()}.focus();refresh_{name()}();">
                                                                <img src="images/poubelle.gif" border="0"/>
                                                        </a>
						</td>
					</tr>
				</table>
				<script language="javascript"><![CDATA[
					iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.designMode='on';
                                        function refresh_]]><xsl:value-of select="name()"/><![CDATA[(t) {
                                                if (!t)
                                                        t =  "" ;
                                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=t;
                                                iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.write("<html><body>"+t+"</body></html>");
                                                iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.location.reload();
                                        }
                                        refresh_]]><xsl:value-of select="name()"/><![CDATA[(document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value) ;
				]]></script>
			</td>
		</tr>
	</xsl:template>
        <!-- ============= rich html=============== -->
        <xsl:template match="/nextimeSupaXml/data/publication/node()[@ion:type='rich-text']">
		<tr>
                        <td align="right" class="text" valign="top" nowrap="nowrap">
				<b>
                                        <xsl:value-of select="@ion:label"/> :
				</b>
			</td>
                        <td nowrap="nowrap">
				<textarea name="FIELD_{name()}" style="visibility : hidden; position:absolute; width:1; height:1">
					<xsl:value-of select="."/>
				</textarea>
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
                                                <td rowspan="10">
                                                        <iframe  style="border:1px solid black;" frameborder="0" id="iframe_{name()}" marginheight="1" marginwidth="1" width="530" height="200" onblur="document.forms[0].FIELD_{name()}.value=iframe_{name()}.document.all.tags('body')[0].innerHTML"/>
						</td>
						<td>
                                                        <a href="#fastedit" title="Mettre en gras" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('bold');">
								<img src="images/bold.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Mettre en italique" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('italic');">
								<img src="images/italic.gif" border="0"/>
							</a>
						</td>
					</tr>
<!--
					<tr>
						<td>
                                                        <a href="#fastedit" title="Souligner" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('underline');">
								<img src="images/underline.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Titre" onClick="iframe_{name()}.focus();iframe_{name()}.document.selection.createRange().pasteHTML('&lt;h5&gt;'+iframe_{name()}.document.selection.createRange().text+'&lt;/h5&gt;');">
								<img src="images/title.gif" border="0"/>
							</a>
						</td>
					</tr>
-->
                                        <tr>
						<td>
                                                        <a href="#fastedit" title="Liste a puces" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('InsertUnorderedList');">
								<img src="images/puces.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Indentation" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('Indent');">
								<img src="images/indent.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#fastedit" title="Supprimer l'indentation" onClick="iframe_{name()}.focus();iframe_{name()}.document.execCommand('Outdent');">
								<img src="images/outdent.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
                                                <td align="center">
                                                        <a href="#null" onclick="iframe_{name()}.focus();ouvrir('/backoffice/listSection.x?action=insertLien_{name()}')"  title="Faire un lien vers une section" >
                                                                <img src="images/section.gif" border="0" />
							</a>
						</td>
					</tr>
					<tr>
                                                <td align="center">
                                                        <a href="#null" onclick="iframe_{name()}.focus();ouvrir('/backoffice/listPublication.x?action=insertLien_{name()}')"  title="Faire un lien vers une publication" >
                                                                <img src="images/publication.gif" border="0" />
							</a>
						</td>
					</tr>
					<tr>
                                                <td align="center">
                                                        <a href="#null" onclick="iframe_{name()}.focus();ouvrir('/backoffice/callout/browserLien.jsp?action=insertLien_{name()}')"  title="Faire un lien" >
								<img src="images/link.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
                                                        <a href="#null" onclick="iframe_{name()}.focus();ouvrir('/backoffice/callout/browser.jsp?btype=images&amp;action=insertImage_{name()}')" title="Inserer une image" >
								<img src="images/img.gif" border="0"/>
							</a>
						</td>
					</tr>
					<tr>
                                                <td align="center">
                                                        <a href="#fastedit" title="Vider le champ" onClick="iframe_{name()}.focus();refresh_{name()}();">
                                                                <img src="images/poubelle.gif" border="0"/>
							</a>
						</td>
					</tr>
				</table>
				<script language="javascript"><![CDATA[
					// insert an image in the mini-word
					function insertImage_]]><xsl:value-of select="name()"/><![CDATA[(value) {
                                                if (value) {
                                                        iframe_]]><xsl:value-of select="name()"/><![CDATA[.focus();
                                                        iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.execCommand('InsertImage',false,value);
                                                }
					}
                                        function insertLien_]]><xsl:value-of select="name()"/><![CDATA[(value) {
                                                if (value) {
                                                        iframe_]]><xsl:value-of select="name()"/><![CDATA[.focus();
                                                        iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.execCommand('createLink',false,value);
                                                }
					}
					iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.designMode='on';
                                        function refresh_]]><xsl:value-of select="name()"/><![CDATA[(t) {
                                                if (!t)
                                                        t =  "" ;
                                                document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value=t;
                                                iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.write("<html><body>"+t+"</body></html>");
                                                iframe_]]><xsl:value-of select="name()"/><![CDATA[.document.location.reload();
                                        }
                                        refresh_]]><xsl:value-of select="name()"/><![CDATA[(document.forms[0].FIELD_]]><xsl:value-of select="name()"/><![CDATA[.value) ;
				]]></script>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="text()">
	</xsl:template>
</xsl:stylesheet>
