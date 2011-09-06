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
	
	<!-- Shop STARTS -->
	<li><a href="#">POS管理<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url value="/pos/list"/>">列表</a></li>
		<li><a href="<s:url value="/pos/detail"/>">新増</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- Shop ENDS -->
	
	<!-- Shop STARTS -->
	<li><a href="#">POS回收<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url value="/unbind/sendURL"/>">发送URL</a></li>
		<li><a href="<s:url value="/unbind/list"/>">生成回收单</a></li>
		<li><a href="<s:url value="/unbind/posSearch"/>">解绑</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- Shop ENDS -->
	
	<!-- Shop STARTS -->
	<li><a href="#">第三方管理<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]-->
		<ul>
		<li><a href="<s:url action="searchAgent" namespace="/agent" />">列表</a></li>
		<li><a href="<s:url action="showEditAgent" namespace="/agent" />">新増</a></li>
		</ul>
	<!--[if lte IE 6]></td></tr></table></a><![endif]-->
	</li>
	<!-- Shop ENDS -->
	
	<!-- Home STARTS-->
	<%-- <li>< s:action name="menuLoginLink" namespace="/" executeResult="true" /></li> --%>
	<!-- Home ENDS  -->
	<li><a href="<s:url action="logout" namespace="/" />">退出</a></li>

</ul>
</div>
