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

<s:form id="editAgent_FormID" namespace="/agent" action="editAgent" onsubmit="return checkForm();">
	<s:hidden id="agentVO.id_ID" name="agentVO.id"/>
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
				<input id="submit_Button_ID" type="button" value="确认" onclick="checkForm();" />
				<input type="button" value="返回" onclick="history.back();" />
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
			return;
		}
		
		var emailVal = document.getElementById('agentVO.email_ID').value;
		if(emailVal == ''){
			alert('邮箱不能为空!');
			return;
		}

		if(!isEmail(emailVal)){
			alert('邮箱格式有误!');
			return;
		}

		var url = '<s:url value="/agent/agentIsExist"/>?a=1';

		url += '&agentVO.id=' + document.getElementById('agentVO.id_ID').value;
		url += '&agentVO.name=' + encodeURIComponent(encodeURIComponent(document.getElementById('agentVO.name_ID').value));
		
		Kernel.Ajax.ajaxRequest(url, Kernel.Ajax.POST, "checkFormCellback");
	}
	
	function checkFormCellback(data){
		if(data.agentIsExist == true){
			alert('名称已存在!');
		}else {
			document.getElementById('editAgent_FormID').submit();
		}
	}

	function but_Listener(){
		Kernel.EventUtil.activeOnclickEventById('submit_Button_ID');
	}

	Kernel.EventUtil.listenKeyUpByTarget(document.getElementById('editAgent_FormID'), Kernel.KeyCode.getKeyCode('Key_Enter'), 'but_Listener');
</script>
</body>
</html>