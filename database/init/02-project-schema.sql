\connect project_db;

CREATE TABLE IF NOT EXISTS projects (
    project_id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_members (
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(32) NOT NULL,
    can_read BOOLEAN NOT NULL DEFAULT TRUE,
    can_add BOOLEAN NOT NULL DEFAULT FALSE,
    can_modify BOOLEAN NOT NULL DEFAULT FALSE,
    can_delete BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_project_members_project
        FOREIGN KEY (project_id) REFERENCES projects(project_id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_projects_owner_id ON projects(owner_id);
CREATE INDEX IF NOT EXISTS idx_project_members_user_id ON project_members(user_id);

CREATE TABLE IF NOT EXISTS project_invitations (
    invitation_id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    invited_email VARCHAR(255) NOT NULL,
    invited_by BIGINT NOT NULL,
    token VARCHAR(128) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,
    can_read BOOLEAN NOT NULL DEFAULT TRUE,
    can_add BOOLEAN NOT NULL DEFAULT FALSE,
    can_modify BOOLEAN NOT NULL DEFAULT FALSE,
    can_delete BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_invitations_project
        FOREIGN KEY (project_id) REFERENCES projects(project_id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_project_invitations_project_id ON project_invitations(project_id);
CREATE INDEX IF NOT EXISTS idx_project_invitations_invited_email ON project_invitations(invited_email);
CREATE INDEX IF NOT EXISTS idx_project_invitations_token ON project_invitations(token);
