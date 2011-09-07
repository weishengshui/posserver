<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%-- pagingToolbar --%>
<link rel="stylesheet" href="<s:url value='/styles/kernel/kernel.css'/>" type="text/css" />
<script type="text/javascript" src="<s:url value='/js/kernel/kernel.js'/>"></script>
</head>

<body>

<div id="queryDeliveryList_DIV_ID" align="center">
	<table align="center" width="800px">
		<tr>
			<td width="50%">交付单编号</td>
			<td width="20%">状态</td>
			<td width="30%">操作</td>
		</tr>
		<s:if test="#request.deliveryNoteVOList != null && #request.deliveryNoteVOList.size()>0">
		  <s:iterator id="deliveryNoteVO" value="#request.deliveryNoteVOList" status="i">
		  	<tr>
				<td>
					<s:property value="#deliveryNoteVO.dnNumber" />
				</td>
				<td>
					<s:if test="'PRINTED' == #deliveryNoteVO.status">
						已打印
					</s:if>
					<s:elseif test="'CONFIRMED' == #deliveryNoteVO.status">
						已确认
					</s:elseif>
					<s:else>
						草稿
					</s:else>
				</td>
				<td>
					<s:if test="#deliveryNoteVO.status == 'PRINTED' || #deliveryNoteVO.status == 'CONFIRMED'">
						<a target="_blank" href='<s:url value="/delivery/printDelivery"/>/<s:property value="#deliveryNoteVO.id"/>'>打印</a>
					</s:if>
					<s:if test="#deliveryNoteVO.status != 'PRINTED' && #deliveryNoteVO.status != 'CONFIRMED'">
						<a href='<s:url value="/delivery/deleteDelivery"/>/<s:property value="#deliveryNoteVO.id"/>'>删除</a>
					</s:if>
				</td>
			</tr>
		  </s:iterator>
	   </s:if>
	</table>
	
	<div id="pagingToolBar_ID" class="pagingToolbar"></div>
</div>

<%-- 加载分页条 --%>
<script type="text/javascript">
	Kernel.PagingToolBar.loadPagingToolBar('pagingToolBar_ID', '<s:property value="#request.currentPage"/>', 
			'<s:property value="#request.pageSize"/>', '<s:property value="#request.countTotal"/>', null, 
			'<s:property value="#request.urlTemplate"/>', '<s:property value="#request.urlMark"/>', 'AJAX', 'queryDeliveryList_DIV_ID');
</script>
</body>
</html>
