-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2018 at 08:00 AM
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
DROP DATABASE IF EXISTS `mountains`;
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

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`id_comment`, `id_mountain`, `user_name`, `time`, `comment`) VALUES
(1, 1, 'janko', '2018-05-30', 'Zelo lep razgled'),
(2, 3, 'janko', '2018-05-30', 'Sm že biu tuki, je zlo lepo');

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id_file` int(11) NOT NULL,
  `id_mountain` int(11) NOT NULL,
  `path` varchar(500) COLLATE utf8_slovenian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`id_file`, `id_mountain`, `path`) VALUES
(1, 1, 'images/upload/Snežnik/Sneznik.jpg'),
(2, 1, 'images/upload/Snežnik/spomin_na_vzpon.jpg'),
(3, 1, 'images/upload/Snežnik/vrh.jpg'),
(4, 2, 'images/upload/Slivnica/Slivnica.jpg'),
(5, 2, 'images/upload/Slivnica/Slivnica2.jpg'),
(6, 2, 'images/upload/Slivnica/Slivnica3.jpg'),
(7, 2, 'images/upload/Slivnica/SvetiVid.jpg'),
(8, 3, 'images/upload/Triglav/p6190040byn71resized8nu.jpg'),
(9, 3, 'images/upload/Triglav/p7050110medium8gt.jpg'),
(10, 3, 'images/upload/Triglav/p7050235medium0ck.jpg'),
(11, 3, 'images/upload/Triglav/pa310405medium6ps.jpg'),
(12, 3, 'images/upload/Triglav/pc140140medium5vl.jpg'),
(13, 4, 'images/upload/Škrlatica/p6190052medium0hb.jpg'),
(14, 4, 'images/upload/Škrlatica/p7050071medium6rl.jpg'),
(15, 4, 'images/upload/Škrlatica/p7050080medium1ee.jpg'),
(16, 4, 'images/upload/Škrlatica/p7120110medium7ri.jpg'),
(17, 4, 'images/upload/Škrlatica/p9220103mediumzh6.jpg'),
(18, 4, 'images/upload/Škrlatica/P7170072251328.jpg'),
(19, 4, 'images/upload/Škrlatica/P8100184591360.jpg'),
(20, 5, 'images/upload/Javorca/P901058497200.jpg'),
(21, 5, 'images/upload/Javorca/P901062168040.jpg'),
(22, 5, 'images/upload/Javorca/P901062668040.jpg'),
(23, 6, 'images/upload/Grintovec/p6250106medium5xm.jpg'),
(24, 6, 'images/upload/Grintovec/p6250131medium7lv.jpg'),
(25, 6, 'images/upload/Grintovec/p8290016medium3cw.jpg'),
(26, 6, 'images/upload/Grintovec/pb020090medium4ff.jpg'),
(27, 7, 'images/upload/Storžič/7785554470387_DSCN9325.JPG'),
(28, 7, 'images/upload/Storžič/pb130030fsg49resized0ek.jpg'),
(29, 7, 'images/upload/Storžič/pb210036medium8gc.jpg'),
(30, 7, 'images/upload/Storžič/Slike nikon 218113400.jpg'),
(31, 8, 'images/upload/Kum/DSCN598585536.jpg'),
(32, 8, 'images/upload/Kum/DSCN598928512.jpg');

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
(1, 1, 'Snežnik', 3, 195, 'Snežnik oz. Veliki Snežnikje z višino 1796 m najvišji vrh v bližnji in daljni okolici, zato je z njega lep razgled po večjem delu Slovenije in bližnje sosednje Hrvaške. Ob lepem vremenu tako vidimo najvišje vrhove Gorskega Kotarja in Istre, kjer izstopajo Risnjak, Snježnik in Učka. Na slovensko stran pa prek prostranih gozdov Notranjske vidimo tudi najvišje vrhove Julijskih Alp, Kamniško Savinjskih Alp in Karavank.\r\nLe nekaj metrov pod vrhom pa se nahaja tudi zavetišče Draga Karolina na Velikem Snežniku. ', 8),
(2, 1, 'Slivnica', 2, 135, 'Slivnica je 1114 m visok vrh, ki se nahaja severno od Cerkniškega polja. Z vrha, na katerem se nahaja vpisna skrinjica je razgled zaradi bližnjega gozda in oddajnikov precej omejen.\r\n\r\nOd cerkve sv. Jurija se sprehodimo skozi vas, nato pa vzpon nadaljujemo po ožji makadamski cesti, ki se komaj znatno vzpenja. Po nekaj minutah lahkotne hoje, pa nas oznake za Slivnico usmerijo desno, na občasno zaraščen kolovoz, ki naprej preči gozdno cesto. Po prečenju ceste se pot vrne v gozd, nato pa nas že po nekaj korakih pripelje do širše ceste, ki vodi na Slivnico. Omenjeni cesti sledimo v levo, le ta pa nas vodi mimo Kačaste smreke (naravna znamenitost ob poti). Nekoliko naprej markirana pot seka desni ovinek in se na cesto vrne ravno pri levem ovinku. Tu je tudi manjše križišče, mi pa nadaljujemo po širši makadamski cesti, kateri nato sledimo kar precej časa. Po nekaj km rahlega vzpona, pridemo do označenega križišča, kjer nadaljujemo ostro desno v smeri doma na Slivnici (levo lovska koča). Cesta, ki nato postopoma preide iz gozda na deloma travnata pobočja, pa nas višje pripelje na križišče, kjer nadaljujemo po levi zgornji cesti (desno dom na Slivnici 100 m). Omenjeni cesti sledimo do njenega konca, nato pa vzpon nadaljujemo levo po markirani peš poti, ki nas skozi pas malinovja v nekaj minutah nadaljnje hoje pripelje na 1114 m visok vrh.', 12),
(3, 2, 'Triglav', 5, 350, 'Triglav je 2864 m visoka gora, ki se nahaja v osrčju Julijskih Alp. Z omenjeno višino je Triglav najvišja gora na ozemlju Slovenije, hkrati pa je tudi najvišja gora v Julijskih Alpah. Na vrhu stoji Aljažev stolp, katerega je leta 1895 postavil Jakob Aljaž, takratni župnik na Dovjem. V Aljaževem stolpu lahko v primeru nevihte vedri do 5 ljudi. Sicer pa je danes Aljažev stolp zaščiten kot kulturni spomenik in kot tak zelo pomemben pri ohranjanju kulturne dediščine.\r\nTriglav je svoj prvi obisk dočakal 26.8.1778, ko so se na vrh povzpeli štirje srčni možje, katerih spomenik stoji v Bohinju. Ti srčni možje so bili Luka Korošec, Matevž Kos, Štefan Rožič in Lovrenc Willomitzer.\r\nSicer pa se nam z vrha Triglava odpre lep razgled, ki seže vse od Jadranskega morja, prek Dolomitov in Visokih Tur do Karavank, Kamniško Savinjskih Alp, Pohorja, prek skoraj celotne Slovenije do najvišjih vrhov Julijskih Alp.', 12),
(4, 2, 'Škrlatica', 7, 450, 'Škrlatica je druga najvišja gora v Sloveniji. Na vrhu stoji križ po katerem lahko s sosednjih gora hitro prepoznamo vrh. Ima tudi zelo prepoznavno severno do severozahodno steno katero lepo vidimo z Vršiške ceste. Z vrha je zelo lep pogled na vse vrhove nad dolino Vrata še posebej na Triglav in sosednjo Dolkovo špico. Proti vzhodu pa lepo vidimo težko dostopne Rokave. Vrh ima tudi vpisno skrinjico.', 12),
(5, 2, 'Javorca', 0, 20, 'Javorca je razgledna vzpetina, ki se nahaja na zahodni strani reke Tolminke. Na vzpetini stoji spominska cerkev sv. Duha, ki je posvečena padlim avstro-ogrskim vojakom. Od cerkve, v kateri so na lesenih ploščah tudi imena padlih vojakov, se nam odpre lep razgled na vrhove, ki obdajajo dolino reke Tolminke.', 12),
(6, 3, 'Grintovec', 4, 285, 'Grintovec je najvišji vrh Kamniških in Savinjskih Alp. Nahaja se nad dolino Kamniške Bistrice, Suhega dola in nad dolino Ravenske Kočne. Razgled z vrha je najlepši proti Jezerski in Kokrski Kočni na zahodni strani, na severu se lepo vidi Jezerska dolina in vrhovi nad Jezerskim proti vzhodu pa se vidi greben Grintovcev od Dolgega Hrbta, čez Štruco do Skute. Proti jugu pa se vidi Kalški greben in v ozadju Ljubljanska kotlina. Vrh ima vpisno knjigo in žig.', 12),
(7, 3, 'Storžič', 4, 240, 'Storžič je 2132 m visoka gora, ki se nahaja na zahodnem delu Kamniško Savinjskih Alp. Z vrha na katerem se nahaja križ se nam odpre lep razgled na Julijske Alpe, Karavanke, Kamniško Savinjske Alpe ter na Ljubljansko kotlino in Gorenjsko ravnino. Vrh ima vpisno skrinjico, žig in razgledno ploščo.', 12),
(8, 8, 'Kum', 3, 210, 'Kum je s 1220 metri najvišji vrh Posavskega hribovja. Ker se vrh dviga nad Zasavjem, ga nekateri imenujejo tudi Zasavski Triglav. Na vrhu je planinski dom, oddajniki ter cerkev sv. Neže. Na vrhu je tudi razgledna plošča, ki kaže bolj znane vrhove večjega dela Slovenije, seveda tiste, katere se z vrha vidi. Ob lepem vremenu razgled z vrha seže od bližnjega Posavskega hribovja do nekoliko bolj oddaljenih Kamniških in Savinjskih Alp, Karavank in Julijskih Alp s Triglavom. Na vrhu je tudi nekaj igral, kjer se lahko igrajo otroci.', 12);

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
  ADD KEY `id_mountain` (`id_mountain`);

--
-- Indexes for table `mountain`
--
ALTER TABLE `mountain`
  ADD PRIMARY KEY (`id`);

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
  MODIFY `id_comment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id_file` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `mountain`
--
ALTER TABLE `mountain`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`id_mountain`) REFERENCES `mountain` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `images`
--
ALTER TABLE `images`
  ADD CONSTRAINT `images_ibfk_1` FOREIGN KEY (`id_mountain`) REFERENCES `mountain` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
