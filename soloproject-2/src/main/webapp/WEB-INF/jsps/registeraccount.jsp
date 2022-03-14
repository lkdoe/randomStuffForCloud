<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register new Account</title>
</head>
<body>

<c:import url="header.jsp"/>

<h1>Register new Account</h1>
<div class="registration-form" style="justified">
	<form action="register" method="post">
		<table>
			<tr>
				<td><label>Choose a username: </label></td>
				<td><input type="text" name="username"/></td>
			</tr>
			<tr>
				<td><label>Enter your email address: </label></td>
				<td><input type="email" name="email"/></td> 
			</tr>
			<tr>
				<td><label>Choose a password (at least six characters): </label></td>
				<td><input type="password" name="password"/></td>
			</tr>
			<tr>
				<td><label>Repeat your password: </label></td>
				<td><input type="password" name="repeatpassword" /></td>
			</tr>
		</table>
		<input type="submit" value="Register Account">
	</form>
	<br/>
	<br/>
	<div class="error-message">${error}</div>
</div>
</body>
</html>