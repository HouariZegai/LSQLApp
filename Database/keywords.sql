USE `learn_sql_db`;

-- Structure of table keywords

CREATE TABLE `keywords` (
	`id` INT(11) PRIMARY KEY AUTO_INCREMENT, 
	`sql_key` VARCHAR(30) NOT NULL UNIQUE,
	`natural_hint` VARCHAR(80),
	`help_key` VARCHAR(30));

-- Content of table keywords

INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('SELECT', 'Use get', 'get');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('FROM', 'Use from', 'from');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('WHERE', 'Use condition', 'condition');

INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('GROUP BY', 'Use grouping', 'grouping');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('HAVING', 'Use condition of grouping', 'condition');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('ORDER BY', 'Use ordered', 'ordered');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('ASC', 'Use croissant', 'croissant');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('DESC', 'Use decroissant', 'decroissant');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('*', 'Use all', 'all');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('AS', 'Use as', 'as');

INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('TOP', 'Use first', 'first');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('RANDOM', 'Use random', 'random');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('DISTINCT', 'Use unique', 'unique');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('LIKE', 'Use like', 'like');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('%', 'Use percentage', 'precentage');

-- Agregation functions
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('COUNT', 'You can get number of', 'count');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('SUM', 'Use the sum', 'summe');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('AVG', 'You can calculate the average', 'average');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('MIN', 'You can calculate the minumum', 'minimum');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('MAX', 'You can calculate the maximum', 'maximum');

-- Arithmetic functions
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('ABS', 'Calculate the absolute value', 'absolute');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('SQRT', 'Calculate the sqrt', 'sqrt');


-- String functions
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('SUBSTRING', 'Device the string', 'substring');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('LEFT', 'Device the string but in left', 'left');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('RIGHT', 'Device the string but in right', 'right');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('LTRIM', 'Use LTRIM', 'lteim');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('RTRIM', 'USE RTRIM', 'ltrim');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('UPPER', 'Calculate upper case', 'uppercase');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('LOWER', 'Calculate lower case', 'lowercase');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('CHARINDEX', 'Calculate the index of char', 'index of char');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('LEN', 'Calculate the length', 'length');

-- Date functions
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('DATEADD', 'Calculate the aditional date', 'aditional');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('DATEDIFF', 'Calculate the different date', 'different');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('DATEPART', 'Calculate the part of date', 'part');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('GETDATE', 'Get the date', 'date');

-- Conversion Functions
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('STR', 'Calculate STR', 'str');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('CONVERT', 'Calculate the conversion', 'convert');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('CAST', 'Change the type', 'cast');

-- Algebra
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('UNION', 'Calculate union', 'union');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('INTERSECT', 'Calculate intersection', 'intersection');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('EXCEPT', 'Calculate except', 'except');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('CROSS JOIN', 'Calculate cross join', 'cross');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('JOIN', 'Calculate join', 'join');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('ON', 'Calculate on', 'on');

-- Comparasion
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('AND', 'Use comparasion And', 'and');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('OR', 'Use comparasion Or', 'or');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('XOR', 'Use comparasion Xor', 'xor');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('&&', 'Use comparasion And', 'and');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('=', 'Use equal', 'equal');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('!=', 'Use not equal', 'not equal');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('<>', 'Use different', 'not equal');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('<', 'Use less than', 'less');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('>', 'Use great than', 'great');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('<=', 'Use less or equal than', 'less or equal');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('>=', 'Use great or equal than', 'great or equal');

INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('IS NULL', 'Use equal null', 'equal null');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('IN', 'Use in', 'in');
INSERT INTO `keywords` (sql_key, natural_hint, help_key) VALUES ('NOT IN', 'Use not in', 'not in');