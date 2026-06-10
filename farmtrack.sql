-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 01, 2026 at 02:04 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `farmtrack`
--

-- --------------------------------------------------------

--
-- Table structure for table `cattle`
--

CREATE TABLE `cattle` (
  `id` int(11) NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `tag_number` varchar(50) NOT NULL,
  `animal_type` varchar(50) NOT NULL,
  `breed` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `weight` double NOT NULL,
  `location` varchar(100) NOT NULL,
  `health_status` varchar(50) NOT NULL DEFAULT 'Healthy',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cattle`
--

INSERT INTO `cattle` (`id`, `farmer_id`, `tag_number`, `animal_type`, `breed`, `age`, `weight`, `location`, `health_status`, `created_at`) VALUES
(2, 5, 'TAG067', 'Cattle', 'Nguni', 2, 349.99, 'North pasture ', 'Sick', '2026-05-25 21:28:42'),
(3, 5, 'TAG01', 'Sheep', 'Boer', 2, 150, 'North pasture ', 'Under Treatment', '2026-05-25 21:29:55'),
(5, 4, 'TAG02', 'Sheep', 'Boer', 3, 150, 'North pasture ', 'Healthy', '2026-05-25 21:41:51'),
(7, 4, 'TAG07', 'Horse', 'Ponies', 3, 350, 'Paddocks ', 'Quarantined', '2026-05-25 21:44:06');

-- --------------------------------------------------------

--
-- Table structure for table `contact_enquiries`
--

CREATE TABLE `contact_enquiries` (
  `id` int(11) NOT NULL,
  `sender_name` varchar(100) NOT NULL,
  `sender_email` varchar(150) NOT NULL,
  `subject` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `submitted_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `contact_messages`
--

CREATE TABLE `contact_messages` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `subject` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `submitted_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `contact_messages`
--

INSERT INTO `contact_messages` (`id`, `name`, `email`, `subject`, `message`, `submitted_at`) VALUES
(1, 'Elizabeth ', 'jennifer@gmail.com', 'General Enquiry', 'Need help with a sick goat', '2026-05-25 21:04:08'),
(2, 'Elizabeth ', 'jennifer@gmail.com', 'General Enquiry', 'need help with ', '2026-05-26 09:59:39'),
(3, 'Elizabeth ', 'jennifer@gmail.com', 'Animal Record Issue', 'Need to be updated ', '2026-05-26 21:04:49');

-- --------------------------------------------------------

--
-- Table structure for table `grazing_records`
--

CREATE TABLE `grazing_records` (
  `id` int(11) NOT NULL,
  `cattle_id` int(11) NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `paddock_name` varchar(100) NOT NULL,
  `assigned_date` date NOT NULL,
  `status` enum('Active','Resting','Moved') NOT NULL DEFAULT 'Active',
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `health_records`
--

CREATE TABLE `health_records` (
  `id` int(11) NOT NULL,
  `cattle_id` int(11) NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `check_date` date NOT NULL,
  `vet_name` varchar(100) NOT NULL,
  `observations` text NOT NULL,
  `health_status` varchar(50) NOT NULL DEFAULT 'Healthy',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('farmer','admin') NOT NULL DEFAULT 'farmer',
  `security_question` varchar(200) DEFAULT NULL,
  `security_answer` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `email`, `password`, `role`, `security_question`, `security_answer`, `created_at`) VALUES
(1, 'System Administrator', 'admin@farmtrack.com', 'admin123', 'admin', NULL, NULL, '2026-05-24 15:17:08'),
(2, 'Test Farmer', 'farmer@farmtrack.com', 'farmer123', 'farmer', NULL, NULL, '2026-05-24 15:17:08'),
(3, 'Jennifer Nghipunya', 'jennifernghipunya@gmail.com', 'Happy', 'farmer', NULL, NULL, '2026-05-24 15:19:12'),
(4, 'Jennifer James', 'jennifer@gmail.com', 'James', 'farmer', NULL, NULL, '2026-05-24 21:32:22'),
(5, 'Suama Sam ', 'suama@gmail.com', 'Suama', 'farmer', NULL, NULL, '2026-05-25 09:37:34'),
(6, 'Suama Sam ', 'sama@gmail.com', 'Suama', 'farmer', NULL, NULL, '2026-05-25 09:46:54'),
(7, 'Emma', 'emma@gmail', 'emma', 'farmer', NULL, NULL, '2026-05-25 11:22:47'),
(8, 'Jennifer Nghipunya', 'JN@gmail.com', '123456', 'farmer', NULL, NULL, '2026-05-25 22:11:10'),
(9, 'Jennifer Nghipunya', 'J@gmail.com', '12345', 'admin', NULL, NULL, '2026-05-26 08:45:31'),
(10, 'Tangeni Erastus', 'tangeni@gmail.com', '123456', 'farmer', NULL, NULL, '2026-05-26 08:56:09'),
(11, 'Tangeni Erastus', 'angeni@gmail.com', '123456', 'admin', NULL, NULL, '2026-05-26 09:57:10'),
(12, 'Tangeni Erastus', 'ngeni@gmail.com', '1234567', 'admin', NULL, NULL, '2026-05-28 00:14:27'),
(13, 'Happy Erastus', 'happy@gmail.com', 'Happines', 'admin', NULL, NULL, '2026-05-28 20:55:52'),
(14, 'Pewa Ileni', 'pewa@gmail.com', 'pewaileni', 'admin', NULL, NULL, '2026-05-29 13:08:38');

-- --------------------------------------------------------

--
-- Table structure for table `vaccinations`
--

CREATE TABLE `vaccinations` (
  `id` int(11) NOT NULL,
  `cattle_id` int(11) NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `vaccine_name` varchar(100) NOT NULL,
  `dosage` varchar(50) NOT NULL,
  `date_given` date NOT NULL,
  `given_by` varchar(100) NOT NULL,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `weight_records`
--

CREATE TABLE `weight_records` (
  `id` int(11) NOT NULL,
  `cattle_id` int(11) NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `weight_kg` double NOT NULL,
  `measured_date` date NOT NULL,
  `recorder_name` varchar(100) NOT NULL,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cattle`
--
ALTER TABLE `cattle`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `tag_number` (`tag_number`),
  ADD KEY `farmer_id` (`farmer_id`);

--
-- Indexes for table `contact_enquiries`
--
ALTER TABLE `contact_enquiries`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `contact_messages`
--
ALTER TABLE `contact_messages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `grazing_records`
--
ALTER TABLE `grazing_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cattle_id` (`cattle_id`),
  ADD KEY `farmer_id` (`farmer_id`);

--
-- Indexes for table `health_records`
--
ALTER TABLE `health_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cattle_id` (`cattle_id`),
  ADD KEY `farmer_id` (`farmer_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `vaccinations`
--
ALTER TABLE `vaccinations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cattle_id` (`cattle_id`),
  ADD KEY `farmer_id` (`farmer_id`);

--
-- Indexes for table `weight_records`
--
ALTER TABLE `weight_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cattle_id` (`cattle_id`),
  ADD KEY `farmer_id` (`farmer_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cattle`
--
ALTER TABLE `cattle`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `contact_enquiries`
--
ALTER TABLE `contact_enquiries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `contact_messages`
--
ALTER TABLE `contact_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `grazing_records`
--
ALTER TABLE `grazing_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `health_records`
--
ALTER TABLE `health_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `vaccinations`
--
ALTER TABLE `vaccinations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `weight_records`
--
ALTER TABLE `weight_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cattle`
--
ALTER TABLE `cattle`
  ADD CONSTRAINT `cattle_ibfk_1` FOREIGN KEY (`farmer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `grazing_records`
--
ALTER TABLE `grazing_records`
  ADD CONSTRAINT `grazing_records_ibfk_1` FOREIGN KEY (`cattle_id`) REFERENCES `cattle` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `grazing_records_ibfk_2` FOREIGN KEY (`farmer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `health_records`
--
ALTER TABLE `health_records`
  ADD CONSTRAINT `health_records_ibfk_1` FOREIGN KEY (`cattle_id`) REFERENCES `cattle` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `health_records_ibfk_2` FOREIGN KEY (`farmer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `vaccinations`
--
ALTER TABLE `vaccinations`
  ADD CONSTRAINT `vaccinations_ibfk_1` FOREIGN KEY (`cattle_id`) REFERENCES `cattle` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `vaccinations_ibfk_2` FOREIGN KEY (`farmer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `weight_records`
--
ALTER TABLE `weight_records`
  ADD CONSTRAINT `weight_records_ibfk_1` FOREIGN KEY (`cattle_id`) REFERENCES `cattle` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `weight_records_ibfk_2` FOREIGN KEY (`farmer_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
