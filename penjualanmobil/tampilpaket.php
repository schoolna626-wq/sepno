<?php
include_once "api_helper.php";
$table = find_table($conn, ["paket", "tb_paket"]);
if (!$table) {
    json_fail("Tabel paket tidak ditemukan");
}
echo_list(fetch_all($conn, "SELECT * FROM `$table`"));
