package com.example.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.BoardVO;
import com.example.domain.ReplyVO;
import com.example.domain.UserVO;
import com.example.paging.PageCriteria;
import com.example.paging.PageMaker;
import com.example.paging.ReplyPageCriteria;
import com.example.paging.ReplyPageMaker;
import com.example.service.BoardService;
import com.example.service.ReplyService;
import com.example.service.UserService;

// 컨트롤러 명시
@Controller
public class BoardController {

	// 객체 주입
	@Autowired
	private BoardService boardService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private UserService userService;

	// 메인 화면 호출
	// pc에 담기는 파라미터 page, perPageNum
	@RequestMapping(value = { "/", "index" }, method = RequestMethod.GET)
	public String responseIndex(@ModelAttribute("pc") PageCriteria pc, Model model) throws Exception {
		// view > controller
		// Controller에서 view가 전송하는 parameter를 받는 여러가지 방법이 있다
		// 아래 예시(변수타입은 파라미터에 맞게 변경)
		// - @RequestParam String 변수명 (파라미터 1개)
		// - @RequestParam("키값") String 변수명 (데이터 커맨드를 키값으로 명시)
		// - @RequestParam String 변수명, @RequestParam int 변수명 (파라미터 2개)
		// 받아올 파라미터가 많아지게 되는 경우
		// - @RequestParam HashMap<String, Object> 변수명 (파라미터 여러개)
		// - 데이터모델(custom) 변수명 (파라미터 여러개)
		// - @ModelAttribute("키값") 데이터모델(custom) 변수명 (파라미터 여러개)
		// view(.jsp)에서 ${키값.데이터명}으로 값을 불러올 수 있다

		// 데이터 커맨드 객체로 pc를 매개변수로 넣어주고 넘어오는 page와 perPageNum 정보를 받는다
		// pc를 이용해서 service > dao > mapper.xml 순으로 접근하면서 DB 처리를 하여
		// 현재 페이지 정보를 기준으로 BoardVO 객체를 담은 ArrayList "list"가 반환된다
		List<BoardVO> bvo = boardService.listPageCriteria(pc);

		// view(.jsp)에서 페이징 처리를 위해 사용할 PageMaker 객체 생성
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPc(pc);
		Integer totalPageNum = boardService.totalCount();
		pageMaker.setTotalCount(totalPageNum);

		// controller > view
		// Model vs ModelAndView
		// Model return URL경로 지정, ModelAndView return mv객체(뷰 경로 따로 지정)
		// view(.jsp)에서 페이징 처리를 위해 pageMaker 객체 저장
		// view(.jsp)에 게시글을 뿌려주기 위해 BoardVO bvo 객체 저장
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("list", bvo);

		return "index";
	}

	// 글 내용 보기
	// params에 담기는 파라미터 page, perPageNum, b_num
	// rpc에 담기는 파라미터 replyPage, replyPerPageNum
	@RequestMapping("board/view")
	public String responseRead(ReplyPageCriteria rpc, @RequestParam HashMap<String, Object> params, Model model,
			HttpSession session) throws Exception {

		// 글 내용 가져오기
		BoardVO bvo = boardService.boardRead(params);
		// 글 조회수 올리기
		boardService.countHit(bvo.getB_num());

		int b_num = Integer.parseInt((String) params.get("b_num"));

		HashMap<String, Object> reply_params = new HashMap<String, Object>();
		reply_params.put("b_num", b_num);
		reply_params.put("replyPageStart", rpc.getReplyPageStart());
		reply_params.put("replyPerPageNum", rpc.getReplyPerPageNum());

		// 댓글 리스트 불러오기
		List<ReplyVO> rvo = replyService.replyListPageCriteria(reply_params);

		// 댓글 페이징
		ReplyPageMaker replyPageMaker = new ReplyPageMaker();
		replyPageMaker.setRpc(rpc);
		Integer replyTotalPageNum = replyService.replyTotalCount(b_num);
		replyPageMaker.setReplyTotalCount(replyTotalPageNum);

		// 현재 세션 로그인 유저 아이디 가져오기
		UserVO uvo = (UserVO) session.getAttribute("login_session");
		// 유저 추천 활성화 시간 조회, "board/recommend" 요청 결과 값을 view.jsp hidden값(u_r_a_t) 갱신을 위해
		// 조회하여 model에 추가한다
		if (uvo != null) {
			Timestamp u_recommend_active_time = userService.checkRecommendActiveTime(uvo.getU_id());
			model.addAttribute("u_recommend_active_time", u_recommend_active_time);
		}

		// 각 객체에 데이터 담아서 view.jsp 페이지로 반환
		model.addAttribute("sessionID", uvo);
		model.addAttribute("replyPageMaker", replyPageMaker);
		model.addAttribute("replyList", rvo);
		model.addAttribute("v_content", bvo);
		model.addAttribute("page", params.get("page"));
		model.addAttribute("perPageNum", params.get("perPageNum"));

		return "board/view";
	}

