<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create New Domain</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value ="/resources/theme/css/layout.css"/>" />
</head>
<body>

	<div class="wrapper">
		<div id="header">
	<%@ include file="adminheader.jsp"%>
		</div>

		<div class="content">
			<div id="left"></div>
			<div id="main">
			<form action="createDomain" method="get">
		<table>
			<tr>
				<td>Domain Creator</td>
				<td><input type="text" name="domainCreator" /></td>
			</tr>
			<tr>
				<td>Domain Name</td>
				<td><input type="text" name="domainName" /></td>
			</tr>
			<tr>
				<td>Organization</td>
				<td><input type="text" name="domainOrg" /></td>
			</tr>
			<tr>
				<td>Category</td>
				<td><select name="category">
						<option value="">SELECT ONE</option>
						<c:forEach items="${categories}" var="cat">
							<option value="${cat}">${cat}</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td>Contact Details</td>
				<td><input type="text" name="contact" /></td>
			</tr>
			<tr>
				<td>Description</td>
				<td><input type="text" name="description" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="create" /></td>
			</tr>
		</table>
	</form>
			</div>
			<div id="right"></div>

		</div>
<div id="footer"></div>
	</div>


</body>
</html>