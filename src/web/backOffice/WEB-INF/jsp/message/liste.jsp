<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<div style="border:1px solid black;overflow-y:auto; height:200; width:100%; background-color:#F2F3F6; position:relative; top:5px; padding:4px">
	<% pageContext.setAttribute("currentDate", new java.util.Date()); %>
        <table width="100%" cellpadding="3" cellspacing="3">
                <form action="addMessage.x">
			<tr>
				<td style="font-size:10px; font-family:verdana;">
					<nobr><img src="images/fforum.gif" align="absmiddle" style="position:relative; top:1px"><b style="color:#3E6AAB"><c:out value="${userLogin}"/></b> <span style="color:#7991B3">(<fmt:formatDate value="${currentDate}" pattern="dd MMM - HH:mm"/>)</span>
					&nbsp;</nobr>
				</td>
				<td style="font-size:10px; font-family:verdana;">
                                        <input type="text" name="message" size="50">
                                        &nbsp;<input type="submit" class="button" value="Valider">
				</td>
				<td height="15"></td>
			</tr>
			<tr>
				<td colspan="3" height="4"></td>
			</tr>
			<tr>
				<td colspan="3" background="images/pt.gif" height="1"></td>
			</tr>
			<tr>
				<td colspan="3" height="4"></td>
			</tr>
		</form>
                <c:forEach var="mes" items="${messageBoard.messages}" varStatus="status">
			<tr>
				<td style="font-size:10px; font-family:verdana;" valign="top" width="10">
					<c:set var="poster" value="${mes.poster}" scope="page"/>
                                        <nobr><img src="images/fforum.gif" align="absmiddle" style="position:relative; top:1px"><b style="color:#3E6AAB;font-size:10px; text-decoration:none;font-family:verdana;"><c:out value="${mes.poster}"/></b> <span style="color:#7991B3">(<fmt:formatDate value="${mes.date}" pattern="dd MMM - HH:mm"/>)</span>
					&nbsp;</nobr>
				</td>
				<td style="font-size:10px; font-family:verdana;" valign="top" align="left">
					<c:out value="${mes.message}"/>
				</td>
				<td width="10" style="font-size:10px; font-family:verdana;" height="15">
                                        <c:if test="${userLogin=='admin' || userLogin==mes.poster}">
                                                <a onclick="return confirm('Voulez vous supprimer ce message ?');" href="deleteMessage.x?nb=<c:out value="${status.index}"/>"><img src="images/poubelle-mini.gif" border="0" alt="supprimer"></a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
