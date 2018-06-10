-- Main DataBase

--
-- remove database learn_sql_db if exists & create new database
--

DROP DATABASE IF EXISTS `learn_sql_db`;
CREATE DATABASE `learn_sql_db` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `learn_sql_db`;

-- --------------------------------------------------------------------------
--
-- structure of the table users
--

CREATE TABLE `users`(
        `id_user`       INT PRIMARY KEY AUTO_INCREMENT,
        `username`      VARCHAR(30) NOT NULL,
        `password`      VARCHAR(50) NOT NULL,
        `first_name`    VARCHAR(50) NOT NULL,
        `last_name`     VARCHAR(25) NOT NULL,
        `date_of_birth` DATE,
        `email` VARCHAR(40),
        `is_teacher` BOOLEAN,
        `section` INT,
        `group` INT
);

--
-- the content of the table users
--

INSERT INTO `users` VALUES (1, "houarizegai", "0000", "houari", "zegai", "1996-11-17", "houarizegai14@gmail.com", 0, 1, 3);
INSERT INTO `users` VALUES (2, "yacine", "1234", "yacine", "raouia", "1996-07-27", "yacineraouia@gmail.com", 0, 1, 3);
INSERT INTO `users` (id_user, username, `password`, first_name, last_name, date_of_birth, email, is_teacher) VALUES (3, "ouared", "0000", "abdelkader", "ouared", "1987-01-02", "a_ouared@esi.dz", 1);

#------------------------------------------------------------
# Table: Log
#------------------------------------------------------------

CREATE TABLE `Log`(
        id_log INT PRIMARY KEY AUTO_INCREMENT,
        date_login DATETIME NOT NULL,
        date_logout DATETIME NOT NULL,
        id_user INT NOT NULL
);

-- ------------------------------------------------------------------------------------------
--
-- the structure of the table question
--

CREATE TABLE `question` (
	`id_question` INT PRIMARY KEY AUTO_INCREMENT,
	`type_question` VARCHAR(15) NOT NULL,
	`question` TEXT NOT NULL,
	`answer` TEXT NOT NULL
	);

--
-- the content of the table question
--

