# yesaladin_shop
YesAladin Shop은 애플리케이션 이용에 필요한 API를 제공함으로써 클라이언트의 요청을 받아 이에  서비스를 수행하는 API Server입니다.

## Getting Started

```bash
./mvnw spring-boot:run
```

## ERD

![ERD](http://drive.google.com/uc?export=view&id=1gE5gufiU6RAjOKba50ianKreUAw3hnl9)


## Project Architecture

<img width="1055" alt="스크린샷 2023-02-22 오전 10 15 46" src="https://user-images.githubusercontent.com/60968342/220495610-18e516d6-4bae-49b4-bfdb-84f72d890af5.png">

## CI/CD
(무중단 배포 및 CI/CD flow 첨부할 것)

## Test Coverage
- 목표 : 라인 커버리지 80% 이상 (2023.02.xx.(?) 기준 xx.x%)  
  (스크린샷 첨부할 것)

## Features

### [@송학현](https://github.com/alanhakhyeonsong)

- **회원 관리**
  - 회원 가입
    - 일반 회원 가입
    - OAuth2 회원 등록 시 추가 정보 입력 후 등록 (Member 테이블 단일화)
  - 회원 정보 수정
  - 회원 탈퇴
  - 회원 통계
- **Shop API Server 보안** (Co-authored-by: [@김홍대](https://github.com/mongmeo-dev))
  - Shop API Server 내 Spring Security를 추가하여 각 API 별 Method Security 적용

### [@이수정](https://github.com/sujeong68)

- **Object Storage**
  - `Object Storage` Properties 설정
  - 인증 토큰 발급 및 토큰 만료 전까지 `Redis` 내 보관
  - Front에서 Object Storage 인증 토큰 요청 시 `Redis` 내 토큰 획득 혹은 발급하여 Front에 전달
  - Front -> Shop 으로 `MultipartFile`을 전달받아 Object Storage 파일 업로드 후 업로드 정보를 Front에 전달
- **파일 관리**
  - DB에 업로드한 파일의 정보(URL, 업로드 시간)을 등록 및 조회
- **상품 관리**
  - 상품 등록/수정/Soft Delete
  - 상품의 판매여부, 강제품절여부, 노출여부, 재고수량 수정
  - 상품 제목, 중복여부, 수량 조회
  - 상품 상세 조회
  - 상품 수정용 정보 조회
  - 관리자용, 일반사용자용 `Paging` 조회
  - 장바구니 내 상품의 정보 조회
  - 상품 유형 전체 조회
  - 전체 할인율 조회/수정
- **상품 연관관계 관리**
  - 상품 간 연관관계 생성/삭제
  - 관리자용, 일반사용자용 `Paging` 조회
- **출판사, 출판 관리**
  - 출판사 등록/수정
  - 출판사 단건 조회
  - 관리자용 출판사 `Paging` 조회
  - 출판사와 상품 사이의 관계(=출판) 등록/삭제
  - 상품으로 출판사와 상품 사이의 관계(=출판) 조회
- **태그, 태그관계 관리**
  - 태그 등록/수정
  - 태그 단건 조회
  - 관리자용 태그 `Paging` 조회
  - 태그와 상품 사이의 관계 등록/삭제
  - 상품으로 태그와 상품 사이의 관계 조회
- **저자, 집필 관리**
  - 저자 등록/수정 
  - 저자 단건 조회
  - 관리자용 저자 `Paging` 조회
  - 저자와 상품사이의 관계(=집필) 등록/삭제
  - 상품으로 저자와 상품 사이의 관계(=집필) 조회
- **매출 통계 및 베스트셀러**
  - 정해진 기간의 매출 통계 조회
  - 1년 기준 베스트셀러 12개 조회
- **주문 후 장바구니 내 상품 삭제**

### [@최예린](https://github.com/Yellin36)
- **주문**
  - 회원/비회원 일반 주문 생성
  - 주문 숨김처리
  - 주문 확정
  - 쿠폰 사용
  - 포인트 사용
- **회원 관리**
  - 회원 배송지 등록/수정/삭제
  - 회원 등급 내역 기간별 `Paging` 조회
  - 회원 등급 조회
  - 회원 차단/차단해지
- **포인트 관리**
  - 포인트 사용/적립/집계 내역 생성
  - 포인트 사용/적립 `Paging` 내역 조회
  - 회원의 포인트 조회
- **주문 변경 상태 내역 관리**
  - 주문 변경 상태 내역 생성
- **주문 상품 관리**
  - 주문 상품 생성

### [@배수한](https://github.com/shbaeNhnacademy)

- **카테고리** 
  - 1차, 2차 카테고리와 같이 **자기참조** 형식의 카테고리 구현 
  - 엔티티의 Pk를 `Auto Increment`가 아닌 static한 값으로 사용 
    - 생성 : 1차 카테고리 10000만씩 증가, 2차 카테고리 100씩 증가 
    - 자동으로 생성될 경우, 상품-카테고리간의 관계 맵핑이 힘들어짐??? <- 설명 추가
  - 카테고리 등록/수정/삭제
  - 카테고리 단건/다건 `paging` 조회
  - 카테고리 순서 변경 
- **결제**
  - 결제 요청 및 승인 처리 by [Toss](https://docs.tosspayments.com/reference)
    - 카드 
    - 간편 결제 
  - 결제 내역 조회
- 주문 조회
  - 기간별 주문 조회 및 일자 지정별 주문 조회 
  - 주문 상태 별 주문 조회 
  - 주문 상세 조회 (주문정보+상품정보+배송정보+결제정보)


### [@김홍대](https://github.com/mongmeo-dev)
- **쿠폰**
  - Kafka를 이용하여 쿠폰 서버와 메시지 기반 비동기 통신 구현
  - 마이크로서비스의 트랜잭션 보장을 위한 시스템 설계
- **Project Management**
  - NHN Cloud Log & Crash를 연동하여 모니터링 환경 구축
  - Spring Cloud Config를 연동하여 설정 정보 외부화

### [@서민지](https://github.com/narangd0)

### [@김선홍](https://github.com/ssun4098)

### 공통
- SonarQube 및 체크 스타일을 활용한 코드 품질 개선 및 컨벤션 체크
- Unit Test 수행

## Technical Issue
### Kafka (가제)

### 주문/결제
- 결제 서비스 메서드 실행 중, RollBack 되어야는 상황에 토스와의 통신이 정상 종료된 경우
  - 데이터베이스는 트랜잭션이 비정상 종료되어 rollback이 되었다.
  - 하지만 토스 결제 취소는 HTTP 통신이므로 rollback과는 무관하게 동작하므로 대책이 필요 
  - **TransactionEventListener** 를 사용하여 해결 
    - phase를 _TransactionPhase.AFTER_ROLLBACK_ 으로 설정하여 사용 

### Web Socket
- 서버가 이중화 되어있어 메시지를 전달하고자 하는 클라이언트에게 메시지가 전달되지 않을 수 있는 문제 발생
  - Redis의 PUB/SUB을 이용하여 모든 인스턴스에서 메시지를 전달할 수 있도록 구현
  - 설계 및 flow의 복잡도 증가 및 소켓 연결 유지로 인한 서버 부담 증가
    - 최종적으로 소켓 서버를 분리하여 해결

### 인증/인가
- JWT 토큰 검증 결과 payload에 포함된 식별 정보를 통해 Shop API Server 내 `OncePerRequestFilter`로 자체 인증 객체 생성
  - 생성된 `Authentication`에 포함된 `Roles`를 사용하여 API에 Method Security 적용
  - 생성된 `Authentication`에 포함된 `loginId`를 통해 AOP를 구성하여 회원 자신의 개인정보에 관련된 API 호출 시 개인정보 노출 최소화

### Spring Cache (가제)

### Elastic Search
- ELK 스택을 이용한 상품검색(비정형 데이터에 대한 색인과 검색 제공)
- 관리자가 데이터 관리를 위한 DB 검색(데이터의 정합성, 무결성을 보장하기 위해)


## Tech Stack

### Languages

![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java)

### Frameworks

![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)
![SpringCloud](https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=flat&logo=Spring&logoColor=white)
![Spring Security](https://img.shields.io/static/v1?style=flat-square&message=Spring+Security&color=6DB33F&logo=Spring+Security&logoColor=FFFFFF&label=)
![SpringDataJPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat&logo=Spring&logoColor=white)
![QueryDSL](http://img.shields.io/badge/QueryDSL-4479A1?style=flat-square&logo=Hibernate&logoColor=white)

### Database
![MySQL](http://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=flat&logo=Elasticsearch&logoColor=white)

### Build Tool

![ApacheMaven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=ApacheMaven&logoColor=white)

### DevOps

![Kafka](https://img.shields.io/badge/Kafka-231F20?style=flat&logo=ApacheKafka&logoColor=white)
![NHN Cloud](https://img.shields.io/badge/-NHN%20Cloud-blue?style=flat&logo=iCloud&logoColor=white)
![Jenkins](http://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E98CD?style=flat&logo=SonarQube&logoColor=white)

### Web Server

![NGINX](https://img.shields.io/badge/NGINX-009639?style=flat&logo=NGINX&logoColor=white)

### Unit Test

![Junit5](https://img.shields.io/badge/Junit5-25A162?style=flat&logo=Junit5&logoColor=white)

### 형상 관리 전략

![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white)

- Git Flow 전략을 사용하여 Branch를 관리하며 Main/Develop Branch로 Pull Request 시 코드 리뷰 진행 후 merge 합니다.
  ![image](https://user-images.githubusercontent.com/60968342/219870689-9b9d709c-aa55-47db-a356-d1186b434b4a.png)
- Main: 배포시 사용
- Develop: 개발 단계가 끝난 부분에 대해 Merge 내용 포함
- Feature: 기능 개발 단계
- Hot-Fix: Merge 후 발생한 버그 및 수정 사항 반영 시 사용

#### Dooray 칸반 보드 활용
(스크린샷 첨부할 것)

## Contributors

<a href="https://github.com/NHN-YesAladin/yesaladin_shop/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=NHN-YesAladin/yesaladin_front" />
</a>
