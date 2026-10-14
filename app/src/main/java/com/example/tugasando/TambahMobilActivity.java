package com.example.tugasando;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class TambahMobilActivity extends AppCompatActivity {

    private EditText editKodeMobil, editMerek, editTipe, editWarna, editHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mobil);

        editKodeMobil = findViewById(R.id.edit_kode_mobil);
        editMerek = findViewById(R.id.edit_merek);
        editTipe = findViewById(R.id.edit_tipe);
        editWarna = findViewById(R.id.edit_warna);
        editHarga = findViewById(R.id.edit_harga);
        Button tombolSimpanMobil = findViewById(R.id.tombol_simpan_mobil);

        tombolSimpanMobil.setOnClickListener(v -> simpanDataMobil());
    }

    private void simpanDataMobil() {
        String kode = editKodeMobil.getText().toString().trim();
        String merek = editMerek.getText().toString().trim();
        String tipe = editTipe.getText().toString().trim();
        String warna = editWarna.getText().toString().trim();
        String harga = editHarga.getText().toString().trim();

        if (kode.isEmpty() || merek.isEmpty() || tipe.isEmpty() || warna.isEmpty() || harga.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Config.TAMBAH_MOBIL,
                response -> {
                    if (JsonHelper.success(response)) {
                        Toast.makeText(this, JsonHelper.message(response, "Data mobil berhasil disimpan"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, JsonHelper.message(response, "Data mobil gagal disimpan"), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal menyimpan data mobil", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kode_mobil", kode);
                params.put("merek", merek);
                params.put("merk", merek);
                params.put("merk_mobil", merek);
                params.put("tipe", tipe);
                params.put("type", tipe);
                params.put("type_mobil", tipe);
                params.put("warna", warna);
                params.put("warna_mobil", warna);
                params.put("harga", harga);
                params.put("harga_mobil", harga);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
