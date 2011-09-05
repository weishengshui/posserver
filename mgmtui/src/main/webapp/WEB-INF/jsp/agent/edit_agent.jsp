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

<s:form  namespace="/agent" action="editAgent" onsubmit="return checkForm();">
	<s:hidden name="agentVO.id"/>
	<table align="center" width="500px">
		<tr>
			<td>
				<s:textfield id="agentVO.name_ID" name="agentVO.name" label="名称"/>
			</td>
		</tr>
		<tr>
			<td>
				<s:textfield id="agentVO.email_ID" name="agentVO.email" label="e-mail"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" name="submit" value="提交" />
			</td>
		</tr>
	</table>
</s:form>

<script type="text/javascript">
	function isEmail(str){
	    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	    return reg.test(str);
	}
	
	function checkForm(){
		var nameVal = document.getElementById('agentVO.name_ID').value;
		if(nameVal == ''){
			alert('名称不能为空!');
			return false;
		}

		var emailVal = document.getElementById('agentVO.email_ID').value;
		if(emailVal == ''){
			alert('邮箱不能为空!');
			return false;
		}

		if(!isEmail(emailVal)){
			alert('邮箱格式有误!');
			return false;
		}
		return true;
	}
</script>
</body>
</html>