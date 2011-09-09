<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<link href="<s:url value='/styles/page.css'/>" rel="stylesheet" type="text/css" media="all"/>
	
	<table align="center" width="800px">
		<tr>
			<td align="right">
				<a href='<s:url value="/pos/detail"/>'>添加</a>
			</td>
		</tr>
	</table>
<s:form  namespace="/pos" action="list" name="searchPosForm" theme="simple" id="searchPosForm">
	<table align="center" width="600px">
			<tr>
				<td width="20%">
					POS机编号:
				</td>
				<td width="30%">
					<s:textfield name="posId" label="posId" id="posId"/>
				</td>
				<td width="20%">
					厂商编号:
				</td>
				<td width="30%">
					<s:textfield name="model" label="model" id="model"/>
				</td>
			</tr>
			<tr>
				<td >
					型号:
				</td>
				<td >
					<s:textfield name="sn" label="Serial number" id="sn"/>
				</td>
				<td >
					电机号码:
				</td>
				<td >
					<s:textfield name="simPhoneNo" label="simPhoneNo" id="simPhoneNo"/>
				</td>
			</tr>
			<tr>
				<td >
					交付状态:
				</td>
				<td >
					<s:select name="dstatus" id="dstatus" value="dstatus"   list="#{'DELIVERED':'已交付','RETURNED':'已回收'}" listKey="key" listValue="value" theme="simple" headerKey="" headerValue="--------" />
				</td>
				<td >
					初始化:
				</td>
				<td >
					<s:select name="istatus" value="istatus" id="istatus" list="#{'INITED':'是','UNINITED':'否'}"  listKey="key" listValue="value" theme="simple" headerKey="" headerValue="--------" />
				</td>
			</tr>
			<tr>
				<td >
					运营状态:
				</td>
				<td >
					<s:select name="ostatus" value="ostatus" id="ostatus"   list="#{'ALLOWED':'允许','STOPPED':'禁止'}"  listKey="key" listValue="value"  theme="simple" headerKey="" headerValue="--------" />
				</td>
				<td >
					密钥:
				</td>
				<td >
					<s:textfield name="secret" id="secret" label="secret"/>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<input type="submit" name="submit" value="查询" />
				</td>
			</tr>
		</table>
</s:form>
	
	<div id="pos_list_center_div">
		数据加载中...
	</div>
<script type="text/javascript">
var posId = document.getElementById("posId").value;
var sn = document.getElementById("sn").value;
var model = document.getElementById("model").value;
var simPhoneNo = document.getElementById("simPhoneNo").value;
var dstatus = document.getElementById("dstatus").value;
var istatus = document.getElementById("istatus").value;
var ostatus = document.getElementById("ostatus").value;
var secret = document.getElementById("secret").value;

function setPage(page){
	var pageObj = document.getElementById("page");
	if("previous" == page){
		pageObj.value = Number(pageObj.value) - 1;
	}else if("next" == page){
		pageObj.value = Number(pageObj.value) + 1;
	}else if("current" != page){
		pageObj.value = page;
	}
	var url = "<s:url namespace='/pos' action='listCenter'/>";
	var params = {posId:posId,
				sn:sn,
				model:model,
				simPhoneNo:simPhoneNo,
				dstatus:dstatus,
				istatus:istatus,
				ostatus:ostatus,
				secret:secret,
				page:pageObj.value};
    jQuery.post(url, params, setPageBack, '');
}

function setPageBack(data){
	$("#pos_list_center_div").html(data);
}

function initPosList(){
	var url = "<s:url namespace='/pos' action='listCenter'/>";
	var params = {posId:posId,
			sn:sn,
			model:model,
			simPhoneNo:simPhoneNo,
			dstatus:dstatus,
			istatus:istatus,
			ostatus:ostatus,
			secret:secret,
			page:1};
	jQuery.post(url, params, setPageBack, '');
}

$(function() {
	initPosList();
})
</script>