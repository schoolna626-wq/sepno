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

public class TambahKreditActivity extends AppCompatActivity {

    private EditText editKodeKredit, editTanggal, editCicilan;
    private Spinner spinnerPembeli, spinnerMobil, spinnerPaket;
    private Button btnSimpan;

    private final ArrayList<String> listPembeli = new ArrayList<>();
    private final ArrayList<String> listMobil = new ArrayList<>();
    private final ArrayList<String> listPaket = new ArrayList<>();
    private final ArrayList<String> idPembeli = new ArrayList<>();
    private final ArrayList<String> idMobil = new ArrayList<>();
    private final ArrayList<String> idPaket = new ArrayList<>();
    private final ArrayList<Double> listDp = new ArrayList<>();
    private final ArrayList<Integer> listTenor = new ArrayList<>();
    private final ArrayList<Double> listBunga = new ArrayList<>();
    private final ArrayList<Double> listHargaMobil = new ArrayList<>();
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_beli_kredit);

        editKodeKredit = findViewById(R.id.edit_kode_kredit);
        editTanggal = findViewById(R.id.edit_tanggal);
        editCicilan = findViewById(R.id.edit_cicilan);
        spinnerPembeli = findViewById(R.id.spinner_pembeli);
        spinnerMobil = findViewById(R.id.spinner_mobil);
        spinnerPaket = findViewById(R.id.spinner_paket);
        btnSimpan = findViewById(R.id.tombol_simpan_kredit);

        editTanggal.setOnClickListener(v -> new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    calendar.set(year, month, day);
                    editTanggal.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        android.widget.AdapterView.OnItemSelectedListener hitungListener =
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        hitungCicilanOtomatis();
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                };
        spinnerMobil.setOnItemSelectedListener(hitungListener);
        spinnerPaket.setOnItemSelectedListener(hitungListener);

        btnSimpan.setOnClickListener(v -> simpanDataKredit());
        loadPembeli();
        loadMobil();
        loadPaket();
    }

    private void loadPembeli() {
        StringRequest request = new StringRequest(Request.Method.GET, Config.TAMPIL_PEMBELI,
                response -> {
                    try {
                        JSONArray array = JsonHelper.asArray(response);
                        listPembeli.clear();
                        idPembeli.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            String ktp = JsonHelper.str(data, "ktp");
                            String nama = JsonHelper.str(data, "nama", "nama_pembeli");
                            idPembeli.add(ktp);
                            listPembeli.add(nama + " (" + ktp + ")");
                        }
                        spinnerPembeli.setAdapter(spinnerAdapter(listPembeli));
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal membaca data pembeli", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data pembeli", Toast.LENGTH_LONG).show());
        Volley.newRequestQueue(this).add(request);
    }

    private void loadMobil() {
        StringRequest request = new StringRequest(Request.Method.GET, Config.TAMPIL_MOBIL,
                response -> {
                    try {
                        JSONArray array = JsonHelper.asArray(response);
                        listMobil.clear();
                        idMobil.clear();
                        listHargaMobil.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            String kode = JsonHelper.str(data, "kode_mobil");
                            String nama = JsonHelper.str(data, "nama_mobil", "merk", "merek", "merk_mobil");
                            double harga = JsonHelper.num(data, "harga", "harga_mobil");
                            idMobil.add(kode);
                            listHargaMobil.add(harga);
                            listMobil.add(nama + " - Rp " + String.format(Locale.US, "%.0f", harga));
                        }
                        spinnerMobil.setAdapter(spinnerAdapter(listMobil));
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal membaca data mobil", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data mobil", Toast.LENGTH_LONG).show());
        Volley.newRequestQueue(this).add(request);
    }

    private void loadPaket() {
        StringRequest request = new StringRequest(Request.Method.GET, Config.TAMPIL_PAKET,
                response -> {
                    try {
                        JSONArray array = JsonHelper.asArray(response);
                        listPaket.clear();
                        idPaket.clear();
                        listDp.clear();
                        listTenor.clear();
                        listBunga.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            String kode = JsonHelper.str(data, "kode_paket");
                            double dp = JsonHelper.num(data, "uang_muka", "dp", "nilai_uang_muka");
                            int tenor = (int) JsonHelper.num(data, "tenor", "jumlah_cicilan", "lama_cicilan");
                            double bunga = JsonHelper.num(data, "bunga_cicilan", "bunga");
                            idPaket.add(kode);
                            listDp.add(dp);
                            listTenor.add(tenor);
                            listBunga.add(bunga);
                            listPaket.add(kode + " | DP " + dp + "% | Tenor " + tenor + " bulan | Bunga " + bunga + "%");
                        }
                        spinnerPaket.setAdapter(spinnerAdapter(listPaket));
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal membaca data paket", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data paket", Toast.LENGTH_LONG).show());
        Volley.newRequestQueue(this).add(request);
    }

    private ArrayAdapter<String> spinnerAdapter(ArrayList<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void hitungCicilanOtomatis() {
        int posMobil = spinnerMobil.getSelectedItemPosition();
        int posPaket = spinnerPaket.getSelectedItemPosition();
        if (posMobil < 0 || posPaket < 0
                || posMobil >= listHargaMobil.size()
                || posPaket >= listDp.size()) {
            return;
        }

        double harga = listHargaMobil.get(posMobil);
        double dpPersen = listDp.get(posPaket);
        int tenor = listTenor.get(posPaket);
        double bungaPersen = listBunga.get(posPaket);
        if (tenor <= 0) {
            return;
        }

        double uangMuka = harga * dpPersen / 100.0;
        double hutang = harga - uangMuka;
        double bungaTotal = hutang * (bungaPersen / 100.0) / 12.0 * tenor;
        double cicilan = (hutang + bungaTotal) / tenor;
        editCicilan.setText(String.format(Locale.US, "%.0f", cicilan));
    }

    private void simpanDataKredit() {
        String kodeKredit = editKodeKredit.getText().toString().trim();
        String tanggal = editTanggal.getText().toString().trim();
        String cicilan = editCicilan.getText().toString().trim();
        int posPembeli = spinnerPembeli.getSelectedItemPosition();
        int posMobil = spinnerMobil.getSelectedItemPosition();
        int posPaket = spinnerPaket.getSelectedItemPosition();

        if (kodeKredit.isEmpty() || tanggal.isEmpty() || cicilan.isEmpty()
                || posPembeli < 0 || posPembeli >= idPembeli.size()
                || posMobil < 0 || posMobil >= idMobil.size()
                || posPaket < 0 || posPaket >= idPaket.size()) {
            Toast.makeText(this, "Lengkapi kode, tanggal, pembeli, mobil, dan paket", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSimpan.setEnabled(false);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Config.TAMBAH_KREDIT,
                response -> {
                    btnSimpan.setEnabled(true);
                    if (JsonHelper.success(response)) {
                        Toast.makeText(this, JsonHelper.message(response, "Data kredit berhasil disimpan"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, JsonHelper.message(response, "Data kredit gagal disimpan"), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    btnSimpan.setEnabled(true);
                    Toast.makeText(this, "Gagal menyimpan data kredit", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kode_kredit", kodeKredit);
                params.put("kode_paket", idPaket.get(posPaket));
                params.put("ktp", idPembeli.get(posPembeli));
                params.put("kode_mobil", idMobil.get(posMobil));
                params.put("tanggal_kredit", tanggal);
                params.put("tgl_kredit", tanggal);
                params.put("bayar_kredit", cicilan);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
