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
			<s:form  namespace="/agent" action="searchAgent">
				名称：<input type="text" name="agentSearchVO.agentName" value="<s:property value='#request.agentSearchVO.agentName'/>"/>&nbsp;&nbsp;&nbsp;
				<input type="submit" name="submit" value="查询" />
			</s:form>
		</td>
	</tr>
	<tr>
		<td>
			<div id="agentList_DIV_ID">
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
	<s:hidden id="agentVO.page_ID" name="agentSearchVO.page" />
	<s:hidden id="agentVO.size_ID" name="agentSearchVO.size" />
	<s:hidden id="agentVO.name_ID" name="agentSearchVO.agentName" />
</div>


<script type="text/javascript">
	function loadAgentListPageToDIV(){
		var hiddenList = Kernel.Common.getDomById("form_DIV_ID").childNodes;
		var url = '?a=1';
		for(i=0;i<hiddenList.length;i++){
			if ("INPUT" != hiddenList[i].nodeName.toUpperCase() || hiddenList[i].value == null 
					|| hiddenList[i].value == ''){
				continue;
			}
			var value = hiddenList[i].value;
			if(hiddenList[i].name == 'agentSearchVO.agentName'){
				value = encodeURIComponent(encodeURIComponent(value));
			}
			url += '&' + hiddenList[i].name + '=' + value;
		}
		Kernel.Ajax.asynloadPageToArea("<s:url value='/agent/queryAgentList' />"+url, 'agentList_DIV_ID');
	}
	//加载shop列表页面
	loadAgentListPageToDIV();
</script>

</body>
</html>
