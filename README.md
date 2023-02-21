# yesaladin_shop
YesAladin Shop은 애플리케이션 이용에 필요한 API를 제공함으로써 클라이언트의 요청을 받아 이에  서비스를 수행하는 API Server입니다.

## Getting Started

```bash
./mvnw spring-boot:run
```

## ERD

// 이미지 추가할 것
![]()

## Project Architecture

// 이미지 추가할 것
![]()

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

### [@최예린](https://github.com/Yellin36)

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

### [@서민지](https://github.com/narangd0)

### [@김선홍](https://github.com/ssun4098)

### 공통
- SonarQube 및 체크 스타일을 활용한 코드 품질 개선 및 컨벤션 체크
- Unit Test 수행

## Technical Issue
### Kafka (가제)

### 주문/결제 (가제)
- 결제 서비스 메서드 실행 중, RollBack 되어야는 상황에 토스와의 통신이 정상 종료된 경우
  - 데이터베이스는 트랜잭션이 비정상 종료되어 rollback이 되었다.
  - 하지만 토스 결제 취소는 HTTP 통신이므로 rollback과는 무관하게 동작하므로 대책이 필요 
  - **TransactionEventListener** 를 사용하여 해결 
    - phase를 _TransactionPhase.AFTER_ROLLBACK_ 으로 설정하여 사용 

### Web Socket (가제)

### 인증/인가 (가제)
- JWT 토큰 검증 결과 payload에 포함된 식별 정보를 통해 Shop API Server 내 `OncePerRequestFilter`로 자체 인증 객체 생성
  - 생성된 `Authentication`에 포함된 `Roles`를 사용하여 API에 Method Security 적용
  - 생성된 `Authentication`에 포함된 `loginId`를 통해 AOP를 구성하여 회원 자신의 개인정보에 관련된 API 호출 시 개인정보 노출 최소화

### Object Storage (가제)

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
