<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>edit</title>
</head>
<body>

<s:form  namespace="/pos" action="edit" name="posForm" id="posForm" onsubmit="return checkPos();">
<s:hidden name="posVO.id"/>
<table align="center" width="500px">
		<tr>
			<td width="50%">
				<s:textfield name="posVO.posId" label="posId"/>
			</td>
			<td width="50%">
				<s:textfield name="posVO.sn" label="Serial number"/>
			</td>
		</tr>
		<tr>
			<td width="50%">
				<s:textfield name="posVO.model" label="model"/>
			</td>
			<td width="50%">
				<s:textfield name="posVO.simPhoneNo" label="simPhoneNo"/>
			</td>
		</tr>
		<tr>
			<td width="50%">
				<s:textfield name="posVO.dstatus" label="dstatus"/>
			</td>
			<td width="50%">
				<s:textfield name="posVO.istatus" label="istatus"/>
			</td>
		</tr>
		<tr>
			<td width="50%">
				<s:textfield name="posVO.ostatus" label="ostatus"/>
			</td>
			<td width="50%">
				<s:textfield name="posVO.secret" label="secret"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" name="submit" value="submit" />
			</td>
		</tr>
	</table>
</s:form>

<script type="text/javascript">
	function checkPos(){
		return true;
	}
</script>
</body>
</html>