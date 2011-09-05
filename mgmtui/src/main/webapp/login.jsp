<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form action="login" method="POST">
	<font style="color:red;"><s:fielderror fieldName="loginError" /></font>

	<s:textfield id="username" name="username" label="用户名" tabindex="1"/>
	<s:password id="passowrd" name="password" label="密　码" tabindex="2" />
	<s:submit name="submit" value="登录" />
</s:form>

<script type="text/javascript">
$(function() {
	$('#username').focus();
})
</script>