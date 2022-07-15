CREATE TABLE IF NOT EXISTS player (playerId BIGINT PRIMARY KEY, playerName VARCHAR(255));
CREATE TABLE IF NOT EXISTS account (accountNumber BIGINT PRIMARY KEY, balance DOUBLE PRECISION, playerId BIGINT);
CREATE TABLE IF NOT EXISTS transaction_history (transactionId VARCHAR(20) PRIMARY KEY, amount DOUBLE PRECISION, accountNumber BIGINT, updateDtime TIMESTAMP);

CREATE SEQUENCE IF NOT EXISTS player_seq AS INTEGER START WITH 10;
CREATE SEQUENCE IF NOT EXISTS account_seq AS INTEGER START WITH 1000;

insert into PLAYER(PLAYERID, PLAYERNAME) VALUES (101, 'PLAYER1');
insert into PLAYER(PLAYERID, PLAYERNAME) VALUES (102, 'PLAYER2');
insert into PLAYER(PLAYERID, PLAYERNAME) VALUES (103, 'PLAYER3');


INSERT INTO ACCOUNT(ACCOUNTNUMBER, BALANCE, PLAYERID) VALUES(1001, 100.0, 101);
INSERT INTO ACCOUNT(ACCOUNTNUMBER, BALANCE, PLAYERID) VALUES(1002, 50.0, 102);
INSERT INTO ACCOUNT(ACCOUNTNUMBER, BALANCE, PLAYERID) VALUES(1003, 200.0, 103);


INSERT INTO TRANSACTION_HISTORY(TRANSACTIONID, AMOUNT, ACCOUNTNUMBER, UPDATEDTIME) VALUES ('12345',100.0, 1001, '2022-07-13 17:06:55.082515');
INSERT INTO TRANSACTION_HISTORY(TRANSACTIONID, AMOUNT, ACCOUNTNUMBER, UPDATEDTIME) VALUES ('12346',50.0, 1002, '2022-07-13 17:06:55.082515');
INSERT INTO TRANSACTION_HISTORY(TRANSACTIONID, AMOUNT, ACCOUNTNUMBER, UPDATEDTIME) VALUES ('12347',100.0, 1003, '2022-07-13 17:06:55.082515');
INSERT INTO TRANSACTION_HISTORY(TRANSACTIONID, AMOUNT, ACCOUNTNUMBER, UPDATEDTIME) VALUES ('12348',100.0, 1003, '2022-07-13 17:06:55.082515');