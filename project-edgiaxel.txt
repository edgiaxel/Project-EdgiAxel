-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               12.0.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for project-edgiaxel
CREATE DATABASE IF NOT EXISTS `project-edgiaxel` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `project-edgiaxel`;

-- Dumping structure for table project-edgiaxel.car_model
CREATE TABLE IF NOT EXISTS `car_model` (
  `car_model_id` int(11) NOT NULL AUTO_INCREMENT,
  `manufacturer_id` int(11) NOT NULL,
  `model_name` varchar(100) NOT NULL,
  `base_rating` int(11) NOT NULL COMMENT 'Base performance rating (Hypercar: 90-100, GT3: 60-80)',
  PRIMARY KEY (`car_model_id`),
  UNIQUE KEY `model_name` (`model_name`),
  KEY `manufacturer_id` (`manufacturer_id`),
  CONSTRAINT `car_model_ibfk_1` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.car_model: ~17 rows (approximately)
DELETE FROM `car_model`;
INSERT INTO `car_model` (`car_model_id`, `manufacturer_id`, `model_name`, `base_rating`) VALUES
	(1, 1, 'Aston Martin Valkyrie', 91),
	(2, 2, 'Porsche 963', 98),
	(3, 3, 'Toyota GR010 Hybrid', 100),
	(4, 4, 'Cadillac V-Series.R', 94),
	(5, 5, 'BMW M Hybrid V8', 95),
	(6, 6, 'Alpine A424', 92),
	(7, 7, 'Ferrari 499P', 97),
	(8, 8, 'Peugeot 9X8', 93),
	(9, 9, 'Chevrolet Corvette Z06 GT3.R', 78),
	(10, 10, 'McLaren 720S GT3 Evo', 79),
	(11, 11, 'Mercedes-AMG GT3 Evo', 77),
	(12, 12, 'Ford Mustang GT3', 75),
	(13, 13, 'Lexus RC F GT3', 76),
	(14, 1, 'Aston Martin Vantage AMR GT3 Evo', 80),
	(15, 5, 'BMW M4 GT3 Evo', 79),
	(16, 7, 'Ferrari 296 GT3', 80),
	(17, 2, 'Porsche 911 GT3 R (992)', 78);

-- Dumping structure for table project-edgiaxel.championship_season
CREATE TABLE IF NOT EXISTS `championship_season` (
  `season_id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `status` enum('Created','Ongoing','Finished') DEFAULT 'Created',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`season_id`),
  UNIQUE KEY `year` (`year`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.championship_season: ~1 rows (approximately)
DELETE FROM `championship_season`;
INSERT INTO `championship_season` (`season_id`, `year`, `status`, `created_at`) VALUES
	(1, 2025, 'Ongoing', '2025-10-17 17:53:57');

-- Dumping structure for table project-edgiaxel.circuit
CREATE TABLE IF NOT EXISTS `circuit` (
  `circuit_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `location` varchar(100) NOT NULL,
  `country` varchar(50) NOT NULL,
  `length_km` decimal(5,3) NOT NULL,
  `race_type` enum('6 Hours','8–10 Hours','24 Hours') NOT NULL,
  PRIMARY KEY (`circuit_id`),
  UNIQUE KEY `uk_circuit_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.circuit: ~8 rows (approximately)
DELETE FROM `circuit`;
INSERT INTO `circuit` (`circuit_id`, `name`, `location`, `country`, `length_km`, `race_type`) VALUES
	(1, 'Lusail International Circuit', 'Lusail', 'Qatar', 5.419, '8–10 Hours'),
	(2, 'Imola Circuit', 'Imola', 'Italy', 4.909, '6 Hours'),
	(3, 'Spa Francorchamps', 'Stavelot', 'Belgium', 7.004, '6 Hours'),
	(4, 'LeMans', 'Le Mans', 'France', 13.629, '24 Hours'),
	(5, 'Interlagos Circuit', 'São Paulo', 'Brazil', 4.309, '6 Hours'),
	(6, 'COTA Americas', 'Austin, Texas', 'United States', 5.514, '6 Hours'),
	(7, 'Fuji Speedway', 'Oyama, Shizuoka', 'Japan', 4.563, '6 Hours'),
	(8, 'Bahrain International Circuit', 'Sakhir', 'Bahrain', 5.412, '8–10 Hours');

-- Dumping structure for table project-edgiaxel.driver
CREATE TABLE IF NOT EXISTS `driver` (
  `driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `nationality` varchar(50) NOT NULL,
  PRIMARY KEY (`driver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.driver: ~124 rows (approximately)
DELETE FROM `driver`;
INSERT INTO `driver` (`driver_id`, `first_name`, `last_name`, `nationality`) VALUES
	(1, 'Tom', 'Gamble', 'United Kingdom'),
	(2, 'Harry', 'Tincknell', 'United Kingdom'),
	(3, 'Ross', 'Gunn', 'United Kingdom'),
	(4, 'Alex', 'Riberas', 'Spain'),
	(5, 'Marco', 'Sørensen', 'Denmark'),
	(6, 'Roman', 'De Angelis', 'Canada'),
	(7, 'Julien', 'Andlauer', 'France'),
	(8, 'Michael', 'Christensen', 'Denmark'),
	(9, 'Mathieu', 'Jaminet', 'France'),
	(10, 'Nico', 'Müller', 'Switzerland'),
	(11, 'Kévin', 'Estre', 'France'),
	(12, 'Laurens', 'Vanthoor', 'Belgium'),
	(13, 'Matt', 'Campbell', 'Australia'),
	(14, 'Pascal', 'Wehrlein', 'Germany'),
	(15, 'Kamui', 'Kobayashi', 'Japan'),
	(16, 'Nyck', 'de Vries', 'Netherlands'),
	(17, 'Mike', 'Conway', 'United Kingdom'),
	(18, 'José María', 'López', 'Argentina'),
	(19, 'Brendon', 'Hartley', 'New Zealand'),
	(20, 'Ryō', 'Hirakawa', 'Japan'),
	(21, 'Sébastien', 'Buemi', 'Switzerland'),
	(22, 'Alex', 'Lynn', 'United Kingdom'),
	(23, 'Norman', 'Nato', 'France'),
	(24, 'Will', 'Stevens', 'United Kingdom'),
	(25, 'Earl', 'Bamber', 'New Zealand'),
	(26, 'Sébastien', 'Bourdais', 'France'),
	(27, 'Jenson', 'Button', 'United Kingdom'),
	(28, 'Kevin', 'Magnussen', 'Denmark'),
	(29, 'Raffaele', 'Marciello', 'Switzerland'),
	(30, 'Dries', 'Vanthoor', 'Belgium'),
	(31, 'René', 'Rast', 'Germany'),
	(32, 'Robin', 'Frijns', 'Netherlands'),
	(33, 'Sheldon', 'van der Linde', 'South Africa'),
	(34, 'Marco', 'Wittmann', 'Germany'),
	(35, 'Paul-Loup', 'Chatin', 'France'),
	(36, 'Ferdinand', 'Habsburg', 'Austria'),
	(37, 'Charles', 'Milesi', 'France'),
	(38, 'Jules', 'Gounon', 'France'),
	(39, 'Frédéric', 'Makowiecki', 'France'),
	(40, 'Mick', 'Schumacher', 'Germany'),
	(41, 'Antonio', 'Fuoco', 'Italy'),
	(42, 'Miguel', 'Molina', 'Spain'),
	(43, 'Nicklas', 'Nielsen', 'Denmark'),
	(44, 'James', 'Calado', 'United Kingdom'),
	(45, 'Antonio', 'Giovinazzi', 'Italy'),
	(46, 'Alessandro', 'Pier Guidi', 'Italy'),
	(47, 'Phil', 'Hanson', 'United Kingdom'),
	(48, 'Robert', 'Kubica', 'Poland'),
	(49, 'Yifei', 'Ye', 'China'),
	(50, 'Paul', 'di Resta', 'United Kingdom'),
	(51, 'Mikkel', 'Jensen', 'Denmark'),
	(52, 'Jean-Éric', 'Vergne', 'France'),
	(53, 'Loïc', 'Duval', 'France'),
	(54, 'Malthe', 'Jakobsen', 'Denmark'),
	(55, 'Stoffel', 'Vandoorne', 'Belgium'),
	(56, 'Théo', 'Pourchaire', 'France'),
	(57, 'Neel', 'Jani', 'Switzerland'),
	(58, 'Nico', 'Pino', 'Chile'),
	(59, 'Nicolás', 'Varrone', 'Argentina'),
	(60, 'Eduardo', 'Barrichello', 'Brazil'),
	(61, 'Valentin', 'Hasse-Clot', 'France'),
	(62, 'Derek', 'DeBoer', 'United States'),
	(63, 'Anthony', 'McIntosh', 'United States'),
	(64, 'Mattia', 'Drudi', 'Italy'),
	(65, 'Ian', 'James', 'United Kingdom'),
	(66, 'Zacharie', 'Robichon', 'Canada'),
	(67, 'François', 'Heriau', 'France'),
	(68, 'Simon', 'Mann', 'United States'),
	(69, 'Alessio', 'Rovera', 'Italy'),
	(70, 'Francesco', 'Castellacci', 'Italy'),
	(71, 'Thomas', 'Flohr', 'Switzerland'),
	(72, 'Davide', 'Rigon', 'Italy'),
	(73, 'Augusto', 'Farfus', 'Brazil'),
	(74, 'Yasser', 'Shahin', 'Australia'),
	(75, 'Timur', 'Boguslavskiy', 'France'),
	(76, 'Pedro', 'Ebrahim', 'Brazil'),
	(77, 'Ahmad', 'Al Harthy', 'Oman'),
	(78, 'Valentino', 'Rossi', 'Italy'),
	(79, 'Kelvin', 'van der Linde', 'South Africa'),
	(80, 'Jonny', 'Edgar', 'United Kingdom'),
	(81, 'Daniel', 'Juncadella', 'Spain'),
	(82, 'Ben', 'Keating', 'United States'),
	(83, 'Rui', 'Andrade', 'Angola'),
	(84, 'Charlie', 'Eastwood', 'Republic of Ireland'),
	(85, 'Tom', 'van Rompuy', 'Belgium'),
	(86, 'Sébastien', 'Baud', 'France'),
	(87, 'James', 'Cottingham', 'United Kingdom'),
	(88, 'Grégoire', 'Saucy', 'Switzerland'),
	(89, 'Sean', 'Gelael', 'Indonesia'),
	(90, 'Darren', 'Leung', 'United Kingdom'),
	(91, 'Marino', 'Sato', 'Japan'),
	(92, 'Matteo', 'Cairoli', 'Italy'),
	(93, 'Matteo', 'Cressoni', 'Italy'),
	(94, 'Claudio', 'Schiavoni', 'Italy'),
	(95, 'Brenton', 'Grove', 'Australia'),
	(96, 'Stephen', 'Grove', 'Australia'),
	(97, 'Andrew', 'Gilbert', 'United Kingdom'),
	(98, 'Lorcan', 'Hanafin', 'United Kingdom'),
	(99, 'Fran', 'Rueda', 'Spain'),
	(100, 'Lin', 'Hodenius', 'Netherlands'),
	(101, 'Maxime', 'Martin', 'Belgium'),
	(102, 'Christian', 'Ried', 'Germany'),
	(103, 'Martin', 'Berry', 'Australia'),
	(104, 'Ben', 'Barker', 'United Kingdom'),
	(105, 'Bernardo', 'Sousa', 'Portugal'),
	(106, 'Ben', 'Tuck', 'United Kingdom'),
	(107, 'Stefano', 'Gattuso', 'Italy'),
	(108, 'Giammarco', 'Levorato', 'Italy'),
	(109, 'Dennis', 'Olsen', 'Norway'),
	(110, 'Finn', 'Gehrsitz', 'Germany'),
	(111, 'Arnold', 'Robin', 'France'),
	(112, 'Ben', 'Burnicoat', 'United Kingdom'),
	(113, 'Esteban', 'Masson', 'France'),
	(114, 'Yuichi', 'Nakayama', 'Japan'),
	(115, 'Jack', 'Hawksworth', 'United Kingdom'),
	(116, 'Clemens', 'Schmid', 'Austria'),
	(117, 'Răzvan', 'Umbrărescu', 'Romania'),
	(118, 'Rahel', 'Frey', 'Switzerland'),
	(119, 'Célia', 'Martin', 'France'),
	(120, 'Michelle', 'Gatting', 'Denmark'),
	(121, 'Sarah', 'Bovy', 'Belgium'),
	(122, 'Ryan', 'Hardwick', 'United States'),
	(123, 'Richard', 'Lietz', 'Austria'),
	(124, 'Riccardo', 'Pera', 'Italy');

-- Dumping structure for table project-edgiaxel.manufacturer
CREATE TABLE IF NOT EXISTS `manufacturer` (
  `manufacturer_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `country` varchar(50) NOT NULL,
  `category` enum('Hypercar','LMGT3') NOT NULL,
  PRIMARY KEY (`manufacturer_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.manufacturer: ~13 rows (approximately)
DELETE FROM `manufacturer`;
INSERT INTO `manufacturer` (`manufacturer_id`, `name`, `country`, `category`) VALUES
	(1, 'Aston Martin', 'United Kingdom', 'Hypercar'),
	(2, 'Porsche', 'Germany', 'Hypercar'),
	(3, 'Toyota', 'Japan', 'Hypercar'),
	(4, 'Cadillac', 'United States', 'Hypercar'),
	(5, 'BMW', 'Germany', 'Hypercar'),
	(6, 'Alpine', 'France', 'Hypercar'),
	(7, 'Ferrari', 'Italy', 'Hypercar'),
	(8, 'Peugeot', 'France', 'Hypercar'),
	(9, 'Chevrolet', 'United States', 'LMGT3'),
	(10, 'McLaren', 'United Kingdom', 'LMGT3'),
	(11, 'Mercedes', 'Germany', 'LMGT3'),
	(12, 'Ford', 'United States', 'LMGT3'),
	(13, 'Lexus', 'Japan', 'LMGT3');

-- Dumping structure for table project-edgiaxel.season_circuit
CREATE TABLE IF NOT EXISTS `season_circuit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `season_id` int(11) NOT NULL,
  `circuit_id` int(11) NOT NULL,
  `race_index` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_race_per_season` (`season_id`,`race_index`),
  KEY `circuit_id` (`circuit_id`),
  CONSTRAINT `season_circuit_ibfk_1` FOREIGN KEY (`season_id`) REFERENCES `championship_season` (`season_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `season_circuit_ibfk_2` FOREIGN KEY (`circuit_id`) REFERENCES `circuit` (`circuit_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.season_circuit: ~8 rows (approximately)
DELETE FROM `season_circuit`;

-- Dumping structure for table project-edgiaxel.team
CREATE TABLE IF NOT EXISTS `team` (
  `team_id` int(11) NOT NULL AUTO_INCREMENT,
  `car_number` varchar(5) NOT NULL,
  `team_name` varchar(100) NOT NULL,
  `manufacturer_id` int(11) NOT NULL,
  `car_model_id` int(11) NOT NULL,
  `nationality` varchar(50) DEFAULT NULL,
  `category` enum('Hypercar','LMGT3') NOT NULL,
  PRIMARY KEY (`team_id`),
  UNIQUE KEY `uk_car_number_name` (`car_number`,`team_name`),
  KEY `manufacturer_id` (`manufacturer_id`),
  KEY `car_model_id` (`car_model_id`),
  CONSTRAINT `team_ibfk_1` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`manufacturer_id`),
  CONSTRAINT `team_ibfk_2` FOREIGN KEY (`car_model_id`) REFERENCES `car_model` (`car_model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.team: ~36 rows (approximately)
DELETE FROM `team`;
INSERT INTO `team` (`team_id`, `car_number`, `team_name`, `manufacturer_id`, `car_model_id`, `nationality`, `category`) VALUES
	(1, '007', 'Aston Martin THOR Team', 1, 1, 'United States', 'Hypercar'),
	(2, '009', 'Aston Martin THOR Team', 1, 1, 'United States', 'Hypercar'),
	(3, '5', 'Porsche Penske Motorsport', 2, 2, 'Germany', 'Hypercar'),
	(4, '6', 'Porsche Penske Motorsport', 2, 2, 'Germany', 'Hypercar'),
	(5, '7', 'Toyota Gazoo Racing', 3, 3, 'Japan', 'Hypercar'),
	(6, '8', 'Toyota Gazoo Racing', 3, 3, 'Japan', 'Hypercar'),
	(7, '12', 'Cadillac Hertz Team Jota', 4, 4, 'United States', 'Hypercar'),
	(8, '38', 'Cadillac Hertz Team Jota', 4, 4, 'United States', 'Hypercar'),
	(9, '15', 'BMW M Team WRT', 5, 5, 'Germany', 'Hypercar'),
	(10, '20', 'BMW M Team WRT', 5, 5, 'Germany', 'Hypercar'),
	(11, '35', 'Alpine Endurance Team', 6, 6, 'France', 'Hypercar'),
	(12, '36', 'Alpine Endurance Team', 6, 6, 'France', 'Hypercar'),
	(13, '50', 'Ferrari AF Corse', 7, 7, 'Italy', 'Hypercar'),
	(14, '51', 'Ferrari AF Corse', 7, 7, 'Italy', 'Hypercar'),
	(15, '83', 'AF Corse', 7, 7, 'Italy', 'Hypercar'),
	(16, '93', 'Peugeot TotalEnergies', 8, 8, 'France', 'Hypercar'),
	(17, '94', 'Peugeot TotalEnergies', 8, 8, 'France', 'Hypercar'),
	(18, '99', 'Proton Competition', 2, 2, 'Germany', 'Hypercar'),
	(19, '10', 'Racing Spirit of Léman', 1, 14, 'France', 'LMGT3'),
	(20, '27', 'Heart of Racing Team', 1, 14, 'United States', 'LMGT3'),
	(21, '21', 'Vista AF Corse', 7, 16, 'Italy', 'LMGT3'),
	(22, '54', 'Vista AF Corse', 7, 16, 'Italy', 'LMGT3'),
	(23, '31', 'The Bend Team WRT', 5, 15, 'Belgium', 'LMGT3'),
	(24, '46', 'Team WRT', 5, 15, 'Belgium', 'LMGT3'),
	(25, '33', 'TF Sport', 9, 9, 'United Kingdom', 'LMGT3'),
	(26, '81', 'TF Sport', 9, 9, 'United Kingdom', 'LMGT3'),
	(27, '59', 'United Autosports', 10, 10, 'United Kingdom', 'LMGT3'),
	(28, '95', 'United Autosports', 10, 10, 'United Kingdom', 'LMGT3'),
	(29, '60', 'Iron Lynx', 11, 11, 'Italy', 'LMGT3'),
	(30, '61', 'Iron Lynx', 11, 11, 'Italy', 'LMGT3'),
	(31, '77', 'Proton Competition', 12, 12, 'Germany', 'LMGT3'),
	(32, '88', 'Proton Competition', 12, 12, 'Germany', 'LMGT3'),
	(33, '78', 'Akkodis ASP Team', 13, 13, 'France', 'LMGT3'),
	(34, '87', 'Akkodis ASP Team', 13, 13, 'France', 'LMGT3'),
	(35, '85', 'Iron Dames', 2, 17, 'Italy', 'LMGT3'),
	(36, '92', 'Manthey 1st Phorm', 2, 17, 'Germany', 'LMGT3');

-- Dumping structure for table project-edgiaxel.team_driver
CREATE TABLE IF NOT EXISTS `team_driver` (
  `team_driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  PRIMARY KEY (`team_driver_id`),
  UNIQUE KEY `uk_team_driver` (`team_id`,`driver_id`),
  KEY `driver_id` (`driver_id`),
  CONSTRAINT `team_driver_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`),
  CONSTRAINT `team_driver_ibfk_2` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table project-edgiaxel.team_driver: ~124 rows (approximately)
DELETE FROM `team_driver`;
INSERT INTO `team_driver` (`team_driver_id`, `team_id`, `driver_id`) VALUES
	(1, 1, 1),
	(2, 1, 2),
	(3, 1, 3),
	(4, 2, 4),
	(5, 2, 5),
	(6, 2, 6),
	(7, 3, 7),
	(8, 3, 8),
	(9, 3, 9),
	(10, 3, 10),
	(11, 4, 11),
	(12, 4, 12),
	(13, 4, 13),
	(14, 4, 14),
	(15, 5, 15),
	(16, 5, 16),
	(17, 5, 17),
	(18, 5, 18),
	(19, 6, 19),
	(20, 6, 20),
	(21, 6, 21),
	(22, 7, 22),
	(23, 7, 23),
	(24, 7, 24),
	(25, 8, 25),
	(26, 8, 26),
	(27, 8, 27),
	(28, 9, 28),
	(29, 9, 29),
	(30, 9, 30),
	(35, 11, 35),
	(36, 11, 36),
	(37, 11, 37),
	(38, 12, 38),
	(39, 12, 39),
	(40, 12, 40),
	(41, 13, 41),
	(42, 13, 42),
	(43, 13, 43),
	(44, 14, 44),
	(45, 14, 45),
	(46, 14, 46),
	(47, 15, 47),
	(48, 15, 48),
	(49, 15, 49),
	(50, 16, 50),
	(51, 16, 51),
	(52, 16, 52),
	(53, 17, 53),
	(54, 17, 54),
	(55, 17, 55),
	(56, 17, 56),
	(57, 18, 57),
	(58, 18, 58),
	(59, 18, 59),
	(60, 19, 60),
	(61, 19, 61),
	(62, 19, 62),
	(63, 19, 63),
	(64, 20, 64),
	(65, 20, 65),
	(66, 20, 66),
	(67, 21, 67),
	(68, 21, 68),
	(69, 21, 69),
	(70, 22, 70),
	(71, 22, 71),
	(72, 22, 72),
	(73, 23, 73),
	(74, 23, 74),
	(75, 23, 75),
	(76, 23, 76),
	(77, 24, 77),
	(78, 24, 78),
	(79, 24, 79),
	(80, 25, 80),
	(81, 25, 81),
	(82, 25, 82),
	(83, 26, 83),
	(84, 26, 84),
	(85, 26, 85),
	(86, 27, 86),
	(87, 27, 87),
	(88, 27, 88),
	(89, 28, 89),
	(90, 28, 90),
	(91, 28, 91),
	(100, 30, 100),
	(101, 30, 101),
	(102, 30, 102),
	(103, 30, 103),
	(104, 31, 104),
	(105, 31, 105),
	(106, 31, 106),
	(107, 32, 107),
	(108, 32, 108),
	(109, 32, 109),
	(116, 34, 116),
	(117, 34, 117),
	(118, 34, 18),
	(119, 34, 115),
	(120, 35, 118),
	(121, 35, 119),
	(122, 35, 120),
	(123, 35, 121),
	(124, 36, 122),
	(125, 36, 123),
	(126, 36, 124),
	(131, 10, 31),
	(132, 10, 32),
	(133, 10, 33),
	(134, 10, 34),
	(138, 33, 110),
	(139, 33, 111),
	(140, 33, 112),
	(141, 33, 113),
	(142, 29, 92),
	(143, 29, 93),
	(144, 29, 94),
	(145, 29, 99);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
