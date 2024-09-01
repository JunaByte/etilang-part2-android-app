package com.example.reskrim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Home extends AppCompatActivity implements View.OnClickListener {
    String user_,nama_, hariIni, tgll, nm, ft;
    ImageView foto,keluar;
    TextView username, dat;
    CardView profil, pengaduan, history, tentang;
    Animation animTv;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bacaPreferensi();
        data_profil();
        profil=findViewById(R.id.cv_profil);
        pengaduan=findViewById(R.id.cv_pengaduan);
        history=findViewById(R.id.cv_history);
        keluar=findViewById(R.id.img_keluar);
        dat=findViewById(R.id.tvDate);
        username=findViewById(R.id.td_nama);
        foto=findViewById(R.id.img_profil);
        tentang=findViewById(R.id.cv_about);

        Date dateNow = Calendar.getInstance().getTime();
        animTv = AnimationUtils.loadAnimation(this, R.anim.anim_tv);
        hariIni = (String) DateFormat.format("EEEE", dateNow);
        dat.setAnimation(animTv);
        if (hariIni.equalsIgnoreCase("sunday")) {
            hariIni = "Minggu";
        } else if (hariIni.equalsIgnoreCase("monday")) {
            hariIni = "Senin";
        } else if (hariIni.equalsIgnoreCase("tuesday")) {
            hariIni = "Selasa";
        } else if (hariIni.equalsIgnoreCase("wednesday")) {
            hariIni = "Rabu";
        } else if (hariIni.equalsIgnoreCase("thursday")) {
            hariIni = "Kamis";
        } else if (hariIni.equalsIgnoreCase("friday")) {
            hariIni = "Jumat";
        } else if (hariIni.equalsIgnoreCase("saturday")) {
            hariIni = "Sabtu";
        }

        getToday();

        profil.setOnClickListener(this);
        pengaduan.setOnClickListener(this);
        history.setOnClickListener(this);
        keluar.setOnClickListener(this);
		tentang.setOnClickListener(this);
    }

    private void data_profil() {
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
                                    ft = c.getString(koneksi.gambar);
                                    nm = c.getString(koneksi.nama);
                                }
                                username.setText(nm);
                                Glide.with(Home.this).load(ft)
                                        .into(foto);

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("YSPL", "onErrorResponse: " + error.getMessage());
                new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
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

    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        user_ = pref.getString(koneksi.username, "0");
       nama_ = pref.getString(koneksi.nama, "0");
    }

    private void getToday() {
        Date date = Calendar.getInstance().getTime();
        String tanggal = (String) DateFormat.format("d", date);
        String monthNumber = (String) DateFormat.format("M", date);
        String year = (String) DateFormat.format("yyyy", date);

        int month = Integer.parseInt(monthNumber);
        String bulan = null;
        if (month == 1) {
            bulan = "Januari";
        } else if (month == 2) {
            bulan = "Februari";
        } else if (month == 3) {
            bulan = "Maret";
        } else if (month == 4) {
            bulan = "April";
        } else if (month == 5) {
            bulan = "Mei";
        } else if (month == 6) {
            bulan = "Juni";
        } else if (month == 7) {
            bulan = "Juli";
        } else if (month == 8) {
            bulan = "Agustus";
        } else if (month == 9) {
            bulan = "September";
        } else if (month == 10) {
            bulan = "Oktober";
        } else if (month == 11) {
            bulan = "November";
        } else if (month == 12) {
            bulan = "Desember";
        }
        tgll= tanggal + " " + bulan + " " + year;
        String formatFix = hariIni + ", " +tgll ;
        dat.setText(formatFix);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_profil:
                Intent pro=new Intent(Home.this, Profil.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
                startActivity(pro);
                finish();
                break;
            case R.id.cv_pengaduan:
                Intent pg=new Intent(Home.this, Pengaduan.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
                startActivity(pg);
                finish();
                break;
            case R.id.cv_history:
                Intent ch=new Intent(Home.this, List_Pengaduan.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
                startActivity(ch);
                finish();
                break;
			case R.id.cv_about:
				tentang_aplikasi(v);
				break;

            case R.id.img_keluar:
                new SweetAlertDialog(Home.this, SweetAlertDialog.BUTTON_CONFIRM)
                        .setTitleText("KELUAR")
                        .setContentText("Kamu yakin ingin keluar ?")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent hc=new Intent(Home.this, Login.class);
                                SharedPreferences pref = getSharedPreferences("akun",MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(koneksi.username, "0");
                                editor.putString(koneksi.nama, "0");
                                editor.commit();
                                startActivity(hc);
                                finish();

                            }
                        })
                        .setCancelButton("Batal", SweetAlertDialog::dismissWithAnimation)
                        .show();
                break;
        }
    }

	private void tentang_aplikasi(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
		View dialogview = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.pop_about, null);
		builder.setView(dialogview);
		final AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alertDialog.show();

	}

	@Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar.make(findViewById(R.id.img_keluar), "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
