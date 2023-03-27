package com.project.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.mapper.IndexMapper;
import com.project.mapper.ReviewMapper;
import com.project.model.Img;
import com.project.model.IndexRequest;
import com.project.model.Pagination;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.Review;
import com.project.model.SearchDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IndexService {

	@Autowired
	private final IndexMapper pMapper;

	@Autowired
	private final ReviewMapper rMapper;

	@Value("${file.Upimg}")
	private String path;

	public ArrayList<Product> Productbest() { // 메인 페이지 인기상품 호출 
		return pMapper.Productbest();
	}

	public ArrayList<Product> Productnew() { // 메인페이지 신상품 호출
		return pMapper.Productnew();
	}

	/**
	 * 
	 * @param params 페이징 처리를 위해 SearchDto로 설정
	 * @param p_category 카테고리 별 호출을 위해 카테고리를 매개변수로 설정
	 * @return 페이징 처리를 넘기기 위해 페이징요청으로 설정
	 */
	public PagingResponse<Product> Category(SearchDto params, String p_category) {
		int count = pMapper.category_count(p_category); // 카테고리별 상품의 숫자를 체크

		if (count < 1) { 
			return new PagingResponse<>(Collections.emptyList(), null); // 상품의 숫자가 없을때 검색결과 없음을 표시
		}

		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", pagination.getLimitStart()); // 페이징 첫 페이지 10 단뒤
		map.put("recordSize", params.getRecordSize()); // 페이징 뒷 페이지 10단위
		List<Product> list = pMapper.Category(map);
		return new PagingResponse<>(list, pagination);
	}

	/**
	 * 
	 * @param params 페이징 처리를 위해 SearchDto로 설정
	 * @return 페이징 처리를 넘기기 위해 페이징요청으로 설정
	 */
	public PagingResponse<Product> PagingList(SearchDto params) {
		int count = pMapper.count();
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);// 상품의 숫자가 없을때 검색결과 없음을 표시
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		List<Product> list = pMapper.PagingList(params);
		return new PagingResponse<>(list, pagination);

	}
	/**
	 * 
	 * @param params 페이징 처리를 위해 SearchDto로 설정
	 * @param request 정렬을 위한 파라미터
	 * @return 페이징 처리를 넘기기 위해 페이징요청으로 설정
	 */
	public PagingResponse<Product> sortList(SearchDto params, IndexRequest request) {
		int count;
		String p_category = request.getCategory();
		String sort = request.getSort();
		String type = request.getType();
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);// 상품의 숫자가 없을때 검색결과 없음을 표시
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("sort", sort);
		map.put("type", type);		
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());// 페이징 첫 페이지 10 단뒤
		map.put("recordSize", params.getRecordSize());// 페이징 뒷 페이지 10단위
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.sortList(map);
		} else {
			list = pMapper.categorySortList(map);
		}
		return new PagingResponse<>(list, pagination);

	}
	
	/**
	 * 
	 * @param params 페이징 처리를 위해 SearchDto로 설정
	 * @param keyword 검색 카테고리 파라미터
	 * @param search 검색 내용
	 * @return
	 */
	public PagingResponse<Product> Search(SearchDto params, String keyword, String search) {
		int count;
		if (keyword.equals("total")) {
			count = pMapper.SearchTotCount(search);
		} else {
			count = pMapper.SearchCount(keyword, search);
		}
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);// 상품의 숫자가 없을때 검색결과 없음을 표시
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("keyword", keyword);
		map.put("limitStart", params.getPagination().getLimitStart());// 페이징 첫 페이지 10 단뒤
		map.put("recordSize", params.getRecordSize());// 페이징 뒷 페이지 10단위
		List<Product> list = null;
		if (keyword.equals("total")) {
			list = pMapper.SearchTotal(map);
		} else {
			list = pMapper.Search(map);
		}
		return new PagingResponse<>(list, pagination);
	}

}
