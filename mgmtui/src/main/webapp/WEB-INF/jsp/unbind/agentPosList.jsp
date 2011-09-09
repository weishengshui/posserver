<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>填写申请单</title>
</head>
<body>
<s:if test="errorMsg!=null">
<b>${errorMsg}</b>
</s:if>
<s:form action="confirmRnNumber" namespace="/returnnote" method="Post" id="confirmForm">
<s:hidden name="agentId" id="agentId" />
<s:hidden name="agentName" id="agentName" />
<s:hidden name="posIds" id="posIds" />
<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
<s:hidden name="inviteCode" id="inviteCode" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="77%">第三方名称：<s:property value="agentName" /></td>
		<td width="13%"></td>
	</tr>
</table>
<s:if test="pageInfo.items!=null && pageInfo.items.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td></td>
		<td>posId</td>
		<td>simPhoneNo</td>
		<td>sn</td>
	</tr>
	<s:iterator value="pageInfo.items" id="list">
	<tr>
		<td><input type="checkbox" name="posId" value="<s:property value="#list.id" />" onclick="ckPosId(this)"/></td>
		<td><s:property value="#list.posId" /></td>
		<td><s:property value="#list.simPhoneNo" /></td>
		<td><s:property value="#list.sn" /></td>
	</tr>
	</s:iterator>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td><p:page pageInfo="${pageInfo}" /></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td><button type="button" onclick="confirmRnNumber()">回收</button></td>
	</tr>
</table>
</s:if>
</s:form>

<script type="text/javascript">
	
	$().ready(function() {
		autoCheckPos();
	});
	
	function autoCheckPos() {
		var posIds = document.getElementById("posIds").value;
		if ($.trim(posIds) != "") {
			$(":checkbox[name='posId']").each(function() {
				if (posIds.indexOf(this.value) != -1) {
					this.checked = true; 
				}
			});
		}
	}
	
	function confirmRnNumber() {
		var posIds = document.getElementById("posIds").value;
		if ($.trim(posIds) == "") {
			alert("请选择要回收的POS机!");
			return;
		}
		document.getElementById("posIds").value = posIds.substring(0,posIds.length-1);
		var formObj = document.getElementById("confirmForm");
		formObj.action = "${ctx}/returnnote/confirmRnNumber";
		formObj.submit();
	}
	
	function ckPosId(obj) {
		var posIds = document.getElementById("posIds").value;
		if (obj.checked) {
			posIds += obj.value + ",";
		} else {
			posIds = posIds.replace(new RegExp(obj.value + ",","gm"), "");
		}
		document.getElementById("posIds").value = posIds;
		alert(document.getElementById("posIds").value);
	}
	
	function goPage(pageId) {
		var formObj = document.getElementById("confirmForm");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${ctx}/returnnote/goPage";
		formObj.submit();
	}
	
</script>
</body>
</html>