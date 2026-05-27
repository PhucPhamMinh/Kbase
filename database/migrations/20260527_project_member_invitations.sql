\connect project_db;

ALTER TABLE project_members
    ADD COLUMN IF NOT EXISTS can_read BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS can_add BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS can_modify BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS can_delete BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE project_members
SET can_read = TRUE,
    can_add = TRUE,
    can_modify = TRUE,
    can_delete = TRUE
WHERE role = 'OWNER';

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
