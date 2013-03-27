<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Liste de mot-clés</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text"  onload="top.resizeTo(450,370)">
        <% if (request.getParameter("nom") != null 
        	&& !"".equals(request.getParameter("nom")) 
        	&& request.getParameter("valeur")!=null && 
        	!"".equals(request.getParameter("valeur"))) { %>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>" method="POST">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='plid' name='plid' value="<c:out value="${param['plid']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <c:import url="/edx/rep2form.xsl" var="xsl"/>
                <x:transform xslt="${xsl}">
                <c:out value="${param['valeur']}" escapeXml="false"/>
                </x:transform>
                </form>
                <script language="javascript">document.formu.submit();</script>
        <% } else { %>
                <script language="javascript">
                
                <%-- 
                	Quand on clique sur terminer 
                	xml : xml persitant 
                	zxml : la valeur à afficher dans text area de la fenêtre parent 
                --%>
                function lcC() {
                        eval(top.opener.<c:out value="${param['zaction']}"/>(document.formu.xml.value,document.formu.zxml.value));
                        self.close();
                }
                var _focus="";
                function searchLc(v) {
                        if (_focus) {
                                _focus.focus();
                                _focus.value = v;
                        }
                        else alert("Veuillez choisir un champ mot-clé");
                }
                function callbackFromThesaurus(v) {
                	var frm = document.formu;
                	frm.listeMotCles.value = v.toString();
                	frm.laction.value='m'; <%-- les mot clés viennent du popup --%>
                	frm.submit();
                }
                
                </script>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>" method="POST">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='plid' name='plid' value="<c:out value="${param['plid']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <input type='hidden' id='laction' name='laction'>
                <input type='hidden' id='lid' name='lid'>
                <input type='hidden' id='listeMotCles' name='listeMotCles'>
                
                
                <table bgcolor="#F5F5F5" border="0" style="border:1px solid black;">
                <%
                        String lid = request.getParameter("lid");
                        String laction = request.getParameter("laction");
                        java.util.Vector ps = new java.util.Vector();
                        java.util.Enumeration ep = request.getParameterNames();
                        java.util.Hashtable extraParams = new java.util.Hashtable();
						java.util.ArrayList extraValues = new java.util.ArrayList();
                        
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
                                if (!name.equals("listeMotCles")) {
	                                extraValues.add(request.getParameter(name));
								}
                        }

                        
                        int zId = 0;
                        if (lid != null && !"".equals(lid))
                                zId = ps.indexOf(lid) ;
                        try {
								if ("a".equals(laction)) { // ajouter
                                        ps.add(1+zId,"r_"+max);
                                }
                                else if("u".equals(laction) && zId>0) { // up 
                                        ps.removeElementAt(zId);
                                        ps.add(zId-1,lid);
                                }
                                else if("d".equals(laction) && zId<ps.size()-1) { // down
                                        ps.removeElementAt(zId);
                                        ps.add(zId+1,lid);
                                } else if ("m".equals(laction)) { // vient du popup 
                                       String listeMotCles=request.getParameter("listeMotCles");

                                       if (listeMotCles!=null) {
	                                       java.util.StringTokenizer st = new java.util.StringTokenizer(listeMotCles,",	");
	  										while (st.hasMoreTokens()) {
		  											String nextToken = st.nextToken();
		  											System.out.println(nextToken);
		  											if (!extraValues.contains(nextToken)) {
														ps.add("r_"+max);
														extraParams.put("r_"+max,nextToken);
														System.out.println("put " + "r_"+ max + ": " + nextToken);													
														max = max + 1;
													}
											}
                                       }
      								
                                        

                                }
                        }
                        catch(Exception e) {
		                        System.out.println(e);
                        }
                        int j = -1 ;
                        String sXml = "";
                        for (int i=0; i<ps.size();i++) {
                                String name = ps.get(i) + "";
                                if (name != null && !"".equals(name)) {
                                        j++ ;
										String checkVal  = request.getParameter("c_" + name);
										boolean isChecked =  (checkVal!=null && (checkVal.length() > 0));
										
										// dans le cas c'est le dernier mot clé fils, check = true
										if ((i==ps.size()-1)
											&& (extraParams.get(name)!=null)) {
											isChecked = true;
										}
										sXml += "<r ordre='"+j+"'>"
                                                +"<d>"+request.getParameter("d_"+name)+"</d>"
												+"<c>"+ (isChecked?"true":"") +"</c>"
                                                +"</r>\n" ;
                                                

                                        if (!"t".equals(laction)) { // pas encore terminé, alors construire la page  %> 
                        <tr>
                                <td valign="center" align='left' class='text' width='100' rowspan="2"><input type='hidden' name='<%=name%>' value='<%=j%>'><b>Mot-clé<br>(<%=j%>): </b></td>
                                <%
                                	String valueText= "";
                                	if (request.getParameter("d_"+name)!=null) {
                                		valueText=request.getParameter("d_"+name);
                                	}
                                	if (extraParams.get(name)!=null) {
                                		valueText=(String)extraParams.get(name);
                                	}
                                %>
                                
                                <td class=text>ID :&nbsp;<input onfocus="_focus=this" type='text' name='d_<%=name%>' 

                                value="<%=valueText%>" style="width:150px;">
                                &nbsp;
                                </td>
                                <td align="right"><input type="checkbox" name='c_<%=name%>' 
                                	<%
                                	if (isChecked) {
                                	%>
	                                	checked="checked" 
	                                <%	
	                                }
                                	%>
                                /></td>
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
                		<tr>
                                <td width='160' align='right' class='text'><b>Assigner un mot-clé</b></td>
                                <td valign='middle' align='center'><a href="#search" onclick="window.open('/backoffice/callout/thesaurus.jsp','thesaurus','width=640,height=450,resizable=yes,scrollbars=yes');"><img style='border:0px;background-color:#F5F5F5' alt='Assigner un mot clé' src='/backoffice/images/pucef.gif' border='0'></a>
                                </td>
                        </tr>
                        <tr>
                                <td height="1" bgcolor="#949494"></td>
                                <td height="1" bgcolor="#949494"></td>
                        </tr>
                        <tr><td width='160' align='right' class='text'><b>Terminer </b></td>
                        <td valign='middle' align='center'><input type='image' style='border:0px;background-color:#F5F5F5' alt='Terminer' src='/backoffice/images/valide.gif'
                        border='0'
                        onclick="laction.value='t';">
                                </td>
                        </tr>
                </table>
                <textarea id="xml" name="xml" style="visibility:hidden;width:300px;height:1px"><lc><%=sXml%></lc></textarea>
                <% if("t".equals(laction)){ %>
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