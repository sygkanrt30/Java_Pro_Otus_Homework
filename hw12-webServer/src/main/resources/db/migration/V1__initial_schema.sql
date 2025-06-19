CREATE TABLE address
(
    address_id BIGINT NOT NULL,
    street     VARCHAR(255),
    CONSTRAINT pk_address PRIMARY KEY (address_id)
);

CREATE TABLE client
(
    id         BIGINT NOT NULL,
    name       VARCHAR(255),
    address_id BIGINT,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

ALTER TABLE client
    ADD CONSTRAINT fk_client_on_address FOREIGN KEY (address_id) REFERENCES address (address_id);

CREATE TABLE phone
(
    phone_id  BIGINT NOT NULL,
    number    VARCHAR(255),
    client_id BIGINT,
    CONSTRAINT pk_phone PRIMARY KEY (phone_id)
);

ALTER TABLE phone
    ADD CONSTRAINT fk_phone_on_client FOREIGN KEY (client_id) REFERENCES client (id);

CREATE TABLE users
(
    id       BIGINT NOT NULL,
    name     VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

INSERT INTO users (id, name, password) VALUES (1, 'user7', '11111');