<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="msg != null">
	<s:property value="msg"/>
</s:if>
<s:form  namespace="/pos" action="edit" name="posForm" theme="simple" id="posForm" onsubmit="return checkPos();">
<s:hidden name="posVO.id"/>
<table align="center" width="600px">
		<tr>
			<td width="20%">
				posId:
			</td>
			<td width="30%">
				<s:textfield name="posVO.posId" label="posId" id="posVO.posId"/>
			</td>
			<td width="20%">
				Serial number:
			</td>
			<td width="30%">
				<s:textfield name="posVO.sn" label="Serial number"/>
			</td>
		</tr>
		<tr>
			<td >
				model:
			</td>
			<td >
				<s:textfield name="posVO.model" label="model"/>
			</td>
			<td >
				simPhoneNo:
			</td>
			<td >
				<s:textfield name="posVO.simPhoneNo" label="simPhoneNo" id="posVO.simPhoneNo"/>
			</td>
		</tr>
		<tr>
			<td >
				dstatus:
			</td>
			<td >
				<s:select name="posVO.dstatus" value="posVO.dstatus"  list="#{'DELIVERED':'已交付','RETURNED':'已回收'}" listKey="key" listValue="value" theme="simple" headerKey="" headerValue="--------" />
			</td>
			<td >
				istatus:
			</td>
			<td >
				<s:select name="posVO.istatus" value="posVO.istatus"  list="#{'UNINITED':'未初始化','INITED':'已初始化'}"  listKey="key" listValue="value" theme="simple" headerKey="" headerValue="--------" />
			</td>
		</tr>
		<tr>
			<td >
				ostatus:
			</td>
			<td >
				<s:select name="posVO.ostatus" value="posVO.ostatus"  list="#{'ALLOWED':'开机','STOPPED':'停机'}" listKey="key" listValue="value"  theme="simple" headerKey="" headerValue="--------" />
			</td>
			<td >
				secret:
			</td>
			<td >
				<s:textfield name="posVO.secret" label="secret" id="posVO.secret"/>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="submit" name="submit" value="确定" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="button" value="取消" onclick='window.location.href="<s:url value='/pos/list'/>"'/>
			</td>
		</tr>
	</table>
</s:form>

<script type="text/javascript">
	function checkPos(){
	     var posId = document.getElementById('posVO.posId').value.trim();      
	     if(posId.length==0){      
	         alert("POSID不能为空或者为空格!");
	         return false; 
	     }      
	     var simPhoneNo = document.getElementById('posVO.simPhoneNo').value;  
		 if(simPhoneNo != ""){
			 if(!isphone(simPhoneNo)){
			    	alert("请填写正确的手机号码！");
			    	return false;
			    } 
		 }				
		 var secret = document.getElementById('posVO.secret').value.trim();  
		 var pr = /^([0-9]{6})$/;
		 if(secret != "" && !pr.test(secret)){
			 	alert("请输入六位数字secret！");
		    	return false;
		 } 
		return true;
	}
	
	function isphone(phone){
		var pr = /^1[3|5|8]([0-9]{1})([0-9]{8})$/;
		return pr.test(phone)
	}
		
</script>