<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<title>QQ Group Buying Validation POS Network</title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
	<meta http-equiv="expires" content="0" />
	<script type="text/javascript" src="<s:url value='/js/jquery/jquery-latest.min.js' />"></script>
	<link href="<s:url value='/styles/main.css'/>" rel="stylesheet" type="text/css" media="all"/>
</head>

<body>

<table align="center" width="800px">
	<tr align="center">
		<td colspan="2">
			<strong>POS机交付单</strong>
		</td>
	</tr>
	<tr style="height: 10px">
		<td colspan="2">
		</td>
	</tr>
	<tr >
		<td>
			交付单编号:
		</td>
		<td align="left">
			<s:property value="#request.deliveryNoteVO.dnNumber"/>
		</td>
	</tr>
	<tr>
		<td>
			确定日期:
		</td>
		<td align="left">
			<s:date name="#request.deliveryNoteVO.confirmDate" format="%{getText('dateformat.ymdhm')}" />
		</td>
	</tr>
	<tr>
		<td>
			交予:
		</td>
		<td align="left">
			<s:property value="#request.deliveryNoteVO.agent.name"/>
		</td>
	</tr>
</table>
<br/>

<table align="center" width="800px">
	<tr align="left">
		<td colspan="4"><strong>POS机列表</strong></td>
	</tr>
	<tr align="center">
		<td>&nbsp;&nbsp;</td>
		<td>POS机编号</td>
		<td>电机号码</td>
		<td>机身序列号</td>
	</tr>
	<s:if test="#request.deliveryNoteDetailVOList != null && #request.deliveryNoteDetailVOList.size()>0">
		  <s:iterator  value="#request.deliveryNoteDetailVOList" id="deliveryNoteDetailVO" status="stat">
			  <tr align="center">
					<td>
						<s:property value="%{#stat.index + 1}"/>
					</td>
					<td>
						<s:property value="#deliveryNoteDetailVO.posId" />
					</td>
					<td><s:property value="#deliveryNoteDetailVO.simPhoneNo" /></td>
					<td><s:property value="#deliveryNoteDetailVO.sn" /></td>
			  </tr>
		  </s:iterator>
   </s:if> 
</table>

<table align="center" width="800px">
	<tr align="left">
		<td>
			&nbsp;&nbsp;共 <s:property value="#request.deliveryNoteDetailVOList.size()"/> 台
		</td>
	</tr>
</table>
<br/><br/><br/>
<table align="center" width="800px">
	<tr align="left">
		<td>
			&nbsp;&nbsp;签收
		</td>
	</tr>
	<tr style="height: 20px">
		<td></td>
	</tr>
	<tr style="height: 30px">
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;____________
		</td>
	</tr>
	<tr style="height: 30px">
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;(姓名:___________)
		</td>
	</tr>
	<tr style="height: 30px">
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;日期:____/____/____(年/月/日)
		</td>
	</tr>
</table>

<table align="center" width="800px" class="noprint">
	<tr align="center">
		<td>
			<input type="button" value="打印"onClick="window.print();" />
		</td>
	</tr>
</table>
<script type="text/javascript">

$(function() {
	window.print();
})
</script>
</body>
</html>
