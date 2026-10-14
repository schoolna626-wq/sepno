package com.example.tugasando;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonHelper {

    private JsonHelper() {
    }

    public static JSONArray asArray(String response) throws JSONException {
        String hasil = response == null ? "" : response.trim();
        if (hasil.isEmpty()) {
            return new JSONArray();
        }
        if (hasil.startsWith("[")) {
            return new JSONArray(hasil);
        }

        JSONObject objek = new JSONObject(hasil);
        if (objek.has("result") && objek.get("result") instanceof JSONArray) {
            return objek.getJSONArray("result");
        }
        if (objek.has("data") && objek.get("data") instanceof JSONArray) {
            return objek.getJSONArray("data");
        }
        return new JSONArray();
    }

    public static String str(JSONObject objek, String... keys) {
        for (String key : keys) {
            if (objek.has(key) && !objek.isNull(key)) {
                String nilai = objek.optString(key, "").trim();
                if (!nilai.isEmpty() && !"null".equalsIgnoreCase(nilai)) {
                    return nilai;
                }
            }
        }
        return "";
    }

    public static double num(JSONObject objek, String... keys) {
        String nilai = str(objek, keys);
        if (nilai.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(nilai.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean success(String response) {
        if (response == null) {
            return false;
        }
        String hasil = response.trim();
        if (hasil.equalsIgnoreCase("BERHASIL")
                || hasil.toLowerCase().contains("berhasil disimpan")
                || hasil.toLowerCase().contains("berhasil diupdate")
                || hasil.toLowerCase().contains("berhasil dihapus")) {
            return true;
        }
        try {
            JSONObject objek = new JSONObject(hasil);
            return objek.optBoolean("success", false)
                    || objek.optInt("code", 0) == 1
                    || "sukses".equalsIgnoreCase(objek.optString("status"));
        } catch (JSONException e) {
            return false;
        }
    }

    public static String message(String response, String fallback) {
        if (response == null || response.trim().isEmpty()) {
            return fallback;
        }
        try {
            JSONObject objek = new JSONObject(response.trim());
            String pesan = objek.optString("message", "");
            if (!pesan.isEmpty()) {
                return pesan;
            }
        } catch (JSONException ignored) {
        }
        return response.trim();
    }
}
