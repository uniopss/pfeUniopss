<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>


<form action="commentaire.x" method="POST">
        <span class="title-list">Entrez votre commentaire ci-dessous</span>
	<hr width="100%" size="1" noshade color="#949494">
	<input type="hidden" name="retour" value="<c:out value="${param['retour']}"/>">
	<input type="hidden" name="action" value="<c:out value="${param['action']}"/>">
	<input type="hidden" name="version" value="<c:out value="${param['version']}"/>">
	<input type="hidden" name="id" value="<c:out value="${param['id']}"/>">
        <input type="hidden" name="sid" value="<c:out value="${param['sid']}"/>">
        <input type="text" style="width:350px" name="commentaire">
	<br><img src="images/blank.gif" height="3"><br>
        <input onclick="return confirm('Voulez-vous continuer ?')" type="submit" class="button" value="<bean:message key="general.ok"/>">
</form>

