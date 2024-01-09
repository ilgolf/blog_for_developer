CREATE TABLE MEMBER
(
    member_id       BIGINT AUTO_INCREMENT                 NOT NULL COMMENT '회원 식별자',
    email           VARCHAR(50)                           NOT NULL UNIQUE COMMENT '이메일',
    password        VARCHAR(100)                          NOT NULL COMMENT '비밀번호',
    name            VARCHAR(30)                           NOT NULL COMMENT '회원 이름',
    nickname        VARCHAR(30)                           NOT NULL COMMENT '별칭',
    description     TEXT                                  NULL COMMENT '자기소개',
    jobType         VARCHAR(30)                           NOT NULL COMMENT '직업 유형',
    company         VARCHAR(30)                           NOT NULL COMMENT '재직 중인 회사 정보',
    experience      INT         DEFAULT 0                 NOT NULL COMMENT '경력 연차',
    role            VARCHAR(20) DEFAULT 'USER'            NOT NULL COMMENT '권한',
    follow_count    BIGINT      DEFAULT 0                 NOT NULL COMMENT '팔로워 수',
    following_count BIGINT      DEFAULT 0                 NOT NULL COMMENT '팔로워 수',
    board_count     BIGINT      DEFAULT 0                 NOT NULL COMMENT '게시판 수',
    activate        BOOLEAN     DEFAULT TRUE              NOT NULL COMMENT '회원 활성화 여부',
    created_at      DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (member_id)
);

create index ix_member_1 ON MEMBER (email, name, nickname);


select *
from MEMBER
where experience <> 2;

