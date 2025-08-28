CREATE TYPE role_type AS ENUM ('CONSUMER', 'PRO', 'ADMIN');
CREATE TYPE legal_forms AS ENUM ('SARL', 'SAS', 'SASU', 'EI', 'EURL', 'SA', 'SNC', 'SCOP');

CREATE TABLE IF NOT EXISTS account (
    account_id INT PRIMARY KEY DEFAULT gen_random_uuid(),
    credentials_id UUID NOT NULL UNIQUE,
    role_type role_type NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    phone_number VARCHAR UNIQUE NOT NULL,
    birth_date DATE,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_account_role_type ON account(role_type);

CREATE TABLE IF NOT EXISTS pro_informations (
    account_id INT PRIMARY KEY REFERENCES account(account_id) ON DELETE CASCADE,
    kbis_ref VARCHAR,
    siret VARCHAR NOT NULL,
    address1 VARCHAR NOT NULL,
    address2 VARCHAR NOT NULL,
    address3 VARCHAR NOT NULL,
    city VARCHAR NOT NULL,
    country VARCHAR NOT NULL,
    cp VARCHAR NOT NULL,
    legal_form   legal_forms NOT NULL,
    legal_name VARCHAR NOT NULL
);



