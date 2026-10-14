<?php
include_once "api_helper.php";
$table = find_table($conn, ["beli_cash", "cash", "tb_cash", "bayar_cash"]);
if (!$table) {
    json_fail("Tabel cash tidak ditemukan");
}
echo_list(fetch_all($conn, "SELECT * FROM `$table`"));
