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

	<table align="center" width="800px">
		<tr>
			<td><s:form id="search_bill" namespace="/finance" action="search_bill" method="GET">
					<!--  
				<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
				<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
				-->
				代理商：
					<input type="text" name="" />&nbsp;&nbsp;
				开始时间：
					<input type="text" name="dateFrom" cssClass="date" id="dateFrom"/>&nbsp;&nbsp;
				结束时间：
					<input type="text" name="dateTo" cssClass="date" id="dateTo"/>&nbsp;
				<input type="submit" value="查询" />
				</s:form></td>
		</tr>
		<tr>
			<td>
				<div id="billList_DIV_ID">
					<table align="center" width="800px" border="1">
						<tr align="center">
							<td>月份</td>
							<td>代理商名称</td>
							<td>POS机编号</td>
							<td>基本运营费a</td>
							<td>实际验证个数</td>
							<td>超额验证个数b</td>
							<td>单价c</td>
							<td>超额运营费d=b*c</td>
							<td>运营费用e=a+d</td>
							<%-- 
							<td>操作</td>
							 --%>
						</tr>
						<s:if test="pageInfo.items != null && pageInfo.items.size()>0">
							<s:iterator id="billVO" value="pageInfo.items" status="i">
								<tr align="center">
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
									<td><s:property value="" />
									</td>
								</tr>
							</s:iterator>
						</s:if>
					</table>
				</div></td>
		</tr>
		<tr>
			<td align="left"><a href='<s:url value="/agent/showEditAgent"/>'>生成Excel</a>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
	initializeDatepicker("dateFrom","dateTo");
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
	</script>
</body>
</html>
