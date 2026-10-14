<?php
header("Content-Type: application/json; charset=utf-8");
header("Access-Control-Allow-Origin: *");
include_once "Koneksi.php";

function json_fail($message) {
    echo json_encode([
        "success" => false,
        "code" => 0,
        "status" => "gagal",
        "message" => $message
    ]);
    exit;
}

function json_ok($message) {
    echo json_encode([
        "success" => true,
        "code" => 1,
        "status" => "sukses",
        "message" => $message
    ]);
    exit;
}

function post_val($keys, $default = "") {
    foreach ((array) $keys as $key) {
        if (isset($_POST[$key]) && $_POST[$key] !== "") {
            return trim($_POST[$key]);
        }
    }
    return $default;
}

function table_exists($conn, $table) {
    $safe = $conn->real_escape_string($table);
    $result = $conn->query("SHOW TABLES LIKE '$safe'");
    return $result && $result->num_rows > 0;
}

function find_table($conn, $candidates) {
    foreach ($candidates as $table) {
        if (table_exists($conn, $table)) {
            return $table;
        }
    }
    return null;
}

function table_columns($conn, $table) {
    $columns = [];
    $result = $conn->query("SHOW COLUMNS FROM `$table`");
    if (!$result) {
        return $columns;
    }
    while ($row = $result->fetch_assoc()) {
        $columns[] = $row["Field"];
    }
    return $columns;
}

function pick_column($columns, $candidates) {
    foreach ($candidates as $candidate) {
        foreach ($columns as $column) {
            if (strcasecmp($column, $candidate) === 0) {
                return $column;
            }
        }
    }
    return null;
}

function enrich_row($row) {
    $aliases = [
        "nama" => ["nama", "nama_pembeli"],
        "nama_pembeli" => ["nama_pembeli", "nama"],
        "alamat" => ["alamat", "alamat_pembeli"],
        "alamat_pembeli" => ["alamat_pembeli", "alamat"],
        "telp" => ["telp", "telp_pembeli", "no_hp", "hp"],
        "telp_pembeli" => ["telp_pembeli", "telp", "no_hp", "hp"],
        "merk" => ["merk", "merek", "merk_mobil", "nama_mobil"],
        "merek" => ["merek", "merk", "merk_mobil"],
        "merk_mobil" => ["merk_mobil", "merk", "merek"],
        "type" => ["type", "tipe", "type_mobil"],
        "tipe" => ["tipe", "type", "type_mobil"],
        "type_mobil" => ["type_mobil", "type", "tipe"],
        "warna_mobil" => ["warna_mobil", "warna"],
        "harga" => ["harga", "harga_mobil"],
        "harga_mobil" => ["harga_mobil", "harga"],
        "nama_mobil" => ["nama_mobil", "merk", "merek", "merk_mobil"],
        "uang_muka" => ["uang_muka", "dp", "nilai_uang_muka"],
        "dp" => ["dp", "uang_muka", "nilai_uang_muka"],
        "tenor" => ["tenor", "jumlah_cicilan", "lama_cicilan"],
        "bunga_cicilan" => ["bunga_cicilan", "bunga"],
        "bunga" => ["bunga", "bunga_cicilan"],
        "cash_bayar" => ["cash_bayar", "bayar_cash"],
        "bayar_cash" => ["bayar_cash", "cash_bayar"],
        "cash_tanggal" => ["cash_tanggal", "tgl_cash", "tanggal_cash"],
        "tanggal_kredit" => ["tanggal_kredit", "tgl_kredit"],
        "tanggal_cicilan" => ["tanggal_cicilan", "tgl_cicilan"],
        "cicilanke" => ["cicilanke", "cicilan_ke"],
        "jumlah_cicilan" => ["jumlah_cicilan", "besar_cicilan"]
    ];

    foreach ($aliases as $want => $from) {
        if (!isset($row[$want]) || $row[$want] === null || $row[$want] === "") {
            foreach ($from as $source) {
                if (isset($row[$source]) && $row[$source] !== "") {
                    $row[$want] = $row[$source];
                    break;
                }
            }
        }
    }
    return $row;
}

function fetch_all($conn, $sql) {
    $rows = [];
    $result = $conn->query($sql);
    if (!$result) {
        json_fail($conn->error);
    }
    while ($row = $result->fetch_assoc()) {
        $rows[] = enrich_row($row);
    }
    return $rows;
}

function echo_list($rows) {
    echo json_encode(array_values($rows));
    exit;
}

function insert_mapped($conn, $table, $fieldMap) {
    $columns = table_columns($conn, $table);
    $data = [];
    foreach ($fieldMap as $item) {
        $candidates = (array) $item[0];
        $value = isset($item[1]) ? $item[1] : "";
        $column = pick_column($columns, $candidates);
        if ($column !== null && $value !== "") {
            $data[$column] = $value;
        }
    }
    if (empty($data)) {
        json_fail("Tidak ada kolom yang cocok untuk disimpan");
    }

    $cols = [];
    $placeholders = [];
    $values = [];
    $types = "";
    foreach ($data as $column => $value) {
        $cols[] = "`$column`";
        $placeholders[] = "?";
        $values[] = $value;
        $types .= "s";
    }

    $sql = "INSERT INTO `$table` (" . implode(",", $cols) . ") VALUES (" . implode(",", $placeholders) . ")";
    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        json_fail($conn->error);
    }
    $stmt->bind_param($types, ...$values);
    if (!$stmt->execute()) {
        json_fail($stmt->error);
    }
    json_ok("Data berhasil disimpan");
}

function update_mapped($conn, $table, $whereColumnCandidates, $whereValue, $fieldMap) {
    $columns = table_columns($conn, $table);
    $whereColumn = pick_column($columns, $whereColumnCandidates);
    if ($whereColumn === null) {
        json_fail("Kolom kunci tidak ditemukan");
    }

    $sets = [];
    $values = [];
    $types = "";
    foreach ($fieldMap as $item) {
        $candidates = (array) $item[0];
        $value = isset($item[1]) ? $item[1] : "";
        $column = pick_column($columns, $candidates);
        if ($column !== null) {
            $sets[] = "`$column`=?";
            $values[] = $value;
            $types .= "s";
        }
    }
    if (empty($sets)) {
        json_fail("Tidak ada kolom yang bisa diupdate");
    }

    $values[] = $whereValue;
    $types .= "s";
    $sql = "UPDATE `$table` SET " . implode(",", $sets) . " WHERE `$whereColumn`=?";
    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        json_fail($conn->error);
    }
    $stmt->bind_param($types, ...$values);
    if (!$stmt->execute()) {
        json_fail($stmt->error);
    }
    json_ok("Data berhasil diupdate");
}

function delete_mapped($conn, $table, $whereColumnCandidates, $whereValue) {
    $columns = table_columns($conn, $table);
    $whereColumn = pick_column($columns, $whereColumnCandidates);
    if ($whereColumn === null) {
        json_fail("Kolom kunci tidak ditemukan");
    }
    $stmt = $conn->prepare("DELETE FROM `$table` WHERE `$whereColumn`=?");
    if (!$stmt) {
        json_fail($conn->error);
    }
    $stmt->bind_param("s", $whereValue);
    if (!$stmt->execute()) {
        json_fail($stmt->error);
    }
    json_ok("Data berhasil dihapus");
}
