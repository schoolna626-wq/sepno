<?php
include_once "api_helper.php";
$table = find_table($conn, ["bayar_cicilan", "cicilan", "tb_cicilan"]);
if (!$table) {
    json_fail("Tabel cicilan tidak ditemukan");
}

$kode = post_val(["kode_cicilan"]);
$kredit = post_val(["kode_kredit"]);
$tanggal = post_val(["tanggal_cicilan", "tgl_cicilan"]);
$ke = post_val(["cicilanke", "cicilan_ke"]);
$jumlah = post_val(["jumlah_cicilan", "besar_cicilan"]);
$sisa = post_val(["sisa_cicilan"]);

if ($kode === "" || $kredit === "" || $tanggal === "" || $ke === "" || $jumlah === "" || $sisa === "") {
    json_fail("Semua data cicilan wajib diisi");
}

insert_mapped($conn, $table, [
    [["kode_cicilan"], $kode],
    [["kode_kredit"], $kredit],
    [["tanggal_cicilan", "tgl_cicilan"], $tanggal],
    [["cicilanke", "cicilan_ke"], $ke],
    [["jumlah_cicilan", "besar_cicilan"], $jumlah],
    [["sisa_cicilan"], $sisa]
]);
