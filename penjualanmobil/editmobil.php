<?php
include_once "api_helper.php";
$table = find_table($conn, ["mobil", "tb_mobil"]);
if (!$table) {
    json_fail("Tabel mobil tidak ditemukan");
}

$kode = post_val(["kode_mobil", "kode"]);
$merk = post_val(["merk", "merek", "merk_mobil"]);
$type = post_val(["type", "tipe", "type_mobil"]);
$warna = post_val(["warna", "warna_mobil"]);
$harga = post_val(["harga", "harga_mobil"]);

if ($kode === "") {
    json_fail("Kode mobil wajib diisi");
}

update_mapped($conn, $table, ["kode_mobil", "kode"], $kode, [
    [["merk", "merek", "merk_mobil"], $merk],
    [["type", "tipe", "type_mobil"], $type],
    [["warna", "warna_mobil"], $warna],
    [["harga", "harga_mobil"], $harga]
]);
