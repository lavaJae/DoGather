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


## 추가 작업
같은 기능을 하던 정렬 코드를 하나로 묶는 추가 작업을 진행했다.

4개로 분리 되어 있던 메소드를 정렬값을 파라미터로 받아 정렬값을 구현했다.
