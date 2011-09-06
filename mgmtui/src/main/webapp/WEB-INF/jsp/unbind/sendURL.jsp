<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<s:if test="errorMsg!=null">
<b>${errorMsg}</b>
</s:if>
<s:form action="sendURL" namespace="/unbind" method="Post" id="sendURLForm">
<s:hidden name="agentId" id="agentId" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="77%">第三方名称：<input type="text" name="agentName" value="${agentName}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="agent!=null && !''.equals(agent.id)">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>Agent Id</td>
		<td>Agent Name</td>
		<td>Agent Email</td>
		<td>操作</td>
	</tr>
	<tr>
		<td><s:property value="agent.id" /></td>
		<td><s:property value="agent.name" /></td>
		<td><s:property value="agent.email" /></td>
		<td><button type="button" onclick="sendURL()">发送</button></td>
	</tr>
</table>
</s:if>
</s:form>

<script type="text/javascript">
	function sendURL() {
		var formObj = document.getElementById("sendURLForm");
		formObj.action = "${ctx}/unbind/createRnNumber";
		formObj.submit();
	}
</script>
</body>
</html>