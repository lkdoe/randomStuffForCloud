<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>${project.projectName}</title>
</head>
<body>
	
<c:import url="header.jsp"/>

<h1>${project.projectName}</h1>
<a href="/group/${group.groupName}"><h3>${group.groupName}</h3></a>

	<div class="favourite">
		<form action="/addFavourite" method="post">
			<input type="hidden" name="projectid" value="${project.projectId}"/>
			<input type="image" name="submit" src="${heart}" alt="submit" style="width: 50px;"/>
		</form>
	</div>

	<div class="main-content" style="">
		<div class="project-overview">
			<table>
				<tr>
					<td><label>Date of Creation: </label></td>
					<td>${project.dateOfCreation}</td>
				</tr>
				<tr>
					<td><label>Topic: </label></td>
					<td>${project.projectTopic}</td>
				</tr>
			</table>
			<div class="description">
				<p>${project.projectDescription}</p>
			</div>
		</div>
		<div class="article-list">
			<h2>Articles</h2>
			<div class="articles">
				<c:forEach items="${project.articles}" var="article">
					<div class="article">
						<h3>${article.articleTitle}</h3>
						${article.dateOfWriting}<br/>
						<img alt="Article Image" src="/images/projects/${project.projectName}/${article.image}"
						width="auto" height="200">
						${article.articleText}
					</div>
				</c:forEach>
			</div>			
		</div>

	</div>


</body>
</html>