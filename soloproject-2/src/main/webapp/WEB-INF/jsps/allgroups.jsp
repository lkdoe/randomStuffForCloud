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

<c:import url="header.jsp"/>
<h1>All groups</h1>


	<div class="main-content">
		<div class="group-list">
			<c:forEach items="${groups}" var="group">
				<div class="grid-item">
					<img class="image-thumb" alt="Group Image" src="" width="150" height="150"
						onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
					<br/>
					<a href="/group/${group.groupName}" style="justify: center" >${group.groupName}</a>
					<br/>
				</div>
			</c:forEach>
		</div>

	</div>


</body>
</html>