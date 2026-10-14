<?php
include_once "api_helper.php";
$table = find_table($conn, ["beli_cash", "cash", "tb_cash", "bayar_cash"]);
if (!$table) {
    json_fail("Tabel cash tidak ditemukan");
}

$kode = post_val(["kode_cash"]);
$ktp = post_val(["ktp"]);
$mobil = post_val(["kode_mobil"]);
$tanggal = post_val(["cash_tanggal", "tgl_cash", "tanggal_cash"]);
$bayar = post_val(["cash_bayar", "bayar_cash"]);

if ($kode === "" || $ktp === "" || $mobil === "" || $tanggal === "" || $bayar === "") {
    json_fail("Semua data cash wajib diisi");
}

insert_mapped($conn, $table, [
    [["kode_cash"], $kode],
    [["ktp"], $ktp],
    [["kode_mobil"], $mobil],
    [["cash_tanggal", "tgl_cash", "tanggal_cash"], $tanggal],
    [["cash_bayar", "bayar_cash"], $bayar]
]);
