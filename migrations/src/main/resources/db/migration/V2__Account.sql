CREATE TYPE legal_forms AS ENUM ('SARL', 'SAS', 'SASU', 'EI', 'EURL', 'SA', 'SNC', 'SCOP');

CREATE TABLE IF NOT EXISTS account (
    account_id SERIAL PRIMARY KEY,
    credentials_id UUID NOT NULL UNIQUE,
    role_type VARCHAR(50) NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    phone_number VARCHAR UNIQUE NOT NULL,
    birth_date DATE,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_account_role_type ON account(role_type);
CREATE INDEX IF NOT EXISTS idx_account_phoneNumber ON account(phone_number);

CREATE TABLE IF NOT EXISTS pro_informations (
    account_id     INT PRIMARY KEY,
    kbis_ref       VARCHAR,
    siret          VARCHAR(14) NOT NULL,
    address1       VARCHAR NOT NULL,
    address2       VARCHAR,
    address3       VARCHAR,
    city           VARCHAR NOT NULL,
    country        VARCHAR NOT NULL,
    cp             VARCHAR NOT NULL,
    legal_form     legal_forms NOT NULL,
    legal_name     VARCHAR NOT NULL,
    CONSTRAINT fk_proinfo_account
        FOREIGN KEY (account_id)
            REFERENCES account(account_id)
            ON DELETE CASCADE,
    CONSTRAINT uq_proinfo_siret UNIQUE (siret),
    CONSTRAINT chk_proinfo_siret_digits CHECK (siret ~ '^[0-9]{14}$')
);



