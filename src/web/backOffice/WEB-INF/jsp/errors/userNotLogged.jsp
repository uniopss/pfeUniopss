<%@ page isErrorPage="true" %>
<html>
<head>
        <title>Erreur de session</title>
</head>
<body style="background :#FFFFFF" style="font-family:verdana;color:#cc0000;font-size:9px;font-weight:bold;">
<p>&nbsp;</p>
<center>
<table cellpadding="3" cellspacing="0" border="0" style="border : 1px solid #999;" width="413">
<tr style="background : #999;"><td style="color : #FFF;font-weight : bold;;font-size : 14px">Erreur de session</td></tr>
<tr style="background : #DEDEDE;"><td valign="top"><span style="line-height : 15px;text-align : justify;font-size : 12px">Votre session a expir�e.</span></td></tr>
</table>
</center>
<script language="JavaScript">
        if (parent && parent.document && parent.document!=document)
                parent.document.location.reload();
        if (top && top.opener && top.opener.document) {
                top.opener.document.location.reload();
                self.close();
        }
</script>
</body>
</html>

