<%@ taglib uri='/WEB-INF/tlds/struts-logic.tld' prefix='logic' %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
<head>
        <title>Les caracteristiques techniques</title>
        <link rel="stylesheet" type="text/css" href="/backoffice/styles/global.css">
</head>
<body background="/backoffice/images/fond.gif" bgcolor="white" style="cursor:default" class="text"  onload="top.resizeTo(590,500)">
        <% if (!"LC".equals(request.getParameter("valeur"))) { %>
                <%
                        String valeur = (String)request.getParameter("valeur");
                        if (valeur==null || "".equals(valeur)) {
                                valeur = "" ;
                                try {
                                        // bp_sm_LV_LV60
                                        String[] s = request.getParameter("sid").toString().split("_");
                                        java.io.File f = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getProduitDirectory(),s[0]+"_"+s[1]+"_"+s[2]+"/caracteristique.xml");
                                        if (f.exists()) {
                                        // lit le contenu du fichier
                                        String line = "";
                                        java.io.BufferedReader is = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream(f) ) );
                                        while( line != null ) {
                                        valeur+=line+"\r\n";
                                        line = is.readLine();
                                        }
                                        is.close();
                                        }
                                }catch(Exception e) {}
                        }
                        if (valeur==null || "".equals(valeur))
                                valeur = "<lc/>" ;
                %>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <input type='hidden' id='plid' name='plid' value="<c:out value="${param['plid']}"/>">
                <input type='hidden' id='valeur' name='valeur' value="LC">
                <c:import url="/edx/rep2form.xsl" var="xsl"/>
                <x:transform xslt="${xsl}">
                <%=valeur%>
                </x:transform>
                </form>
                <script language="javascript">document.formu.submit();</script>
        <% } else { %>
        <%
                String sC = "";
                try {
                        String[] s = request.getParameter("sid").toString().split("_");
                        java.io.File f = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getProduitDirectory(),s[0]+"_"+s[1]+"_"+s[2]+"/caracteristique.xml");
                        // lit le contenu du fichier
                        String line = "";
                        java.io.BufferedReader is = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream(f) ) );
                        while( line != null ) {
                        sC+=line+"\r\n";
                        line = is.readLine();
                        }
                        is.close();

                } catch (Exception e) {
                }
                if (sC==null || "".equals(sC))
                        sC = "<lc/>" ;
        %>
                <script language="javascript">
                function lcC() {
                        eval(top.opener.<c:out value="${param['zaction']}"/>(document.formu.xml.value,document.formu.zxml.value));
                        self.close();
                }
                function lcM(id,c) {
                        if(top.opener.ca_MD["CTV_"+c] && top.opener.ca_MD["CTV_"+c]!="") {
                                obj = eval("document.formu.d_"+id);
                                obj.value=top.opener.ca_MD["CTV_"+c];
                                obj.readOnly=true ;
                                obj.className = "button2";
                                obj = eval("document.formu.l_"+id);
                                obj.value=top.opener.ca_MD["CTL_"+c];
                                obj.className = "button2";
                        }
                }
                </script>
                <form id="formu" name="formu" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='plid' name='plid' value="<c:out value="${param['plid']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <input type='hidden' id='laction' name='laction'>
                <input type='hidden' id='lid' name='lid'>
                <input type='hidden' id='valeur' name='valeur' value="LC">
                <table bgcolor="#F5F5F5" border="0" style="border:1px solid black;">
                <%

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
                                                +"<c>"+("1".equals(request.getParameter("c_"+name))?"1":"0")+"</c>"
                                                +"<l>"+request.getParameter("l_"+name)+"</l>"
                                                +"<i>"+request.getParameter("i_"+name)+"</i>"
                                                +"<d>"+request.getParameter("d_"+name)+"</d>"
                                                +"</r>\n" ;
                                        if (!"t".equals(laction)) { %>
                        <tr>
                                <td valign="center" align='left' class='text' ><input type='hidden' name='<%=name%>' value='<%=j%>'><b>CT<br>(<%=j%>): </b></td>
                                <td valign="center" class=text >
                                        <% request.setAttribute("cCode","i_"+name);%>
                                        <% request.setAttribute("cNom",request.getParameter("l_"+name)!=null?request.getParameter("l_"+name):request.getParameter("i_"+name));%>
                                        <% request.setAttribute("cValeur",request.getParameter("i_"+name)!=null?request.getParameter("i_"+name):"");%>
                                        <input type="text"   name="l_<%=name%>"  readonly  style="width:270px" value="<c:out value="${cNom}"/>"/>
                                        <input type="hidden" name="<c:out value="${cCode}"/>" value="<c:out value="${cValeur}"/>"/>
                                        &nbsp;=&nbsp;
                                        <input type='text' name='d_<%=name%>' value="<%=request.getParameter("d_"+name)!=null?request.getParameter("d_"+name):""%>" style="width:200px;">&nbsp;
                                        <script>lcM("<%=name%>","<c:out value="${cValeur}"/>");</script>
                                </td>
                                <td align="right">
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
                <table style='border:1px solid black;;background-color:#F5F5F5'>
                <% if(ps.size()==0){%>
                        <tr>
                                <td width='160' align='right' class='text'><b>Ajouter une caractéristique </b></td>
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
                <% if (!"t".equals(laction)) { %>
                <form id="zimport" name="zimport" action="/backoffice/callout/<c:out value="${param['nom']}"/>.jsp" method="POST">
                <input type='hidden' id='sid' name='sid' value="<c:out value="${param['sid']}"/>">
                <input type='hidden' id='zaction' name='zaction' value="<c:out value="${param['zaction']}"/>">
                <input type='hidden' id='nom' name='nom' value="<c:out value="${param['nom']}"/>">
                <input type='hidden' id='plid' name='plid' value="<c:out value="${param['plid']}"/>">
                <input type='hidden' id='valeur' name='valeur' value="">
                </form>
                <% } %>
        <% } %>
</body>
</html>



