<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<script type="text/javascript">
	function deliveryPrint(deliveryId){
		window.location.href = '<s:url value="/delivery/printDelivery"/>/'+deliveryId;
	}
	
	function backToList(){
		window.location.href = '<s:url value="/delivery/searchDelivery"/>';
	}
</script>
</head>
<body>

<table align="center" width="100%" border="0">
	<tr height="50">
		<td colspan="5">
			交付单已确定!
		</td>
	</tr>
	<tr height="50">
		<td colspan="2">
			交付单编号：
		</td>
		<td colspan="1" align="left">
			<s:property value="#request.deliveryNoteVO.dnNumber"/>
		</td>
		<td colspan="2"/>
	</tr>
	<tr height="50">
		<td colspan="5">
			POS机数量：
			<s:if test="#request.deliveryNoteDetailVOList != null">
				<s:property value="#request.deliveryNoteDetailVOList.size()"/>
			</s:if>
			<s:else>0</s:else>
			台
		</td>
	</tr>
	<tr>
		<td colspan="5" align="center">
			<input type="button" value="打印" onclick="deliveryPrint('<s:property value="#request.deliveryNoteVO.id"/>');" />&nbsp;&nbsp;&nbsp;
			<input type="button" value="返回列表" onclick="backToList();" />
		</td>
	</tr>
</table>

</body>
</html>
