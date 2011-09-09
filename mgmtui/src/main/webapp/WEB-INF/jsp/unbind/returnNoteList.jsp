<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>回收单查询</title>
</head>
<body>
<s:form action="getReturnNoteList" namespace="/unbind" method="Post" id="listForm">
<s:token/>
<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td width="77%">回收单号：<input type="text" name="rnNum" value="${rnNum}" /></td>
		<td width="13%">
			<input type="submit" value="查询" id="searchBtn" />
		</td>
	</tr>
</table>
<s:if test="pageInfo.items!=null && pageInfo.items.size()>0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td class="td_200 td_title">回收单号</td>
		<td class="td_200 td_title">第三方</td>
		<td class="td_left td_title">状态</td>
		<td class="td_left td_title">生成时间</td>
	</tr>
	<s:iterator value="pageInfo.items" id="list">
	<tr>
		<td><a href="${ctx}/unbind/getReturnNoteInfo?rnId=${list.id}" target="_blank"><s:property value="#list.rnNumber" /></a></td>
		<td><s:property value="#list.agentName" /></td>
		<td><s:property value="#list.status" /></td>
		<td><s:date name="#list.createDate" format="%{getText('dateformat.ymdhm')}" /></td>
	</tr>
	</s:iterator>
	<tr>
		<td colspan="4" class="td_pageInfo"><p:page pageInfo="${pageInfo}" /></td>
	</tr>
</table>
</s:if>
</s:form>

<script type="text/javascript">

	function goPage(pageId) {
		var formObj = document.getElementById("listForm");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${ctx}/unbind/goPageForRnList";
		formObj.submit();
	}
	
</script>
</body>
</html>
