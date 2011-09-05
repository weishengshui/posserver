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
					posId:
				</td>
				<td width="30%">
					<s:textfield name="posId" label="posId" id="posId"/>
				</td>
				<td width="20%">
					Serial number:
				</td>
				<td width="30%">
					<s:textfield name="sn" label="Serial number" id="sn"/>
				</td>
			</tr>
			<tr>
				<td >
					model:
				</td>
				<td >
					<s:textfield name="model" label="model" id="model"/>
				</td>
				<td >
					simPhoneNo:
				</td>
				<td >
					<s:textfield name="simPhoneNo" label="simPhoneNo" id="simPhoneNo"/>
				</td>
			</tr>
			<tr>
				<td >
					dstatus:
				</td>
				<td >
					<s:select name="dstatus" id="dstatus" value="dstatus"  list="{'DELIVERED','RETURNED'}" theme="simple" headerKey="" headerValue="--------" />
				</td>
				<td >
					istatus:
				</td>
				<td >
					<s:select name="istatus" value="istatus" id="istatus" list="{'UNINITED','INITED'}" theme="simple" headerKey="" headerValue="--------" />
				</td>
			</tr>
			<tr>
				<td >
					ostatus:
				</td>
				<td >
					<s:select name="ostatus" value="ostatus" id="ostatus"  list="{'ALLOWED','STOPPED'}" theme="simple" headerKey="" headerValue="--------" />
				</td>
				<td >
					secret:
				</td>
				<td >
					<s:textfield name="secret" id="secret" label="secret"/>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<input type="submit" name="submit" value="search" />
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