<?php
include_once "api_helper.php";
$table = find_table($conn, ["bayar_cicilan", "cicilan", "tb_cicilan"]);
if (!$table) {
    json_fail("Tabel cicilan tidak ditemukan");
}
echo_list(fetch_all($conn, "SELECT * FROM `$table`"));
