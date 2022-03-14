<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>Write Message to ${recipient.userName}</title>
</head>
<body>

	<c:import url="header.jsp"/>

	<h1>Welcome ${user.userName}!</h1>
	
	<div class="main-content">
		<div class="sendmessage">
			<form action="/writemessage" method="post">
				<label>To: </label>
				<input type="text" value="${recipient.userName}" name="recipientname"
					placeholder="Send to..."/>
				<input type="text" placeholder="Title" name="title"/>
				<br/>
				<br/>
				<textarea rows="5" cols="50" name="messagebody" placeholder="Write a message..."></textarea>
				<br/>
				<input type="submit" value="Send Message"/>
			</form>
		</div>
		<div class="previous-messages">
			<h2>Previous Messages</h2>
			<c:forEach items="${messages}" var="message">
				<div class="message">
					<b>Message: ${message.title} (${message.timeOfWriting})</b><br/>
					<span>Message from ${message.sender.userName}</span>
					<p>${message.text}</p>
				</div>
			</c:forEach>
		</div>
	</div>
</body>
</html>