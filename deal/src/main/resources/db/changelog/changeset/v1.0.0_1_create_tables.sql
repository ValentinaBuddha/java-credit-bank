--liquibase formatted sql
--changeset Valentina Vakhlamova:create-tables

CREATE TABLE IF NOT EXISTS passport
(
    passport_id             UUID                   PRIMARY KEY,
    passport_data           JSONB
);

CREATE TABLE IF NOT EXISTS employment
(
    employment_id           UUID                   PRIMARY KEY,
    employment_data         JSONB
);

CREATE TABLE IF NOT EXISTS client
(
    client_id               UUID                   PRIMARY KEY,
    last_name               VARCHAR(30)  NOT NULL,
    first_name              VARCHAR(30)  NOT NULL,
    middle_name             VARCHAR(30),
    birth_date              DATE         NOT NULL,
    email                   VARCHAR(255),
    gender                  VARCHAR(10),
    marital_status          VARCHAR(30),
    dependent_amount        INT,
    passport_id             UUID                   REFERENCES passport (passport_id),
    employment_id           UUID                   REFERENCES employment (employment_id),
    account_number          VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS credit
(
    credit_id               UUID                   PRIMARY KEY,
    amount                  DECIMAL      NOT NULL,
    term                    INT          NOT NULL,
    monthly_payment         DECIMAL      NOT NULL,
    rate                    DECIMAL      NOT NULL,
    psk                     DECIMAL      NOT NULL,
    payment_schedule        JSONB        NOT NULL,
    insurance_enable        BOOLEAN      NOT NULL,
    salary_client           BOOLEAN      NOT NULL,
    credit_status           VARCHAR(30)  NOT NULL
);

CREATE TABLE IF NOT EXISTS statement
(
    statement_id            UUID                   PRIMARY KEY,
    client_id               UUID         NOT NULL  REFERENCES client (client_id),
    credit_id               UUID                   REFERENCES credit (credit_id),
    status                  VARCHAR(30),
    creation_date           TIMESTAMP    NOT NULL,
    applied_offer           JSONB,
    sign_date               TIMESTAMP,
    ses_code                VARCHAR(30),
    status_history          JSONB
);