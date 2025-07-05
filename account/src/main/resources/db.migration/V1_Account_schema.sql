CREATE type role_type as ENUM ('CONSUMER', 'PRO', 'ADMIN');
CREATE type legal_forms as ENUM ('SARL', 'SAS', 'SASU', 'EI', 'EURL', 'SA', 'SNC', 'SCOP');
CREATE TABLE IF NOT EXISTS account (
    account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    credentials_id UUID NOT NULL UNIQUE,
    role_type role_type NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    phone_number VARCHAR NOT NULL,
    birth_date DATE,
    city VARCHAR,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pro_account (
    account_id UUID PRIMARY KEY REFERENCES account(account_id) ON DELETE CASCADE,
    legal_name          VARCHAR(255) NOT NULL,
    legal_form          legal_forms NOT NULL,
    trade_name          VARCHAR(255),
    business_registration_number VARCHAR(50) NOT NULL,
    vat_number          VARCHAR(50),
    industry_code       VARCHAR(20),
    address             VARCHAR(255) NOT NULL
);



