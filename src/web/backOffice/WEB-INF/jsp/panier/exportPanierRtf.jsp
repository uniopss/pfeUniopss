<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%@ taglib uri="/WEB-INF/tlds/struts-template.tld" prefix="template" %>
<%
        response.setContentType("application/rtf");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader ("Content-Disposition", "attachment;filename=export.rtf");
%>
<ion:begin/>
<%
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        try {
                com.lowagie.text.rtf.RtfWriter2.getInstance(document, baos);
                document.open();

                java.util.Enumeration e = request.getParameterNames();
                while(e.hasMoreElements()){
                        String tmp = (String)e.nextElement();
                        if( tmp.indexOf("sc_") == 0 && request.getParameter(tmp.replaceAll("sc_",""))!=null) {
                                pageContext.setAttribute("pId",tmp.replaceAll("sc_",""));
                                pageContext.setAttribute("pVe",request.getParameter(tmp.replaceAll("sc_","")));
                                request.setAttribute("pRef",request.getParameter(tmp.replaceAll("sc_","rf_")));
                                request.setAttribute("txt","1");
%>
<c:set var="xml" scope="request">
        <ion:selectObjects type="org.nextime.ion.frontoffice.objectSelector.GetPublicationView">
		<ion:param name="publication" value="${pId}"/>
		<ion:param name="version" value="${pVe}"/>
                <ion:param name="view" value="special_export_rtf"/>
		<ion:param name="max" value="1"/>
		<ion:iterateOver var="lc">
                        <c:out value="${lc.view}" escapeXml="false"/>
		</ion:iterateOver>
        </ion:selectObjects>
</c:set>
<c:set var="type" scope="request" value="equivalent"/><c:set var="xml" scope="request"><template:insert template="util.jsp" /></c:set>
<c:set var="type" scope="request" value="variante"/><c:set var="xml" scope="request"><template:insert template="util.jsp" /></c:set>
<c:set var="type" scope="request" value="option"/><c:set var="xml" scope="request"><template:insert template="util.jsp" /></c:set>
<%
                                try {
                                        // Bug STYLE_ITALIC permute avec STYLE_BOLD
                                        com.lowagie.text.rtf.style.RtfFont rf = new com.lowagie.text.rtf.style.RtfFont("Arial",12);
                                        com.lowagie.text.rtf.style.RtfFont rfb = new com.lowagie.text.rtf.style.RtfFont("Arial",12,com.lowagie.text.rtf.style.RtfFont.STYLE_ITALIC);
                                        String[] _b = request.getAttribute("xml").toString().split("\n");
                                        for(int i=0;i<_b.length;i++) {
                                                com.lowagie.text.Paragraph p ;
                                                if (_b[i].startsWith("__LC__")) {
                                                        p = new com.lowagie.text.Paragraph(_b[i].substring(7),rfb);
                                                }
                                                else
                                                        p = new com.lowagie.text.Paragraph(_b[i],rf);
                                                document.add(p);
                                        }
                                        com.lowagie.text.Paragraph p = new com.lowagie.text.Paragraph("\n\n");
                                        document.add(p);
                                        //com.lowagie.text.Chunk c = new com.lowagie.text.Chunk("");
                                        //c.setNewPage();
                                        //document.add(c);
                                }
                                catch(Exception ee) {
                                }
                        }
                }
        } catch(Exception e) {out.println("<center><img src='images/warning.gif' align='absmiddle'> Erreur a la generation du RTF</center>");
        }
        document.close();
%>
<ion:close/>
<%
        try {
                response.setContentLength(baos.size());
                baos.writeTo(response.getOutputStream());
                response.getOutputStream().flush();
        }
        catch(Exception e) {
        }
%>
