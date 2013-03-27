<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
        <title>Méta données</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='' background="images/fond.gif">
        <form id=formu name=formu action="publicationMd.x" method="post" onsubmit="return false;">
             <input type="Hidden" name="sid"                  value="<c:out value="${sid}"/>">
             <input type="Hidden" name="id"                  value="<c:out value="${publication.id}"/>">
                <input type="Hidden" name="version"             value="<c:out value="${version}"/>">
                <input type='hidden' id='metaToDelete' name='metaToDelete'>
                <input type='hidden' id='metaToSave' name='metaToSave'>
   	<img src="images/publication-edit-tile.gif" align="absmiddle">
	<span class="title">
                Méta données de la publication " <c:out value="${publication.metaData['name']}" default="<i>(undefined)</i>" escapeXml="false"/> "
	</span>
	<br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="title-list"><c:out value="${section.pathLabel}" default="" escapeXml="false"/></span>
	<br>
	<hr width="100%" size="1" noshade color="#949494">
        <div style="border:1px solid black;height:180px;overflow:auto" class="button2">
        <table width='100%' class=text style='background-color:#F5F5F5'>
        <%
                boolean bouton = false;
                try {
                java.util.Hashtable ht = (java.util.Hashtable)request.getAttribute("metaData") ;
                java.util.Enumeration en = ht.keys();
		while (en.hasMoreElements()) {
			String key = en.nextElement() + "";
                        if (!"name".equals(key) && !"diffusion".equals(key) && !"objet".equals(key) && !key.startsWith("lc") && !key.startsWith("index_")) {
                        bouton = true;  %>
                <tr>
                        <td align='left' class='text' ><b><%=key%>: </b></td>
                        <td><input type='text' name='META_<%=key%>' value="<%=ht.get(key)%>" style="width:300px">
                                &nbsp;<a onclick="if (confirm('Voulez-vous supprimer cette méta donnée ?')) { metaToDelete.value='<%=key%>' ; document.formu.submit(); } " href="#" title="supprimer"><img style="cursor:hand;" src="images/poubelle.gif" style="border=0; background-color:transparent" border="0"></a>

                        </td>
                </tr>
                <%     }
                }
                }catch(Exception e) {}

        %>

        </table>
        </div>
        <P/>
        <table style='border:1px solid black;;background-color:#F5F5F5'>
        <tr>
                <td width='360' align='right' class='text'><b>Ajouter une nouvelle méta donnée nommée </b>&nbsp;<input type='text' name='newMETA' style='width:100px'></td>
                <td valign='middle' align='center'>
                        <input type='image' style='border:0px;background-color:#F5F5F5' alt='Créer' src='/backoffice/images/add.gif'
                        border='0' onclick="if (!document.formu.newMETA.value) {alert('Veuillez saisir un nom de méta donnée');return false;} document.formu.submit();">
                </td>
        </tr>
        <%if (bouton) {%>
        <tr>
                <td height="1" bgcolor="#949494"></td>
                <td height="1" bgcolor="#949494"></td>
        </tr>
        <tr>
                <td  align='right' class='text'><b>Sauvegarder ces méta données </b></td>
                <td valign='middle' align='center'>
                        <input type='image' style='border:0px;background-color:#F5F5F5' alt='Créer' src='/backoffice/images/valide.gif'
                        border='0' onclick="if (confirm('Voulez vous sauvegarder ?')) {metaToSave.value='1';document.formu.submit();}">
                </td>
        </tr>
        <%}%>
        </table>
        </form>
</body>

</html>



