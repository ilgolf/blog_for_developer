CREATE TABLE POST
(
    post_id          BIGINT AUTO_INCREMENT NOT NULL COMMENT '게시글 식별자',
    member_id        BIGINT                             NOT NULL COMMENT '회원 식별자',
    title            VARCHAR(100)                       NOT NULL COMMENT '게시글 제목',
    content          TEXT                               NOT NULL COMMENT '게시글 내용',
    post_save_status VARCHAR(20)                        NOT NULL COMMENT '게시글 저장 상태',
    view_count       BIGINT   DEFAULT 0                 NOT NULL COMMENT '조회수',
    like_count       BIGINT   DEFAULT 0                 NOT NULL COMMENT '좋아요 수',
    reply_count      BIGINT   DEFAULT 0                 NOT NULL COMMENT '댓글 수',
    deleted          BOOLEAN  DEFAULT FALSE             NOT NULL COMMENT '삭제 여부',
    created_by       BIGINT                             NOT NULL COMMENT '게시글 작성자',
    last_modified_by BIGINT                             NOT NULL COMMENT '게시글 수정자',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_At DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (post_id)
)