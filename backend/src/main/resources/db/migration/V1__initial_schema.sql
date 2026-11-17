CREATE TABLE organisations (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL
);
CREATE TABLE users (
    id UUID PRIMARY KEY,
    organisation_id UUID NOT NULL REFERENCES organisations(id),
    email VARCHAR(160) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(80) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('SUPER_ADMIN','ADMIN','TECHNICIAN','EMPLOYEE')),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_user_org_email UNIQUE (organisation_id, email)
);
CREATE INDEX idx_users_organisation ON users(organisation_id);
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ
);
CREATE INDEX idx_refresh_token_hash ON refresh_tokens(token_hash);
