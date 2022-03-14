<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>Layout</title>
</head>
<body>

	<div class="topnav">
		<a href="/">Home</a>
		<a href="/groups">Groups</a>
		<a href="/projects">Projects</a>
		<a href="/topics">Topics</a>
		<a href="/profile">Your profile, Groups and Projects</a>
		<c:choose>
			<c:when test="${user == null}"><a href="/login">Login/Register</a></c:when>
			<c:otherwise><a href="/logout">Logout</a></c:otherwise>
		</c:choose>
		
		<div class="search-container">
			<form action="/search" method="get">
				<input type="text" name="searchterm" placeholder="Search projects..."/>
				<input type="submit" value="Search"/>
			</form>
		</div>
	</div>

<h1>Layout</h1>


	<div class="main-content" style="">


	</div>


</body>
</html>