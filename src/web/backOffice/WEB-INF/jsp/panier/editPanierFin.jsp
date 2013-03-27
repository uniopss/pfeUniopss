<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<script language="JavaScript">
<c:if test="${param['ok']!=null}">
        top.opener.document.panier_<c:out value="${param['id']}"/>.etat.checked=<c:choose><c:when test="${param['etat']=='1'}">false</c:when><c:otherwise>true</c:otherwise></c:choose>;
        top.opener.document.panier_<c:out value="${param['id']}"/>.etat.value=<c:choose><c:when test="${param['etat']=='1'}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
</c:if>
        self.close();
</script>
