<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>Messages</title>
</head>
<body>

	<c:import url="header.jsp"/>

	<h1>Welcome ${user.userName}!</h1>

	<div class="senders-list">
		<c:choose>
			<c:when test="${not empty senders}">
				<h3>You have recieved messages from:</h3>
				<c:forEach items="${senders}" var="sender">
					<div class="sender">
						<a href="/message/${sender.userName}">${sender.userName}</a>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>
	<div class="recipients-list">
		<c:choose>
			<c:when test="${not empty recipients}">
				<h3>You have sent messages to:</h3>
				<c:forEach items="${recipients}" var="recipient">
					<div class="sender">
						<a href="/message/${recipient.userName}">${recipient.userName}</a>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>
</body>
</html>