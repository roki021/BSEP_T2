INSERT INTO role (id, name) VALUES (0, 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES (1, 'ROLE_DOCTOR');

INSERT INTO privilege (id, name) VALUES (0, 'READ_DEVICES_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (1, 'WRITE_DEVICES_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (2, 'CREATE_CSR_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (3, 'CHANGE_TOKEN_STATUS_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (4, 'READ_TOKEN_STATUS_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (5, 'READ_ALARMS_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (6, 'WRITE_ALARMS_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (7, 'DELETE_ALARMS_PRIVILEGE');

INSERT INTO hospital_user (id, username, firstName, lastName, email, password, locked, lastAccess)
VALUES (0, 'admin', 'Jovan', 'Gruncic', 'zdravko.dugi@hospital-a.com', '$2y$12$xpC9vhFB42C6M8loyxyyBemcr77Lh603KMDRi0GXqC8W2PzfSynPu', false, CURRENT_TIMESTAMP);
-- K1inik@12E

INSERT INTO hospital_user (id, username, firstName, lastName, email, password, locked, lastAccess)
VALUES (1, 'doctorPera', 'Petar', 'Kralj', 'doctor.pera@hospital-a.com', '$2y$12$xpC9vhFB42C6M8loyxyyBemcr77Lh603KMDRi0GXqC8W2PzfSynPu', false, CURRENT_TIMESTAMP);
-- K1inik@12E

INSERT INTO hospital_user (id, username, firstName, lastName, email, password, locked, lastAccess)
VALUES (2, 'doctorSteva', 'Stevan', 'Sremac', 'doctor.steva@hospital-a.com', '$2y$12$xpC9vhFB42C6M8loyxyyBemcr77Lh603KMDRi0GXqC8W2PzfSynPu', false, CURRENT_TIMESTAMP);
-- K1inik@12E

INSERT INTO hospital_users_roles (hospital_user_id, role_id) VALUES (0, 0);
INSERT INTO hospital_users_roles (hospital_user_id, role_id) VALUES (1, 1);
INSERT INTO hospital_users_roles (hospital_user_id, role_id) VALUES (2, 1);

INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 1);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 2);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 3);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 4);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 5);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 6);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (0, 7);

INSERT INTO users_privileges (user_id, privilege_id) VALUES (1, 0);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (1, 5);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (1, 6);
INSERT INTO users_privileges (user_id, privilege_id) VALUES (1, 7);
--INSERT INTO users_privileges (user_id, privilege_id) VALUES (1, 1);

INSERT INTO users_privileges (user_id, privilege_id) VALUES (2, 0);