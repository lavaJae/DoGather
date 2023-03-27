package com.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.Discount;
import com.project.model.Img;
import com.project.model.IndexRequest;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.SearchDto;
import com.project.service.IndexService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final IndexService iService;

/*   -------------------------메인페이지-----------------------------	 */
	
	/**
	 * 쇼핑몰 메인 페이지
	 * @param model = view에 리턴시켜줄 데이터를 설정해주기 위한 객체
	 * @return 로직을 실행 후 이동할 View의 이름. html이 생략 되어 있다. 
	 */
	@GetMapping({"/index","/"}) 
	public String mainPage(Model model) {
		
		List<Product> bestlist = iService.Productbest();
		List<Product> newlist = iService.Productnew();
		
		model.addAttribute("bestlist", bestlist);
		model.addAttribute("newlist", newlist);
		
		
		return "index";
	}
	
	/**
	 * 
	 * @param params 페이징 처리를 위해 데이터의 형태를 SearchDto의 형태로 만들었다.
	 * @param model view에 리턴시켜줄 데이터를 설정해주기 위한 객체
	 * @return 로직을 실행 후 이동할 View의 이름
	 */
	@GetMapping("/index/list")
	public String PagingList(@ModelAttribute("params") SearchDto params, Model model) {
		PagingResponse<Product> plist = iService.PagingList(params);
		
		List<Img> img_name = new ArrayList<>(); // Product에 Img를 묶는 작업 
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		List<Discount>dis = new ArrayList<>(); //Product에 Discount(할인률)를 묶는 작업
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		
		return "prodlist";
	}
	
	/**
	 * 
	 * @param params 페이징 처리를 위해 데이터의 형태를 SearchDto의 형태로 만들었다.
	 * @param p_category 카테고리별 조회를 위해 넘겨 받을 파라미터 설정
	 * @param model 로직을 실행 후 이동할 View의 이름
	 * @return 로직을 실행 후 이동할 View의 이름
	 */
	@GetMapping("/index/category")
	public String category(@ModelAttribute("params") SearchDto params, String p_category, Model model) {
		
		PagingResponse<Product> plist = iService.Category(params, p_category);
		
		List<Img> img_name = new ArrayList<>(); // Product에 Img를 묶는 작업 
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		List<Discount>dis = new ArrayList<>(); //Product에 Discount(할인률)를 묶는 작업
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);	
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		return "prodlist";
	}
	
	/**
	 * 
	 * @param params 페이징 처리를 위해 데이터의 형태를 SearchDto의 형태로 만들었다.
	 * @param model model 로직을 실행 후 이동할 View의 이름
	 * @param request 정렬을 위한 파라미터를 쉽게 받기 위해 IndexRequest의 객체로 설정
	 * @return 로직을 실행 후 이동할 View의 이름
	 */
	@GetMapping(value = {"/index/filter"})
	public String sortList(@ModelAttribute("params") SearchDto params, Model model,IndexRequest request) {
		PagingResponse<Product> plist = iService.sortList(params, request);
		List<Img> img_name = new ArrayList<>();// product와 img를 묶는 작업
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>(); // Product에 Discount(할인률)를 묶는 작업
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}

		model.addAttribute("dis", dis);
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", request.getCategory());
		
		return "prodlist";
	}
	
	/**
	 * 
	 * @param params 페이징 처리를 위해 데이터의 형태를 SearchDto의 형태로 만들었다.
	 * @param model model 로직을 실행 후 이동할 View의 이름
	 * @param keyword 검색 카테고리를 받기 위한 파라미터
	 * @param search 검색 내용을 받기 위한 파라미터
	 * @return 로직을 실행 후 이동할 View의 이름
	 */
	@GetMapping("/index/search")
	public String Search(@ModelAttribute("params") SearchDto params, Model model,
			@RequestParam("keyword") String keyword, @RequestParam("search") String search) {
		PagingResponse<Product> plist = iService.Search(params, keyword, search);
		
		List<Img> img_name = new ArrayList<>(); //Product에 Img를 묶는 작업
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>(); //Product에 Discount(할인률)를 묶는 작업
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);	
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("keyword", keyword);		
		return "prodlist";
	}
	
	
}
