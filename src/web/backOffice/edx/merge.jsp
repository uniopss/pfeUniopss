<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<html>
	<body>
<div id="_chargement" style="visibility:hidden;position:absolute; top:250px; left:200px; z-index:10; background-color: #FFFFEE;"><!--<table width=400 border=0 cellpadding=2 cellspacing=3 style='border:1px solid black;background-color:#F5F5F5' class='text'><tr><td align=center valign=middle><table width=100% border=0 cellpadding=2 cellspacing=3><tr><td align=center valign=middle><b>Veuillez patienter un moment...</b></td></tr></table></td></tr></table>--></div>
<script language="javascript">document.getElementById("_chargement").style.visibility = "visible";</script>
		<html:form action="editPublication.x">
<%try{%>
                        <c:import url="/edx/getPublicationXml.jsp?id=${param['id']}&version=${param['version']}" var="old"/>
                        <c:import url="/edx/formMerger.xsl" var="xsl" />
                        <c:set var="zXml">
                                <x:transform xslt="${xsl}">
					<nextimeSupaXml>
						<c:out value="${param['data']}" escapeXml="false"/>
						<data>
							<%
								java.util.Enumeration names = request.getParameterNames();
								while( names.hasMoreElements() ) {
									String name = names.nextElement()+"";
									String value = request.getParameter(name);

									// hack for € symbol
									value = value.replaceAll( "€", "&amp;euro;" );

									if( name.startsWith("FIELD_") ) {
										%><<%=name.substring(6)%>><![CDATA[<%=value%>]]></<%=name.substring(6)%>><%
									}
								}
							%>
						</data>
					</nextimeSupaXml>
					<x:param name="locale" value="${param['currentLocale']}"/>
				</x:transform>
                        </c:set>
<%}catch(Exception e) {}%>
                        <textarea cols="70" rows="15" name="data" style="visibility:hidden; position:absolute">
                                <c:out value="${zXml}" escapeXml="false"/>
			</textarea>

			<input type="Hidden" name="id" value="<c:out value="${param['id']}"/>">
                        <input type="Hidden" name="sid" value="<c:out value="${param['sid']}"/>">
			<input type="Hidden" name="version" value="<c:out value="${param['version']}"/>">
			<input type="Hidden" name="retour" value="<c:out value="${param['retour']}"/>">
			<input type="Hidden" name="itsOk" value="itsOk">
			<input type="hidden" name="date" size="12" value="<c:out value="${param['date']}"/>">

		</html:form>
	</body>
</html>
<script>
       document.forms(0).submit();
</script>