INSERT INTO `question` VALUES (1, 'basic', 'Write a query in SQL to display all the information of the employees.', 'SELECT * FROM employees;');
INSERT INTO `question` VALUES (2, 'basic', 'Write a query to get unique department ID from employees table.', 'SELECT DISTINCT dep_id FROM employees;');
INSERT INTO `question` VALUES (3, 'basic', 'Write a query in SQL to find the salaries of all employees', 'SELECT salary FROM employees;');
INSERT INTO `question` VALUES (4, 'basic', 'Write a query in SQL to display the unique designations for the employees.', 'SELECT DISTINCT job_name FROM employees;');
INSERT INTO `question` VALUES (5, 'basic', 'Write a query in SQL to list the emp_id,salary, and commission of all the employees.', 'SELECT emp_id, salary, commission FROM employees;');
INSERT INTO `question` VALUES (6, 'basic', 'Write a query in SQL to list the employees who does not belong to department 2001.', 'SELECT * FROM employees WHERE dep_id NOT IN (2001);');
INSERT INTO `question` VALUES (7, 'basic', 'Write a query in SQL to list the employees who joined before 1991.', "SELECT * FROM employees WHERE hire_date<('1991-1-1');");
INSERT INTO `question` VALUES (8, 'basic', 'Write a query in SQL to display the details of the employee BLAZE.', "SELECT * FROM employees WHERE emp_name = 'BLAZE';");
INSERT INTO `question` VALUES (9, 'basic', 'Write a query in SQL to find the details of highest paid employee.', 'SELECT * FROM employees WHERE salary IN (SELECT max(salary) FROM employees);');
INSERT INTO `question` VALUES (10, 'basic', 'Write a query in SQL to find the total salary given to the MANAGER.', "SELECT SUM(salary) FROM employees WHERE job_name = 'MANAGER';");
INSERT INTO `question` VALUES (11, 'hospital', "Write a query in SQL to find all the information of the nurses who are yet to be registered.", "SELECT * FROM `nurse` WHERE `registered`=0;");
INSERT INTO `question` VALUES (12, 'hospital', 'Write a query in SQL to find the name of the nurse who are the head of their department.', "SELECT `name` AS 'Name', POSITION AS 'Position' FROM `nurse` WHERE POSITION='Head Nurse';");
INSERT INTO `question` VALUES (13, 'hospital', 'Write a query in SQL to obtain the name of the physicians who are the head of each department.', "SELECT d.name AS 'Department', p.name AS 'Physician' FROM department d, physician p WHERE d.head=p.employeeid;");
INSERT INTO `question` VALUES (14, 'hospital', 'Write a query in SQL to count the number of patients who taken appointment with at least one physician.', "SELECT count(DISTINCT patient) AS 'No. of patients taken at least one appointment' FROM appointment;");
INSERT INTO `question` VALUES (15, 'hospital', 'Write a query in SQL to count the number available rooms.', "SELECT count(*) 'Number of available rooms' FROM room WHERE unavailable='false';");
INSERT INTO `question` VALUES (16, 'hospital', 'Write a query in SQL to count the number of unavailable rooms.', "SELECT count(*) 'Number of available rooms' FROM room WHERE unavailable='true';");
-- this question bellow N°: 27
INSERT INTO `question` VALUES (17, 'hospital', 'Write a query in SQL to find out the floor where the minimum no of rooms are available.', "SELECT blockfloor as 'Floor', count(*) AS  'No of available rooms' FROM room WHERE unavailable='false' GROUP BY blockfloor HAVING count(*) = (SELECT min(zz) AS highest_total FROM ( SELECT blockfloor, count(*) AS zz FROM room WHERE unavailable='false' GROUP BY blockfloor ) AS t );");
-- this question bellow N°: 26
INSERT INTO `question` VALUES(18, 'hospital', 'Write a query in SQL to find out the floor where the maximum no of rooms are available.', "SELECT blockfloor as 'Floor', count(*) AS  'No of available rooms' FROM room WHERE unavailable='false' GROUP BY blockfloor HAVING count(*) = (SELECT max(zz) AS highest_total FROM ( SELECT blockfloor , count(*) AS zz FROM room WHERE unavailable='false' GROUP BY blockfloor ) AS t );");
-- this question bellow N°: 25
INSERT INTO `question` VALUES(19, 'hospital', 'Write a query in SQL to count the number of unavailable rooms for each block in each floor.', "SELECT blockfloor AS 'Floor', blockcode AS 'Block', count(*) 'Number of available rooms' FROM room WHERE unavailable='true' GROUP BY blockfloor, blockcode ORDER BY blockfloor, blockcode;");
-- this question bellow N°: 24
INSERT INTO `question` VALUES(20, 'hospital', 'Write a query in SQL to count the number of available rooms for each block in each floor.', "SELECT blockfloor AS 'Floor', blockcode AS 'Block', count(*) 'Number of available rooms' FROM room WHERE unavailable='false' GROUP BY blockfloor, blockcode ORDER BY blockfloor, blockcode;");

#------------------------------------------------------------
# Table: do_exercise
#------------------------------------------------------------

CREATE TABLE `do_exercise`(
        `is_solved`        BOOLEAN DEFAULT 0,
        `n_syntax_error`   INT DEFAULT 0,
        `n_semantic_error` INT DEFAULT 0,
        `n_analyse_error`  INT DEFAULT 0,
        `id_user`          Int NOT NULL,
        `id_question`      Int NOT NULL,
        PRIMARY KEY (id_user ,id_question)
);

ALTER TABLE Log ADD CONSTRAINT FK_Log_id_user FOREIGN KEY (id_user) REFERENCES users(id_user);
ALTER TABLE do_exercise ADD CONSTRAINT FK_do_exercise_id_user FOREIGN KEY (id_user) REFERENCES users(id_user);
ALTER TABLE do_exercise ADD CONSTRAINT FK_do_exercise_id_question FOREIGN KEY (id_question) REFERENCES Question(id_question);