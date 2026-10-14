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

public class TambahPaketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_paket);

        EditText editKode = findViewById(R.id.editkodepaket);
        EditText editDp = findViewById(R.id.editdp);
        EditText editTenor = findViewById(R.id.edittenor);
        EditText editBunga = findViewById(R.id.editbunga);
        Button tombolSimpan = findViewById(R.id.tombolsimpan);

        tombolSimpan.setOnClickListener(v -> {
            String kode = editKode.getText().toString().trim();
            String dp = editDp.getText().toString().trim();
            String tenor = editTenor.getText().toString().trim();
            String bunga = editBunga.getText().toString().trim();

            if (kode.isEmpty() || dp.isEmpty() || tenor.isEmpty() || bunga.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data paket", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Config.TAMBAH_PAKET,
                    response -> {
                        if (JsonHelper.success(response)) {
                            Toast.makeText(this, JsonHelper.message(response, "Data paket berhasil disimpan"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, JsonHelper.message(response, "Data paket gagal disimpan"), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> Toast.makeText(this, "Gagal menyimpan data paket", Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("kode_paket", kode);
                    params.put("uang_muka", dp);
                    params.put("dp", dp);
                    params.put("tenor", tenor);
                    params.put("jumlah_cicilan", tenor);
                    params.put("bunga_cicilan", bunga);
                    params.put("bunga", bunga);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        });
    }
}
