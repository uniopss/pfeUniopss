<%
	// standard stuff
	response.setContentType("text/xml");
	String sPublicationId = request.getParameter("id");
	int iVersion = Integer.parseInt(request.getParameter("version"));

	String sXml = "";

	if( request.getSession().getAttribute("currentXml")==null ) {

		org.nextime.ion.framework.business.Publication oPublication = null;
		try {
			org.nextime.ion.framework.mapping.Mapping.begin();
			oPublication = org.nextime.ion.framework.business.Publication.getInstance(sPublicationId);
		} catch( Exception e ) {
		} finally {
			// no dirty transaction here
			org.nextime.ion.framework.mapping.Mapping.rollback();
		}
		// get back xml content
		// replaceAll(".*<\\x3Fxml.*\\x3F>","")
		sXml = oPublication.getVersion(iVersion).getDataAsString();
                String sMd = "<metaDatas>" ;
                java.util.Enumeration mdks = oPublication.getMetaData().keys();
                while (mdks.hasMoreElements()) {
                        String key = mdks.nextElement() + "";
                        sMd += "<metaData name='"+key+"'>"+oPublication.getMetaData(key)+"</metaData>" ;
                }
                sMd += "</metaDatas>" ;
                sXml = sXml.replace("</publication>",sMd+"</publication>");
	} else {

		sXml = request.getSession().getAttribute("currentXml")+"";
		request.getSession().removeAttribute("currentXml");
	}

	sXml = sXml.substring( sXml.indexOf("<publication") );

	// a l'ancienne pke ca L?FJKFKJFKFD
	sXml = sXml.replaceAll("ion:lang","xml:lang");

	// hack for € symbol
	sXml = sXml.replaceAll("&euro;","€");

%>

<%-- write content--%>
<%=sXml%>
