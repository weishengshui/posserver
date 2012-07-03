<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>edit</title>
</head>
<style>
.showTable th{font-size:12px; font-weight:normal; text-align:left; padding:1px 0px 1px 5px; background-color:#e0e0e0;line-height:22px;}
.showTable td{font-size:12px; text-align:left; padding-left:5px; line-height:22px;}
#main td{font-size:12px; line-height:22px;}
.tdLabel{ text-align:right;}
</style>
<body>

<s:form id="editAgent_FormID" namespace="/agent" action="editAgent" onsubmit="return checkForm();">
	<s:hidden id="agentVO.id_ID" name="agentVO.id"/>
	<table width="100%" border="0" >
		<tr>
			<td >
				<s:textfield id="agentVO.name_ID" name="agentVO.name" label="名称"/>
			</td>
		</tr>
		<tr>
			<td>
				<s:textfield id="agentVO.email_ID" name="agentVO.email" label="e-mail"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="left">
				<input id="submit_Button_ID" type="button" value="确认" onclick="checkForm();" />
				<input type="button" value="返回" onclick="history.back();" />
			</td>
		</tr>
		
	</table>
</s:form>
<s:if test="agentVO != null && agentVO.id != null">
<s:form  namespace="/agent" action="searchAgentPos" name="searchAgentPos" theme="simple" id="searchPosForm">
<s:hidden id="agentVO.id" name="agentVO.id"/>
<s:hidden id="agentVO.name" name="agentVO.name"/>
<s:hidden id="agentVO.email" name="agentVO.email"/>		
<table width="100%" border="0">
		<tr>
			<td  colspan="2" align="left">
				<input type="submit" value="显示第三方POS机" align="left">
			</td>
		</tr>
	</table>


	<div id="pos_list_center_div">
	<s:if test="agentPosList != null && agentPosList.size > 0">
		<table width="100%" border="0" class="showTable">
		<tr align="center">
			<th>POS机编号</th>
			<th>厂商编号</th>
			<th>型号</th>
			<th>电机号码</th>
			<th>交付状态</th>
			<th>初始化</th>
			<th>运营状态</th>
			<th>密钥</th>
			<th >允许升级固件</th>
			<th >固件档案名称</th>
			<th>第三方名称</th>
			<%--
				<td>操作</td>
			 --%>
		</tr>
	  <s:iterator  value="agentPosList" id="posTmp" status="stat">
	  <tr align="center">
			<td>
				<s:property value="#posTmp.posId" />
			</td>
			<td><s:property value="#posTmp.sn" /></td>	<%-- 厂商编号 --%>
			<td><s:property value="#posTmp.model" /></td>
			<td><s:property value="#posTmp.simPhoneNo" /></td>
			<td>
				<s:if test="#posTmp.dstatus == 'DELIVERED'">
					已交付
				</s:if>
				<s:elseif test="#posTmp.dstatus == 'RETURNED'">
					已回收
				</s:elseif>	
				<s:else>
					&nbsp;&nbsp;
				</s:else>
			</td>
			<td>
				<s:if test="#posTmp.istatus == 'UNINITED'">
					否
				</s:if>
				<s:elseif test="#posTmp.istatus == 'INITED'">
					是
				</s:elseif>	
				<s:else>
					&nbsp;&nbsp;
				</s:else>
			</td>
			<td>
				<s:if test="#posTmp.ostatus == 'ALLOWED'">
					允许
				</s:if>
				<s:elseif test="#posTmp.ostatus == 'STOPPED'">
					禁止
				</s:elseif>	
				<s:else>
					&nbsp;&nbsp;
				</s:else>
			</td>
			<td><s:property value="#posTmp.secret" /></td>
			<td>
				<s:if test="#posTmp.upgradeRequired == true">
					允许
				</s:if>
				<s:elseif test="#posTmp.upgradeRequired == false">
					禁止
				</s:elseif>
			</td>
			<td><s:property value="#posTmp.firmware" /></td>
			<td><s:property value="#posTmp.deliveryAgent" /></td>
			<%--
				<td>
					<a href='<s:url value="/pos/del"/>/<s:property value="#posTmp.id"/>'>删除</a>
				</td>
			 --%>
		</tr>
	  </s:iterator>
	</table>
   </s:if> 
	
	</div>
</s:form>
</s:if>	
<script type="text/javascript">
	function isEmail(str){
	    var reg = /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
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
