<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<title>${group.groupName}</title>
</head>
<body>

<c:import url="header.jsp"/>

<h1>${group.groupName}</h1>

	<div class="main-content">

		<div class="main-image">
			<img alt="Group image" 
				src="/images/${group.projects[0]}/${group.projects[0].articles[0].articleImage}"
				onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
		</div>
		<div class="description">
			<p>${group.groupDescription}</p>
		</div>
		
		<div class="project-list">
		
			<h3>Our projects:</h3>
			<c:if test="${membership == 'member' || membership == 'moderator'}">
				<form action="/createproject" method="post">
					<input type="hidden" value="${group.groupId}" name="groupid"/>
					<input type="text" placeholder="Project name" name="projectname"/>
					<input type="submit" value="Start a new Project"/>
				</form>
				<span class="error">${ProjectCreationError}</span>
			</c:if>
			
			<c:forEach items="${group.projects}" var="project">
				<div class="grid-item">
					<img class="image-thumb" alt="Project Image" src="" width="150" height="150"
						onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
					<br/>
					<a href="/project/${project.projectName}">${project.projectName}</a>
					<br/>
				</div>
			</c:forEach>
		</div>
		
		<div class="member-list">
			<h3>Our people:</h3> 
			<c:forEach items="${group.groupMods}" var="moderator">
				<div>
					<img class="image-thumb" alt="User Image" src="/images/accountimages/${moderator.profileImage}" 
						width="auto" height="150" onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
					<br/>
					<label>Moderator</label>
					<br/>
					<label>${moderator.userName}</label>
					<a href="/message/${moderator.userName}"><button>Message</button></a>
					<br/>
				</div>
			</c:forEach>
			<c:forEach items="${group.normalMembers}" var="member">
				<div>
					<img class="image-thumb" alt="User Image" src="/images/accountimages/${member.profileImage}" 
						width="auto" height="150" onerror="this.onerror=null; this.src='/images/cube.jpg'"/>
					<br/>
					<label>Group Member</label>
					<br/>
					<label>${member.userName}</label>
					<a href="/message/${member.userName}"><button>Message</button></a>
					<br/>
				</div>
			</c:forEach>
		</div>
	</div>
	
	
	<div class="membership-management">
		<h3>Group management</h3>
		<c:choose>
			<c:when test="${membership == 'moderator'}">
				<table>
					<c:forEach items="${group.groupApplicants}" var="applicant">
						<tr>
							<td>${applicant.userName}</td>
							<td>
								<form action="/acceptapplication" method="post">
									<input type="hidden" value="${applicant.userName}" name="username"/>
									<input type="hidden" value="${group.groupId}" name="groupid"/>
									<input type="submit" value="Accept Application" />
								</form>
							</td>
							<td>
								<form action="/denyapplication" method="post">
									<input type="hidden" value="${applicant.userName}" name="username"/>
									<input type="hidden" value="${group.groupId}" name="groupid"/>
									<input type="submit" value="Deny Application" />
								</form>
							</td>
						</tr>
					</c:forEach>
					<c:forEach items="${group.normalMembers}" var="member">
						<tr>
							<td>
								${member.userName}
							</td>
							<td>
								<form action="/makemod" method="post">
									<input type="hidden" value="${member.userId}" name="userid"/>
									<input type="hidden" value="${group.groupId}" name="groupid"/>
									<input type="submit" value="Make a Moderator"/>
								</form>
							</td>
							<td>
								<form action="/throwout" method="post">
									<input type="hidden" value="${member.userId}" name="userid"/>
									<input type="hidden" value="${group.groupId}" name="groupid"/>
									<input type="submit" value="Throw out of ${group.groupName}"/>
								</form>
							</td>
						</tr>						
					</c:forEach>
				</table>
				<br/>
				<form action="/abdicate" method="post">
					<input type="hidden" value="${group.groupId}" name="groupid"/>
					<input type="submit" value="Abdicate as Moderator" />
				</form>
				<form action="/leavegroup" method="post">
					<input type="hidden" value="${group.groupId}" name="groupid"/>
					<input type="submit" value="Leave ${group.groupName}"/>
				</form>
			</c:when>

			<c:when test="${membership == 'member'}">
				<form action="/leavegroup" method="post" >
					<input type="hidden" value="${group.groupId}" name="groupid"/>
					<input type="submit" value="Leave ${group.groupName}"/>
				</form>
			</c:when>
			
			<c:when test="${membership == 'applicant'}">
				<form action="/cancelapplication" method="post">
					<input type="hidden" value="${group.groupId}" name="groupid"/>
					<input type="submit" value="Cancel Application"/>
				</form>
			</c:when>
			
			<c:otherwise>
				<form action="/applyforgroup" method="post">
					<input type="hidden" value="${group.groupId}" name="groupid"/>
					<input type="submit" value="Apply for Membership" />
				</form>
			</c:otherwise>
		</c:choose>
	</div>
	


</body>
</html>