	// 글쓰기 페이지로 이동
	@RequestMapping("board/writeForm")
	public String responseMoveWriteForm(HttpSession session, Model model) {
		// 현재 세션 로그인 유저 아이디 가져오기
		UserVO uvo = (UserVO) session.getAttribute("login_session");
		// 세션값 없으면 메세지 값 보냄
		if (uvo == null) {
			model.addAttribute("msg", "글쓰기 권한이 없습니다.. 로그인 해주세요");
		}
		// board/writeForm.jsp로 이동
		return "board/writeForm";
	}

	// 글쓰기
	@RequestMapping("board/write")
	public String responseWrite(@RequestParam HashMap<String, Object> params) throws Exception {

		boardService.boardWrite(params);

		return "redirect:/index";
	}

	// 글수정 페이지로 이동(+b_num에 맞는 글내용 조회)
	@RequestMapping("board/updateForm")
	public String responseMoveUpdateForm(@RequestParam HashMap<String, Object> params, Model model, HttpSession session,
			RedirectAttributes rttr) throws Exception {

		// 수정할 글 세부 정보 가져오기
		BoardVO bvo = boardService.boardRead(params);

		// 현재 세션 로그인 유저 아이디 가져오기
		UserVO uvo = (UserVO) session.getAttribute("login_session");

		// 현재 세션 로그인 유저 equals 선택 글 작성자인 경우 수정 페이지로 이동
		// ==는 객체의 참조 주소값을 비교하는 연산자
		// equals()는 객체의 내용 값을 비교하는 메소드
		if (bvo.getB_writer().equals(uvo.getU_id())) {
			model.addAttribute("u_content", bvo);
			model.addAttribute("page", params.get("page"));
			model.addAttribute("perPageNum", params.get("perPageNum"));
			return "board/updateForm";
		} else {
			rttr.addFlashAttribute("msg", "수정 권한이 없습니다!!");
			// 다시 보고 있던 글로 리턴
			return "redirect:view?b_num=" + params.get("b_num") + "&page=" + params.get("page") + "&perPageNum="
					+ params.get("perPageNum");
		}

	}

	// 글수정
	// 글수정시 자동으로 등록날짜 업데이트(Mysql5.7 on update 기능 사용)
	@RequestMapping("board/update")
	public String responseUpdate(@RequestParam HashMap<String, Object> params) throws Exception {

		boardService.boardUpdate(params);

		return "redirect:/index";
	}

	// 글삭제
	@RequestMapping("board/delete")
	public String responseDelete(@RequestParam HashMap<String, Object> params) throws Exception {
		System.out.println("글 삭제 파라미터: " + params);

		boardService.boardDelete(params);

		return "redirect:/index";
	}

	// 검색
	@RequestMapping("/search")
	public String responseSearch(@ModelAttribute("pc") PageCriteria pc, @RequestParam HashMap<String, Object> params,
			Model model) throws Exception {
		// 검색결과 20개씩 뿌리기
		pc.setPerPageNum(20);

		// 파라미터 합치기
		HashMap<String, Object> search_params = new HashMap<String, Object>();
		search_params.put("search_condition", params.get("search_condition"));
		search_params.put("search_content", params.get("search_content"));
		search_params.put("pageStart", pc.getPageStart());
		search_params.put("perPageNum", pc.getPerPageNum());
		System.out.println("search_params 값: " + search_params);

		// 검색 리스트 부르기
		List<BoardVO> bvo = boardService.searchBoard(search_params);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPc(pc);
		// 검색한 결과값의 개수 구하기
		Integer totalPageNum = boardService.searchTotalCount(params);
		pageMaker.setTotalCount(totalPageNum);

		// 모델에 값 담기
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("list", bvo);
		model.addAttribute("search_condition", params.get("search_condition"));
		model.addAttribute("search_content", params.get("search_content"));

		// 검색했던 결과값 유지해야 페이징 기능 이용이 가능하다
		return "search";
	}

	// 추천하기
	@RequestMapping("board/recommend")
	public String responseRecommend(@RequestParam HashMap<String, Object> params, HttpSession session) throws Exception {

		// 현재시간 > u_recommend_active_time 인 경우, 추천Go
		boardService.countRecommend(params);
		// 추천 후, 유저 u_recommend_active_time에 현재시간+1분(시간 변경가능, board_mapper) 업데이트
		userService.updateRecommendActiveTime((String) params.get("u_id"));

		return "redirect:view?b_num=" + params.get("b_num") + "&page=" + params.get("page") + "&perPageNum=" + params.get("perPageNum");
	}
}
