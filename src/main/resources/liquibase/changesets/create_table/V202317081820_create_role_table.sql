CREATE TABLE IF NOT EXISTS user_roles
(
    id          UUID            NOT NULL,
    role        VARCHAR(255)    NOT NULL,
                                PRIMARY KEY(id,role),
    constraint fk_user_roles_user FOREIGN KEY (id) references users (user_id) ON DELETE CASCADE
);