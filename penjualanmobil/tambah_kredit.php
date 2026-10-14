<?php
include_once "api_helper.php";
$table = find_table($conn, ["beli_kredit", "kredit", "tb_kredit"]);
if (!$table) {
    json_fail("Tabel kredit tidak ditemukan");
}

$kode = post_val(["kode_kredit"]);
$paket = post_val(["kode_paket"]);
$ktp = post_val(["ktp"]);
$mobil = post_val(["kode_mobil"]);
$tanggal = post_val(["tanggal_kredit", "tgl_kredit"]);
$bayar = post_val(["bayar_kredit", "cicilan"]);

if ($kode === "" || $paket === "" || $ktp === "" || $mobil === "" || $tanggal === "" || $bayar === "") {
    json_fail("Semua data kredit wajib diisi");
}

insert_mapped($conn, $table, [
    [["kode_kredit"], $kode],
    [["kode_paket"], $paket],
    [["ktp"], $ktp],
    [["kode_mobil"], $mobil],
    [["tanggal_kredit", "tgl_kredit"], $tanggal],
    [["bayar_kredit", "cicilan"], $bayar]
]);
