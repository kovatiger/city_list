CREATE TABLE users_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    constraint fk_user FOREIGN KEY (user_id) references users (user_id),
    constraint fk_role FOREIGN KEY (role_id) references roles (role_id)
);