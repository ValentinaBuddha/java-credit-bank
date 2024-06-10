CREATE TABLE IF NOT EXISTS passport
(
    passport_id             UUID                   PRIMARY KEY,
    series                  VARCHAR(4)   NOT NULL,
    number                  VARCHAR(6)   NOT NULL,
    issue_branch            VARCHAR(255) NOT NULL,
    issue_date              DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS employment
(
    employment_id           UUID                   PRIMARY KEY,
    status                  VARCHAR(30)  NOT NULL,
    employer_inn            VARCHAR(12)  NOT NULL,
    salary                  DECIMAL      NOT NULL,
    position                VARCHAR(30)  NOT NULL,
    work_experience_total   INT          NOT NULL,
    work_experience_current INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS client
(
    client_id               UUID                   PRIMARY KEY,
    last_name               VARCHAR(30)  NOT NULL,
    first_name              VARCHAR(30)  NOT NULL,
    middle_name             VARCHAR(30),
    birth_date              DATE         NOT NULL,
    email                   VARCHAR(255) NOT NULL  UNIQUE,
    gender                  VARCHAR(10)  NOT NULL,
    marital_status          VARCHAR(30)  NOT NULL,
    dependent_amount        INT          NOT NULL,
    passport_id             UUID         NOT NULL  REFERENCES passport (id),
    employment_id           UUID         NOT NULL  REFERENCES employment (id),
    account_number          VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS credit
(
    credit_id               UUID                   PRIMARY KEY,
    amount                  DECIMAL      NOT NULL  CHECK (amount >= 30000),
    term                    INT          NOT NULL  CHECK (term >= 6),
    monthly_payment         DECIMAL      NOT NULL,
    rate                    DECIMAL      NOT NULL,
    psk                     DECIMAL      NOT NULL,
    payment_schedule        JSONB        NOT NULL,
    insurance_enable        BOOLEAN      NOT NULL,
    salary_client           BOOLEAN      NOT NULL,
    credit_status           VARCHAR(30)  NOT NULL
);

CREATE TABLE IF NOT EXISTS status_history
(
    status                  VARCHAR(20),
    time                    TIMESTAMP,
    change_type             VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS statement
(
    statement_id            UUID                   PRIMARY KEY,
    client_id               UUID         NOT NULL  REFERENCES client (id),
    credit_id               UUID         NOT NULL  REFERENCES credit (id),
    status                  VARCHAR(30)  NOT NULL,
    creation_date           TIMESTAMP    NOT NULL,
    applied_offer           JSONB        NOT NULL,
    sign_date               TIMESTAMP    NOT NULL,
    ses_code                VARCHAR(30)  NOT NULL,
    status_history          JSONB        NOT NULL
);