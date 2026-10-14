package com.example.tugasando;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TambahCicilanActivity extends AppCompatActivity {

    private EditText editKodeCicilan, editTanggal, editCicilKe, editBesarCicilan, editSisa;
    private Spinner spinnerKodeKredit;
    private Button tombolSimpan;
    private final ArrayList<String> daftarKredit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bayar_cicilan);

        editKodeCicilan = findViewById(R.id.editkodecicilan);
        spinnerKodeKredit = findViewById(R.id.spinnerkodekredit);
        editTanggal = findViewById(R.id.edittanggal);
        editCicilKe = findViewById(R.id.editcicilke);
        editBesarCicilan = findViewById(R.id.editbesarcicilan);
        editSisa = findViewById(R.id.editsisa);
        tombolSimpan = findViewById(R.id.tombolsimpan);

        editTanggal.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, day) -> editTanggal.setText(
                            String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        tombolSimpan.setOnClickListener(v -> simpanDataCicilan());
        loadKredit();
    }

    private void loadKredit() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Config.TAMPIL_KREDIT,
                response -> {
                    try {
                        JSONArray array = JsonHelper.asArray(response);
                        daftarKredit.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            String kode = JsonHelper.str(data, "kode_kredit");
                            if (!kode.isEmpty()) {
                                daftarKredit.add(kode);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this, android.R.layout.simple_spinner_item, daftarKredit);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerKodeKredit.setAdapter(adapter);
                        if (daftarKredit.isEmpty()) {
                            Toast.makeText(this, "Data kredit belum tersedia", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Format data kredit tidak sesuai", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data kredit. Cek XAMPP dan PHP", Toast.LENGTH_LONG).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void simpanDataCicilan() {
        String kodeCicilan = editKodeCicilan.getText().toString().trim();
        String tanggal = editTanggal.getText().toString().trim();
        String cicilanKe = editCicilKe.getText().toString().trim();
        String jumlah = editBesarCicilan.getText().toString().trim();
        String sisa = editSisa.getText().toString().trim();
        Object selected = spinnerKodeKredit.getSelectedItem();
        String kodeKredit = selected == null ? "" : selected.toString();

        if (kodeCicilan.isEmpty() || kodeKredit.isEmpty() || tanggal.isEmpty()
                || cicilanKe.isEmpty() || jumlah.isEmpty() || sisa.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        tombolSimpan.setEnabled(false);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Config.TAMBAH_CICILAN,
                response -> {
                    tombolSimpan.setEnabled(true);
                    if (JsonHelper.success(response)) {
                        Toast.makeText(this, JsonHelper.message(response, "Pembayaran cicilan berhasil disimpan"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, JsonHelper.message(response, "Pembayaran cicilan gagal disimpan"), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    tombolSimpan.setEnabled(true);
                    Toast.makeText(this, "Gagal mengirim data ke server", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kode_cicilan", kodeCicilan);
                params.put("kode_kredit", kodeKredit);
                params.put("tanggal_cicilan", tanggal);
                params.put("tgl_cicilan", tanggal);
                params.put("cicilanke", cicilanKe);
                params.put("cicilan_ke", cicilanKe);
                params.put("jumlah_cicilan", jumlah);
                params.put("besar_cicilan", jumlah);
                params.put("sisa_cicilan", sisa);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
