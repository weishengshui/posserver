<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table align="center" width="800px">
		<tr align="center">
			<td>&nbsp;&nbsp;</td>
			<td>POS机编号</td>
			<td>厂商编号</td>
			<td>型号</td>
			<td>电机号码</td>
			<td>交付状态</td>
			<td>初始化</td>
			<td>运营状态</td>
			<td>密钥</td>
			<td>操作</td>
		</tr>
	<s:if test="#request.posVOList != null && #request.posVOList.size()>0">
	  <s:iterator  value="#request.posVOList" id="posTmp" status="stat">
	  <tr align="center">
			<td>
				<s:property value="%{(page - 1) * 15 + #stat.index + 1}"/>
			</td>
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
				<a href='<s:url value="/pos/del"/>/<s:property value="#posTmp.id"/>'>删除</a>
			</td>
		</tr>
	  </s:iterator>
   </s:if> 
	</table>
	
<s:hidden name="page" id="page" />
	<s:if test="#request.pageList !=null && #request.pageList.size()>0">
		<div style="margin-top: 15px;"  class="page">
			<s:if test="#request.startFlag">
				<a href="javascript:setPage(1)" class="home">首页</a>
				<a href="javascript:setPage('previous')" class="up"> << 上一页</a>
			</s:if>
			<s:iterator  value="#request.pageList" id="pageStr" status="stat">
				<s:if test="#pageStr[1] == 'true' ">	
					<a href="javascript:void(0)" class="link"><s:property  value="#pageStr[0]"/></a>
				</s:if>
				<s:if test="#pageStr[1] == 'false' ">	
					<a href="javascript:setPage('<s:property  value="#pageStr[0]"/>')" ><s:property  value="#pageStr[0]"/></a>
				</s:if>
			</s:iterator>
			<s:if test="#request.endFlag">
				<a href="javascript:setPage('next')" class="home">下一页 >></a>
				<a href="javascript:setPage('<s:property  value="#request.pageCount"/>')" class="up">末页</a>
			</s:if>
		</div>
		</s:if>
	