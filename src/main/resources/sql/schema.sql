DROP TABLE IF EXISTS USERS CASCADE;

CREATE TABLE IF NOT EXISTS USERS (
    ID                  INT                 NOT NULL AUTO_INCREMENT,
    EMAIL               VARCHAR(45)         UNIQUE NOT NULL,
    PASSWORD            VARCHAR(255)        NOT NULL,
    NICKNAME            VARCHAR(15)         UNIQUE NOT NULL,
    NAME                VARCHAR(15)         NOT NULL,
    PHONE               VARCHAR(11)         NOT NULL,
    LOGIN_TYPE          CHAR(5)             NOT NULL,
    ENABLED             TINYINT(1)          NOT NULL,
    DELETED_YN          CHAR(1)             NOT NULL DEFAULT 'N',
    CREATED_AT          DATETIME            NOT NULL DEFAULT NOW(),
    UPDATED_AT          DATETIME            NOT NULL,
    DELETED_AT          DATETIME            NULL,
    PRIMARY KEY (ID)
);


DROP TABLE IF EXISTS USER_ROLES CASCADE;

CREATE TABLE IF NOT EXISTS USER_ROLES (
    USER_ID             INT                 NOT NULL,
    ROLE                VARCHAR(20)         NOT NULL,
    CREATED_AT          DATETIME            NOT NULL DEFAULT NOW(),
    UPDATED_AT          DATETIME            NOT NULL,
    PRIMARY KEY (USER_ID, ROLE),
    CONSTRAINT FK_USER_ROLES_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);