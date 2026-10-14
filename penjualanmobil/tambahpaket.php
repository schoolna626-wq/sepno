<?php
include_once "api_helper.php";
$table = find_table($conn, ["paket", "tb_paket"]);
if (!$table) {
    json_fail("Tabel paket tidak ditemukan");
}

$kode = post_val(["kode_paket"]);
$dp = post_val(["uang_muka", "dp", "nilai_uang_muka"]);
$tenor = post_val(["tenor", "jumlah_cicilan", "lama_cicilan"]);
$bunga = post_val(["bunga_cicilan", "bunga"]);

if ($kode === "" || $dp === "" || $tenor === "" || $bunga === "") {
    json_fail("Semua data paket wajib diisi");
}

insert_mapped($conn, $table, [
    [["kode_paket"], $kode],
    [["uang_muka", "dp", "nilai_uang_muka"], $dp],
    [["tenor", "jumlah_cicilan", "lama_cicilan"], $tenor],
    [["bunga_cicilan", "bunga"], $bunga]
]);
