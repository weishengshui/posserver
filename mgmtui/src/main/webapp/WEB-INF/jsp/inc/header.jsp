<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>	
<%@ taglib prefix="s" uri="/struts-tags"  %>

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