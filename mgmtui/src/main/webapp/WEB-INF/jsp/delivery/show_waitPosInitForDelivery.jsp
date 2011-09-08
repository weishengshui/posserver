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
		
		timerTaskForWaitPosInit = window.setInterval(timerRun, 1000 * intervalLength);
	}

	function closeTimerTask(){
		if(timerTaskForWaitPosInit != null){
			window.clearInterval(timerTaskForWaitPosInit);
		}
	}

	function timerRun(){
		var deliveryNoteDetailVOList_SIZE = document.getElementById('deliveryNoteDetailVOList_SIZE_ID').value;
		if(deliveryNoteDetailVOList_SIZE == 0){
			window.location.href = '<s:url value="/delivery/showIsAllowUpdateDelivery"/>?deliveryId='+'<s:property value="#request.deliveryNoteVO.id"/>';
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
		<td colspan="2">
			交付单编号：
		</td>
		<td colspan="1" align="left">
			<s:property value="#request.deliveryNoteVO.dnNumber"/>
		</td>
		<td colspan="2"/>
	</tr>
	<tr>
		<td colspan="2">
			以下POS机尚未初始化，请初始化。
		</td>
		<td colspan="1"></td>
		<td colspan="2" align="right">
			<input type="button" value="手动刷新" onclick="window.location.reload();"/>&nbsp;&nbsp;&nbsp;
			<input type="checkbox" checked="checked" onclick="isAutoRefresh(this.checked);"/>自动刷新&nbsp;
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
</table>

<script type="text/javascript">
	isAutoRefresh(true);
</script>
</body>
</html>
