<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
</head>
<body>



<table align="center" width="800px">
	<tr>
		<td>
			<form id="searchAgent_FORM_ID" action="<s:url value='/agent/searchAgent'/>" method="GET">
				<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
				<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
			
				名称：<input type="text" name="agentSearchVO.agentName" value="<s:property value='#request.agentSearchVO.agentName'/>"/>&nbsp;&nbsp;&nbsp;
				<input type="submit" value="查询" />
			</form>
		</td>
	</tr>
	<tr>
		<td>
			<div id="agentList_DIV_ID">
					<table align="center" width="800px">
						<tr>
							<td width="50%">名称</td>
							<td width="50%">Email</td>
							<%-- 
							<td>操作</td>
							 --%>
						</tr>
						<s:if test="pageInfo.items != null && pageInfo.items.size()>0">
						  <s:iterator id="agentVO" value="pageInfo.items" status="i">
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
					<p:page pageInfo="${pageInfo}" />
			</div>
		</td>
	</tr>
	<tr>
		<td align="left">
			<a href='<s:url value="/agent/showEditAgent"/>'>+新增</a>
		</td>
	</tr>
</table>


<script type="text/javascript">
	function goPage(pageId) {
		var formObj = document.getElementById("searchAgent_FORM_ID");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/agent/searchAgent";
		formObj.submit();
	}
</script>

</body>
</html>
