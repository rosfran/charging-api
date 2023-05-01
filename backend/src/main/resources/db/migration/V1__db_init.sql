CREATE SEQUENCE IF NOT EXISTS sequence_solar_grid START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS sequence_role START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS sequence_network START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS sequence_state START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS public.sequence_user START WITH 1 INCREMENT BY 5;

CREATE TABLE solar_grid
(
    id              BIGINT      NOT NULL,
    name            VARCHAR(500) NOT NULL,
    id_network      BIGINT      NOT NULL,
    CONSTRAINT pk_solar_grid PRIMARY KEY (id)
);

CREATE TABLE network
(
    id              BIGINT      NOT NULL,
    name            VARCHAR(50),
    id_user         BIGINT      NOT NULL,
    CONSTRAINT pk_network PRIMARY KEY (id)
);

CREATE TABLE state
(
    id              BIGINT      NOT NULL,
    age             INT         NOT NULL,
    power_output    REAL         NOT NULL,
    id_solar_grid   BIGINT      NOT NULL,
    created_at      timestamp    NOT NULL,
    is_first_state bool         NOT NULL DEFAULT false,
    CONSTRAINT pk_state PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   BIGINT      NOT NULL,
    solarGrid VARCHAR(20) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE solarGrid
(
    id          BIGINT      NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(50),
    CONSTRAINT pk_type PRIMARY KEY (id)
);

CREATE TABLE public."user"
(
    id         BIGINT       NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(20)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE public.user_role
(
    id_role BIGINT NOT NULL,
    id_user BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (id_role, id_user)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_type UNIQUE (solarGrid);

ALTER TABLE solarGrid
    ADD CONSTRAINT uc_type_name UNIQUE (name);

ALTER TABLE public."user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE solar_grid
    ADD CONSTRAINT FK_TO_NETWORK_FROM_SOLAR_GRID FOREIGN KEY (id_network) REFERENCES solarGrid (id);

ALTER TABLE network
    ADD CONSTRAINT FK_TO_USER_FROM_NETWORK FOREIGN KEY (id_user) REFERENCES "user" (id);

ALTER TABLE public.user_role
    ADD CONSTRAINT FK_TO_ROLE_FROM_USER_ROLE FOREIGN KEY (id_role) REFERENCES role (id);

ALTER TABLE public.user_role
    ADD CONSTRAINT FK_TO_USER_FROM_USER_ROLE FOREIGN KEY (id_user) REFERENCES "user" (id);