CREATE TABLE if not exists role (
    role_id SERIAL PRIMARY KEY,
    name varchar(200) NOT NULL UNIQUE
);

CREATE TABLE if not exists scope (
    scope_id SERIAL PRIMARY KEY,
    action varchar(50) NOT NULL,
    target varchar(50) NOT NULL
);

CREATE TABLE if not exists role_scope (
    role_id INT NOT NULL,
    scope_id INT NOT NULL,
    PRIMARY KEY (role_id, scope_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (scope_id) REFERENCES scope(scope_id) ON DELETE CASCADE
);

CREATE TABLE if not exists credentials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(150) NOT NULL UNIQUE,
    password varchar(200) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);