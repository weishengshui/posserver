<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE table PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
</head>
<body>

<table align="center" width="800px">
	<tr>
		<td>
			<div id="DATA_List_DIV_ID">
				数据加载中...
			</div>
		</td>
	</tr>
	<tr>
		<td align="left">
			<a href='<s:url value="/agent/showEditAgent"/>'>+新增</a>
		</td>
	</tr>
</table>

<%-- 表单内容 --%>
<div id="form_DIV_ID" style="display: none;">
	<s:hidden id="currentPage_ID" name="currentPage" />
	<s:hidden id="pageSize_ID" name="pageSize" />
</div>


<script type="text/javascript">
	function loadDeliveryListPageToDIV(){
		var hiddenList = Kernel.Common.getDomById("form_DIV_ID").childNodes;
		var url = '?a=1';
		for(i=0;i<hiddenList.length;i++){
			if ("INPUT" != hiddenList[i].nodeName.toUpperCase() || hiddenList[i].value == null 
					|| hiddenList[i].value == ''){
				continue;
			}
			var value = hiddenList[i].value;
			url += '&' + hiddenList[i].name + '=' + value;
		}
		Kernel.Ajax.asynloadPageToArea("<s:url value='/delivery/queryDeliveryList' />"+url, 'DATA_List_DIV_ID');
	}
	//加载数据列表页面
	loadDeliveryListPageToDIV();
</script>

</body>
</html>
