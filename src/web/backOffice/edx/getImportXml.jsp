<%
	response.setContentType("text/xml");
        String id = request.getParameter("id");
        String nid = request.getParameter("nid");
        String sXml = "<publication/>";
        try {
                java.io.File f = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getImportDirectory(), "/cmp/"+nid+".xml");
                if (f.exists()) {
                        java.io.BufferedReader reader =
                                new java.io.BufferedReader(
                                        new java.io.InputStreamReader(new java.io.FileInputStream(f)));
                        sXml = "";
			String line = "";
			while (line != null) {
				line = reader.readLine();
                                if (line != null)
                                        sXml += line;
			}
                        reader.close();
                        org.jdom.Document _d =  org.nextime.ion.framework.xml.XML.getInstance().getDocument(sXml);
                        _d = null ;
                }
                sXml = sXml.replaceFirst(".*<\\x3Fxml.*\\x3F>","");
        } catch (Exception e) {
                sXml = "<error>Erreur pour publication "+id+"</error>";
        }
%><%=sXml%>
