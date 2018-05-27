-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 27, 2018 at 06:32 PM
-- Server version: 10.1.31-MariaDB
-- PHP Version: 7.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mountains`
--
CREATE DATABASE IF NOT EXISTS `mountains` DEFAULT CHARACTER SET utf8 COLLATE utf8_slovenian_ci;
USE `mountains`;

-- --------------------------------------------------------

--
-- Stand-in structure for view `alldetails`
-- (See below for the actual view)
--
CREATE TABLE `alldetails` (
`id` int(11)
,`range_id` int(11)
,`range_name` varchar(100)
,`mountain_name` varchar(150)
,`height` int(4)
,`walk_time` int(11)
,`description` text
,`author_id` int(11)
);

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id_comment` int(11) NOT NULL,
  `id_mountain` int(11) NOT NULL,
  `user_name` varchar(30) COLLATE utf8_slovenian_ci NOT NULL,
  `time` date NOT NULL,
  `comment` varchar(250) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id_file` int(11) NOT NULL,
  `id_mountain` int(11) NOT NULL,
  `path` varchar(500) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mountain`
--

CREATE TABLE `mountain` (
  `id` int(11) NOT NULL,
  `range_id` int(11) NOT NULL,
  `name` varchar(150) COLLATE utf8_slovenian_ci NOT NULL,
  `height` int(4) NOT NULL,
  `walk_time` int(11) NOT NULL,
  `description` text COLLATE utf8_slovenian_ci NOT NULL,
  `author_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rangenames`
--

CREATE TABLE `rangenames` (
  `range_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Dumping data for table `rangenames`
--

INSERT INTO `rangenames` (`range_id`, `name`) VALUES
(1, 'Goriško, Notranjsko in Snežniško hribovje'),
(2, 'Julijske Alpe'),
(3, 'Kamniško Savinjske Alpe'),
(4, 'Karavanke'),
(5, 'Pohorje in ostala severovzhodna Slovenija'),
(6, 'Polhograjsko hribovje in Ljubljana'),
(7, 'Škofjeloško, Cerkljansko hribovje in Jelovica'),
(8, 'Zasavsko - Posavsko hribovje in Dolenjska');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(30) COLLATE utf8_slovenian_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(8, 'test2', '$2y$10$rJjmm9SwfXJrZSlUpUNEe.yi4w8nLqnLfnv49pdsKIlV7BrKBQT3i'),
(11, 'simba', '$2y$10$4YCEr8or7bxnHdTwnxz8HeqvPoZugTfz0QNpRcisoRAjWC3KJ2yGW'),
(12, 'janko', '$2y$10$CLk.t153dnHRuHRKYEFrY.MotCCcFMsMVY.VYxEaVNbnM5ky8PXmC');

-- --------------------------------------------------------

--
-- Structure for view `alldetails`
--
DROP TABLE IF EXISTS `alldetails`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `alldetails`  AS  select `m`.`id` AS `id`,`m`.`range_id` AS `range_id`,`r`.`name` AS `range_name`,`m`.`name` AS `mountain_name`,`m`.`height` AS `height`,`m`.`walk_time` AS `walk_time`,`m`.`description` AS `description`,`m`.`author_id` AS `author_id` from ((`mountain` `m` join `rangenames` `r` on((`m`.`range_id` = `r`.`range_id`))) join `users` `u` on((`m`.`author_id` = `u`.`id`))) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id_comment`),
  ADD KEY `id_mountain` (`id_mountain`);

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id_file`),
  ADD KEY `fk` (`id_mountain`);

--
-- Indexes for table `mountain`
--
ALTER TABLE `mountain`
  ADD PRIMARY KEY (`id`),
  ADD KEY `range_id` (`range_id`),
  ADD KEY `author_id` (`author_id`);

--
-- Indexes for table `rangenames`
--
ALTER TABLE `rangenames`
  ADD PRIMARY KEY (`range_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id_comment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id_file` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- AUTO_INCREMENT for table `mountain`
--
ALTER TABLE `mountain`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- AUTO_INCREMENT for table `rangenames`
--
ALTER TABLE `rangenames`
  MODIFY `range_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`id_mountain`) REFERENCES `mountain` (`id`);

--
-- Constraints for table `images`
--
ALTER TABLE `images`
  ADD CONSTRAINT `images_ibfk_1` FOREIGN KEY (`id_mountain`) REFERENCES `mountain` (`id`);

--
-- Constraints for table `mountain`
--
ALTER TABLE `mountain`
  ADD CONSTRAINT `mountain_ibfk_1` FOREIGN KEY (`range_id`) REFERENCES `rangenames` (`range_id`),
  ADD CONSTRAINT `mountain_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
