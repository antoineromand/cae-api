CREATE TABLE if not exists role (
    role_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
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
    credentials_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(200) NOT NULL UNIQUE,
    password varchar(200) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

INSERT INTO role (role_id, name) VALUES 
    (1, 'CONSUMMER'), 
    (2, 'PRO'), 
    (3, 'ADMIN');

-- Scopes for CONSUMMER
INSERT INTO scope (scope_id, action, target) VALUES
    (1, 'read', 'menu'),
    (2, 'read', 'store'),
    (3, 'list', 'store'),
    (4, 'create', 'order'),
    (5, 'read', 'order'),
    (6, 'list', 'order'),
    (7, 'cancel', 'order'),
    (8, 'review', 'order'),
    (9, 'read', 'payment-method'),
    (10, 'add', 'payment-method'),
    (11, 'update', 'payment-method'),
    (12, 'delete', 'payment-method'),
    (13, 'update', 'user-password'),
    (14, 'update', 'user-profile'),
    (15, 'read', 'user'),
    (16, 'delete', 'user'),
    (17, 'read', 'order-status'),
    (18, 'read', 'notification'),
    (19, 'update', 'notification-settings'),
    (20, 'update', 'user-location');

-- Link CONSUMMER to previous scopes
INSERT INTO role_scope (role_id, scope_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (1, 11),
    (1, 12),
    (1, 13),
    (1, 14),
    (1, 15),
    (1, 16),
    (1, 17),
    (1, 18),
    (1, 19),
    (1, 20);


-- Link PRO to previous scopes
INSERT INTO scope (scope_id, action, target) VALUES
    (21, 'create', 'store'),
    (22, 'update', 'store'),
    (23, 'read', 'store'),
    
    (24, 'create', 'menu'),
    (25, 'read', 'menu'),
    (26, 'update', 'menu'),
    (27, 'delete', 'menu'),
    
    (28, 'list', 'order'),
    (29, 'read', 'order'),
    (30, 'update', 'order-status'),
    
    (31, 'update', 'user-profile'),
    (32, 'update', 'user-password');

-- Scope for ADMIN
INSERT INTO scope (scope_id, action, target) VALUES
    (33, '*', '*');

INSERT INTO role_scope (role_id, scope_id) VALUES
    (3, 33);