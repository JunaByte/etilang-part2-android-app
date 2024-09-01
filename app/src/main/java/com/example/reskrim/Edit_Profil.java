package com.example.reskrim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Edit_Profil extends AppCompatActivity {
    String user_;
    EditText nama, username, alamat, tempat, tanggal, nohp;
    Spinner jenis_k;
    ImageView dt, foto;
    Button update;
    String jenis;
    String us,nm,jk,tl,np,alm,ft, tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        bacaPreferensi();
        profil();
        nama=findViewById(R.id.m_nama);
        username=findViewById(R.id.m_user);
        alamat=findViewById(R.id.m_alamat);
        tempat=findViewById(R.id.m_tempat);
        tanggal=findViewById(R.id.m_tgl);
        nohp=findViewById(R.id.m_no);
        jenis_k=findViewById(R.id.spin_jk);
        dt=findViewById(R.id.r_date);
        update=findViewById(R.id.m_btn);
        foto=findViewById(R.id.ep_profil);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Edit_Profil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "-" + month + "-" + year;
                        tanggal.setText(date);
                    }
                }, year, month, day
                );
                datePickerDialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_profil();
            }
        });
    }

    private void update_profil() {
        String c=nama.getText().toString();
        String jenis_kelamin=jenis_k.getSelectedItem().toString();
        String d=tempat.getText().toString();
        String e=tanggal.getText().toString();
        String f=alamat.getText().toString();
        String h=nohp.getText().toString();
        if (jenis_kelamin.equals("Laki-Laki")){
            jenis="L";
        }else if(jenis_kelamin.equals("Perempuan")){
            jenis="P";
        }
       if (c.equals("")) {
            nama.setError("Belum diisi");
            nama.requestFocus();
        }else if (d.equals("")) {
            tempat.setError("Belum diisi");
            tempat.requestFocus();
        }else if (e.equals("")) {
            tanggal.setError("Belum diisi");
            tanggal.requestFocus();
        }else if (f.equals("")) {
            alamat.setError("Belum diisi");
            alamat.requestFocus();
        }else if (h.equals("")) {
            nohp.setError("Belum diisi");
            nohp.requestFocus();
        }else {
                Dialog dialog = new Dialog(Edit_Profil.this);
                dialog.setContentView(R.layout.loading);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
                dialog.setCancelable(false);
                StringRequest update = new StringRequest(Request.Method.POST, koneksi.edit,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt("hasil") == 1) {
                                        dialog.dismiss();
                                        new SweetAlertDialog(Edit_Profil.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Data Terkirim")
                                                .setContentText("Data Berhasil Tersimpan")
                                                .setConfirmText("Ok")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        Intent hc = new Intent(Edit_Profil.this, Profil.class);
                                                        startActivity(hc);
                                                        finish();

                                                    }
                                                })
                                                .show();
                                    } else if (jsonObject.getInt("hasil") == 0) {
                                        dialog.dismiss();
                                        new SweetAlertDialog(Edit_Profil.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Data Gagal Tersimpan")
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(Edit_Profil.this, SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Jaringan tidak ada")
                                .show();
                        dialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put(koneksi.username, user_);
                        param.put(koneksi.nama, c);
                        param.put(koneksi.tempat, d);
                        param.put(koneksi.tanggal, e);
                        param.put(koneksi.alamat, f);
                        param.put(koneksi.no_hp, h);
                        param.put(koneksi.jenis_kelamin, jenis);
                        param.put(koneksi.id, "editdata");
                        return param;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(update);

        }
    }

    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        user_ = pref.getString(koneksi.username, "0");
    }
    private void profil() {
        StringRequest cari = new StringRequest(Request.Method.POST, koneksi.profil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            if (jsonObject.getInt("hasil")==1) {
                                JSONArray hasil=jsonObject.getJSONArray("Profil");
                                for (int i = 0; i < hasil.length(); i++) {
                                    JSONObject c = hasil.getJSONObject(i);
                                    nm = c.getString(koneksi.nama);
                                    jk = c.getString(koneksi.jenis_kelamin);
                                    tl = c.getString("tanggall");
                                    tp = c.getString(koneksi.tempat);
                                    alm = c.getString(koneksi.alamat);
                                    np = c.getString(koneksi.no_hp);
                                    ft = c.getString(koneksi.gambar);

                                }
                                nama.setText(nm);
                                if (jk.equals("L")){
                                   jenis_k.setSelection(0);
                                }else if(jk.equals("P")){
                                    jenis_k.setSelection(1);
                                }
                                Glide.with(Edit_Profil.this).load(ft)
                                        .into(foto);
                                alamat.setText(alm);
                                nohp.setText(np);
                                tempat.setText(tp);
                                tanggal.setText(tl);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(Edit_Profil.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Jaringan Tidak Ada")
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put(koneksi.username, user_);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(cari);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Profil.class));
        finish();
        super.onBackPressed();
    }
}
