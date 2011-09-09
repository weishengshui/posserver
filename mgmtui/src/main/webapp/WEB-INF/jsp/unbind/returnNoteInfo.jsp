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
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
	<tr>
		<td>POS机回收单</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
			<tr>
				<td class="td_left">编号：</td>
				<td><s:property value="rnInfo.rn.rnNumber" /></td>
			</tr>
			<tr>
				<td class="td_left">生成日期：</td>
				<td><s:date name="rnInfo.rn.createDate" format="yyyy-MM-dd hh:mm:ss" /></td>
			</tr>
			<tr>
				<td class="td_left">第三方：</td>
				<td><s:property value="rnInfo.agent.name" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
			<tr>
				<td>posId</td>
				<td>sn</td>
				<td>simPhoneNo</td>
			</tr>
			<s:if test="rnInfo.rnDetailList!=null && rnInfo.rnDetailList.size()>0">
			<s:iterator value="rnInfo.rnDetailList" id="list">
			<tr>
				<td><s:property value="#list.posId" /></td>
				<td><s:property value="#list.sn" /></td>
				<td><s:property value="#list.simPhoneNo" /></td>
			</tr>
			</s:iterator>
			</s:if>
		</table>
		</td>
	</tr>
	<tr>
		<td>共<s:property value="rnInfo.rnDetailList.size()" />台</td>
	</tr>
	<tr>
		<td>签收：_____________________</td>
	</tr>
	<tr>
		<td>　　　(姓名：______________)</td>
	</tr>
	<tr>
		<td>日期：______年_____月______</td>
	</tr>
	<tr>
		<td><button type="button" onclick="printInfo()">打印</button></td>
	</tr>
</table>

<script type="text/javascript">

	function printInfo() {
		window.print();
	}
	
</script>
</body>
</html>
