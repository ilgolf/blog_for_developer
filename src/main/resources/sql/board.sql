CREATE TABLE BOARD
(
    board_id               BIGINT AUTO_INCREMENT NOT NULL COMMENT '게시판 식별자',
    name             VARCHAR(30)                        NOT NULL COMMENT '게시판 이름',
    description      TEXT NULL COMMENT '게시판 설명',
    board_url        VARCHAR(30)                        NOT NULL COMMENT '게시판 URL',
    view_count       BIGINT   DEFAULT 0                 NOT NULL COMMENT '조회수',
    post_count       BIGINT   DEFAULT 0                 NOT NULL COMMENT '게시글 수',
    deleted          BOOLEAN  DEFAULT FALSE             NOT NULL COMMENT '삭제 여부',
    created_by       BIGINT                             NOT NULL COMMENT '게시판 작성자',
    last_modified_by BIGINT                             NOT NULL COMMENT '게시판 수정자',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
)