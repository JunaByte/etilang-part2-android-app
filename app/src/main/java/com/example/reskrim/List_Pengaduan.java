package com.example.reskrim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class List_Pengaduan extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil_datapengaduan = new ArrayList<HashMap<String, String>>();
    adapter_datapengaduan adapter;
    String user_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengaduan);
        bacaPreferensi();
        tampil_pengaduan();
        rv = findViewById(R.id.rv_pengaduan);
        rv.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(getApplicationContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lim);
    }
    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        user_ = pref.getString(koneksi.username, "0");
    }
    private void tampil_pengaduan() {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.loading2);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();
            dialog.setCancelable(false);
            final StringRequest list_guru = new StringRequest(Request.Method.POST, koneksi.tampil,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("TAG", "onResponse: "+response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("hasil") == 1) {
                                    JSONArray hasil = jsonObject.getJSONArray("Pengaduan");
                                    tampil_datapengaduan.clear();
                                    for (int i = 0; i < hasil.length(); i++) {
                                        JSONObject c = hasil.getJSONObject(i);
                                        String tgl = c.getString(koneksi.tanggal);
                                        String id = c.getString(koneksi.id);
                                        String kategori = c.getString(koneksi.ket);
                                        String keterangan = c.getString(koneksi.uraian);
                                        String status = c.getString(koneksi.status);
                                        String nama_pelanggar = c.getString(koneksi.nama);
                                        String nama_petugas = c.getString(koneksi.username);
                                        String gmb = c.getString(koneksi.gambar);
										String itp = c.getString(koneksi.itp);
										String idp = c.getString(koneksi.idp);
										String lat = c.getString(koneksi.lati);
										String lng = c.getString(koneksi.longi);
										String alamat = c.getString(koneksi.alamat);
										String no = c.getString(koneksi.no_hp);
										String stnk = c.getString(koneksi.stnk);
										String merk = c.getString(koneksi.merk);
										String plat = c.getString(koneksi.plat);
										String warna = c.getString(koneksi.warna);
										String denda = c.getString(koneksi.denda);
										String jadwal = c.getString(koneksi.jadwal);
										String lokasi = c.getString(koneksi.lokasi);
										String tujuan = c.getString(koneksi.tujuan);

                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put(koneksi.tanggal, tgl);
                                        map.put(koneksi.id, id);
                                        map.put(koneksi.ket, kategori);
                                        map.put(koneksi.uraian, keterangan);
                                        map.put(koneksi.status, status);
                                        map.put(koneksi.nama, nama_pelanggar);
                                        map.put(koneksi.username, nama_petugas);
                                        map.put(koneksi.gambar, gmb);
										map.put(koneksi.itp, itp);
										map.put(koneksi.idp, idp);
										map.put(koneksi.lati, lat);
										map.put(koneksi.longi, lng);
										map.put(koneksi.alamat, alamat);
										map.put(koneksi.no_hp, no);
										map.put(koneksi.stnk, stnk);
										map.put(koneksi.merk, merk);
										map.put(koneksi.plat, plat);
										map.put(koneksi.warna, warna);
										map.put(koneksi.denda, denda);
										map.put(koneksi.jadwal, jadwal);
										map.put(koneksi.lokasi, lokasi);
										map.put(koneksi.tujuan, tujuan);
                                        tampil_datapengaduan.add(map);
                                    }
                                    adapter = new adapter_datapengaduan(getApplicationContext(), tampil_datapengaduan);
                                    rv.setAdapter(adapter);
                                    dialog.dismiss();
                                } else if (jsonObject.getInt("hasil") == 0) {
                                    dialog.dismiss();
                                    new SweetAlertDialog(List_Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf!!!")
                                            .setContentText("Data Tidak Ditemukan")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    new SweetAlertDialog(List_Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Jaringan Tidak Ada")
                            .show();
                    dialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put(koneksi.id, "pengaduan");
                    param.put(koneksi.username, user_);
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(list_guru);
        }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        super.onBackPressed();
    }
}
