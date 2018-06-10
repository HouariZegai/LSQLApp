-- This database using in exercise type: hospital

--
-- remove database hospital if exists & create new database
--

DROP DATABASE IF EXISTS `hospital`;
CREATE DATABASE `hospital` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `hospital`;

-- -----------------------------------------------------------------------------
--
-- structure of the table nurse
--

CREATE TABLE `nurse` (
	`employeeid` INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`position` VARCHAR(50) NOT NULL,
	`registered` BOOLEAN NOT NULL,
	`ssn` INT(20) NOT NULL
	);

--
-- the content of the table nurse
--

INSERT INTO `nurse` (`employeeid`, `name`, `position`, `registered`, `ssn`) VALUES (1, 'Carla Espinosa', 'Head Nurse', 1, 111111110);
INSERT INTO `nurse` (`employeeid`, `name`, `position`, `registered`, `ssn`) VALUES (2, 'Leverne Roberts', 'Nurse', 1, 22222220);
INSERT INTO `nurse` (`employeeid`, `name`, `position`, `registered`, `ssn`) VALUES (3, 'Paul Flowers', 'HeadNurse', 0, 333333330);

-- ---------------------------------------------------------------
--
-- structure of the table physician
--

CREATE TABLE `physician` (
	`employeeid` INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`position` VARCHAR(50) NOT NULL,
	`ssn` INT(20) NOT NULL);

--
-- the content of the table physician
--

INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (1, 'John Dorian', 'Staff Internist', 111111110);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (2, 'Elliot Reid', 'Attending Physician', 222222220);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (3, 'Christopher Turk', 'Surgical Attending Physician', 333333330);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (4, 'Percival Cox', 'Senior Attending Physician', 444444440);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (5, 'Bob Kelso', 'Head Chief of Medicine', 555555550);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (6, 'Todd Quinlan', 'Surgical Attending Physician', 666666660);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (7, 'John Wen', 'Surgical Attending Physician', 777777770);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (8, 'Keith Dudemeister', 'MD Resident', 888888880);
INSERT INTO `physician` (`employeeid`, `name`, `position`, `ssn`) VALUES (9, 'Molly Clock', 'Attending Psychiatrist', 999999990);

-- --------------------------------------------------------------------------------
--
-- structure of the table department
--

CREATE TABLE `department` (
	`departmentid` INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`head` INT(10) NOT NULL);

--
-- the content of the table department
--

INSERT INTO `department` (`departmentid`, `name`, `head`) VALUES (1, 'General Medicine', 4);
INSERT INTO `department` (`departmentid`, `name`, `head`) VALUES (2, 'Surgery', 7);
INSERT INTO `department` (`departmentid`, `name`, `head`) VALUES (3, 'Psychiatry', 9);

-- ----------------------------------------------------------------------------------------
--
-- structure of the table appointment
--

CREATE TABLE `appointment` (
	`appointmentid` INT PRIMARY KEY AUTO_INCREMENT,
	`patient` INT NOT NULL,
	`prepnurse` INT(15),
	`physician` INT(15) NOT NULL,
	`start_dt_time` VARCHAR(30) NOT NULL,
	`end_dt_time`  VARCHAR(30) NOT NULL,
	`examinationroom`  VARCHAR(3) NOT NULL
	);

--
-- the content of the table appointment
--

INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (13216584, 100000001, 101, 1, '2008-04-24 10:00:00', '2008-04-24 11:00:00', 'A');
INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (26548913, 100000002, 101, 2, '2008-04-24 10:00:00', '2008-04-24 11:00:00', 'B');
INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (36549879, 100000001, 102, 1, '2008-04-25 10:00:00', '2008-04-24 11:00:00', 'A');
INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (46846589, 100000004, 103, 4, '2008-04-25 10:00:00', '2008-04-24 11:00:00', 'B');
INSERT INTO `appointment` (`appointmentid`, `patient`,              `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (59871321, 100000004     , 4, '2008-04-26 10:00:00', '2008-04-24 11:00:00', 'C');
INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (69879231, 100000003, 103, 2, '2008-04-26 11:00:00', '2008-04-24 12:00:00', 'C');
INSERT INTO `appointment` (`appointmentid`, `patient`,              `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (76983231, 100000001     , 3, '2008-04-26 12:00:00', '2008-04-24 13:00:00', 'C');
INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (86213939, 100000004, 102, 9, '2008-04-27 10:00:00', '2008-04-24 11:00:00', 'A');
INSERT INTO `appointment` (`appointmentid`, `patient`, `prepnurse`, `physician`, `start_dt_time`, `end_dt_time`, `examinationroom`) VALUES (93216548, 100000002, 101, 2, '2008-04-27 10:00:00', '2008-04-24 11:00:00', 'B');

-- --------------------------------------------------------------------------------------------------------
--
-- structure of the table room
--

CREATE TABLE `room` (
	`roomnumber` INT(15) PRIMARY KEY AUTO_INCREMENT,
	`roomtype` VARCHAR(15) NOT NULL,
	`blockfloor` INT(5),
	`blockcode` INT(5) NOT NULL,
	`unavailable` BOOLEAN NOT NULL
	);

--
-- the content of the table room
--

INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (101, 'Single', 1, 1, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (102, 'Single', 1, 1, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (103, 'Single', 1, 1, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (111, 'Single', 1, 2, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (112, 'Single', 1, 2, 'true');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (113, 'Single', 1, 2, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (121, 'Single', 1, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (122, 'Single', 1, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (123, 'Single', 1, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (201, 'Single', 2, 1, 'true');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (202, 'Single', 2, 1, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (203, 'Single', 2, 1, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (211, 'Single', 2, 2, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (212, 'Single', 2, 2, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (213, 'Single', 2, 2, 'true');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (221, 'Single', 2, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (222, 'Single', 2, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (223, 'Single', 2, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (301, 'Single', 3, 1, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (302, 'Single', 3, 1, 'true');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (411, 'Single', 4, 2, 'true');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (412, 'Single', 4, 2, 'true');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (421, 'Single', 4, 3, 'false');
INSERT INTO `room` (`roomnumber`, `roomtype`, `blockfloor`, `blockcode`, `unavailable`) VALUES (422, 'Single', 4, 3, 'false');


-- ----------------------------------------------------------------------------------------------------
--
-- structure of the table procedure
--

CREATE TABLE `procedure` (
	`code` INT(15) PRIMARY KEY AUTO_INCREMENT,
	`name`  VARCHAR(80) NOT NULL,
	`cost`  INT(15) NOT NULL
	);

--
-- the content of the table procedure
--

INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (1, 'Reverse Rhinopodoplasty', 1500);
INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (2, 'Obtuse Pyloric Recombobulation', 3750);
INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (3, 'Folded Demiophtalmectomy', 4500);
INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (4, 'Complete Walletectomy', 10000);
INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (5, 'Obfuscated Dermogastrotomy', 4899);
INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (6, 'Reversible Pancreomyoplasty', 5600);
INSERT INTO `procedure` (`code`, `name`, `cost`) VALUES (7, 'Follicular Demiectomy', 25);

-- ----------------------------------------------------------------------------------------------------
--
-- structure of the table patient
--

CREATE TABLE `patient`(
	`ssn` INT(15) PRIMARY KEY AUTO_INCREMENT,
	`name`  VARCHAR(80) NOT NULL,
	`address`  VARCHAR(100) NOT NULL,
	`phone`  VARCHAR(100) NOT NULL,
	`insuranceid`  INT(15) NOT NULL,
	`pcp`  INT(15) NOT NULL
	);

	--
	-- the content of the table patient
	--

	INSERT INTO `patient` (`ssn`, `name`, `address`, `phone`, `insuranceid`, `pcp`) VALUES (100000001, 'John Smith', '42 Foobar Lane', '555-0256', 68476213, 1);
	INSERT INTO `patient` (`ssn`, `name`, `address`, `phone`, `insuranceid`, `pcp`) VALUES (100000002, 'Grace Ritchie', '37 Snafu Drive', '555-0512', 36546321, 2);
	INSERT INTO `patient` (`ssn`, `name`, `address`, `phone`, `insuranceid`, `pcp`) VALUES (100000003, 'Random J. Patient', '101 Omgbbq Street', '555-1204', 65465421, 2);
	INSERT INTO `patient` (`ssn`, `name`, `address`, `phone`, `insuranceid`, `pcp`) VALUES (100000004, 'Dennis Doe', '1100 Foobaz Avenue', '555-2048', 68421879, 3);
