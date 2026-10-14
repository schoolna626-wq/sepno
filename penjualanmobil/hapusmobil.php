<?php
include_once "api_helper.php";
$table = find_table($conn, ["mobil", "tb_mobil"]);
if (!$table) {
    json_fail("Tabel mobil tidak ditemukan");
}
$kode = post_val(["kode_mobil", "kode"]);
if ($kode === "") {
    json_fail("Kode mobil wajib diisi");
}
delete_mapped($conn, $table, ["kode_mobil", "kode"], $kode);
