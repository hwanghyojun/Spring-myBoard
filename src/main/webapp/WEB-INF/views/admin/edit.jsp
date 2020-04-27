<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Home</title>
<!-- 제이쿼리 -->
<script src='https://code.jquery.com/jquery-3.3.1.min.js'></script>

<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" type="text/css"
	href="/resources/css/my-login.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- 부가적인 테마 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

</head>
<body>
	<div class="page-wrapper">
		<div class="container-fluid">
			<div class="col-lg-8">
				<!-- 게시판 넓이 -->
				<div class="col-lg-12">
					<h1 class="page-header">회원 관리</h1>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">회원 목록</div>
					<div class="panel-body">
						<form method="post" role="form" autocomplete="off">
							<input name="userNo" id="userNo" type="hidden"
								value="${edit.userNo}" />
							<div class="form-group form-group-lg">
								<label class="control-label">아이디</label> <input name="userId"
									id="userId" type="text" class="form-control"
									value="${edit.userId}">
							</div>
							<div class="form-group form-group-lg">
								<label class="control-label">패스워드</label> <input name="userPass"
									id="userPass" type="text" class="form-control"
									value="${edit.userPass}">
							</div>
							<div class="form-group form-group-lg">
								<label class="control-label">이름</label> <input name="userName"
									id="userName" type="text" class="form-control"
									value="${edit.userName}" readonly>
							</div>
							<div class="form-group form-group-lg">
								<label class="control-label">닉네임</label> <input name="nickName"
									id="nickName" type="text" class="form-control"
									value="${edit.nickName}">
							</div>
							<div class="form-group form-group-lg">
								<select class="form-control" id="verify" name="verify" title="회원등급">
									<c:if test="${edit.verify == '0'}">
										<option value="${edit.verify}" selected="">일반회원</option>
										<option value = "9">관리자</option>
									</c:if>
									<c:if test="${edit.verify == '9'}">
										<option value="${edit.verify}" selected="">관리자</option>
										<option value = "0">일반회원</option>
									</c:if>
								</select>
							</div>
							<button type="submit" id="submit" class="btn btn-primary form-control">수정</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>