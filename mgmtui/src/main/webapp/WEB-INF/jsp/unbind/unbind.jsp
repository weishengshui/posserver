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
<s:form action="posSearch" namespace="/unbind" method="Get" id="unbindForm">
<s:hidden name="posId" id="posId" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td width="77%">POS机ID/SIM卡/SN：<input type="text" name="posCondition" value="${posCondition}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="posVOList!=null && posVOList.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td class="td_title">POS机编号</td>
		<td class="td_title">厂商编号</td>
		<td class="td_title">型号</td>
		<td class="td_title">电机号码</td>
		<td class="td_title">交付状态</td>
		<td class="td_title">运营状态</td>
		<td class="td_title">第三方名称</td>
		<td class="td_title">操作</td>
	</tr>
	<s:iterator value="posVOList" id="list">
	<tr>
		<td><s:property value="#list.posId" /></td>
		<td><s:property value="#list.sn" /></td>	<%-- 厂商编号 --%>
		<td><s:property value="#list.model" /></td>		<%-- 型号 --%>
		<td><s:property value="#list.simPhoneNo" /></td>
		<td>
			<s:if test="#list.dstatus != null && #list.dstatus == 'DELIVERED'">
				已交付
			</s:if>
			<s:elseif test="#list.dstatus != null && #list.dstatus == 'RETURNED'">
				已回收
			</s:elseif>	
		</td>
		<td>
			<s:if test="#list.ostatus != null && #list.ostatus == 'ALLOWED'">
				允许
			</s:if>
			<s:elseif test="#list.ostatus != null && #list.ostatus == 'STOPPED'">
				禁止
			</s:elseif>	
		</td>
		<td><s:property value="#list.deliveryAgent" /></td>
		<td><button type="button" onclick="unbind('<s:property value="#list.posId" />')">解绑</button></td>
	</tr>
	</s:iterator>
</table>
</s:if>
</s:form>

<script type="text/javascript">
	function unbind(posId) {
		if (confirm("确定要解除POS机：\"" + posId + "\"的绑定关系吗？")) {
			document.getElementById("posId").value = posId;
			var formObj = document.getElementById("unbindForm");
			formObj.action = "${pageContext.request.contextPath}/unbind/unbind";
			formObj.submit();
		}
	}
</script>
</body>
</html>
