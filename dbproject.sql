-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2019 at 02:14 AM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.2.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbproject`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` varchar(6) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `nama`, `username`, `password`) VALUES
('ADM001', 'rizaps', 'admin01', 'admin01');

-- --------------------------------------------------------

--
-- Table structure for table `anggota`
--

CREATE TABLE `anggota` (
  `id` varchar(4) NOT NULL,
  `nama` varchar(20) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `tgllahir` varchar(20) NOT NULL,
  `tempatlahir` varchar(20) NOT NULL,
  `gender` varchar(20) NOT NULL,
  `notlp` varchar(15) NOT NULL,
  `noktp` varchar(20) NOT NULL,
  `setujusimpanpokok` varchar(10) NOT NULL,
  `setujusimpanwajib` varchar(10) NOT NULL,
  `setujupinjamlunas` varchar(10) NOT NULL,
  `tglgabung` varchar(20) NOT NULL,
  `keterangan` varchar(20) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `anggota`
--

INSERT INTO `anggota` (`id`, `nama`, `alamat`, `tgllahir`, `tempatlahir`, `gender`, `notlp`, `noktp`, `setujusimpanpokok`, `setujusimpanwajib`, `setujupinjamlunas`, `tglgabung`, `keterangan`, `username`, `password`) VALUES
('A001', 'rizky', 'jalan terusan', '21 Juni 2002', 'cimahi', 'laki laki', '08957030714', '12345678910', 'SETUJU', 'SETUJU', 'SETUJU', '20 November 2019', 'AKTIF', 'rizky123', 'rizky123'),
('A002', 'aditya', 'jalan terusan', '20 Juni 2002', 'cimahi', 'laki laki', '0895378165090', '1122334455', 'SETUJU', 'SETUJU', 'SETUJU', '21 November 2019', 'AKTIF', 'aditya3', 'aditya321'),
('A003', 'max', 'arcadia bay', '01 Desember 2001', 'arcadia bay', 'perempuan', '08123412341234', '12345678901', 'SETUJU', 'SETUJU', 'SETUJU', '22 November 2019', 'AKTIF', 'max12345', 'max12345');

-- --------------------------------------------------------

--
-- Table structure for table `angsuran`
--

CREATE TABLE `angsuran` (
  `id` varchar(6) NOT NULL,
  `idpinjam` varchar(5) NOT NULL,
  `tanggal` varchar(20) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `jasa` int(11) NOT NULL,
  `jumlahbayar` int(11) NOT NULL,
  `angsuranke` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `angsuran`
--

INSERT INTO `angsuran` (`id`, `idpinjam`, `tanggal`, `jumlah`, `jasa`, `jumlahbayar`, `angsuranke`) VALUES
('ANG001', 'PJ001', '27 November 2019', 10000, 1000, 11000, 1),
('ANG002', 'PJ001', '27 November 2019', 10000, 800, 10800, 2),
('ANG003', 'PJ002', '27 November 2019', 10000, 1000, 11000, 1),
('ANG004', 'PJ002', '28 November 2019', 10000, 800, 10800, 2),
('ANG005', 'PJ003', '28 November 2019', 10000, 1000, 11000, 1),
('ANG006', 'PJ003', '28 November 2019', 10000, 800, 10800, 2),
('ANG007', 'PJ003', '28 November 2019', 10000, 600, 10600, 3),
('ANG008', 'PJ003', '28 November 2019', 10000, 400, 10400, 4),
('ANG009', 'PJ003', '28 November 2019', 10000, 200, 10200, 5),
('ANG010', 'PJ001', '29 November 2019', 10000, 600, 10600, 3);

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `id` varchar(4) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `petugas`
--

INSERT INTO `petugas` (`id`, `nama`, `username`, `password`) VALUES
('P001', 'perkasa', 'petugas01', 'petugas01'),
('P002', 'susila', 'petugas02', 'petugas02'),
('P003', 'jojo', 'petugas03', 'jojo03');

-- --------------------------------------------------------

--
-- Table structure for table `pinjam`
--

CREATE TABLE `pinjam` (
  `id` varchar(5) NOT NULL,
  `idanggota` varchar(4) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `sisa` int(11) NOT NULL,
  `jasa` double NOT NULL,
  `angsuran` int(11) NOT NULL,
  `tanggalpinjam` varchar(20) NOT NULL,
  `jatuhtempo` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pinjam`
--

INSERT INTO `pinjam` (`id`, `idanggota`, `jumlah`, `sisa`, `jasa`, `angsuran`, `tanggalpinjam`, `jatuhtempo`, `status`) VALUES
('PJ001', 'A001', 50000, 20000, 2, 5, '27 November 2019', '27 April 2020', 'BELUM LUNAS'),
('PJ002', 'A002', 50000, 30000, 2, 5, '27 November 2019', '27 April 2020', 'BELUM LUNAS'),
('PJ003', 'A003', 50000, 0, 2, 5, '28 November 2019', '28 April 2020', 'LUNAS');

-- --------------------------------------------------------

--
-- Table structure for table `saldo`
--

CREATE TABLE `saldo` (
  `id` varchar(6) NOT NULL,
  `idanggota` varchar(4) NOT NULL,
  `saldowajib` int(11) NOT NULL,
  `saldopokok` int(11) NOT NULL,
  `saldosukarela` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `saldo`
--

INSERT INTO `saldo` (`id`, `idanggota`, `saldowajib`, `saldopokok`, `saldosukarela`) VALUES
('SAL001', 'A001', 50000, 100000, 300000),
('SAL002', 'A002', 50000, 100000, 30000),
('SAL003', 'A003', 50000, 100000, 10000);

-- --------------------------------------------------------

--
-- Table structure for table `simpan`
--

CREATE TABLE `simpan` (
  `id` varchar(4) NOT NULL,
  `idanggota` varchar(4) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `tanggal` varchar(20) NOT NULL,
  `jenis` varchar(20) NOT NULL,
  `jatuhtempo` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `simpan`
--

INSERT INTO `simpan` (`id`, `idanggota`, `jumlah`, `tanggal`, `jenis`, `jatuhtempo`) VALUES
('S001', 'A001', 100000, '27 November 2019', 'POKOK', '15 November 2019'),
('S002', 'A001', 50000, '27 November 2019', 'WAJIB', '15 November 2019'),
('S003', 'A001', 500000, '27 November 2019', 'SUKARELA', '15 November 2019'),
('S004', 'A002', 100000, '27 November 2019', 'POKOK', '15 November 2019'),
('S005', 'A002', 50000, '27 November 2019', 'WAJIB', '15 November 2019'),
('S006', 'A002', 100000, '27 November 2019', 'SUKARELA', '15 November 2019'),
('S007', 'A003', 100000, '28 November 2019', 'POKOK', '15 November 2019'),
('S008', 'A003', 50000, '28 November 2019', 'WAJIB', '15 November 2019'),
('S009', 'A003', 60000, '28 November 2019', 'SUKARELA', '15 November 2019');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `anggota`
--
ALTER TABLE `anggota`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `angsuran`
--
ALTER TABLE `angsuran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idpinjam` (`idpinjam`);

--
-- Indexes for table `petugas`
--
ALTER TABLE `petugas`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pinjam`
--
ALTER TABLE `pinjam`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idanggota` (`idanggota`);

--
-- Indexes for table `saldo`
--
ALTER TABLE `saldo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idanggota` (`idanggota`);

--
-- Indexes for table `simpan`
--
ALTER TABLE `simpan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idanggota` (`idanggota`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `angsuran`
--
ALTER TABLE `angsuran`
  ADD CONSTRAINT `angsuran_ibfk_1` FOREIGN KEY (`idpinjam`) REFERENCES `pinjam` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `pinjam`
--
ALTER TABLE `pinjam`
  ADD CONSTRAINT `pinjam_ibfk_1` FOREIGN KEY (`idanggota`) REFERENCES `anggota` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `saldo`
--
ALTER TABLE `saldo`
  ADD CONSTRAINT `saldo_ibfk_1` FOREIGN KEY (`idanggota`) REFERENCES `anggota` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `simpan`
--
ALTER TABLE `simpan`
  ADD CONSTRAINT `simpan_ibfk_1` FOREIGN KEY (`idanggota`) REFERENCES `anggota` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
