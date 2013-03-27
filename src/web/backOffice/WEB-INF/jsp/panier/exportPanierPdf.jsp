<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%
        java.util.Enumeration e = request.getParameterNames();
	while(e.hasMoreElements()){
		String tmp = (String)e.nextElement();
                if( tmp.indexOf("sc_") == 0 && request.getParameter(tmp.replaceAll("sc_",""))!=null) {
                        pageContext.setAttribute("pSc",request.getParameter(tmp).substring(3,5));
                        String pId = tmp.replaceAll("sc_","");
                        pageContext.setAttribute("pId",pId);
                        pageContext.setAttribute("pVe",request.getParameter(pId));
                        break ;
                }
        }
%>
<c:choose>
        <c:when test="${pId!=null && pId!='' && pVe!=null && pVe!='' && pSc!=null && pSc!=''}">
                <c:redirect url="../pdfPublication/${pId}.html?view=${pSc}_fiche_pdf&version=${pVe}&file=pdf.pdf"/>
        </c:when>
        <c:otherwise>
                <html>
                <head>
                        <title>Panier</title>
                        <link rel="stylesheet" type="text/css" href="styles/global.css">
                </head>
                <body class="text" background="images/fond.gif" style="cursor:default" onload="top.resizeTo(500,370)">
                        <center><img src='images/warning.gif' align='absmiddle'> Erreur a la generation du PDF</center>
                </body>
                </html>
        </c:otherwise>
</c:choose>
