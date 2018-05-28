-- phpMyAdmin SQL Dump
-- version 4.8.0
-- https://www.phpmyadmin.net/
--
-- Gostitelj: 127.0.0.1
-- Čas nastanka: 28. maj 2018 ob 13.10
-- Različica strežnika: 10.1.31-MariaDB
-- Različica PHP: 7.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Zbirka podatkov: `mountains`
--
CREATE DATABASE IF NOT EXISTS `mountains` DEFAULT CHARACTER SET utf8 COLLATE utf8_slovenian_ci;
USE `mountains`;

-- --------------------------------------------------------

--
-- Nadomestna struktura pogleda `alldetails`
-- (Oglejte si spodaj za resnični pogled)
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
-- Struktura tabele `comments`
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
-- Struktura tabele `images`
--

CREATE TABLE `images` (
  `id_file` int(11) NOT NULL,
  `id_mountain` int(11) NOT NULL,
  `path` varchar(500) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Odloži podatke za tabelo `images`
--

INSERT INTO `images` (`id_file`, `id_mountain`, `path`) VALUES
(1, 1, 'images/upload/Snežnik/Sneznik.jpg'),
(2, 1, 'images/upload/Snežnik/spomin_na_vzpon.jpg'),
(3, 1, 'images/upload/Snežnik/vrh.jpg');

-- --------------------------------------------------------

--
-- Struktura tabele `mountain`
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
-- Odloži podatke za tabelo `mountain`
--

INSERT INTO `mountain` (`id`, `range_id`, `name`, `height`, `walk_time`, `description`, `author_id`) VALUES
(1, 1, 'Snežnik', 3, 195, 'Snežnik oz. Veliki Snežnikje z višino 1796 m najvišji vrh v bližnji in daljni okolici, zato je z njega lep razgled po večjem delu Slovenije in bližnje sosednje Hrvaške. Ob lepem vremenu tako vidimo najvišje vrhove Gorskega Kotarja in Istre, kjer izstopajo Risnjak, Snježnik in Učka. Na slovensko stran pa prek prostranih gozdov Notranjske vidimo tudi najvišje vrhove Julijskih Alp, Kamniško Savinjskih Alp in Karavank.\r\nLe nekaj metrov pod vrhom pa se nahaja tudi zavetišče Draga Karolina na Velikem Snežniku. ', 8);

-- --------------------------------------------------------

--
-- Struktura tabele `rangenames`
--

CREATE TABLE `rangenames` (
  `range_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Odloži podatke za tabelo `rangenames`
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
-- Struktura tabele `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(30) COLLATE utf8_slovenian_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Odloži podatke za tabelo `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(8, 'test2', '$2y$10$rJjmm9SwfXJrZSlUpUNEe.yi4w8nLqnLfnv49pdsKIlV7BrKBQT3i'),
(11, 'simba', '$2y$10$4YCEr8or7bxnHdTwnxz8HeqvPoZugTfz0QNpRcisoRAjWC3KJ2yGW'),
(12, 'janko', '$2y$10$CLk.t153dnHRuHRKYEFrY.MotCCcFMsMVY.VYxEaVNbnM5ky8PXmC');

-- --------------------------------------------------------

--
-- Struktura pogleda `alldetails`
--
DROP TABLE IF EXISTS `alldetails`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `alldetails`  AS  select `m`.`id` AS `id`,`m`.`range_id` AS `range_id`,`r`.`name` AS `range_name`,`m`.`name` AS `mountain_name`,`m`.`height` AS `height`,`m`.`walk_time` AS `walk_time`,`m`.`description` AS `description`,`m`.`author_id` AS `author_id` from ((`mountain` `m` join `rangenames` `r` on((`m`.`range_id` = `r`.`range_id`))) join `users` `u` on((`m`.`author_id` = `u`.`id`))) ;

--
-- Indeksi zavrženih tabel
--

--
-- Indeksi tabele `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id_comment`),
  ADD KEY `id_mountain` (`id_mountain`);

--
-- Indeksi tabele `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id_file`),
  ADD KEY `id_mountain` (`id_mountain`);

--
-- Indeksi tabele `mountain`
--
ALTER TABLE `mountain`
  ADD PRIMARY KEY (`id`);

--
-- Indeksi tabele `rangenames`
--
ALTER TABLE `rangenames`
  ADD PRIMARY KEY (`range_id`);

--
-- Indeksi tabele `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT zavrženih tabel
--

--
-- AUTO_INCREMENT tabele `comments`
--
ALTER TABLE `comments`
  MODIFY `id_comment` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT tabele `images`
--
ALTER TABLE `images`
  MODIFY `id_file` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT tabele `mountain`
--
ALTER TABLE `mountain`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Omejitve tabel za povzetek stanja
--

--
-- Omejitve za tabelo `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`id_mountain`) REFERENCES `mountain` (`id`) ON DELETE CASCADE;

--
-- Omejitve za tabelo `images`
--
ALTER TABLE `images`
  ADD CONSTRAINT `images_ibfk_1` FOREIGN KEY (`id_mountain`) REFERENCES `mountain` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
