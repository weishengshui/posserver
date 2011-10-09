<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

</head>
<body>

	<table align="center" width="800px">
		<tr>
			<td><s:form id="search_excel" namespace="/finance" action="search_excel" method="GET" theme="simple">
				<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
				<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
				代理商：
					<s:select name="searchVO.agentId" list="agent" />&nbsp;&nbsp;
				开始时间：
					<s:textfield name="searchVO.startDate" cssClass="date" id="startDate"/>&nbsp;&nbsp;
				结束时间：
					<s:textfield name="searchVO.endDate" cssClass="date" id="endDate"/>&nbsp;
				状态：
					<s:select name="searchVO.financeReportHistoryStatus" list="status" />
				<input type="submit" value="查询" />
				</s:form></td>
		</tr>
		<tr>
			<td>
				<div id="excelList_DIV_ID">
					<table align="center" width="800px" border="1">
						<tr align="center">
							<td>文件名称</td>
							<td>代理商</td>
							<td>状态</td>
							<td>下载</td>
						</tr>
						<s:if test="pageInfo.items != null && pageInfo.items.size()>0">
							<s:iterator id="excelVO" value="pageInfo.items" status="i">
								<tr align="center">
									<td>
									<s:if test="#excelVO.startDate == null">
										截止于
									</s:if>
									<s:else>
										<s:property value="#excelVO.startDate" />~
									</s:else>
									<s:property value="#excelVO.endDate" />
									</td>
									<td><s:property value="#excelVO.agentName" />
									</td>
									<td>
									<s:if test="#excelVO.status == 'CREATING'">
										文件生成中
									</s:if>
									<s:elseif test="#excelVO.status == 'COMPLETION'">
										文件已生成
									</s:elseif>
									<s:elseif test="#excelVO.status == 'FAILED'">
										文件生成失败
									</s:elseif>
									<s:else>
										状态未知
									</s:else>
									</td>
									<td><input type="button" value="下载"/>
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<tr>
							<td colspan="9" class="td_pageInfo"><p:page pageInfo="${pageInfo}" /></td>
						</tr>
					</table>
				</div></td>
		</tr>
		<tr>
			<td align="left"><input type="button" onclick="" value="生成Excel" >
			</td>
		</tr>
	</table>
	<script type="text/javascript">
	function goPage(pageId) {
		var formObj = document.getElementById("search_excel");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/finance/search_excel";
		formObj.submit();
	}
	</script>
</body>
</html>
