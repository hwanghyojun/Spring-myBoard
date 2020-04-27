package com.example.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.UserVO;
import com.example.service.UserService;

@Controller
public class UserController {


	@Autowired
	private UserService userService;

	// 회원가입 페이지로 이동
	@RequestMapping("/signUpForm")
	public String responseMoveSignUpForm() {
		// signUpForm.jsp로 이동
		return "signUpForm";
	}

	// 로그인 페이지로 이동
	@RequestMapping("/loginForm")
	public String responseMoveLoginForm() {
		// loginForm.jsp로 이동
		return "loginForm";
	}

	// 아이디 중복 확인 요청
	// paramId에 담기는 파라미터 id
	@RequestMapping(value = "signUpForm/idDupCheck", produces = "application/text; charset=utf8")
	public @ResponseBody String responseIdDupCheck(@RequestParam String paramId) throws Exception {
		int result = -1;
		String checkedId = "";
		// 검색 후, 0이면 사용가능, 1이면 중복, -1이면 메서드 작동X, -2이면 SQL 작동X
		result = userService.idDupCheck(paramId);
		if (result == 0) {
			checkedId = paramId;
		} else if (result == 1) {
			checkedId = "";
		} else {
			checkedId = "";
		}
		return checkedId;
	}


	// 회원가입 요청
	// params에 담기는 파라미터: id, password, name, sex, birthday, email,
	// auth_key(hidden), phone
	@RequestMapping("signUpForm/signUp")
	public String responseSignUp(@RequestParam HashMap<String, Object> params, RedirectAttributes rttr) throws Exception {

		userService.signUp(params);
		
		rttr.addFlashAttribute("msg", "회원 가입을 축하합니다");
		/* JOptionPane.showMessageDialog(null, "회원 가입을 축하합니다"); */
		// "프로젝트명/signUpform/" 경로에 있기 때문에 "/"으로 상위 경로로 나간 후, index로 리다이렉트
		return "redirect:/index";

	}


	// 로그인 요청
	// params에 담기는 파라미터: u_id, u_pwd
	@RequestMapping(value = "loginForm/login", produces = "application/text; charset=utf8")
	public @ResponseBody String jsonLogin(@RequestParam HashMap<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {
		String result = "X";
		if (session.getAttribute("login_session") != null) {
			// 로그인 요청시, login_info 세션 값이 존재하면 삭제
			session.removeAttribute("login_session");
		}
		// 로그인 시도
		UserVO login_info = userService.login(params);
		// 로그인 성공시
		if (login_info != null) {
			// 로그인된 아이디가 활성화 상태인 경우(u_active_state = 1)
				// 1. 최근 로그인 시간 업데이트
				userService.loginLatestTimeUpdate(params);
				// 2. 세션 "login_session"에 조회된 유저 정보 저장
				session.setAttribute("login_session", login_info);
				// 로그인된 아이디 문자열값 반환
				result = login_info.getU_id();
		}else {
		
			result = "로그인 실패";
		}
		
		return result;
	}

	// 로그아웃 요청
	@RequestMapping("logOut")
	public String responseLogOut(HttpSession session) {
		// 전체 세션 삭제
		session.invalidate();
		// 특정 세션 삭제
		// ex) session.removeAttribute("login_session");
		return "redirect:index";
	}

}
