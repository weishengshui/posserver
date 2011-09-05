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
	<div id="pos_list_center_div">
		数据加载中...
	</div>
<script type="text/javascript">
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
	var params = {page:pageObj.value};
    jQuery.post(url, params, setPageBack, '');
}

function setPageBack(data){
	$("#pos_list_center_div").html(data);
}

function initPosList(){
	var url = "<s:url namespace='/pos' action='listCenter'/>";
    var params = {page:1};
    jQuery.post(url, params, setPageBack, '');
}

$(function() {
	initPosList();
})
</script>