package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.Img;
import com.project.model.PagingResponse;
import com.project.model.Review;
import com.project.model.SearchDto;
import com.project.service.IndexService;
import com.project.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	
	private final ReviewService rService;

	/**
	 * 리뷰는 모든 요청을 ajax로 반환해서 responsebody 사용
	 * @param params 페이징 처리를 위해 데이터의 형태를 SearchDto의 형태로 만들었다.
	 * @param r_pnickname_m_fk _리뷰를 작성할 판매자 닉네임
	 * @param prin 시큐리티 처리로 회원정보를 가져오기 위해 Principal 사용
	 * @return 로직을 실행 후 이동할 View의 이름
	 */
	@ResponseBody
	@GetMapping("")
	public Map<String, Object> Reviewlist(@ModelAttribute("params") SearchDto params, String r_pnickname_m_fk, Principal prin){// 페이징 처리를 위한 SearchDTO를 가져옴,어떤 상품의 리뷰를 가져올지 체크하기 위한 제품번호
		
		PagingResponse<Review> prolist = rService.AllReview(params, r_pnickname_m_fk);
		
		Map<String, Object> result = new HashMap<String, Object>(); // 리턴시켜야 하는 값의 자료형이 여러개이기 때문에 HashMap안에 담아서 KEY값으로 한번에 모아서 처리,
		
		int reviewct = rService.reviewct(r_pnickname_m_fk); // 리뷰의 총 갯수
		double reviewstar = rService.reviewStar(r_pnickname_m_fk); // 리뷰의 총점
		reviewstar = Math.round(((reviewstar/reviewct)*10)/10.0); // 리뷰 평균 평점
		
		List<Img> img_name = new ArrayList<>(); //Review에 img를 묶는 작업
		for (Review img : prolist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		String m_nickname="";
		
		if(prin!=null) { //작성자를 파악할 수 있게 닉네임 조회. 삭제에 사용
			m_nickname = rService.findnick(prin.getName());
		}else {
			m_nickname="비회원";
			
		}
		
		result.put("params", params);
		result.put("prolist", prolist.getList());
		result.put("pagination", prolist.getPagination());
		result.put("reviewct", reviewct);
		result.put("reviewstar", reviewstar);
		result.put("img", img_name);
		result.put("m_nickname", m_nickname);
		
		
		return result;
	}
	
	/**
	 * 
	 * @param r 리뷰 정보를 담은 객체
	 * @param prin 시큐리티 처리로 회원정보를 가져오기 위해 Principal 사용
	 * @throws Exception 리뷰를 넣는작업에 service에서 트랜젝션 진행
	 */
	@ResponseBody
	@PostMapping("")
	public void addReview(Review r, Principal prin) throws Exception {
		
		
		String nick = rService.findnick(prin.getName()); // 회원의 닉네임을 조회해서 넣는다
		if(nick == null || nick == "") { // 닉네임이 존재하지 않을시 아이디를 넣는다.
			nick = prin.getName();
		}
		r.setR_nickname_m_fk(nick);
		
		rService.ReviewAdd(r);

		
	}

	/**
	 * 
	 * @param r_id 리뷰의 번호를 조회하여 삭제
	 */
	@ResponseBody
	@DeleteMapping("")
	public void DelReview(@RequestParam int r_id) {
		
		rService.ReviewDel(r_id);
		
		
	}
	

	/**
	 * 
	 * @param prin 시큐리티 처리로 회원정보를 가져오기 위해 Principal 사용
	 * @param p_id 판매자의 게시글을 조회
	 * @return ajax로 리턴
	 */
	@ResponseBody
	@GetMapping("/comment")
	public boolean checkcomment(Principal prin,@RequestParam int p_id) {
		boolean check = false; // 기본 설정. 리뷰 작성 불가
		
		int index = 0;
		
		if(prin!=null) {
			index = rService.checkComments(prin.getName(), p_id); // 판매자의 물건을 구매한 갯수 조회
		}
		
		if(index>0) { // 구매 했으면 true를 리턴. 리뷰 작성 가능
			check = true;
		}
		
		return check;
	}
}
