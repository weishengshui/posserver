<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
// disable the cache for web browser
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<title><decorator:title default="QQ Group Buying Validation POS Network"/></title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
	<meta http-equiv="expires" content="0" />
	<!-- 
	<link rel="stylesheet" type="text/css" href="<s:url value='/style/master.css'/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value='/style/zStyle.css'/>" />
	<link rel="shortcut icon" href="<s:url value='/images/favicon.ico'/>" type="image/x-icon" />
	 -->
	<script type="text/javascript" src="<s:url value='/js/jquery/jquery-latest.min.js' />"></script>
	
    <decorator:head/>
</head>

<body>
	<!-- User thing? -->
	<%-- 
	<s:if test="userSession.checkUserStatus"><jsp:include page="/WEB-INF/inc/guide_footer.jsp"/></s:if>
	<s:else><jsp:include page="/WEB-INF/inc/head.jsp"/></s:else>
	 --%>
    <jsp:include page="/WEB-INF/jsp/inc/header.jsp"/>
    <decorator:body/>
    <jsp:include page="/WEB-INF/jsp/inc/footer.jsp"/>
</body>
</html>
