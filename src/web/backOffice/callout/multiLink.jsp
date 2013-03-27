<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<c:set var="champ" value="${param['action']}"/>
<%
String field=(String)pageContext.getAttribute("champ");
field="FIELD"+field.substring(field.indexOf("_"),field.length());
pageContext.setAttribute("champ",field);
%>
<html>
<head>
<title>Information complémentaires</title>
<link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
<script language="JavaScript" src="/backoffice/scripts/prototype-1.6.0.2.js"></script>



<script language="javascript">

var n=0;
function ajout(libelle,lien)
{

	<%-- remplace single quot par le caractère echape, utilise prototype  --%>	
	var label = libelle.gsub("'", "&#39;");
	var link =  lien.gsub("'", "&#39;");

 n++;
 frm.innerHTML += "<div id='div" + n + "'><input type='hidden' name='test"+n+"' value='true'/><table align='center'>"+
		"<tr>"+
			"<td>Libell&eacute; : </td>"+
			"<td>"+
				"<input type='text' name='libelle" + n + "' value='" + label + "' />"+
			"</td>"+
		"</tr>"+

		"<tr>"+
			"<td>Lien&nbsp;&nbsp;&nbsp; :</td>"+
			"<td> "+
				"<input type='text' name='lien" + n + "' value='" + link + "' />"+
				"&nbsp;&nbsp;<a href='#' onclick=\"window.open('../listSection2.x?action=lien" + n + "','_blank','toolbar=0, location=0, directories=0, status=0, scrollbars=1, resizable=1, copyhistory=0, menuBar=0, width=400, height=400');return(false)\">section</a>&#160;ou&#160;<a href='#' onclick=\"window.open('../listPublication2.x?action=lien"+n+"','_blank','toolbar=0, location=0, directories=0, status=0, scrollbars=1, resizable=1, copyhistory=0, menuBar=0, width=400, height=400');return(false)\">publication</a>&#160;ou&#160;<a href='#' onClick=\"window.open('browserLienMultiLink.jsp?action=lien" + n + "','_blank','toolbar=0, location=0, directories=0, status=0, scrollbars=1, resizable=1, copyhistory=0, menuBar=0, width=400, height=400');return(false)\" >lien</a>&#160;ou&#160;<a href='#' onClick=\"window.open('browserMultiLink.jsp?btype=_download&amp;action=lien" + n + "','_blank','toolbar=0, location=0, directories=0, status=0, scrollbars=1, resizable=1, copyhistory=0, menuBar=0, width=400, height=400');return(false)\">ressource</a>&#160;ou&#160;<a href='#' onClick='hide(" + n + ")'>supprimer</a>"+
			"</td>"+
		"</tr>"+
	"</table><br/></div>";

}

function hide(num){
	document.getElementById("div"+num).innerHTML="<input type='hidden' name='test"+num+"' value='false'/>";

}

</script>
<script language="javascript">

function valider(){
	sXml = "<lc>" ;
    sRep = "" ;
    var lien=new Array();
	var libelle=new Array();
	var a="";
	var test=true;

	for (var i=1;i<=n;i++){		
	if (eval("document.frm.test" + i + ".value")=="true"){
		lien[i]="document.frm.lien" + i + ".value";
		libelle[i]="document.frm.libelle" + i + ".value";
		if (eval(lien[i])==""||eval(libelle[i])=="") {test=test&&false; }
        }
	}
	
	if (test){
	frm.submit();
	for (var i=1;i<=n;i++){		
	if (eval("document.frm.test" + i + ".value")=="true"){
		lien[i]="document.frm.lien" + i + ".value";
		libelle[i]="document.frm.libelle" + i + ".value";
		sXml += "<r ordre='"+(i)+"'><l>"+eval(libelle[i])+"</l><d>"+eval(lien[i])+"</d></r>" ;
        sRep += "&nbsp;"+ eval(libelle[i]) + "&nbsp;&nbsp;" + eval(lien[i]) +"<br/><br/>";
        }
	}
	sXml+="</lc>";
	eval(top.opener.<c:out value="${param['action']}"/>(sXml,sRep));
	self.close();
	} else alert("vérifier la présence de tous les champs svp");
}
var fin=0;
var liens=[''];
var urls=[''];
function charger(){
	var vXml=top.opener.document.forms[0].<c:out value="${champ}"/>.value;
	
	if (vXml!=""){
	
	var indexFin=vXml.lastIndexOf("<r ordre=")+10;
	fin=vXml.substring(indexFin,vXml.indexOf(">",indexFin)-1);
	
	var test=vXml.indexOf("</d>");
	for (i=0;test!=-1;i++){
	
	var finBloc=test+4;
	liens[i]=vXml.substring(vXml.indexOf("<l>")+3,vXml.indexOf("</l>"));
	urls[i]=vXml.substring(vXml.indexOf("<d>")+3,vXml.indexOf("</d>"));
	vXml=vXml.substring(finBloc,vXml.length);
	ajout(liens[i],urls[i]);
	test=vXml.indexOf("</d>");
	}
	} else {
	ajout("","");
	}
	
}

</script>
</head>
<body  class="text" background="/backoffice/images/fond.gif" onload="charger()">

<form action="multiLink.jsp" method="post" name="frm">

	<input name="action" value="<c:out value="${param['action']}"/>" type="hidden"/>
</form>
	<table align='center'>
			<tr>
			<td colspan="2" align="center">
				<input type="button" value="Ajouter un lien" onclick="ajout('','')">
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="button" value="valider" onclick="valider()">
			</td>
		</tr>
	</table>
</body>
</html>
