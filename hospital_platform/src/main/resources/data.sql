INSERT INTO role (id, name) VALUES (0, 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES (1, 'ROLE_DOCTOR');

INSERT INTO hospital_user (id, username, firstName, lastName, email, password)
VALUES (0, 'admin', 'Jovan', 'Gruncic', 'zdravko.dugi@hospital-a.com', '$2y$12$xpC9vhFB42C6M8loyxyyBemcr77Lh603KMDRi0GXqC8W2PzfSynPu');
-- K1inik@12E

INSERT INTO hospital_users_roles (hospital_user_id, role_id) VALUES (0, 0);