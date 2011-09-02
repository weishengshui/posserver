<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<s:property value="agentName" />
<br>
<s:form action="sendURL" namespace="/unbind" method="Post" id="sendURLForm">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="77%">第三方名称：<input type="text" name="agentName" value="${agentName}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="agentList!=null && agentList.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>第三方ID</td>
		<td>第三方名称</td>
		<td>操作</td>
	</tr>
	<s:iterator value="agentList" var="list">
	<tr>
		<td><s:property value="#list.id" /></td>
		<td><s:property value="#list.name" /></td>
		<td><button type="button" onclick="sendURL()">发送</button></td>
	</tr>
	</s:iterator>
</table>
</s:if>
</s:form>

<script type="text/javascript">
	function sendURL() {
		var formObj = document.getElementById("sendURLForm");
		formObj.action = "/qqgbvpnui/unbind/sendURL";
		formObj.submit();
	}
</script>
</body>
</html>