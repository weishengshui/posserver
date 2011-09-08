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
		alert(agentId);
		
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

<s:hidden name="deliveryNoteVO.id"/>

<table align="center" width="500px">
	<tr>
		<td>
			选择第三方：
		</td>
		<td>
			<select>
				<s:iterator id="agentVO" value="#request.agentVOList" status="i">
					<s:if test="#deliveryNoteVO.agent.id == #agentVO.id">
						<option value="<:property value='#agentVO.id'/>" selected="selected">
							<s:property value="#agentVO.name"/>
						</option>
					</s:if>
					<s:else>
						<option value="<:property value='#agentVO.id'/>" >
							<s:property value="#agentVO.name"/>
						</option>
					</s:else>
				</s:iterator>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			编号：
		</td>
		<td>
			<s:property value="#request.deliveryNoteVO.dnNumber"/>
		</td>
	</tr>
	<tr>
		<td>
			状态：
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
	</tr>
	
	
	
	<tr>
		<td colspan="2" align="center">
			<input id="submit_Button_ID" type="button" value="确认" onclick="checkForm();" />
			<input type="button" value="返回" onclick="history.back();" />
		</td>
	</tr>
</table>
	
</body>
</html>
