<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<script type="text/javascript">
	function initDeliveryStatus(deliveryStatus){
		var opts = document.getElementById('deliveryStatusList_ID').options;
		for(i=0;i<opts.length;i++){
			if(opts[i].value == deliveryStatus){
				opts[i].selected = true;
				return;
			}
		}
	}
	
	function initDeliveryAgent(deliveryAgentId){
		var opts = document.getElementById('deliveryAgentList_ID').options;
		for(i=0;i<opts.length;i++){
			if(opts[i].value == deliveryAgentId){
				opts[i].selected = true;
				return;
			}
		}
	}
</script>

</head>
<body>

<table align="center" width="800px">
	<tr>
		<td>
			<s:form id="searchDelivery_FORM_ID" namespace="/delivery" action="searchDelivery" method="GET">
				<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
				<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
			<table>
				<tr>
					<td>交付单编号： <input type="text" name="deliverySearchVO.dnNumber"
						value="<s:property value='#request.deliverySearchVO.dnNumber'/>" />&nbsp;&nbsp;&nbsp;
					</td>
					<td>交付单状态： <select id="deliveryStatusList_ID"
						name="deliverySearchVO.status">
						<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
						<option value="DRAFT">草稿</option>
						<option value="CONFIRMED">已确定</option>
						<option value="PRINTED">已打印</option>
					</select>&nbsp;&nbsp;&nbsp;</td>
					<td>第三方： <select id="deliveryAgentList_ID"
						name="deliverySearchVO.agentId">
						<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
						<s:iterator id="agentVO" value="#request.agentVOList" status="i">
							<option value="<s:property value='#agentVO.id'/>"><s:property
								value="#agentVO.name" /></option>
						</s:iterator>
					</select>&nbsp;&nbsp;&nbsp;</td>
					<td><input type="submit" value="查询" /></td>
				</tr>
			</table>
		</s:form>
		</td>
	</tr>
	<tr>
		<td>
			<div id="DATA_List_DIV_ID">
				<div id="queryDeliveryList_DIV_ID" align="center">
					<table align="center" width="800px">
						<tr>
							<td width="50%">交付单编号</td>
							<td width="20%">状态</td>
							<td width="30%">操作</td>
						</tr>
						<s:if test="pageInfo.items != null && pageInfo.items.size()>0">
						  <s:iterator id="deliveryNoteVO" value="pageInfo.items" status="i">
						  	<tr>
								<td>
									<s:a namespace="/delivery" action="showAddPosForDelivery">
										<s:param name="deliveryId" value="#deliveryNoteVO.id"/>
										<s:property value="#deliveryNoteVO.dnNumber" />
									</s:a>
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
										<a href="javascript:void(0);" onclick="deleteDelivery('<s:url value="/delivery/deleteDelivery"/>/<s:property value="#deliveryNoteVO.id"/>'); return false;">删除</a>
									</s:if>
								</td>
							</tr>
						  </s:iterator>
					   </s:if>
					</table>
					
					<p:page pageInfo="${pageInfo}" />
				</div>
			</div>
		</td>
	</tr>
	<tr>
		<td align="left">
			<a href='<s:url value="/delivery/createDeliveryNote"/>'>+新增</a>
		</td>
	</tr>
</table>

<script type="text/javascript">
	function deleteDelivery(url){
		var res = window.confirm("是否要删除该交付单，删除后将不能恢复?");
		if(res == true){
			window.location.href = url;
		}
	}
</script>

<script type="text/javascript">
	function goPage(pageId) {
		var formObj = document.getElementById("searchDelivery_FORM_ID");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/delivery/searchDelivery";
		formObj.submit();
	}
</script>

<!-- init -->
<script type="text/javascript">
	initDeliveryStatus('<s:property value="#request.deliverySearchVO.status"/>');
	initDeliveryAgent('<s:property value="#request.deliverySearchVO.agentId"/>');
</script>

</body>
</html>
