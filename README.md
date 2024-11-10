## 프로젝트 환경
1. boot 3.2.11
2. jdk 17

## 프로젝트 실행방법 
1. api 프로젝트 src/resources/application.yml 생성
```server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: url
    username: test
    password: 1234

springdoc:
  swagger-ui:
    tags-sorter: alpha
    operations-sorter: method

jwt:
  secret: bWluZHNoYXJlYXBpbWluZHNoYXJlYXBpbWluZHNoYXJlYXBpbWluZHNoYXJlYXBpbWluZHNoYXJlYXBp
```

2. swagger : http://localhost:8080/swagger-ui/index.html
3. 테이블
```
-- auto-generated definition
create table member
(
    id         bigint auto_increment comment '아이디'
        primary key,
    email      varchar(200)                         not null comment '이메일',
    name       varchar(200)                         not null comment '이름',
    password   varchar(200)                         not null comment '비밀번호',
    gender     enum ('F', 'M')                      not null comment '성별',
    is_deleted tinyint(1) default 0                 not null comment '삭제여부',
    created_at datetime   default CURRENT_TIMESTAMP not null comment '등록일',
    updated_at datetime   default CURRENT_TIMESTAMP not null comment '수정일',
    constraint member_email_uindex unique (email)
);



-- auto-generated definition
create table member_role
(
    id         bigint auto_increment comment '아이디'
        primary key,
    member_key bigint                               not null comment '회원 아이디',
    role       enum ('MEMBER', 'MANAGER')           not null comment '권한',
    is_deleted tinyint(1) default 0                 not null comment '삭제여부',
    created_at datetime   default CURRENT_TIMESTAMP not null comment '등록일',
    updated_at datetime   default CURRENT_TIMESTAMP not null comment '수정일',
    constraint fk_member_role_1
        foreign key (member_key) references member (id)
);


-- auto-generated definition
create table board
(
    id         bigint auto_increment comment '아이디'
        primary key,
    type       enum ('SHARE', 'NOTICE')             not null comment '게시글 유형',
    title      varchar(200)                         not null comment '제목',
    content    text                                 null comment '내용',
    views      int        default 0                 not null comment '조회수',
    is_deleted tinyint(1) default 0                 not null comment '삭제여부',
    created_by bigint                               not null comment '등록자',
    created_at datetime   default CURRENT_TIMESTAMP not null comment '등록일',
    updated_by bigint                               not null comment '수정자',
    updated_at datetime   default CURRENT_TIMESTAMP not null comment '수정일',
    constraint fk_board_1
        foreign key (created_by) references member (id),
    constraint fk_board_2
        foreign key (updated_by) references member (id)
);




```

## 고민한점이나 아쉬운점 
1. 모듈 구성 고민
   core/api core/api/auth 3개로 나눌지 고민했지만 security관련 코드를 api에 넣어 간단하게 구성했습니다.
2. 모듈 별 관리 포인트 고민
   core : entity/repository/util/enum 관리
   api : controller/service/dto 관리

## API 목록
<img width="1267" alt="스크린샷 2024-11-11 오전 2 40 47" src="https://github.com/user-attachments/assets/e6546f86-ddf4-44ff-bc7e-eb6c26f7e186">
