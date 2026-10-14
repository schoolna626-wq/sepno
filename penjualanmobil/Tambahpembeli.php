<?php
include_once "api_helper.php";
$table = find_table($conn, ["pembeli", "tb_pembeli"]);
if (!$table) {
    json_fail("Tabel pembeli tidak ditemukan");
}

$ktp = post_val(["ktp"]);
$nama = post_val(["nama", "nama_pembeli"]);
$alamat = post_val(["alamat", "alamat_pembeli"]);
$telp = post_val(["telp", "telp_pembeli", "no_hp", "hp"]);

if ($ktp === "" || $nama === "" || $alamat === "" || $telp === "") {
    json_fail("Semua data pembeli wajib diisi");
}

insert_mapped($conn, $table, [
    [["ktp"], $ktp],
    [["nama", "nama_pembeli"], $nama],
    [["alamat", "alamat_pembeli"], $alamat],
    [["telp", "telp_pembeli", "no_hp", "hp"], $telp]
]);
