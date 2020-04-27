$(document).ready(function() {
	// 검색
	$("#searchSubmit").click(function() {
		if (confirm("검색하시겠습니까?")) {
			$("#search").submit();
		} else {
			return;
		}
	});
});