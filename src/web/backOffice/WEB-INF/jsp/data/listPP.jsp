<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Référentiel</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body class="text" onload='' background="images/fond.gif">
        <c:choose>
        <c:when test="${laction == 'c' && content != null && content != ''}">
                <form id="formu" name="formu" action="listPP.x" method="POST">
                <input type='hidden' id='fm' name='fm' value="<c:out value="${fm}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='laction' name='laction' value="c">
                <c:import url="/edx/rep2form.xsl" var="xsl"/>
                <x:transform xslt="${xsl}">
                <c:out value="${content}" escapeXml="false"/>
                </x:transform>
                </form>
                <script language="javascript">document.formu.submit();</script>
        </c:when>
        <c:otherwise>
        <c:if test="${param['zaction']!=null && param['zaction']!=''}">
        <script language="javascript">
        function lcC() {
                var _valeur = "" ;
                for(var x=0;x<document.formu.elements.length;x++){
                        var y = document.formu.elements[x];
                        if (y.checked && eval("document.formu."+y.value+".value"))
                                _valeur = _valeur+"\n"+eval("document.formu."+y.value+".value") ;
                }
                if (_valeur.indexOf("\n")==0)
                        _valeur=_valeur.substring(1);
                if (_valeur)
                        insert(top.opener._focus,_valeur);
        }
        function insert(f, v) {
                if (f) {
                        if (top.opener.document.selection) {
                                f.focus();
                                sel = top.opener.document.selection.createRange();
                                sel.text = v;
                        }
                        else {
                                f.value = f.value+v;
                        }
                }
                else alert("Veuillez choisir un champ cible");
        }
        </script>
        </c:if>
        <img src="images/data-tile.gif" align="absmiddle">
	<span class="title">
                Les Plus Produit
	</span>
                <form id="formu" name="formu" action="listPP.x" method="POST">
                <c:choose>
                        <c:when test="${param['zaction']!=null && param['zaction']!=''}">
                                <input type='hidden' id='fm' name='fm' value="<c:out value="${fm}"/>">
                        </c:when>
                        <c:otherwise>
                        <table class="text" width="100%" cellpadding="4" cellspacing="4" style="border:1px solid black;background-color:#F5F5F5">
                        <tr>
                        <td align=right valign="top">
                        Code Famille : <select name="fm" style="width=150" onChange="document.formu.submit()">
                                <c:forEach items="${famille}" var="_famille">
                                        <option value="<c:out value="${_famille}"/>" <c:if test="${_famille==fm}">selected</c:if>><c:out value="${_famille}"/></option>
                                </c:forEach>
                        </select>
                        </td>
                        <td align=right valign="top">
                        Ajouter un code famille : <input id="fm_add" name="fm_add">&nbsp;<input align="absmiddle" type='image' style='border:0px;background-color:#F5F5F5' alt='Ajouter' src='/backoffice/images/valide.gif' border='0' onclick="if (!document.formu.fm_add.value) {alert('Saisissez un code famille');return false;} ;return confirm('Voulez-vous continuer ?')"><br>
                        Renommer le code de la famille sélectionnée en : <input id="fm_ren" name="fm_ren">&nbsp;<input align="absmiddle" type='image' style='border:0px;background-color:#F5F5F5' alt='Renommer' src='/backoffice/images/valide.gif' border='0' onclick="if (!document.formu.fm_ren.value) {alert('Saisissez un code famille');return false;} ;return confirm('Voulez-vous continuer ?')"><br>
                        Supprimer la configuration de la famille sélectionnée <input type='hidden' id="fm_del" name="fm_del" value="">&nbsp;<input align="absmiddle" type='image' style='border:0px;background-color:#F5F5F5' alt='Supprimer' src='/backoffice/images/valide.gif' border='0' onclick="if (confirm('Voulez-vous continuer ?')) {document.formu.fm_del.value='<c:out value="${fm}"/>';}"><br>
                        </td>
                        </tr>
                        </table>
                        <P/>

                        </c:otherwise>
                </c:choose>
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='laction' name='laction'>
                <input type='hidden' id='lid' name='lid'>
                <c:if test="${param['zaction']!=null && param['zaction']!=''}">
                        <center><a href="javascript:lcC()">Choisir les Plus Produit sélectionnés <img border=O align="absmiddle" src="/backoffice/images/pucef.gif" alt="Choisir ces Plus Produit et terminer"></a><P></center>
                </c:if>
                <table bgcolor="#F5F5F5" border="0" style="border:1px solid black;">
                <c:set var="j" value="-1"/>
                <c:forEach var="pps" items="${ps}">
                <c:if test="${pps!=null && pps!=''}">
                        <c:set var="j" value="${j+1}"/>
                        <c:set var="_pps" value="${pps}" scope="request"/>
                        <% request.setAttribute("d","d_"+request.getAttribute("_pps"));%>
                        <tr>
                                <td valign="center" align='center' class='text' width='60' >
                                        <input type='hidden' name='<c:out value="${_pps}"/>' value='<c:out value="${j}"/>'><b>(<c:out value="${j}"/>): </b>
                                        <c:if test="${param['zaction']!=null && param['zaction']!=''}">
                                                <input class="button2" style="border:0px" type=checkbox value="<c:out value="${d}"/>" >
                                        </c:if>
                                </td>
                                <td class="text"><input type=text name="<c:out value="${d}"/>" style="width:550px;" value="<c:out value="${param[d]}"/>">&nbsp;</td>
                                <td align="right">
                                <c:if test="${j gt 0}">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Monter' src='/backoffice/images/up.gif'
                                        border='0'
                                        onclick="laction.value='u';lid.value='<c:out value="${_pps}"/>'">
                                </c:if>
                                <c:if test="${j lt n-1}">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Descendre' src='/backoffice/images/down.gif'
                                        border='0'
                                        onclick="laction.value='d';lid.value='<c:out value="${_pps}"/>'">
                                </c:if>
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Supprimer' src='/backoffice/images/croix.gif'
                                        border='0'
                                        onclick="if (confirm('Voulez-vous supprimer ?')) { laction.value='s';lid.value='<c:out value="${_pps}"/>'} else return false;">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Ajouter' src='/backoffice/images/etoile.gif'
                                        border='0'
                                        onclick="laction.value='a';lid.value='<c:out value="${_pps}"/>'">
                                </td>
                        </tr>
                        <c:if test="${j lt n-1}">
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        </c:if>
                </c:if>
                </c:forEach>
                </table>
                <P>
                <table style='border:1px solid black;;background-color:#F5F5F5'>
                        <tr>
                        <c:choose>
                        <c:when test="${n == null || n==0}">
                                        <td width='160' align='right' class='text'><b>Ajouter un nouvel élément </b></td>
                                        <td valign='middle'>
                                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Créer' src='/backoffice/images/add.gif'
                                                border='0'
                                                onclick="laction.value='n';">
                                        </td>
                        </c:when>
                        <c:otherwise>
                                        <td width='260' align='right' class='text'><b>Sauvegarder dans le référentiel </b></td>
                                        <td valign='middle'>
                                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Enregistrer' src='/backoffice/images/valide.gif'
                                                        border='0'
                                                        onclick="if (confirm('Voulez-vous sauvegarder ?')) {laction.value='t';} else return false;">
                                        </td>
                        </c:otherwise>
                        </c:choose>
                        </tr>
                </table>
                </form>
        </c:otherwise>
        </c:choose>
</body>
</html>



