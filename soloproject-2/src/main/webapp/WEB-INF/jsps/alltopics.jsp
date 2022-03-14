<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>Welcome!</title>
</head>
<body>

	<c:import url="header.jsp"/>

	<h1>Welcome ${user.userName}!</h1>
	<c:if test="${wasSearch}"><h3>Search Result: </h3></c:if>

	<div class="project-list">
		<c:choose>
			<c:when test="${not empty topics}">
				<c:forEach items="${topics}" var="topic">
					<div class="project-thumb">
						<a href="/search?searchterm=${topic}">${topic}</a>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<h3>No Topics found.</h3>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>