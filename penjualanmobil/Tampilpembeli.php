<?php
include_once "api_helper.php";
$table = find_table($conn, ["pembeli", "tb_pembeli"]);
if (!$table) {
    json_fail("Tabel pembeli tidak ditemukan");
}
echo_list(fetch_all($conn, "SELECT * FROM `$table`"));
