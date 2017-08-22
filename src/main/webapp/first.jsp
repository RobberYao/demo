<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="val.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试</title>
</head>
<body>
	<form>
		<input type="text" name="username" />
		<br/>
		<input type="text" name="password" />
		<br/>
		<input type="button" id="button1" onclick="alertMsg()" value="Button 1" />
	</form>
	
	<script type="text/javascript">
		function alertMsg() {
	    	validate();
		}
	</script>
</body>
</html>