<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Les plus produits</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text"  onload="top.resizeTo(580,490)">
        <% if (request.getParameter("nom") != null 
        	&& !"".equals(request.getParameter("nom")) 
        	&& request.getParameter("valeur")!=null 
        	&& !"".equals(request.getParameter("valeur"))) { %>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <c:import url="/edx/rep2form.xsl" var="xsl"/>
                <x:transform xslt="${xsl}">
                <c:out value="${param['valeur']}" escapeXml="false"/>
                </x:transform>
                </form>
                <script language="javascript">document.formu.submit();</script>
        <% } else { %>
                <script language="javascript">
                function lcC() {
                        eval(top.opener.<c:out value="${param['zaction']}"/>(document.formu.xml.value,document.formu.zxml.value));
                        self.close();
                }
                var _focus="";
                </script>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <input type='hidden' id='laction' name='laction'>
                <input type='hidden' id='lid' name='lid'>
                <table bgcolor="#F5F5F5" border="0" style="border:1px solid black;">
                <%
                        String sid = "";
                        try {
                        String[] s = request.getParameter("sid").toString().split("_");
                        sid = s[0]+"_"+s[1]+"_"+s[2];
                        }catch(Exception e) {}

                        String lid = request.getParameter("lid");
                        String laction = request.getParameter("laction");
                        java.util.Vector ps = new java.util.Vector();
                        java.util.Enumeration ep = request.getParameterNames();
                        int max = 0 ;
                        while (ep.hasMoreElements()) {
                                String name = ep.nextElement() + "";
                                if (name.startsWith("r_") && !(name.equals(lid) && "s".equals(laction))) {
                                        int zN = Integer.parseInt(request.getParameter(name)) ;
                                        while (zN>=ps.size())
                                                ps.add("");
                                        ps.set(zN,name);
                                        int n = Integer.parseInt(name.substring(2));
                                        if (n >= max)
                                                max = n + 1;
                                }
                        }
                        int zId = 0;
                        if (lid != null && !"".equals(lid))
                                zId = ps.indexOf(lid) ;
                        try {
                                if ("n".equals(laction)) {
                                        ps.add("r_"+max);
                                }
                                else if ("a".equals(laction)) {
                                        ps.add(1+zId,"r_"+max);
                                }
                                else if("u".equals(laction) && zId>0) {
                                        ps.removeElementAt(zId);
                                        ps.add(zId-1,lid);
                                }
                                else if("d".equals(laction) && zId<ps.size()-1) {
                                        ps.removeElementAt(zId);
                                        ps.add(zId+1,lid);
                                }
                        }
                        catch(Exception e) {}
                        int j = -1 ;
                        String sXml = "";
                        for (int i=0; i<ps.size();i++) {
                                String name = ps.get(i) + "";
                                if (name != null && !"".equals(name)) {
                                        j++ ;
                                        sXml += "<r ordre='"+j+"'>"
                                                +"<l>"+request.getParameter("l_"+name)+"</l>"
                                                +"<d>"+request.getParameter("d_"+name)+"</d>"
                                                +"</r>\n" ;
                                        if (!"t".equals(laction)) { %>
                        <tr>
                                <td valign="center" align='left' class='text' width='100' rowspan="2"><input type='hidden' name='<%=name%>' value='<%=j%>'><b>Plus produit<br>(<%=j%>): </b></td>
                                <td class=text>Titre :&nbsp;<input onfocus="_focus=this" type='text' name='l_<%=name%>' value="<%=request.getParameter("l_"+name)!=null?request.getParameter("l_"+name):""%>" style="width:265px;">&nbsp;
                                <a href="#plusProduit" onclick="window.open('/backoffice/listPP.x?sid=<%=sid%>&zaction=l_<%=name%>','plusproduit','width=750,height=400,resizable=yes,scrollbars=yes')">Choisir</a>&nbsp;
                                </td>
                                <td align="right">
                                <% if(i>0){%>
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Monter' src='/backoffice/images/up.gif'
                                        border='0'
                                        onclick="laction.value='u';lid.value='<%=name%>'">
                                <%} %>
                                <% if(i<ps.size()-1){%>
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Descendre' src='/backoffice/images/down.gif'
                                        border='0'
                                        onclick="laction.value='d';lid.value='<%=name%>'">
                                <%} %>
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Supprimer' src='/backoffice/images/croix.gif'
                                        border='0'
                                        onclick="if (confirm('Voulez-vous supprimer ?')) { laction.value='s';lid.value='<%=name%>'} else return false;">
                                <input type='image' style='border:0px;background-color:#F5F5F5' alt='Ajouter' src='/backoffice/images/etoile.gif'
                                        border='0'
                                        onclick="laction.value='a';lid.value='<%=name%>'">
                                </td>
                        </tr>
                        <tr>
                                <td colspan="2">
                                <textarea name="d_<%=name%>" onfocus="_focus=this" style="width:300px;height:60px"><%=request.getParameter("d_"+name)!=null?request.getParameter("d_"+name):""%></textarea>
                                <a href="#plusProduit" onclick="window.open('/backoffice/listPP.x?sid=<%=sid%>&zaction=d_<%=name%>','plusproduit','width=750,height=400,resizable=yes,scrollbars=yes')">Choisir</a>
                                </td>
                        </tr>
                        <% if(i<ps.size()-1){%>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <%
                                        }
                                        }
                                }
                        }
                %>
                </table>
                <P/>
                <table style='border:1px solid black;background-color:#F5F5F5'>
                <% if(ps.size()==0){%>
                        <tr>
                                <td width='160' align='right' class='text'><b>Ajouter un 'plus produit' </b></td>
                                <td valign='middle' align='center'>
                                        <input type='image' style='border:0px;background-color:#F5F5F5' alt='Créer' src='/backoffice/images/add.gif'
                                        border='0'
                                        onclick="laction.value='n';">
                                </td>
                        </tr>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                <%}%>
                        <tr><td width='160' align='right' class='text'><b>Terminer </b></td>
                        <td valign='middle' align='center'><input type='image' style='border:0px;background-color:#F5F5F5' alt='Terminer' src='/backoffice/images/valide.gif'
                        border='0'
                        onclick="laction.value='t';">
                                </td>
                        </tr>
                </table>
                <textarea id="xml" name="xml" style="visibility:hidden;width:300px;height:1px"><lc><%=sXml%></lc></textarea>
                <% if("t".equals(laction)){%>
                <textarea id="zxml" name="zxml" style="visibility:hidden;width:300px;height:1px">
                <c:import url="/edx/rep2lst.xsl" var="xsl"/>
                <x:transform xslt="${xsl}"><lc><%=sXml%></lc></x:transform>
                </textarea>
                <script language="javascript">
                        lcC();
                </script>
                <%}%>
                </form>
        <% } %>
</body>
</html>



