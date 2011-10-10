<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="msg != null">
	<span style="color:red"><s:property value="msg"/></span>
	
</s:if>
<s:form  namespace="/pos" action="edit" name="posForm" theme="simple" id="posForm" onsubmit="return checkPos();">
<s:hidden name="posVO.id"/>
<s:hidden name="posVO.version"/>

<table align="center" width="600px">
		<tr>
			<td width="20%">
				POS机编号:
			</td>
			<td width="30%">
				<s:if test="posVO.id == null">
					<s:textfield name="posVO.posId" label="posId" id="posVO.posId" />
				</s:if>
				<s:else>
					<s:textfield name="posVO.posId" label="posId" id="posVO.posId" readonly="true"/>
				</s:else>
			</td>
			<td width="20%">
				厂商编号:
			</td>
			<td width="30%">
				<s:textfield name="posVO.sn" label="Serial number"/>	<%-- 厂商编号 --%>
			</td>
		</tr>
		<tr>
			<td >
				型号:
			</td>
			<td >
				<s:textfield name="posVO.model" label="model"/>
			</td>
			<td >
				电机号码:
			</td>
			<td >
				<s:textfield name="posVO.simPhoneNo" label="simPhoneNo" id="posVO.simPhoneNo"/>
			</td>
		</tr>
		<tr>
			<td >
				交付状态:
			</td>
			<td >
				<input type="hidden" name="dstatus" value='<s:property value="posVO.dstatus"/>'/>
				
				<s:if test="posVO.id != null && posVO.dstatus == 'DELIVERED'">
					已交付到<s:property value="posVO.deliveryAgent"/>
				</s:if>
				<s:else>
					已回收
				</s:else>
			</td>
			<td >
				初始化:
			</td>
			<td >
				<input type="hidden" name="istatus" value='<s:property value="posVO.istatus"/>'/>
				<s:radio name="posVO.istatus" disabled="true" value="posVO.istatus"  list="#{'INITED':'是','UNINITED':'否'}"  listKey="key" listValue="value" theme="simple" />
			</td>
		</tr>
		<tr>
			<td >
				运营状态:
			</td>
			<td >
				<s:radio name="posVO.ostatus" value="posVO.ostatus"  list="#{'ALLOWED':'允许','STOPPED':'禁止'}" listKey="key" listValue="value"  theme="simple" />
			</td>
			<td >
				密钥:
			</td>
			<td >
				<s:textfield name="posVO.secret" label="secret" id="posVO.secret" readonly="true"/>
			</td>
		</tr>
		<tr>
			<td >
				允许升级固件:
			</td>
			<td >
				<s:radio name="posVO.upgradeRequired" value="posVO.upgradeRequired"  list="#{true:'允许',false:'禁止'}" listKey="key" listValue="value"  theme="simple" />
			</td>
			<td >
				固件档案名称:
			</td>
			<td >
				<s:textfield name="posVO.firmware" label="firmware"/>
			</td>
		</tr>
		<s:if test="posVO.id != null ">
			<s:if test="posVO.createAt != null">
				<tr>
					<td>
						输入时间:
					</td>
					<td charoff="3" align="left">
						<s:date name="posVO.createAt" format="%{getText('dateformat.ymdhm')}" />
					</td>
				</tr>
			</s:if>
			<s:if test="posVO.lastModifyAt != null">
				<tr>
					<td>
						最近修改时间:
					</td>
					<td charoff="3" align="left">
						<s:date name="posVO.lastModifyAt" format="%{getText('dateformat.ymdhm')}" />
					</td>
				</tr>
			</s:if>
		</s:if>
		
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
	         alert("POSID不能为空!");
	         return false; 
	     }      
	     var simPhoneNo = document.getElementById('posVO.simPhoneNo').value.trim();  
	     if(simPhoneNo.length == 0){  
	         alert("电机号码不能为空!");
	         return false; 
		 }
		 if(!isphone(simPhoneNo)){
		    	alert("请填写正确的电机号码！");
		    	return false;
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