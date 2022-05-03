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
	<div class="main-content">
		<div class="project-list">
			<c:choose>
				<c:when test="${not empty recommended}">
					<c:forEach items="${recommended}" var="project">
						<div class="grid-item">
							<img class="image-thumb" alt="Project Image" 
								src="/images/projects/${project.projectName}/${project.articles[0].image}" 
								width="auto" height="150"
								onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
							<br/>
							<a href="/project/${project.projectName}">${project.projectName}</a>
							<br/>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<h3>No Projects found.</h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>