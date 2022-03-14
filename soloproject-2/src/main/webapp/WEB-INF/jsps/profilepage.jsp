<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>${user.userName}: Profile Page</title>
</head>
<body>

<c:import url="header.jsp"/>

<h1>${user.userName}: Profile Page</h1>

	<div class="main-content">
		<div class="accout-details">
		
			<div class="main-image">
				<img class="image" alt="Profile image" src="/images/accountimages/${user.profileImage}"
					onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
				
				<form action="deleteaccountimage" method="post">
					<input type="submit" value="Delete Image"/>
				</form>
			</div>
		
			<form action="/profileimage" method="post" enctype="multipart/form-data">
				<input type="file" name="image"/>
				<input type="submit" value="Replace Profile Image"/>
			</form>
<!-- 			
			<form action="updateaccount" method="post">
				<input type="hidden" value="${user.userId}" name="userid"/>
				<table>
					<tr>
						<td><label>User name</label></td>
						<td><input type="text" name="username" value="${user.userName}"/></td>
					</tr>
					<tr>
						<td><label>Email address</label></td>
						<td><input type="text" name="email" value="${user.email}" /></td>
				</table>
				<input type="submit" value="Update User Details"/>
			</form>
-->
		</div>
		<br/>
		<div class="newgroup">
			<form action="/creategroup" method="post">
				<input type="text" name="groupname" placeholder="Group name"/>
				<input type="submit" value="Create a new Group"/>
			</form>
		</div>
		<br/>
		<div class="groups">
			<h3>Groups</h3>
			<div class="group-list">
				<c:forEach items="${mygroups}" var="group">
					<div>
						<c:if test="${not empty group}">
							<img class="image-thumb" alt="Group Image" src="" width="150" height="150"
								onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
							<br/>
							<a href="/group/${group.groupName}"> ${group.groupName}</a>
						</c:if>
						<br/>
					</div>
				</c:forEach>
			</div>
		</div>

		<div class="favourites">
		<h3>Favourite Projects</h3>
			<div class="project-list">
				<c:forEach items="${user.favourites}" var="project">
					<div class="item">
						<a href="/project/${project.projectName}">${project.projectName}</a>
					</div>
				</c:forEach>
			</div>
		</div>
		
	</div>


</body>
</html>