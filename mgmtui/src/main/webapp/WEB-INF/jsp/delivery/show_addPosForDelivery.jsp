<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">
	function associateAgent(deliveryId, agentId){
		var url = '<s:url value="/delivery/addAgentForDelivery"/>?a=1';

		url += '&deliveryId=' + deliveryId;
		url += '&agentId=' + agentId;
		
		Kernel.Ajax.ajaxRequest(url, Kernel.Ajax.POST, "associateAgentCellback");
	}

	function associateAgentCellback(data){
		var b = true;
		if(data != null){
			if(data.processSuccess == false){
				b = false;
			}
		}else{
			b = false;
		}

		if(!b){
			alert('系统正忙，请稍后再试!');
		}
	}

	function initAgentSelect(agentId){
		var opts = document.getElementById('agentList_ID').options;
		for(i=0;i<opts.length;i++){
			if(opts[i].value == agentId){
				opts[i].selected = true;
				return;
			}
		}
	}

	function checkFormContent(){
		var posNum = document.getElementById('posNum_ID').value;
		if(posNum == ''){
			alert('请输入POS编号!');
			return false;
		}
		return true;
	}

	function toNextPage(){
		window.location.href = '<s:url value="/delivery/showWaitPosInitDelivery"/>?deliveryId=<s:property value="#request.deliveryNoteVO.id"/>';
	}

	function removeDelivery(){
		window.location.href = '<s:url value="/delivery/deleteDelivery"/>/<s:property value="#request.deliveryNoteVO.id"/>';
	}

	function printDelivery(){
		window.location.href = '<s:url value="/delivery/printDelivery"/>/<s:property value="#request.deliveryNoteVO.id"/>';
	}
</script>

</head>
<body>



<table align="center" width="100%" border="0">
	<tr height="50">
		<td width="20%">
			选择第三方：
		</td>
		<td colspan="4">
			<select id="agentList_ID" onchange="associateAgent('<s:property value="#request.deliveryNoteVO.id"/>', this.options[this.selectedIndex].value);">
				<option value="null"></option>
				<s:iterator id="agentVO" value="#request.agentVOList" status="i">
					<option value="<s:property value='#agentVO.id'/>" >
						<s:property value="#agentVO.name"/>
					</option>
				</s:iterator>
			</select>
		</td>
	</tr>
	<tr height="50">
		<td>
			编号：
		</td>
		<td colspan="4">
			<s:property value="#request.deliveryNoteVO.dnNumber"/>
		</td>
	</tr>
	<tr height="50">
		<td>
			状态：
		</td>
		<td colspan="4">
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
	</tr>
	<s:if test="#request.deliveryNoteDetailVOList != null && #request.deliveryNoteDetailVOList.size() > 0">
		<tr>
			<td colspan="5">
				<table width="100%">
					<tr>
						<td>POS机编号</td>
						<td>电话号码</td>
						<td>制造厂商</td>
						<td>机身编号</td>
						<td>初始化状态</td>
						<td>操作</td>
					</tr>
					<s:iterator id="deliveryNoteDetailVO" value="#request.deliveryNoteDetailVOList" status="i">
						<tr>
							<td>
								<s:property value="#deliveryNoteDetailVO.posId"/>
							</td>
							<td>
								<s:property value="#deliveryNoteDetailVO.simPhoneNo"/>
							</td>
							<td>
								<s:property value="#deliveryNoteDetailVO.model"/>
							</td>
							<td>
								<s:property value="#deliveryNoteDetailVO.sn"/>
							</td>
							<td>
								<s:if test="#deliveryNoteDetailVO.istatus == 'UNINITED'">
									否
								</s:if>
								<s:else>
									是
								</s:else>
							</td>
							<td>
								<s:a namespace="/delivery" action="removePosForDelivery">
									<s:param name="deliveryId" value="#request.deliveryNoteVO.id"/>
									<s:param name="deliveryNoteDetailId" value="#deliveryNoteDetailVO.id"/>
									移除
								</s:a>
							</td>
						</tr>		
					</s:iterator>
				</table>
			</td>
		</tr>
	</s:if>
	<tr height="50">
		<td colspan="5">
			共
			<s:if test="#request.deliveryNoteDetailVOList != null">
				<s:property value="#request.deliveryNoteDetailVOList.size()"/>
			</s:if>
			<s:else>0</s:else>
			台
		</td>
	</tr>
	<tr height="50">
		<td>加入POS机：</td>
		<td colspan="4">
			<form action="<s:url value='/delivery/addPosForDelivery'/>" method="post" onsubmit="return checkFormContent();">
				<input type="hidden" name="deliveryId" value="<s:property value='#request.deliveryNoteVO.id'/>"/>
				<input type="text" id="posNum_ID" name="posNum" value="<s:property value='#request.posNum'/>"/>
				<input type="submit" value="添加"/>
			</form>
		</td>
	</tr>
	<tr>
		<td colspan="5" align="center">
			<s:if test="#deliveryNoteVO.status == 'CONFIRMED' || #deliveryNoteVO.status == 'PRINTED'">
				<input type="button" value="打印" onclick="printDelivery();" />&nbsp;&nbsp;&nbsp;
			</s:if>
			<s:else>
				<input type="button" value="下一步" onclick="toNextPage();" />&nbsp;&nbsp;&nbsp;
				<input type="button" value="删除" onclick="removeDelivery();" />&nbsp;&nbsp;&nbsp;
			</s:else>
			<input type="button" value="返回" onclick="history.back();" />
		</td>
	</tr>
</table>

<script type="text/javascript">
	function showErrorCode(addPosStatus){
		if(addPosStatus != null && !isNaN(addPosStatus)){
			//{1 不存在, 2已被使用}
			if(addPosStatus == 1){
				alert('POS不存在');
			}else if(addPosStatus == 2){
				alert('POS机已被使用');
			}
		}
	}
	
	initAgentSelect('<s:property value="#request.deliveryNoteVO.agent.id"/>');
	showErrorCode('<s:property value="#request.addPosStatus"/>');
</script>
</body>
</html>