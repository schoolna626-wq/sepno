<?php
include_once "api_helper.php";
$table = find_table($conn, ["beli_kredit", "kredit", "tb_kredit"]);
if (!$table) {
    json_fail("Tabel kredit tidak ditemukan");
}

$pembeli = find_table($conn, ["pembeli", "tb_pembeli"]);
$mobil = find_table($conn, ["mobil", "tb_mobil"]);

if ($pembeli && $mobil) {
    $sql = "SELECT k.*, p.*, m.* FROM `$table` k
            LEFT JOIN `$pembeli` p ON k.ktp = p.ktp
            LEFT JOIN `$mobil` m ON k.kode_mobil = m.kode_mobil";
    echo_list(fetch_all($conn, $sql));
}

echo_list(fetch_all($conn, "SELECT * FROM `$table`"));
