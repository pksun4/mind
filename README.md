## 프로젝트 환경
1. boot 3.2.11
2. jdk 17

## 프로젝트 실행방법 
1. application.yml 생성
2. swagger : http://localhost:8080/swagger-ui/index.html
3. 테이블

## 고민한점이나 아쉬운점 
1. 모듈 구성 고민
   core/api core/api/auth 3개로 나눌지 고민했지만 security관련 코드를 api에 넣어 간단하게 구성했습니다.
2. 모듈 별 관리 포인트 고민
   core : entity/repository/util/enum 관리
   api : controller/service/dto 관리

## API 목록
<img width="1267" alt="스크린샷 2024-11-11 오전 2 40 47" src="https://github.com/user-attachments/assets/e6546f86-ddf4-44ff-bc7e-eb6c26f7e186">
