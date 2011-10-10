<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
<link rel="stylesheet"
	href="<s:url value='/styles/jquery-ui/jquery-ui-1.8.2.custom.css' />"
	type="text/css" />
<script type="text/javascript"
	src="<s:url value='/js/jquery/jquery-ui-i18n.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/js/jquery/jquery-ui-1.8.2.custom.min.js' />"></script>
</head>
<body>

	<table align="center" width="800px" class="table_style">
		<tr>
			<td><s:form id="search_excel" namespace="/finance" action="search_excel" method="GET" theme="simple">
				<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
				<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
				<input type="hidden" id="fileName" name="fileName" />
				<input type="hidden" id="reportId" name="reportId" />
				代理商：
					<s:select name="searchVO.agentId" list="agent" />&nbsp;
				开始时间：
					<s:textfield name="searchVO.startDate" cssClass="date" id="startDate" readonly="true">
						<s:param name="value"><s:date name="searchVO.startDate" format="yyyy-MM-dd" /></s:param>
					</s:textfield>
					&nbsp;
				结束时间：
					<s:textfield name="searchVO.endDate" cssClass="date" id="endDate" readonly="true">
						<s:param name="value"><s:date name="searchVO.endDate" format="yyyy-MM-dd" /></s:param>
					</s:textfield>
					&nbsp;
				状态：
					<s:select name="searchVO.financeReportHistoryStatus" list="status" />
				<input type="submit" value="查询" />
				</s:form></td>
		</tr>
		<tr>
			<td style="font-size: 12px;font-weight:bold;color:red;">*提示：您可以刷新页面或点击查询来更新报表状态</td>
		</tr>
		<tr>
			<td>
				<div id="excelList_DIV_ID">
					<table align="center" width="800px" border="1" style="border-collapse: collapse;margin-top:10px;">
						<tr align="center">
							<td>文件名称</td>
							<td>代理商</td>
							<td>状态</td>
							<td>创建时间</td>
							<td>生成时间</td>
							<td>下载</td>
						</tr>
						<s:if test="pageInfo.items != null && pageInfo.items.size()>0">
							<s:iterator id="excelVO" value="pageInfo.items" status="i">
								<tr align="center">
									<td id="file_name_${excelVO.id}" style="text-align:left;padding-left:10px;">
									<s:if test="#excelVO.startDate == null">
										截止于
									</s:if>
									<s:else>
										<s:date name="#excelVO.startDate" format="yyyy-MM-dd" />~
									</s:else>
									<s:date name="#excelVO.endDate" format="yyyy-MM-dd" />.csv
									</td>
									<td>
									<s:if test="#excelVO.agentName != null">
										<s:property value="#excelVO.agentName" />
									</s:if>
									<s:else>
										全部
									</s:else>
									</td>
									<td>
									<s:if test="#excelVO.status != null && 'CREATING' == #excelVO.status.toString()">
										文件生成中
									</s:if>
									<s:elseif test="#excelVO.status != null && 'COMPLETION' == #excelVO.status.toString()">
										文件已生成
									</s:elseif>
									<s:elseif test="#excelVO.status != null && 'FAILED' == #excelVO.status.toString()">
										文件生成失败
									</s:elseif>
									<s:else>
										状态未知
									</s:else>
									</td>
									<td><s:date name="#excelVO.createDate" format="%{getText('dateformat.ymdhm')}" />
									</td>
									<td><s:date name="#excelVO.modifyDate" format="%{getText('dateformat.ymdhm')}" />
									</td>
									<td>
									<s:if test="#excelVO.status != null && 'CREATING' == #excelVO.status.toString()">
										<input type="button" value="下载" disabled="true"/>
									</s:if>
									<s:elseif test="#excelVO.status != null && 'COMPLETION' == #excelVO.status.toString()">
										<input type="button" value="下载"  onclick="download('${excelVO.id}','<s:date name="#excelVO.startDate" format="yyyy-MM-dd" />','<s:date name="#excelVO.endDate" format="yyyy-MM-dd" />')"/>
									</s:elseif>
									<s:elseif test="#excelVO.status != null && 'FAILED' == #excelVO.status.toString()">
										<input type="button" value="显示错误信息" onclick='showError("${excelVO.reportDetail}")'/>
									</s:elseif>
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
	</table>
	<script type="text/javascript">
	initializeDatepicker("startDate","endDate");
	function initializeDatepicker(idFrom, idTo) {
		var dates = $('#' + idFrom + ', ' + '#' + idTo).datepicker({
			showAnim: "",
			dateFormat: "yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			onSelect: function(selectedDate) {
				var instance = $(this).data("datepicker");
				var date = $.datepicker.parseDate(instance.settings.dateFormat || 
						$.datepicker._defaults.dateFormat, selectedDate, instance.settings);
				dates.not('#' + idFrom).datepicker("option", "minDate", date);
			}
		});
	}
	
	function goPage(pageId) {
		var formObj = document.getElementById("search_excel");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/finance/search_excel";
		formObj.submit();
	}
	
	function download(reportId,startDate,endDate){
		var fileName = "";
		if (startDate != null && $.trim(startDate) != "") {
			fileName += startDate + "~";
		} else {
			fileName += "截止于";
		}
		fileName += endDate + ".csv";
		window.location.href = "${pageContext.request.contextPath}/finance/download_excel?reportId=" + reportId + "&fileName=" + encodeURIComponent(fileName);
	}
	
	function showError(errorMsg) {
		alert(errorMsg);
	}
	</script>
</body>
</html>
