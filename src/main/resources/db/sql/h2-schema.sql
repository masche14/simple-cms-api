DROP TABLE IF EXISTS contents CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 사용자 테이블
CREATE TABLE users
(
    user_id             VARCHAR(50) PRIMARY KEY  NOT NULL UNIQUE,
    password           VARCHAR(255)             NOT NULL,
    role               VARCHAR(20)              NOT NULL,
    register_date      TIMESTAMP DEFAULT NOW()  NOT NULL,
    register_by        VARCHAR(50)
);

-- 콘텐츠 테이블
CREATE TABLE contents
(
    id                 BIGINT PRIMARY KEY      NOT NULL AUTO_INCREMENT,
    title              VARCHAR(100)            NOT NULL,
    description        TEXT,
    view_count         BIGINT                  NOT NULL,
    created_date       TIMESTAMP,
    created_by         VARCHAR(50)             NOT NULL,
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(50)
);
