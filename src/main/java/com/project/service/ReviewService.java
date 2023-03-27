package com.project.service;

import java.io.File;
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
import com.project.model.Pagination;
import com.project.model.PagingResponse;
import com.project.model.Review;
import com.project.model.SearchDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {
	
	@Autowired
	private final ReviewMapper rMapper;
	
	@Value("${file.Upimg}")
	private String path;

	/**
	 * 
	 * @param r 리뷰 객체
	 * @throws Exception 트랜젝션 설정으로 에러발생시 롤백
	 */
	@Transactional
	public void ReviewAdd(Review r) throws Exception {
		rMapper.AddReview(r);
		for (MultipartFile img : r.getR_img()) {
			if (!img.isEmpty()) {// 리뷰는 이미지를 선택해서 넣을 수 있기에 넣을때를 설정
				AddImg(r.getR_img(), r.getR_id(), "r_img");
			}
		}

	}

	/**
	 * 
	 * @param params 페이징 처리를 위한 SearchDto 설정
	 * @param r_pnickname_m_fk 판매자의 닉네임 왜래키
	 * @return 페이징 처리를 넘기기 위해 페이징요청으로 설정
	 */
	public PagingResponse<Review> AllReview(SearchDto params, String r_pnickname_m_fk) {
		int count = rMapper.Review_count(r_pnickname_m_fk);
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("r_pnickname_m_fk", r_pnickname_m_fk);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Review> list = rMapper.ReviewList(map);
		return new PagingResponse<>(list, pagination);
	}

	public String findnick(String m_email) { // 작성자의 아이디를 찾는 작업

		return rMapper.findnick(m_email);
	}

	public int reviewct(String r_pnickname_m_fk) { // 리뷰의 갯수 조회
		return rMapper.Review_count(r_pnickname_m_fk);
	}

	public double reviewStar(String r_pnickname_m_fk) { // 리뷰의 평점 조회
		return rMapper.ReviewStar(r_pnickname_m_fk);
	}

	public void ReviewDel(int r_id) { // 리뷰 삭제 
		rMapper.RemoveReview(r_id);
		delimg(r_id);
	}

	/**
	 * 
	 * @param file MutipartFile 설정
	 * @param r_id 리뷰의 번호
	 * @param keyword 현재 들어가는 이미지 타입 (리뷰 ,판매메인, 판매상세)
	 * @throws Exception
	 */
	@Transactional // 이미지 생성 , 수정
	public void AddImg(List<MultipartFile> file, int r_id, String keyword) throws Exception {
		
		if (!CollectionUtils.isEmpty(file)) {
			for (MultipartFile imgfile : file) {
				String origName = imgfile.getOriginalFilename(); // 입력한 원본 파일의 이름
				String uuid = String.valueOf(UUID.randomUUID());// 문자+숫자의 랜덤한 파일명으로 변경
				String extension = origName.substring(origName.lastIndexOf(".")); // 원본파일의 파일확장자
				String savedName = uuid + extension; // 랜덤이름 + 확장자
				File converFile = new File(path, savedName); // path = 상품 이미지 파일의 저장 경로가 들어있는 프로퍼티 설정값
				if (!converFile.exists()) {
					converFile.mkdirs();
				}
				imgfile.transferTo(converFile); // --- 실제로 저장을 시켜주는 부분 , 해당 경로에 접근할 수 있는 권한이 없으면 에러 발생
				Img img = Img.builder().img_keyword(keyword).img_name(savedName).img_origname(origName)
						.img_rid_r_fk(r_id).build();
				rMapper.AddImg(img);
			}
		}
	}

	public void delimg(int img_rid_r_fk) {//리뷰 삭제
		rMapper.RemoveImg(img_rid_r_fk);
	}

	public int checkComments(String m_email, int p_id) { // 판매자 물건을 구매했는지 조회
		 
		 Map<String, Object> map = new HashMap<>();
		 
		 map.put("m_email", m_email);
		 map.put("p_id", p_id);
		 
		 int chk = rMapper.CheckComments(map);
		 
		 return chk;
	 }
}
