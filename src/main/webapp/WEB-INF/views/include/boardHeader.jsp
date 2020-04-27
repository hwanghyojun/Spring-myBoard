<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 커스텀 header CSS -->
<link rel="stylesheet" type="text/css" href="../resources/css/header.css">
<!-- 최상단 네비 -->
<div class="container-fluid">
	<nav class="navbar navbar-default">
		<ul class="nav navbar-nav navbar-right">
			<!-- 회원가입 및 로그인 버튼 -->
			<!-- indexHeader /jboard/, boardHeader /jboard/board/-->
			<c:choose>
				<c:when test="${login_session == null}">
					<li><a href="../signUpForm">회원가입</a></li>
					<li><a href="../loginForm">로그인</a></li>
				</c:when>
				<c:otherwise>
					<li id="userImageLi"><img src="../resources/img/j_profile.jpg" class="img-circle" width="30px" height="30px"></li>
					<li><a>${login_session.u_id} 님</a></li>
					<li><a href="../logOut">로그아웃</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</nav>
	<nav class="col-sm-9 navbar navbar-default">
		<ul class="nav navbar-nav navbar-right">
			<li>
				<!-- 검색 폼 -->
				<form class="navbar-form" action="/">
					<div class="form-group">
						<input id="integratedSearch" type="text" class="form-control" placeholder="통합검색">
					</div>
					<button type="button" class="btn btn-primary">검색</button>
				</form>
			</li>
		</ul>
	</nav>
</div>