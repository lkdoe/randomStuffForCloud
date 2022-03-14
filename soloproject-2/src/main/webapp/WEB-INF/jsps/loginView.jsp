<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>Login</title>
</head>
<body>

<c:import url="header.jsp"/>

<h1>Login</h1>
<br/>
	<div class="login-page" style="justified">
	<div class="register-success">${success}</div>
		<div class="login-form">
			<form method="post" action="/login">
				<input type="text" name="username" placeholder="username" /><br><br>
				<input type="password" name="password" placeholder="password" /><br><br>
				<input type="submit" value="login"/>
				
				<p class="message">Not registered? <a href="/registration">Create an account</a>
					
				</p>
			</form>
		</div>
	</div>


</body>
</html>