package com.example.tugasando;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataCicilActivity extends AppCompatActivity {

    private ArrayList<String> itemList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pembayarancicilan);

        Button btnTambah = findViewById(R.id.tomboltambah);
        ListView listView = findViewById(R.id.listbayarcicilan);

        itemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        btnTambah.setOnClickListener(v ->
                startActivity(new Intent(this, TambahCicilanActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tampilData();
    }

    private void tampilData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Config.TAMPIL_CICILAN,
                response -> {
                    try {
                        JSONArray jsonArray = JsonHelper.asArray(response);
                        itemList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            itemList.add(
                                    "Kode Cicilan: " + JsonHelper.str(obj, "kode_cicilan")
                                            + "\nKode Kredit: " + JsonHelper.str(obj, "kode_kredit")
                                            + "\nTanggal: " + JsonHelper.str(obj, "tanggal_cicilan", "tgl_cicilan")
                                            + "\nCicilan ke: " + JsonHelper.str(obj, "cicilanke", "cicilan_ke")
                                            + "\nJumlah: " + JsonHelper.str(obj, "jumlah_cicilan", "besar_cicilan")
                                            + "\nSisa: " + JsonHelper.str(obj, "sisa_cicilan")
                            );
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal membaca data cicilan", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data cicilan. Cek XAMPP dan IP 192.168.1.8", Toast.LENGTH_LONG).show()
        );
        Volley.newRequestQueue(this).add(request);
    }
}
