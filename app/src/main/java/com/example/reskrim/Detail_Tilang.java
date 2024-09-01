package com.example.reskrim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Detail_Tilang extends AppCompatActivity {
	String idd;
	ImageView gmb1;
	TextView id1, tgl1, nama_petugas1, nama_pelanggar1, alamat1, nohp1, stnk1, merk1, plat1,
			warna1, denda1, jadwal1, lokasi_sidang1, tujuan1, keterangan1, kategori1, status1;
	RecyclerView rv;
	Button btnnotif;

	ArrayList<HashMap<String, String>> tampil_detail = new ArrayList<>();
	adapter_detail adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_tilang);

		gmb1 = findViewById(R.id.pg_gambar);
		id1 = findViewById(R.id.pg_id);
		tgl1 = findViewById(R.id.pg_tgl);
		nama_petugas1 = findViewById(R.id.pg_namapetugas);
		nama_pelanggar1 = findViewById(R.id.pg_namapelanggar);
		alamat1 = findViewById(R.id.pg_alamat);
		nohp1 = findViewById(R.id.pg_nohp);
		stnk1 = findViewById(R.id.pg_stnk);
		merk1 = findViewById(R.id.pg_merk);
		plat1 = findViewById(R.id.pg_plat);
		warna1 = findViewById(R.id.pg_warna);
		denda1 = findViewById(R.id.pg_denda);
		jadwal1 = findViewById(R.id.pg_jadwal);
		lokasi_sidang1 = findViewById(R.id.pg_lokasi);
		tujuan1 = findViewById(R.id.pg_tujuan);
		kategori1 = findViewById(R.id.pg_kategori);
		keterangan1 = findViewById(R.id.pg_isi);
		status1 = findViewById(R.id.pg_status);
		btnnotif = findViewById(R.id.btn_notif);
		rv = findViewById(R.id.rv_detail);
		rv.setHasFixedSize(true);
		LinearLayoutManager lim = new LinearLayoutManager(getApplicationContext());
		lim.setOrientation(LinearLayoutManager.VERTICAL);
		rv.setLayoutManager(lim);

		Intent intent = getIntent();
		idd = intent.getStringExtra(koneksi.id);

		// Debug log to check URL
		String imageUrl = intent.getStringExtra(koneksi.gambar);
		Log.d("Detail_Tilang", "URL Gambar: " + imageUrl);

		// Load image with Glide and add placeholders and error handling
		Glide.with(Detail_Tilang.this)
				.load(imageUrl)

				.into(gmb1);

		id1.setText(idd);
		tgl1.setText(intent.getStringExtra(koneksi.tanggal));
		nama_petugas1.setText(intent.getStringExtra(koneksi.username));
		nama_pelanggar1.setText(intent.getStringExtra(koneksi.nama));
		alamat1.setText(intent.getStringExtra(koneksi.alamat));
		nohp1.setText(intent.getStringExtra(koneksi.no_hp));
		stnk1.setText(intent.getStringExtra(koneksi.stnk));
		merk1.setText(intent.getStringExtra(koneksi.merk));
		plat1.setText(intent.getStringExtra(koneksi.plat));
		warna1.setText(intent.getStringExtra(koneksi.warna));
		denda1.setText(intent.getStringExtra(koneksi.denda));
		jadwal1.setText(intent.getStringExtra(koneksi.jadwal));
		lokasi_sidang1.setText(intent.getStringExtra(koneksi.lokasi));
		tujuan1.setText(intent.getStringExtra(koneksi.tujuan));
		kategori1.setText(intent.getStringExtra(koneksi.ket));
		keterangan1.setText(intent.getStringExtra(koneksi.uraian));
		status1.setText(intent.getStringExtra(koneksi.status));

		String kategori = kategori1.getText().toString();
		if (kategori.equalsIgnoreCase("Slip Merah")) {
			denda1.setVisibility(View.GONE);
			tujuan1.setVisibility(View.GONE);
		} else if (kategori.equalsIgnoreCase("Slip Biru")) {
			jadwal1.setVisibility(View.GONE);
			lokasi_sidang1.setVisibility(View.GONE);
		}

		btnnotif.setOnClickListener(v -> kirimNotifikasi());

		tampil_pasal();
	}

	private void tampil_pasal() {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.loading);
		if (dialog.getWindow() != null) {
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		}
		dialog.show();
		dialog.setCancelable(false);

		StringRequest penjualan = new StringRequest(Request.Method.POST, koneksi.tampil,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("TAG", "onResponse: " + response);
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject.getInt("hasil") == 1) {
								JSONArray data = jsonObject.getJSONArray("list");
								for (int i = 0; i < data.length(); i++) {
									JSONObject c = data.getJSONObject(i);
									String a = c.getString(koneksi.nama);

									HashMap<String, String> map = new HashMap<>();
									map.put(koneksi.nama, a);
									tampil_detail.add(map);
								}
								adapter = new adapter_detail(getApplicationContext(), tampil_detail);
								rv.setAdapter(adapter);
							} else if (jsonObject.getInt("hasil") == 0) {
								new SweetAlertDialog(Detail_Tilang.this, SweetAlertDialog.ERROR_TYPE)
										.setTitleText("Maaf!!!")
										.setContentText("Data Tidak Ditemukan")
										.setConfirmClickListener(sDialog -> {
											Intent hc = new Intent(Detail_Tilang.this, List_Pengaduan.class);
											finish();
											startActivity(hc);
										})
										.show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Detail", "onErrorResponse: " + error.getMessage());
				new SweetAlertDialog(Detail_Tilang.this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText("Oops...")
						.setContentText("Jaringan tidak ada" + error.getMessage())
						.show();
				dialog.dismiss();
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> param = new HashMap<>();
				param.put(koneksi.username, idd);
				param.put(koneksi.id, "data_pasal");
				return param;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
		requestQueue.add(penjualan);
	}

	private void kirimNotifikasi() {
		StringRequest request = new StringRequest(Request.Method.POST, "http://etilang.us.to/layanan/kirim_notif.php",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Handle response from PHP script (if any)
						Log.d("NotificationResponse", response);
						// Add any UI feedback if needed
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Handle errors
				Log.e("NotificationError", "Error sending notification: " + error.getMessage());
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("tgl", tgl1.getText().toString());
				params.put("nama_petugas", nama_petugas1.getText().toString());
				params.put("nama_pelanggar", nama_pelanggar1.getText().toString());
				params.put("alamat", alamat1.getText().toString());
				params.put("nohp", nohp1.getText().toString());
				params.put("stnk", stnk1.getText().toString());
				params.put("merk", merk1.getText().toString());
				params.put("plat", plat1.getText().toString());
				params.put("warna", warna1.getText().toString());
				params.put("denda", denda1.getText().toString());
				params.put("jadwal", jadwal1.getText().toString());
				params.put("lokasi_sidang", lokasi_sidang1.getText().toString());
				params.put("tujuan", tujuan1.getText().toString());
				params.put("keterangan", keterangan1.getText().toString());
				params.put("kategori", kategori1.getText().toString());
				params.put("status", status1.getText().toString());
				return params;
			}
		};

		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
		queue.add(request);
	}
}
