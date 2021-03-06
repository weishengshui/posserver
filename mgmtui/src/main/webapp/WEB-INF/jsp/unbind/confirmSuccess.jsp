<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生成回收单成功</title>
</head>
<body>
<s:if test="errorMsg!=null">
<b>${errorMsg}</b>
</s:if>
<s:form action="list" namespace="/unbind" method="Get" id="successForm">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td width="77%">已生成回收单！</td>
	</tr>
	<tr>
		<td width="77%">编号：<s:property value="rnNum" /></td>
	</tr>
	<tr>
		<td width="77%">POS机：<s:property value="posCount" />台</td>
	</tr>
	<tr>
		<td width="77%">发出邀请时间：<s:date name="rnTime" format="%{getText('dateformat.ymdhm')}" /></td>
	</tr>
	<s:if test="#request.isAgent!=null && #request.isAgent == 'true'">
	<tr>
		<td width="77%"><button type="button" onclick="closeWindow()">完成</button></td>
	</tr>
	</s:if>
	<s:else>
	<tr>
		<td width="77%"><button type="button" onclick="goPrint('${rnId}')">打印</button>　　<input type="submit" value="完成" id="searchBtn" /></td>
	</tr>
	</s:else> 
</table>
</s:form>

<s:form action="getReturnNoteInfo" namespace="/unbind" method="Get" id="printForm">
<input type="hidden" id="rnId" name="rnId" />
</s:form>
<script type="text/javascript">
	function closeWindow() {
		window.close(); 
	}
	
	function goPrint(rnId) {
		document.getElementById("rnId").value = rnId;
		var formObj = document.getElementById("printForm");
		formObj.action = "${pageContext.request.contextPath}/unbind/getReturnNoteInfo";
		formObj.submit();
	}
</script>
</body>
</html>