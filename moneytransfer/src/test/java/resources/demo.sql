
DROP TABLE IF EXISTS User;

CREATE TABLE User (user_id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
 first_name VARCHAR(100) NOT NULL,
 last_name VARCHAR(100) NOT NULL,
 email VARCHAR(255) NOT NULL);

CREATE UNIQUE INDEX idx_ue on User(email);

INSERT INTO User (first_name,last_name, email) VALUES ('Jane','Doe','jane@gmail.com');
INSERT INTO User (first_name,last_name, email) VALUES ('Jhon','Doe','jhon@gmail.com');



DROP TABLE IF EXISTS Account;

CREATE TABLE Account (account_id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
user_id LONG,
balance DECIMAL(19,4),
currency_code VARCHAR(5)
);

CREATE UNIQUE INDEX idx_acc on Account(user_id,currency_code);

INSERT INTO Account (user_id,balance,currency_code) VALUES ('1',100.0000,'USD');
INSERT INTO Account (user_id,balance,currency_code) VALUES ('2',200.0000,'USD');
INSERT INTO Account (user_id,balance,currency_code) VALUES ('1',500.0000,'EUR');
INSERT INTO Account (user_id,balance,currency_code) VALUES ('2',500.0000,'EUR');
INSERT INTO Account (user_id,balance,currency_code) VALUES ('1',500.0000,'GBP');
INSERT INTO Account (user_id,balance,currency_code) VALUES ('2',500.0000,'GBP');
