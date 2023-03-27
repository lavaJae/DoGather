# DoGather
공동 구매 쇼핑몰
판매 기간을 정해놓고 판매한 수량에 따라 추가 할인률이 들어가는 쇼핑몰

## 사용기술
Java, Javascript, SpringBoot, Mysql, jquey, Mybatis, 

## 맡은 기능

* 쇼핑몰 메인 페이지

인기상품과 신상품을 불러오는 메인페이지

![스크린샷 2023-03-25 145751](https://user-images.githubusercontent.com/118964206/227972149-12b3af68-d59c-4d07-afb8-5eb7b3921ed5.png)

![스크린샷 2023-03-25 145315](https://user-images.githubusercontent.com/118964206/227972188-79f68040-a45a-4c7b-8ea5-578439929385.png)


* 카테고리 메뉴를 통해 이동한 페이지

카테고리 별 데이터를 받아 페이징 처리를 위해 스크립트로 구현

![스크린샷 2023-03-25 145837](https://user-images.githubusercontent.com/118964206/227972676-fc23eacc-72e4-45af-9211-c8aefea29c64.png)

![스크린샷 2023-03-25 145812](https://user-images.githubusercontent.com/118964206/227972709-488cb49f-fbe0-448d-841a-b8bfeaea1963.png)


* 검색결과 페이지

검색 된 데이터를 받아 카테고리와 같이 처리

![스크린샷 2023-03-25 145549](https://user-images.githubusercontent.com/118964206/227972829-5e55d309-6bc7-4412-8848-1f304de987ee.png)

* 판매자 리뷰 페이지

로그인 한 사람만 작성할 수 있게 했고 다시 조건을 걸어 판매자의 물건을 구매한 사람만 작성이 가능하도록 처리

리뷰도 페이징 처리를 위해 스크립트로 구현

![스크린샷 2023-03-25 145423](https://user-images.githubusercontent.com/118964206/227973022-8bbb3074-8bf1-4591-a9ff-c31bf0d7eeb0.png)


## 느낀점
처음 해보는 팀 프로젝트라 많이 버벅였다.
만들때는 그냥 만들었는데 나중에 보니 같은 기능을 하는 코드들을 하나하나 늘어놓은 것이 매우 낭비였고(정렬)
변수면과 메소드명의 규칙을 지키지 않고 너무 중구난방으로 짜놓게 되어 나중에 알아보기가 힘들었다.
더 많은 사전 준비가 필요하다고 느겼다. 또 타임리프를 처음으로 사용해서 많이 헤맨 것 같다. 다음 프로젝트를 
할때는 사용하게 될 기술을 미리 간단하게라도 사용해보고 시작을 해야겠다는 생각을 했다.


## 추가 작업
같은 기능을 하던 정렬 코드를 하나로 묶는 추가 작업을 진행했다.

4개로 분리 되어 있던 메소드를 정렬값을 파라미터로 받아 정렬값을 구현했다.


정렬 Html 


before

```
<li th:if="${category}==null" ><a href="/newlist">최신순</a></li>
<li th:if="${category}!=null" ><a th:href="|/newlist/${category}|">최신순</a></li>		        
<li th:if="${category}==null" ><a href="/pricelist">낮은가격순</a></li>
<li th:if="${category}!=null"><a th:href="|/pricelist/${category}|">낮은가격순</a></li>
<li th:if="${category}==null"><a href="/pricelistdesc">높은가격순</a></li>
<li th:if="${category}!=null"><a th:href="|/pricelistdesc/${category}|">높은가격순</a></li>
<li th:if="${category}==null"><a href="/bestlist">판매량순</a></li>
<li th:if="${category}!=null"><a th:href="|/bestlist/${category}|">판매량순</a></li>

```


after

정렬 키워드를 묶어서 파라미터로 전달

```
<li th:if="${category}==null and ${keyword}==null" ><a href="/index/filter?sort=p_recruitdate">최신순</a></li>
<li th:if="${category}!=null and ${keyword}==null" ><a th:href="|/index/filter?sort=p_recruitdate&category=${category}|">최신순</a></li>		        
<li th:if="${category}==null and ${keyword}==null" ><a href="/index/filter?sort=p_price">낮은가격순</a></li>
<li th:if="${category}!=null and ${keyword}==null"><a th:href="|/index/filter?sort=p_price&category=${category}|">낮은가격순</a></li>
<li th:if="${category}==null and ${keyword}==null"><a href="/index/filter?sort=p_price&type=desc">높은가격순</a></li>
<li th:if="${category}!=null and ${keyword}==null"><a th:href="|/index/filter?sort=p_price&category=${category}&type=desc|">높은가격순</a></li>
<li th:if="${category}==null and ${keyword}==null"><a href="/index/filter?sort=p_sell">판매량순</a></li>
<li th:if="${category}!=null and ${keyword}==null"><a th:href="|/index/filter?sort=p_sell&category=${category}|">판매량순</a></li>
```


정렬 Controller


before

```
@GetMapping(value = {"/newlist/{category}","/newlist"})
	public String newlist(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {
		PagingResponse<Product> plist = iService.newlist(params, p_category);
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		System.out.println(p_category);
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping(value = {"/pricelist/{category}","/pricelist"})
	public String pricelist(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {

		PagingResponse<Product> plist = iService.pricelist(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}

		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping(value = {"/pricelistdesc/{category}", "/pricelistdesc"})
	public String pricelistdesc(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {
		PagingResponse<Product> plist = iService.pricelistdesc(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping(value = {"/bestlist/{category}", "/bestlist"})
	public String bestlist(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {
		PagingResponse<Product> plist = iService.bestlist(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
```


after

```
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
```


정렬 Service


before

```
public PagingResponse<Product> newlist(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		System.out.println(p_category);
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.newlist(params);
		} else {
			list = pMapper.CategoryNew(map);
		}
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> pricelist(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.pricelist(params);
		} else {
			list = pMapper.CategoryPrice(map);
		}
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> pricelistdesc(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.pricelistdesc(params);
		} else {
			list = pMapper.CategoryPriceDesc(map);
		}
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> bestlist(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.bestlist(params);
		} else {
			list = pMapper.CategoryBest(map);
		}
		return new PagingResponse<>(list, pagination);
	}
```


after

```
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
```


정렬 Mapper


before

```
List<Product> CategoryNew(Map<String, Object> map);
	
  List<Product> CategoryPrice(Map<String, Object> map);

  List<Product> CategoryPriceDesc(Map<String, Object> map);

  List<Product> CategoryBest(Map<String, Object> map);

  List<Product> newlist(SearchDto params);

  List<Product> pricelist(SearchDto params);

  List<Product> pricelistdesc(SearchDto params);

  List<Product> bestlist(SearchDto params);
```


after

```
  List<Product> categorySortList(Map<String, Object> map);

	List<Product> sortList(Map<String, Object> map);
```


정렬 Mybatis


before

```
<!--  최신순 정렬  -->
<select id="newlist" parameterType="com.project.model.SearchDto" resultMap="ProductMap"> SELECT * FROM product order by p_recruitdate desc LIMIT #{pagination.limitStart},#{recordSize} </select>
<!--  낮은가격순 정렬  -->
<select id="pricelist" parameterType="com.project.model.SearchDto" resultMap="ProductMap"> SELECT * FROM product order by p_price LIMIT #{pagination.limitStart},#{recordSize} </select>
<!--  높은가격순 정렬  -->
<select id="pricelistdesc" parameterType="com.project.model.SearchDto" resultMap="ProductMap"> SELECT * FROM product order by p_price desc LIMIT #{pagination.limitStart},#{recordSize} </select>
<!--  판매량순 정렬  -->
<select id="bestlist" parameterType="com.project.model.SearchDto" resultMap="ProductMap"> SELECT * FROM product order by p_sell LIMIT #{pagination.limitStart},#{recordSize} </select>
<!--  카테고리 최신순 정렬  -->
<select id="CategoryNew" parameterType="hashMap" resultMap="ProductMap"> SELECT * FROM product WHERE p_category=#{p_category} order by p_recruitdate LIMIT #{limitStart},#{recordSize} </select>
<!--  카테고리 낮은가격순 정렬  -->
<select id="CategoryPrice" parameterType="hashMap" resultMap="ProductMap"> SELECT * FROM product WHERE p_category=#{p_category} order by p_price LIMIT #{limitStart},#{recordSize} </select>
<!--  카테고리 높은가격순 정렬  -->
<select id="CategoryPriceDesc" parameterType="hashMap" resultMap="ProductMap"> SELECT * FROM product WHERE p_category=#{p_category} order by p_price desc LIMIT #{limitStart},#{recordSize} </select>
<!--  카테고리 판매량순 정렬  -->
<select id="CategoryBest" parameterType="hashMap" resultMap="ProductMap"> SELECT * FROM product WHERE p_category=#{p_category} order by p_chk LIMIT #{limitStart},#{recordSize} </select>

```


after

```
    <!-- 전체 정렬 -->
    <select id="sortList" parameterType="hashMap" resultMap="ProductMap">
        SELECT * FROM product where p_chk = 'start'
        order by ${sort}
        <if test="type != null">
			desc
		</if> 
        LIMIT #{limitStart},#{recordSize}
    </select>
    <!-- 카테고리 정렬 -->
    <select id="categorySortList" parameterType="hashMap" resultMap="ProductMap">
		SELECT * FROM product WHERE p_category=#{p_category} and p_chk = 'start'
		order by ${sort}
		<if test="type != null">
			desc
		</if>
		LIMIT #{limitStart},#{recordSize}
	</select>
```
