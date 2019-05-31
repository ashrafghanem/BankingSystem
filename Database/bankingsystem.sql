-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 25, 2018 at 12:15 AM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bankingsystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `account_num` int(9) NOT NULL,
  `password` varchar(20) NOT NULL,
  `balance` int(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`account_num`, `password`, `balance`) VALUES
(12345, 'pass1', 120),
(23456, 'pass2', 50);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `account_num` int(9) NOT NULL,
  `operation` varchar(9) NOT NULL,
  `amount` int(9) NOT NULL,
  `date` datetime NOT NULL,
  `transID` int(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`account_num`, `operation`, `amount`, `date`, `transID`) VALUES
(23456, 'deposit', 50, '2018-03-24 16:12:27', 13),
(12345, 'deposit', 50, '2018-03-24 16:18:13', 14),
(12345, 'deposit', 20, '2018-03-24 16:18:18', 15),
(12345, 'withdraw', 100, '2018-03-24 16:18:22', 16),
(12345, 'deposit', 80, '2018-03-24 23:29:26', 17),
(12345, 'withdraw', 100, '2018-03-24 23:29:30', 18),
(23456, 'deposit', 200, '2018-03-24 23:29:48', 19),
(23456, 'withdraw', 150, '2018-03-24 23:29:53', 20),
(12345, 'deposit', 100, '2018-03-25 01:34:56', 21),
(12345, 'withdraw', 150, '2018-03-25 01:35:02', 22),
(23456, 'deposit', 150, '2018-03-25 01:35:43', 23),
(23456, 'withdraw', 200, '2018-03-25 01:35:48', 24),
(12345, 'deposit', 50, '2018-03-25 01:42:40', 25),
(12345, 'deposit', 100, '2018-03-25 01:43:39', 26),
(12345, 'withdraw', 300, '2018-03-25 01:43:45', 27),
(23456, 'deposit', 100, '2018-03-25 01:44:09', 28),
(23456, 'withdraw', 150, '2018-03-25 01:44:13', 29),
(12345, 'deposit', 120, '2018-03-25 00:08:09', 30),
(12345, 'withdraw', 100, '2018-03-25 00:08:15', 31);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`account_num`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transID`),
  ADD KEY `account_num` (`account_num`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transID` int(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`account_num`) REFERENCES `clients` (`account_num`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
