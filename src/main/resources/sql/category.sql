CREATE TABLE CATEGORY
(
    category_id               BIGINT AUTO_INCREMENT NOT NULL COMMENT '카테고리 식별자',
    name             VARCHAR(30)                        NOT NULL COMMENT '카테고리 이름',
    description      TEXT NULL COMMENT '카테고리 설명',
    parent_id        BIGINT                             NOT NULL COMMENT '부모 카테고리 식별자',
    order            INT                                NOT NULL COMMENT '카테고리 순서',
    use_yn           BOOLEAN                            NOT NULL COMMENT '카테고리 사용 여부',
    created_by       BIGINT                             NOT NULL COMMENT '카테고리 작성자',
    last_modified_by BIGINT                             NOT NULL COMMENT '카테고리 수정자',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
)