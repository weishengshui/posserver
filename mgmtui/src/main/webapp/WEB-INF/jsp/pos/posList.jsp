<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>posList</title>
</head>
<body>
	
	<table align="center" width="500px">
		<tr>
			<td align="right">
				<a href='<s:url value="/pos/detail"/>'>添加</a>
			</td>
		</tr>
	</table>
	
	<table align="center" width="500px">
		<tr>
			<td>&nbsp;&nbsp;</td>
			<td>PosId</td>
			<td>修改</td>
			<td>删除</td>
		</tr>
	<s:if test="#request.posVOList != null && #request.posVOList.size()>0">
	  <s:iterator  value="#request.posVOList" id="posTmp" status="stat">
	  <tr>
			<td>&nbsp;&nbsp;</td>
			<td><s:property value="#posTmp.posId" /></td>
			<td>
				<a href='<s:url value="/pos/detail"/>/<s:property value="#posTmp.id"/>'>修改</a>
			</td>
			<td>
				<a href='<s:url value="/pos/del"/>/<s:property value="#posTmp.id"/>'>删除</a>
			</td>
		</tr>
	  </s:iterator>
   </s:if> 
   
	</table>
</body>
</html>