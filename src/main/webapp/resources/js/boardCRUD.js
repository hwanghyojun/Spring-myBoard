$(document).ready(function() {
	
	// 네이버 에디터2
	var oEditors = [];
	nhn.husky.EZCreator.createInIFrame({
		oAppRef : oEditors,
		// elPlaceHoder와 <textarea> id값과 일치해야 적용된다
		elPlaceHolder : "content",
		sSkinURI : "../resources/js/naver_smart_editor2/SmartEditor2Skin.html",
		fCreator : "createSEditor2",
		htParams : {
			// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseToolbar : true,
			// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseVerticalResizer : false,
			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
			bUseModeChanger : false,
			// "저장하지 않고 나가시겠습니까" 메시지 스킵을 위한 기능
			fOnBeforeUnload : function() {
			}
		},
		fOnAppLoad : function() {
		}
	});
	
	// 글쓰기
	$("#writeSubmit").click(function() {
		if (confirm("글 작성을 완료하시겠습니까?")) {
			// 네이버 에디터에서 작성한 내용을 textarea 값으로 저장한다
			oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
			// submit 전 validation 검사 필요

			// 서버로 전송
			$("#write").submit();
		} else {
			return;
		}

	});
	
	// 글수정
	$("#updateSubmit").click(function() {
		if (confirm("글 수정을 완료하시겠습니까?")) {
			// 네이버 에디터에서 작성한 내용을 textarea 값으로 저장한다
			oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
			// submit 전 validation 검사 필요

			$("#update").submit();
		} else {
			return;
		}
		
	});
	// 글삭제
	$("#deleteSubmit").click(function() {
		if (confirm("글 삭제하시겠습니까?")) {
			window.location = "delete?number="+$("#number").val()+"&writer="+$("#writer").val();
		} else {
			return;
		}
	});		
});