-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 08, 2018 at 06:04 PM
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

--
-- Dumping data for table `mountain`
--

INSERT INTO `mountain` (`id`, `range_id`, `name`, `height`, `walk_time`, `description`, `author_id`) VALUES
(1, 2, 'Test TEst', 6, 380, 'Kr en opiss', 8),
(2, 2, 'TEST TEST', 2, 120, 'OPUSS', 8),
(3, 6, 'nekii', 3, 215, 'opiss', 8);

-- --------------------------------------------------------

--
-- Table structure for table `rangenames`
--

CREATE TABLE `rangenames` (
  `id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Dumping data for table `rangenames`
--

INSERT INTO `rangenames` (`id`, `name`) VALUES
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
(1, 'simon', 'simbator8'),
(8, 'test2', '$2y$10$rJjmm9SwfXJrZSlUpUNEe.yi4w8nLqnLfnv49pdsKIlV7BrKBQT3i');

--
-- Indexes for dumped tables
--

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
  ADD PRIMARY KEY (`id`);

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
-- AUTO_INCREMENT for table `mountain`
--
ALTER TABLE `mountain`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `rangenames`
--
ALTER TABLE `rangenames`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mountain`
--
ALTER TABLE `mountain`
  ADD CONSTRAINT `mountain_ibfk_1` FOREIGN KEY (`range_id`) REFERENCES `rangenames` (`id`),
  ADD CONSTRAINT `mountain_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
