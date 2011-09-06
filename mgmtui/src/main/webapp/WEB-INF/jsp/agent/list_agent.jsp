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

<div id="queryAgentList_DIV_ID" align="center">
	<table align="center" width="800px">
		<tr>
			<td width="50%">名称</td>
			<td width="50%">Email</td>
			<%-- 
			<td>操作</td>
			 --%>
		</tr>
		<s:if test="#request.agentVOList != null && #request.agentVOList.size()>0">
		  <s:iterator id="agentVO" value="#request.agentVOList" status="i">
		  	<tr>
				<td>
					<a href='<s:url value="/agent/showEditAgent"/>?agentId=<s:property value="#agentVO.id"/>'><s:property value="#agentVO.name" /></a>
				</td>
				<td><s:property value="#agentVO.email" /></td>
				<%-- 
				<td>
					<a href='<s:url value="/agent/deleteAgent"/>?agentId=<s:property value="#agentVO.id"/>'>删除</a>
				</td>
				 --%>
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
			'<s:property value="#request.urlTemplate"/>', '<s:property value="#request.urlMark"/>', 'AJAX', 'queryAgentList_DIV_ID');
</script>
</body>
</html>


	