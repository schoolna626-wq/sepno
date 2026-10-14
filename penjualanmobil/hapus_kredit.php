<?php
include_once "api_helper.php";
$table = find_table($conn, ["beli_kredit", "kredit", "tb_kredit"]);
if (!$table) {
    json_fail("Tabel kredit tidak ditemukan");
}
$kode = post_val(["kode_kredit"]);
if ($kode === "") {
    json_fail("Kode kredit wajib diisi");
}
delete_mapped($conn, $table, ["kode_kredit"], $kode);
