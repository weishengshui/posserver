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
<s:form action="sendURL" namespace="/unbind" method="Post" id="successForm">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="77%">已生成回收单！</td>
	</tr>
	<tr>
		<td width="77%">编号：<s:property value="#request.rnNumber" /></td>
	</tr>
	<tr>
		<td width="77%">POS机：<s:property value="#request.posCount" />台</td>
	</tr>
	<tr>
		<td width="77%"><input type="submit" value="完成" id="searchBtn" /></td>
	</tr>
</table>
</s:form>

</body>
</html>