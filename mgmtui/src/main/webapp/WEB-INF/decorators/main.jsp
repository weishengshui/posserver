<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<title><decorator:title default="QQ Group Buying Validation POS Network"/></title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
	<meta http-equiv="expires" content="0" />
	<script type="text/javascript" src="<s:url value='/js/jquery/jquery-latest.min.js' />"></script>
	<link href="<s:url value='/styles/main.css'/>" rel="stylesheet" type="text/css" media="all"/>
	
    <decorator:head/>
</head>

<body>
    <div id="page">
        <div id="header" class="clearfix">
        	QQ Group Buying Validation POS Network
            <hr />
        </div>
        <div id="content" class="clearfix">
            <div id="main">
            	<decorator:body/>
                <hr />
            </div>
            <!-- Top Menu Bar BEGINS -->
            <div id="nav">
                <div class="wrapper">
                <!-- <h3>Nav. bar</h3> -->
                <!-- Menu Items BEGINS -->
                <div id="menubar">
                <s:action  namespace="/util" name="menubar" executeResult="true" />
                <!-- Menu Items ENDS -->
                </div>
                </div>
                <hr />
            </div>
            <!-- Top Menu Bar ENDS -->
        </div>
        <div id="footer" class="clearfix">
            China Rewards 积享通 (c) 2010
        </div>
    </div>
</body>
</html>
