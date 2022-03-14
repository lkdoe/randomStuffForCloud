<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>Edit ${project.projectName}</title>
</head>
<body>
	
<c:import url="header.jsp"/>

<h1>Edit ${project.projectName}</h1>

	<div class="favourite">
		<form action="/addFavourite" method="post">
			<input type="hidden" name="projectid" value="${project.projectId}"/>
			<input type="image" name="submit" src="${heart}" alt="submit" style="width: 50px;"/>
		</form>
	</div>

	<div class="main-content" style="">
		<div class="project-details">
			<form action="/updateproject" method="post">
				<input type="hidden" name="projectid" value="${project.projectId}"/>
				<table>
					<tr>
						<td><label>Project name: </label></td>
						<td><input type="text" value="${project.projectName}" name="projectname"/></td>
					</tr>
					<tr>
						<td><label>Date of Creation: </label></td>
						<td>${project.dateOfCreation}</td>
					</tr>
					<tr>
						<td><label>Topic*: </label></td>
						<td><input type="text" value="${project.projectTopic}" name="projectTopic"/></td>
					</tr>
					<tr>
						<td>
				</table>
				*Have a look at the topics page to see which topics are popular.
				
				<br/>
				<textarea rows="4" cols="50" name="description" placeholder="Project description"
					>${project.projectDescription}</textarea>
				<br/>
				<input type="submit" value="Update Project">
			</form>
			
			<br/>
			<form action="/deleteproject" method="post">
				<input type="hidden" name="projectid" value="${project.projectId}"/>
				<input type="submit" value="Delete Project"/>
			</form>
		</div>
		<div class="article-list">
			<h2>Articles</h2>
			<form action="/writetextarticle" method="post" enctype="multipart/form-data">
				<label>Post a new Article:</label><br/>
				<input type="hidden" value="${project.projectId}" name="projectid"/>
				<input type="text" placeholder="Article Title" name="title"/><br/>
				<textarea rows="3" cols="50" name="articletext">Write an Article!</textarea><br/>
				<input type="file" name="image"/>
				<input type="submit" value="Post article"/>
			</form>
			<br/>
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