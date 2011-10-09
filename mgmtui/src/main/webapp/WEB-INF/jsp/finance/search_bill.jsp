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
	<s:form action="generate_excel" namespace="/finance" method="Get" id="generateexcel" theme="simple">
		<s:hidden name="searchVO.agentId" id="searchVO.agentId"/>
		<s:hidden name="searchVO.startDate" id="searchVO.startDate"/>
		<s:hidden name="searchVO.endDate" id="searchVO.endDate"/>
	</s:form>
		<tr>
			<td><s:form id="search_bill" namespace="/finance" action="search_bill_paging" method="GET" theme="simple">
				<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
				<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
				<input type="hidden" name="searchVO.agentName" id="agentName"/>
				
				代理商：
					<s:select name="searchVO.agentId" list="agent" id="agent_select"/>&nbsp;&nbsp;
				开始时间：
					<s:textfield name="searchVO.startDate" cssClass="date" id="startDate"/>&nbsp;&nbsp;
				结束时间：
					<s:textfield name="searchVO.endDate" cssClass="date" id="endDate"/>&nbsp;
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
									<td><s:property value="#billVO.reportMonth" />
									</td>
									<td><s:property value="#billVO.agentName" />
									</td>
									<td><s:property value="#billVO.posId" />
									</td>
									<td><s:property value="#billVO.baseAmount" />
									</td>
									<td><s:property value="#billVO.actuallyValCount" />
									</td>
									<td><s:property value="#billVO.beyondValCount" />
									</td>
									<td><s:property value="#billVO.unitPrice" />
									</td>
									<td><s:property value="#billVO.beyondAmount" />
									</td>
									<td><s:property value="#billVO.amount" />
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
			<td align="left"><input type="button" onclick="generate_excel();" value="生成Excel" >
			</td>
		</tr>
	</table>
	<script type="text/javascript">
	var is_generate = '<s:property value="#request.generate"/>';
	if(is_generate =="false"){
		alert("没有账单记录不能生成Excel文件！");
	}
	
	initializeDatepicker("startDate","endDate");
	function initializeDatepicker(idFrom, idTo) {
		var dates = $('#' + idFrom + ', ' + '#' + idTo).datepicker({
			showAnim: "",
			dateFormat: "yyyy-mm-dd",
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
		var formObj = document.getElementById("search_bill");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/finance/search_bill_paging";
		formObj.submit();
	}
	
	function generate_excel(){
		/* var agent_id = document.getElementById("searchVO.agentId").value;
		var start_date = document.getElementById("searchVO.startDate").value;
		var end_date = document.getElementById("searchVO.endDate").value;
		var pageInfo_pageId = document.getElementById("pageInfo.pageId").value;
		var pageInfo_pageSize = document.getElementById("pageInfo.pageSize").value;
		window.location.href="${pageContext.request.contextPath}/finance/generate_excel?searchVO.agentId="+agent_id+
				"&searchVO.startDate="+start_date+"&searchVO.endDate="+end_date+"&pageInfo.pageId="+pageInfo_pageId+"&pageInfo.pageSize="+pageInfo_pageSize; */
		var formObj = document.getElementById("generateexcel");
				
		if(check_start_date && check_end_date){
			formObj.submit();	
		}
	}
	
	var pattern = /^(\d{4})-(\d{2})-(\d{2})$/;
	
	function check_start_date(){
		if($("#startDate").val == null || $("#startDate").val == "")return false;
		if(!pattern.exec($("#startDate").val)){
			alert('请输入正确的开始时间 /n 例如2011-01-01');
			return false;
		}
		return true;
	}
	function check_end_date(){
		if($("#endDate").val == null || $("#endDate").val == "")return false;
		if(!pattern.exec($("#endDate").val)){
			alert('请输入正确的结束时间 /n 例如2011-01-01');
			return false;
		}
		return true;
	}
	$("#startDate").change(function (){
		check_start_date();
	});
	$("#endDate").change(function (){
		check_end_date();
	});
	
	$("#agent_select").change(function(){
		 $("#agent_select option:selected").each(function () {
             $("#agentName").val($(this).text());
           });

	});
	</script>
</body>
</html>
