INSERT INTO ACT_ID_GROUP
(ID_, REV_, NAME_, TYPE_)
VALUES('camunda-admin', 1, 'camunda BPM Administrators', 'SYSTEM');
INSERT INTO ACT_ID_GROUP
(ID_, REV_, NAME_, TYPE_)
VALUES('client', 1, 'client', 'client');
INSERT INTO ACT_ID_GROUP
(ID_, REV_, NAME_, TYPE_)
VALUES('mobileService', 1, 'mobileService', 'mobileService');
INSERT INTO ACT_ID_GROUP
(ID_, REV_, NAME_, TYPE_)
VALUES('serviceCompany', 1, 'serviceCompany', 'serviceCompany');
INSERT INTO ACT_ID_GROUP
(ID_, REV_, NAME_, TYPE_)
VALUES('teleConsultant', 1, 'teleConsultant', 'teleConsultant');

INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('camunda', 1, 'Admin', 'Admin', 'admin@gmail.com', '{SHA-512}F+sLecuAK9rbnVYlGXQpGBZnFZdn53/mR6AFlz+4qoYF7i298Y58yuc+HybleNLM/6SmJjp8qlSuSyzxZYod0g==', '5BMe6ywEszn4cbbDq38FYA==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('client1', 1, 'client1', 'client1', 'client1@gmail.com', '{SHA-512}vyq54mjyh7muDaMg8lrgnPIoPpNRAooRCYbaO2/HVhyc5oCL6ALWDn4xI5X3+kAU01+U96qxvPc16oP1X6U4lw==', 'YXeU/l0kVZhgObrdpje8Dg==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('client2', 1, 'client2', 'client2', 'client2@gmail.com', '{SHA-512}lItTQwJH0S4uedIeCoOPbCxWYEQh+wK2CQfc6fubtYaBQGsNn9iGDNq8A7ggFkUSzW2oVOfhykHG5oO1vp/zvQ==', 'Vy998FOH4bRM0YGnB8BQAA==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('client3', 1, 'client3', 'client3', 'client3@gmail.com', '{SHA-512}BTk15JiibbzjGSfSG9HTj85noT4cxoNS4dpX/pgIK4jG5ItjHbQXXArRGkXASAZURU+DwvtBMFz5XhnBfOtb4g==', 'BybevO6ugbF6euMfm8v+Lg==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('mobService1', 1, 'mobService1', 'mobService1', 'mobService1@gmail.com', '{SHA-512}BaYXdg03Q59leMl8WxLv5jwKR+4Pde8/w5rGwoXKi/SVhjjcbttzJQBYxgMnJT6LpqPo6tfY9I+OjVo088KT5A==', 'IsbRIixL1A9OyDgUuL3IjA==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('mobService2', 1, 'mobService2', 'mobService2', 'mobService2@gmail.com', '{SHA-512}5Eu2J+3cRHIxK5doNj/p1TIzovAf/XPvF97ETZ7ECNuctG8vXuWs9KOpuM7aTnaOi4t0efFDGEg7Fewp9BOt1A==', '8qwnpeck7zrVxlQtwFTYcw==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('mobService3', 1, 'mobService3', 'mobService3', 'mobService3@gmail.com', '{SHA-512}JBl1I37EbzXhiFh8Pm50DDqDp01i21sowBAUmfGbkASq3msp8a54LGqT6mKa9XrkHti4Y9dFgDlLP6y9NGf6DA==', '/29nWPmPx4Pa0WNwgE+jnw==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('scOperator1', 1, 'scOperator1', 'scOperator1', 'scOperator1@gmail.com', '{SHA-512}PDTsLtZXVsZ9hiOF0dR6VAkXasWnFKJXJzrEB+RhI+wHo4XoFrLpiMgkdxvNO/MHjYGpiE28mu5hX+s0azNWxg==', 'TrzKLxo76/lJgJWtTfkEVA==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('scOperator2', 1, 'scOperator2', 'scOperator2', 'scOperator2@gmail.com', '{SHA-512}yJMidb6v+DZBYIT89GO0g9G4rrCBUfSw9yDTjhNsEfNv3pOVjqxAXBMuNUuidmr5kkwoUXTMLdbG/zcttPf7Rg==', 'wMHzZIt6+kpuyhpX1rwUpA==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('scOperator3', 1, 'scOperator3', 'scOperator3', 'scOperator3@gmail.com', '{SHA-512}K40vNui3IL11BwP3CePLXfL1IQGEH9yqwxgJj0USPeaYzQK2QdDkIhUKFJfWcQue8CCv2NSP4iUwn5H7QqxRaw==', 'Rh4ETR2tsvSBiVu18JhPpw==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('teleConsultant1', 1, 'teleConsultant1', 'teleConsultant1', 'teleConsultant1@gmail.com', '{SHA-512}XA58xzOD2NlRSNb9bnjUWmGyVQG9zJPYJV5494zB/fX21VkGD8AWWuYYz/URe7RPdY/lIZaxc+MvYTiDrLU9bw==', 'fDB8v1Vw4UbyTv1IVBDNeA==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('teleConsultant2', 1, 'teleConsultant2', 'teleConsultant2', 'teleConsultant2@gmail.com', '{SHA-512}/8dqTHx2c7IYYcga8RtsReQDz9EQBJIAqurvYSL0JtpLDohpAC2sgYbO+hRZ2QfHcBWxTwVBFAMLUeuC7aLR+A==', 'MSaP01RXtbc4WRgxygAWbw==', NULL, NULL, NULL);
INSERT INTO ACT_ID_USER
(ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_)
VALUES('teleConsultant3', 1, 'teleConsultant3', 'teleConsultant3', 'teleConsultant3@gmail.com', '{SHA-512}cy2Hkeh+RcwK0aU5w4wUufmZ9bA0qmfeuOVlSY/pqaM/6/pqyW83WAW4u9VV7hROBLuslWauuCFUQor1cfStKA==', 'OrRpRYrB/tYsE0z1icIzEA==', NULL, NULL, NULL);


INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('camunda', 'camunda-admin');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('client1', 'client');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('client2', 'client');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('client3', 'client');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('mobService1', 'mobileService');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('mobService2', 'mobileService');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('mobService3', 'mobileService');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('scOperator1', 'serviceCompany');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('scOperator2', 'serviceCompany');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('scOperator3', 'serviceCompany');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('teleConsultant1', 'teleConsultant');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('teleConsultant2', 'teleConsultant');
INSERT INTO ACT_ID_MEMBERSHIP
(USER_ID_, GROUP_ID_)
VALUES('teleConsultant3', 'teleConsultant');


