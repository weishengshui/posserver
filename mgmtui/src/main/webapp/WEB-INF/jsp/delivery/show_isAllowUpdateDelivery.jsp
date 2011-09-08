<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>	
</head>
<body>

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
		<td colspan="5">
			请确定POS机列表是否正确。
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
		<td colspan="5">
			<span style="color:red;"><strong>当你按下确定后，就不能更改交付单内的信息</strong></span>
		</td>
	</tr>
	<tr>
		<input type="button" value="确定并打印" onclick="confirmToPrint('<s:property value="#request.deliveryNoteVO.id"/>');" />&nbsp;&nbsp;&nbsp;
		<input type="button" value="继续修改" onclick="toUpdatePage('');" />
	</tr>
</table>

</body>
</html>
