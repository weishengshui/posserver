<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>解绑</title>
</head>
<body>
<s:if test="successMsg!=null">
<b>${successMsg}</b>
</s:if>
<s:if test="errorMsg!=null">
<b>${errorMsg}</b>
</s:if>
<s:form action="posSearch" namespace="/unbind" method="Post" id="unbindForm">
<s:token/>
<s:hidden name="posId" id="posId" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="77%">POS机ID/SIM卡/SN：<input type="text" name="posCondition" value="${posCondition}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="posList!=null && posList.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>posId</td>
		<td>simPhoneNo</td>
		<td>sn</td>
		<td>操作</td>
	</tr>
	<s:iterator value="posList" id="list">
	<tr>
		<td><s:property value="#list.posId" /></td>
		<td><s:property value="#list.simPhoneNo" /></td>
		<td><s:property value="#list.sn" /></td>
		<td><button type="button" onclick="unbind('<s:property value="#list.posId" />')">解绑</button></td>
	</tr>
	</s:iterator>
</table>
</s:if>
</s:form>

<script type="text/javascript">
	function unbind(posId) {
		document.getElementById("posId").value = posId;
		var formObj = document.getElementById("unbindForm");
		formObj.action = "${ctx}/unbind/unbind";
		formObj.submit();
	}
</script>
</body>
</html>