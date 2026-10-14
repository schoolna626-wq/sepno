<?php
$host = "localhost";
$user = "root";
$pass = "";
$db   = "penjualanmobil";

$conn = new mysqli($host, $user, $pass);
if ($conn->connect_error) {
    header("Content-Type: application/json");
    echo json_encode([
        "success" => false,
        "message" => "Koneksi MySQL gagal: " . $conn->connect_error
    ]);
    exit;
}

$conn->query("CREATE DATABASE IF NOT EXISTS `$db`");
$conn->select_db($db);
$conn->set_charset("utf8mb4");

$conn->query("CREATE TABLE IF NOT EXISTS mobil (
    kode_mobil VARCHAR(20) PRIMARY KEY,
    merk VARCHAR(50),
    type VARCHAR(50),
    warna VARCHAR(30),
    harga DOUBLE
)");

$conn->query("CREATE TABLE IF NOT EXISTS pembeli (
    ktp VARCHAR(30) PRIMARY KEY,
    nama VARCHAR(100),
    alamat VARCHAR(200),
    telp VARCHAR(20)
)");

$conn->query("CREATE TABLE IF NOT EXISTS paket (
    kode_paket VARCHAR(20) PRIMARY KEY,
    uang_muka DOUBLE,
    tenor INT,
    bunga_cicilan DOUBLE
)");

$conn->query("CREATE TABLE IF NOT EXISTS beli_cash (
    kode_cash VARCHAR(20) PRIMARY KEY,
    ktp VARCHAR(30),
    kode_mobil VARCHAR(20),
    cash_tanggal DATE,
    cash_bayar DOUBLE
)");

$conn->query("CREATE TABLE IF NOT EXISTS beli_kredit (
    kode_kredit VARCHAR(20) PRIMARY KEY,
    kode_paket VARCHAR(20),
    ktp VARCHAR(30),
    kode_mobil VARCHAR(20),
    tanggal_kredit DATE,
    bayar_kredit DOUBLE
)");

$conn->query("CREATE TABLE IF NOT EXISTS bayar_cicilan (
    kode_cicilan VARCHAR(20) PRIMARY KEY,
    kode_kredit VARCHAR(20),
    tanggal_cicilan DATE,
    cicilanke INT,
    jumlah_cicilan DOUBLE,
    sisa_cicilan DOUBLE
)");
