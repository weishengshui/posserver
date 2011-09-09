<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>回收单信息</title>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>POS机回收单</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>编号：</td>
				<td>aaaaaaaaaaa</td>
			</tr>
			<tr>
				<td>生成日期：</td>
				<td>1245665</td>
			</tr>
			<tr>
				<td>第三方：</td>
				<td>1245665</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>posId</td>
				<td>sn</td>
				<td>simPhoneNo</td>
			</tr>
			<tr>
				<td>123123123</td>
				<td>123123123</td>
				<td>12312312</td>
			</tr>
			<tr>
				<td>123123123</td>
				<td>123123123</td>
				<td>12312312</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

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
