<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<script type="text/javascript">
	var timerTaskForWaitPosInit = null;

	function newTimerTask(){
		closeTimerTask();
		
		var intervalLength = document.getElementById('intervalLength_ID').value;
		if(isNaN(intervalLength)){
			intervalLength = 5;
		}
		
		timerTaskForWaitPosInit = window.setInterval(proxy, 1000 * intervalLength);
	}

	function closeTimerTask(){
		if(timerTaskForWaitPosInit != null){
			window.clearInterval(timerTaskForWaitPosInit);
		}
	}

	function proxy(){
		var deliveryNoteDetailVOList_SIZE = document.getElementById('deliveryNoteDetailVOList_SIZE_ID').value;
		if(deliveryNoteDetailVOList_SIZE == 0){
			window.location.href = '<s:url value=""/>?';
		}else {
			window.location.reload();
		}
	}

	//是否自动刷新
	function isAutoRefresh(isChecked){
		if(isChecked == true){
			newTimerTask();
		}else {
			closeTimerTask();
		}
	}
</script>
	
</head>
<body>

<s:if test="#request.deliveryNoteDetailVOList == null || #request.deliveryNoteDetailVOList.size() == 0">
	<input type="hidden" id="deliveryNoteDetailVOList_SIZE_ID" value="0"/>
</s:if>
<s:else>
	<input type="hidden" id="deliveryNoteDetailVOList_SIZE_ID" value="<s:property value='#request.deliveryNoteDetailVOList.size()'/>"/>
</s:else>


<table align="center" width="100%" border="0">
	<tr>
		<td>
			交付单编号：
		</td>
		<td colspan="4">
			<s:property value="#request.deliveryNoteVO.dnNumber"/>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			以下POS机尚未初始化，请初始化。
		</td>
		<td colspan="1"></td>
		<td colspan="2">
			<input type="button" value="手动刷新" onclick="window.location.reload();"/>&nbsp;&nbsp;&nbsp;
			<input type="checkbox" onselect="isAutoRefresh(this.checked);"/>自动刷新&nbsp;
			每<input type="text" id="intervalLength_ID" size="2" maxlength="2" value="5" onchange="newTimerTask();"/>秒刷新
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
	<tr>
		<td colspan="5">
			共
			<s:if test="#request.deliveryNoteDetailVOList != null">
				<s:property value="#request.deliveryNoteDetailVOList.size()"/>
			</s:if>
			<s:else>0</s:else>
			台
		</td>
	</tr>
	<tr>
		<td>加入POS机：</td>
		<td colspan="4">
			<form action="<s:url value='/delivery/addPosForDelivery'/>" method="post" onsubmit="return checkFormContent();">
				<input type="hidden" name="deliveryId" value="<s:property value='#request.deliveryNoteVO.id'/>"/>
				<input type="text" id="posNum_ID" name="posNum" value="<s:property value='#reqeust.posNum'/>"/>
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
	isAutoRefresh(true);
</script>
</body>
</html>
