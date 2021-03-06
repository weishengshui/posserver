<%--

	This JSP contains the top menu items.
	
	This JSP designed to be included by other pages or in use with 
	other Struts action.
	
	The content is copied from http://www.cssplay.co.uk/menus/final_drop.html
	
	- CSS: /webapp/styles/theme/default/menu2.css
	
	@author cyril
	@author cream move it from Tiger.
	@since 0.1.0 2010-04-16

 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="menu">
<ul>

	<!-- Home STARTS-->
	<li><a href="<s:url value="/"/>">首页</a></li>
	<!-- Home ENDS  -->
	
	<s:if test="#session._userSession.user.userName != null && #session._userSession.user.userName != 'finance'">
	<!-- POS manager STARTS -->
	<li><a href="#">POS管理<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url action="list" namespace="/pos" />">列表</a></li>
		<li><a href="<s:url action="detail" namespace="/pos" />">新増</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- POS manager ENDS -->
	
	<!-- 第三方管理 STARTS -->
	<li><a href="#">第三方管理<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url action="searchAgent" namespace="/agent" />">列表</a></li>
		<li><a href="<s:url action="showEditAgent" namespace="/agent" />">新増</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- 第三方管理 ENDS -->
	
	<!-- 交付单管理 STARTS -->
	<li><a href="#">交付单管理<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url action="searchDelivery" namespace="/delivery" />">列表</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- 交付单管理 ENDS -->
	
	<!-- POS回收 STARTS -->
	<li><a href="#">POS回收<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url action="sendURL" namespace="/unbind" />">邀请第三方填写申请表</a></li>
		<li><a href="<s:url action="list" namespace="/unbind" />">我方生成回收单</a></li>
		<li><a href="<s:url action="posSearch" namespace="/unbind" />">解绑</a></li>
		<li><a href="<s:url action="getReturnNoteList" namespace="/unbind" />">回收单查询</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- POS回收 ENDS -->
	</s:if>
	
	<s:if test="#session._userSession.user.userName != null && #session._userSession.user.userName == 'finance'">
	<!-- 财务报表 START -->
	<li><a href="#">财务报表 <!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url action="search_bill" namespace="/finance" />">生成报表</a></li>
		<li><a href="<s:url action="search_excel" namespace="/finance" />">查询报表</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- 财务报表END -->
	</s:if>
	
	<s:if test="#session._userSession.user != null">
	<!-- Home STARTS-->
	<%-- <li>< s:action name="menuLoginLink" namespace="/" executeResult="true" /></li> --%>
	<!-- Home ENDS  -->
	<li><a href="<s:url action="logout" namespace="/" />">退出</a></li>
	</s:if>
	
</ul>
</div>
