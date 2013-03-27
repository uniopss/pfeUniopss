<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>

<html>
<head>
	<title>search</title>
	<link rel="stylesheet" type="text/css" href="styles/global.css">
</head>

<body class="text" onload='setBallon("BallonTip");' background="images/fond.gif">
<!-- scripts pour les tooltips, scripts for the tooltips -->
<div id="BallonTip" style="POSITION:absolute; VISIBILITY:hidden; LEFT:-200px; Z-INDEX: 100" class="text"></div>
<script language="JavaScript" src="scripts/cross.js"></script>
<script language="JavaScript" src="scripts/tooltips.js"></script>
<script language="JavaScript">
	function send(p_ConfirmMessage, p_JobCode) {
		if (confirm(p_ConfirmMessage)) {
			var frm = document.forms[0];
			frm.job.value=p_JobCode;
			frm.submit();
			return false;
		}
		return true;
	}
	



</script>
	<img src="images/search-tile.gif" align="absmiddle">
	<span class="title">
		<bean:message key="job.newsletters.title"/>
	</span>
	<br>

	<%-- liste des tâches newsletters --%>
		<span class="title-list">&nbsp;</span>
		<html:form action="sendNewsLetters.x">
				<input type="hidden" name="job" />
		<table>
		<tr>
        		<td align="left" class="text">Site</td>
                <td align="left" class="text">
                	<html:select property="siteId" style="width:260px">
								<html:options collection="sites" labelProperty="label" property="value" />
					</html:select>
				</td>
        </tr>
	
        <c:if test="${userLogin=='admin'}">
		<tr>
        		<td align="left" class="text">&nbsp;</td>
                <td align="left" class="text">
                				( <img src="images/clef.gif" align="absmiddle">&nbsp;<a onclick="return send('Voulez-vous lancer la tâche NewsLettter Grand Public ?', 'GP');" href="#"><bean:message key="job.newsletters.gp"/></a> )
                </td>
        </tr>
		<tr>
        		<td align="left" class="text">&nbsp;</td>
                <td align="left" class="text">
								( <img src="images/clef.gif" align="absmiddle">&nbsp;<a onclick="return send('Voulez-vous lancer la tâche NewsLettter Adhérent ?', 'AD')" href="#"><bean:message key="job.newsletters.ad"/></a> )
                </td>
        </tr>
		<tr>
        		<td align="left" class="text">&nbsp;</td>
                <td align="left" class="text">
				( <img src="images/clef.gif" align="absmiddle">&nbsp;<a onclick="return send('Voulez-vous lancer la tâche NewsLettter Local ?', 'RE')" href="#"><bean:message key="job.newsletters.lo"/></a> )
                </td>
        </tr> 
        </c:if>        
		<tr>
        		<td align="left" class="text">&nbsp;</td>
                <td align="left" class="text">
				( <img src="images/clef.gif" align="absmiddle">&nbsp;<a onclick="return send('Voulez-vous lancer la tâche NewsLettter à la volée ?', 'VO')" href="#"><bean:message key="job.newsletters.vo"/></a> )
                </td>
        </tr> 
        </table>
        </html:form>
        
	<hr width="100%" size="1" noshade color="#949494">
</body>

</html>
