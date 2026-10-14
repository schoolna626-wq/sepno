<?php
include_once "api_helper.php";
$table = find_table($conn, ["mobil", "tb_mobil"]);
if (!$table) {
    json_fail("Tabel mobil tidak ditemukan");
}
echo_list(fetch_all($conn, "SELECT * FROM `$table`"));
