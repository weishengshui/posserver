<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<link href="<s:url value='/styles/page.css'/>" rel="stylesheet" type="text/css" media="all"/>

<table align="center" width="800px">
	<tr>
		<td align="right">
			<a href='<s:url value="/pos/detail"/>'>添加</a>
		</td>
	</tr>
</table>
<s:form  namespace="/pos" action="list" name="searchPosForm" theme="simple" id="searchPosForm">

<s:hidden name="pageInfo.pageId" id="pageInfo.pageId" />
<s:hidden name="pageInfo.pageSize" id="pageInfo.pageSize" />
			
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
				<input type="submit" value="查询" />
			</td>
		</tr>
	</table>
</s:form>
	
	<div id="pos_list_center_div">
		<table align="center" width="800px">
		<tr align="center">
			<td>POS机编号</td>
			<td>厂商编号</td>
			<td>型号</td>
			<td>电机号码</td>
			<td>交付状态</td>
			<td>初始化</td>
			<td>运营状态</td>
			<td>密钥</td>
			<td >允许升级固件</td>
			<td >固件档案名称</td>
			<%--
				<td>操作</td>
			 --%>
		</tr>
	<s:if test="pageInfo.items != null && pageInfo.items.size()>0">
	  <s:iterator  value="pageInfo.items" id="posTmp" status="stat">
	  <tr align="center">
			<td>
				<a href='<s:url value="/pos/detail"/>/<s:property value="#posTmp.id"/>'>
					<s:property value="#posTmp.posId" />
				</a>
			</td>
			<td><s:property value="#posTmp.model" /></td>
			<td><s:property value="#posTmp.sn" /></td>
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
			<%--
				<td>
					<a href='<s:url value="/pos/del"/>/<s:property value="#posTmp.id"/>'>删除</a>
				</td>
			 --%>
		</tr>
	  </s:iterator>
   </s:if> 
	</table>
	
	<p:page pageInfo="${pageInfo}" />
	</div>
	
<script type="text/javascript">
	function goPage(pageId) {
		var formObj = document.getElementById("searchPosForm");
		document.getElementById("pageInfo.pageId").value = pageId;
		formObj.action = "${pageContext.request.contextPath}/pos/list";
		formObj.submit();
	}
</script>