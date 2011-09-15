<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我方生成回收单</title>
</head>
<body>
<s:if test="errorMsg!=null">
<b>${errorMsg}</b>
</s:if>
<s:form action="search" namespace="/unbind" method="Get" id="listForm">
<s:hidden name="agentId" id="agentId" />
<s:hidden name="posIds" id="posIds" />
<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
<s:hidden name="rnId" id="rnId" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td width="77%">第三方名称：<input type="text" name="agentName" value="${agentName}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="pageInfo.items!=null && pageInfo.items.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td></td>
		<td class="td_title">POS机编号</td>
		<td class="td_title">厂商编号</td>
		<td class="td_title">型号</td>
		<td class="td_title">电机号码</td>
		<td class="td_title">交付状态</td>
	</tr>
	<s:iterator value="pageInfo.items" id="list">
	<tr>
		<td><input type="checkbox" name="posId" value="<s:property value="#list.id" />" onclick="ckPosId(this)"/></td>
		<td><s:property value="#list.posId" /></td>
		<td><s:property value="#list.model" /></td>
		<td><s:property value="#list.sn" /></td>
		<td><s:property value="#list.simPhoneNo" /></td>
		<td>
			<s:if test="#list.dstatus != null && #list.dstatus.toString() == 'DELIVERED'">
				已交付
			</s:if>
			<s:elseif test="#list.dstatus != null && #list.dstatus.toString() == 'RETURNED'">
				已回收
			</s:elseif>	
		</td>
	</tr>
	</s:iterator>
	<tr>
		<td colspan="6" class="td_pageInfo"><p:page pageInfo="${pageInfo}" /></td>
	</tr>
	<tr>
		<td colspan="6" class="td_pageInfo"><button type="button" onclick="confirmAllRnNumber()">全部回收</button>　　<button type="button" onclick="confirmRnNumber()">回收</button></td>
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
		if (confirm("确定要回收以选中的POS机吗？")) {
			document.getElementById("posIds").value = posIds.substring(0,posIds.length-1);
			var formObj = document.getElementById("listForm");
			formObj.action = "${pageContext.request.contextPath}/unbind/confirmRnNumber";
			formObj.submit();
		}
	}
	
	function confirmAllRnNumber() {
		if (confirm("确定要回收所有的POS机吗？")) {
			var formObj = document.getElementById("listForm");
			formObj.action = "${pageContext.request.contextPath}/unbind/confirmAllRnNumber";
			formObj.submit();
		}
	}
	
	function ckPosId(obj) {
		var posIds = document.getElementById("posIds").value;
		if (obj.checked) {
			posIds += obj.value + ",";
		} else {
			posIds = posIds.replace(new RegExp(obj.value + ",","gm"), "");
		}
		document.getElementById("posIds").value = posIds;
	}
	
	function goPage(pageId) {
		var formObj = document.getElementById("listForm");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/unbind/goPage";
		formObj.submit();
	}
	
</script>
</body>
</html>
