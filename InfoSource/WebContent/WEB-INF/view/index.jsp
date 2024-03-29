<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
table{
margin: 2px;
}
</style>
<meta charset="utf-8">
<title>Basic Bootstrap Template</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css"
	href="resources/bootstrap/css/bootstrap.min.css">
<!-- Optional Bootstrap theme -->
<link rel="stylesheet"
	href="resources/bootstrap/css/bootstrap-theme.min.css">
</head>
<body>

	<div class=”container”>
		<div id="nav">
			<ul class="nav nav-tabs">
				<li><a href="account/create">Create</a></li>
				<li><a href="#" onclick="loadUserProfile()">My Profile</a></li>
				<li><a href="#" onclick="updateprofile()">Profile Update</a></li>
				<li><button>button</button></li>
			</ul>
		</div>

		<div id="main">
			<table style="padding: 2px" class="table table-condensed" id="userinfotable"></table>
			<div id="sidebar"></div>
		</div>

		

	</div>


	<script type="text/javascript"
		src="resources/bootstrap/js/jquery-1.12.4.min.js"></script>
	<script src="resources/bootstrap/js/bootstrap.min.js"></script>
	<script src="resources/js/namingconversion/personproperties.js"></script>
	<script src="resources/js/eventhander.js"></script>
	<script src="resources/js/ajaxcallprocessor.js"></script>
</body>
</html>