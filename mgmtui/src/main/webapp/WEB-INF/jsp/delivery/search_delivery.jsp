<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
			<s:form  namespace="/delivery" action="searchDelivery" method="GET">
				交付单编号：
				<input type="text" name="deliverySearchVO.dnNumber" value="<s:property value='#request.deliverySearchVO.dnNumber'/>"/>&nbsp;&nbsp;&nbsp;
				交付单状态：
				<select id="deliveryStatusList_ID" name="deliverySearchVO.status">
					<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					<option value="DRAFT">草稿</option>
					<option value="CONFIRMED">已确定</option>
					<option value="PRINTED">已打印</option>
				</select>&nbsp;&nbsp;&nbsp;
				第三方：
				<select id="deliveryAgentList_ID" name="deliverySearchVO.agentId" >
					<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					<s:iterator id="agentVO" value="#request.agentVOList" status="i">
						<option value="<s:property value='#agentVO.id'/>">
							<s:property value="#agentVO.name"/>
						</option>
					</s:iterator>
				</select>&nbsp;&nbsp;&nbsp;
				<input type="submit" name="submit" value="查询" />
			</s:form>
		</td>
	</tr>
	<tr>
		<td>
			<div id="DATA_List_DIV_ID">
				数据加载中...
			</div>
		</td>
	</tr>
	<tr>
		<td align="left">
			<a href='<s:url value="/delivery/createDeliveryNote"/>'>+新增</a>
		</td>
	</tr>
</table>

<%-- 表单内容 --%>
<div id="form_DIV_ID" style="display: none;">
	<s:hidden id="currentPage_ID" name="currentPage" />
	<s:hidden id="pageSize_ID" name="pageSize" />
	<s:hidden id="deliverySearchVO.dnNumber_ID" name="deliverySearchVO.dnNumber"/>
	<s:hidden id="deliverySearchVO.status_ID" name="deliverySearchVO.status"/>
	<s:hidden id="deliverySearchVO.agentId_ID" name="deliverySearchVO.agentId"/>
</div>


<script type="text/javascript">
	function loadDeliveryListPageToDIV(){
		var hiddenList = Kernel.Common.getDomById("form_DIV_ID").childNodes;
		var url = '?a=1';
		for(i=0;i<hiddenList.length;i++){
			if ("INPUT" != hiddenList[i].nodeName.toUpperCase() || hiddenList[i].value == null 
					|| hiddenList[i].value == ''){
				continue;
			}
			var value = hiddenList[i].value;
			url += '&' + hiddenList[i].name + '=' + value;
		}
		Kernel.Ajax.asynloadPageToArea("<s:url value='/delivery/queryDeliveryList' />"+url, 'DATA_List_DIV_ID');
	}
	//加载数据列表页面
	loadDeliveryListPageToDIV();
</script>

<!-- init -->
<script type="text/javascript">
	initDeliveryStatus('<s:property value="#request.deliverySearchVO.status"/>');
	initDeliveryAgent('<s:property value="#request.deliverySearchVO.agentId"/>');
</script>

</body>
</html>
