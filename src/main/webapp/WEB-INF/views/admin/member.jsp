<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
						<table class="table table-hover">
							<thead>
								<tr>
									<th>회원번호</th>
									<th>아이디</th>
									<th>이름</th>
									<th>닉네임</th>
									<th>가입일</th>
									<th>회원등급</th>
									<th>수정</th>
									<th>삭제</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list}" var="memberVO">
									<tr>
										<td>${memberVO.userNo}</td>
										<td>${memberVO.userId}</td>
										<td>${memberVO.userName}</td>
										<td>${memberVO.nickName}</td>
										<td><fmt:formatDate value="${memberVO.regDate}"
												pattern="yyyy-MM-dd" /></td>
										<td>${memberVO.verify}</td>
										<td><button class="btn btn-primary btn-xs"
												data-title="Edit" data-toggle="modal"
												onclick="mod(${memberVO.userNo})">
												수정<span class="glyphicon glyphicon-pencil"></span></a>
											</button></td>

										<td><button class="btn btn-primary btn-xs"
												data-title="delete" data-toggle="modal"
												onclick="del(${memberVO.userNo})">
												삭제<span class="glyphicon glyphicon-trash"></span></a>
											</button></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="col-md-offset-3">
							<ul class="pagination">
								<c:if test="${pageMaker.prev}">
									<li><a
										href="member${pageMaker.makeSearch(pageMaker.startPage - 1)}">이전</a></li>
								</c:if>

								<c:forEach begin="${pageMaker.startPage}"
									end="${pageMaker.endPage}" var="idx">
									<li
										<c:out value="${pageMaker.cri.page == idx ? 'class=active' : ''}"/>>
										<a href="member${pageMaker.makeSearch(idx)}">${idx}</a>
									</li>
								</c:forEach>

								<c:if test="${pageMaker.next && pageMaker.endPage > 0}">
									<li><a
										href="member${pageMaker.makeSearch(pageMaker.endPage + 1)}">다음</a></li>
								</c:if>
							</ul>
						</div>
						<div class="search row">
							<div class="col-xs-2 col-sm-2">
								<select name="searchType" class="form-control">
									<option value="n"
										<c:out value="${scri.searchType == null ? 'selected' : ''}"/>>-----</option>
									<option value="userId"
										<c:out value="${scri.searchType eq 'userId' ? 'selected' : ''}"/>>아이디</option>
									<option value="userName"
										<c:out value="${scri.searchType eq 'userName' ? 'selected' : ''}"/>>이름</option>
<%-- 									<option value="w"
										<c:out value="${scri.searchType eq 'w' ? 'selected' : ''}"/>>작성자</option>
									<option value="tc"
										<c:out value="${scri.searchType eq 'tc' ? 'selected' : ''}"/>>제목+내용</option> --%>
								</select>
							</div>

							<div class="col-xs-10 col-sm-10">
								<div class="input-group">
									<input type="text" name="keyword" id="keywordInput"
										value="${scri.keyword}" class="form-control" /> <span
										class="input-group-btn">
										<button id="searchBtn" class="btn btn-default">검색</button>
									</span>
								</div>
							</div>

							<script>
	$(function(){
		$('#searchBtn').click(function() {
			self.location = "member" + '${pageMaker.makeQuery(1)}'
					+ "&searchType=" + $("select option:selected").val()
					+ "&keyword=" + encodeURIComponent($('#keywordInput').val());
				});
	});			
	</script>
						</div>
						<div align="center">
							<input type="button" class="btn btn-primary" value="메인으로"
								onclick="location.href='index'" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
<script>
	
	function mod(userNo) {
			location.href='edit?userNo='+userNo;
	}	
	function del(userNo) {
		var chk = confirm("정말 삭제하시겠습니까?");
		if (chk) {
			location.href='delete?userNo='+userNo;
		}
	}	
</script>
</html>