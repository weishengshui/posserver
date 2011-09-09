<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邀请第三方填写申请表</title>
</head>
<body>
<s:if test="errorMsg!=null">
<b>${errorMsg}</b>
</s:if>
<s:form action="sendURL" namespace="/unbind" method="Post" id="sendURLForm">
<s:token/>
<input type="hidden" id="agentId" name="agentId"/>
<input type="hidden" id="agentEmail" name="agentEmail"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td width="77%">第三方名称：<input type="text" id="agentName" name="agentName" value="${agentName}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="agentList!=null && agentList.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td class="td_title">Agent Id</td>
		<td class="td_title">Agent Name</td>
		<td class="td_title">Agent Email</td>
		<td class="td_title">操作</td>
	</tr>
	<s:iterator value="agentList" id="list">
	<tr>
		<td><s:property value="#list.id" /></td>
		<td><s:property value="#list.name" /></td>
		<td><s:property value="#list.email" /></td>
		<td><button type="button" onclick="sendURL('${list.id}', '${list.name}','${list.email}')">发送</button></td>
	</tr>
	</s:iterator>
</table>
</s:if>
</s:form>

<script type="text/javascript">
	function sendURL(agentId, agentName, email) {
		document.getElementById("agentId").value = agentId;
		document.getElementById("agentName").value = agentName;
		document.getElementById("agentEmail").value = email;
		var formObj = document.getElementById("sendURLForm");
		formObj.action = "${ctx}/unbind/createInvite";
		formObj.submit();
	}
</script>
</body>
</html>