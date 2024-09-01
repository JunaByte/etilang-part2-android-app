package com.example.reskrim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import pl.aprilapps.easyphotopicker.EasyImage;


public class Profil extends AppCompatActivity {
    TextView user, nama, jekel, ttl, nohp, alamat, gp;
    String us,nm,jk,tl,np,alm,ft, tp;
    String user_;
    ImageView foto, edit, img_g;
    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALLERY = 002;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        bacaPreferensi();
        profil();
        user = findViewById(R.id.p_user);
        nama = findViewById(R.id.p_nama);
        jekel = findViewById(R.id.p_jk);
        ttl = findViewById(R.id.p_ttl);
        nohp = findViewById(R.id.p_nohp);
        alamat = findViewById(R.id.p_alamat);
        foto = findViewById(R.id.img_profil);
        edit = findViewById(R.id.p_edit);
        gp = findViewById(R.id.p_gpass);
        img_g=findViewById(R.id.img_ganti);

        img_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilih_gambar();
            }
        });
        gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ganti_pass(view);
            }
        });
        
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_profil();
            }
        });
    }

    private void edit_profil() {
        startActivity(new Intent(Profil.this, Edit_Profil.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    private void ganti_pass(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
        View dialogview= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.popup_pass, null);
        builder.setView(dialogview);
        builder.setCancelable(true);
        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        EditText pl=dialogview.findViewById(R.id.p_lama);
        EditText pb=dialogview.findViewById(R.id.p_baru);
        EditText pf=dialogview.findViewById(R.id.p_konf);
        Button sv=dialogview.findViewById(R.id.pop_simpan);

        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pala=pl.getText().toString();
                String paba=pb.getText().toString();
                String pako=pf.getText().toString();

                if (pala.equals("")) {
                    pl.setError("Belum diisi");
                    pl.requestFocus();
                } else if (paba.equals("")) {
                    pb.setError("Belum diisi");
                    pb.requestFocus();
                }else if (pako.equals("")) {
                    pf.setError("Belum diisi");
                    pf.requestFocus();
                }else {
                    if (paba.equals(pako)) {
                        Dialog dialog = new Dialog(Profil.this);
                        dialog.setContentView(R.layout.loading);
                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        }
                        dialog.show();
                        dialog.setCancelable(false);
                        StringRequest registrasi = new StringRequest(Request.Method.POST, koneksi.edit,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        dialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getInt("hasil") == 1) {
                                                dialog.dismiss();
                                               alertDialog.dismiss();
                                                Toasty.success(Profil.this, "Password Berhasil Diganti",
                                                        Toasty.LENGTH_SHORT).show();
                                            } else if (jsonObject.getInt("hasil") == 0) {
                                                dialog.dismiss();
                                                alertDialog.dismiss();
                                                Toasty.warning(Profil.this, "Password Gagal Diganti",
                                                        Toasty.LENGTH_SHORT).show();
                                            }else if (jsonObject.getInt("hasil") == 2) {
                                                dialog.dismiss();
                                                Toasty.warning(Profil.this, "Password Lama Salah",
                                                        Toasty.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                new SweetAlertDialog(Profil.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Jaringan tidak ada")
                                        .show();
                                dialog.dismiss();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<>();
                                param.put(koneksi.password, pala);
                                param.put(koneksi.alamat, paba);
                                param.put(koneksi.username, user_);
                                param.put(koneksi.id, "gpass");
                                return param;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(registrasi);
                    }else {
                        pf.setError("Password Tidak Sama");
                        pf.requestFocus();
                    }
                }

            }
        });

    }

    private void pilih_gambar() {
        CharSequence[] item = {"Kamera", "Galeri"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Tambah Gambar")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Membuka Kamera Untuk Mengambil Gambar
                                EasyImage.openCamera(Profil.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                //Membuaka Galeri Untuk Mengambil Gambar
                                EasyImage.openGallery(Profil.this, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        request.create();
        request.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Error pada Image
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Image
                switch (type) {
                    case REQUEST_CODE_CAMERA:
                        Glide.with(Profil.this)
                                .asBitmap()
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        foto.setImageBitmap(resource);
                                        bitmap = resource;
                                      ganti_foto();
                                    }
                                });
                        break;

                    case REQUEST_CODE_GALLERY:
                        Glide.with(Profil.this)
                                .asBitmap()
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        foto.setImageBitmap(resource);
                                        bitmap = resource;
                                        ganti_foto();
                                    }
                                });

                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

    private void ganti_foto() {
        Dialog dialog = new Dialog(Profil.this);
        dialog.setContentView(R.layout.loading);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
        dialog.setCancelable(false);
        StringRequest registrasi = new StringRequest(Request.Method.POST, koneksi.edit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("hasil") == 1) {
                                dialog.dismiss();
                                Toasty.success(Profil.this, "Foto Profil diganti",
                                        Toasty.LENGTH_SHORT).show();
                            } else if (jsonObject.getInt("hasil") == 0) {
                                dialog.dismiss();
                                Toasty.warning(Profil.this, "Foto Profil gagal diganti",
                                        Toasty.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(Profil.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Jaringan tidak ada")
                        .show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put(koneksi.username, user_);
                param.put(koneksi.gambar, imageTostring(bitmap));
                param.put(koneksi.id, "gfoto");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(registrasi);
    }

    private String imageTostring(Bitmap bm){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
        byte[]imaBytes=outputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imaBytes,Base64.DEFAULT);
        return encodeImage;
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
                                    tl = c.getString(koneksi.tanggal);
                                    tp = c.getString(koneksi.tempat);
                                    alm = c.getString(koneksi.alamat);
                                    np = c.getString(koneksi.no_hp);
                                    ft = c.getString(koneksi.gambar);

                                }
                                nama.setText(nm);
                                Glide.with(Profil.this).load(ft)
                                        .into(foto);
                                if (jk.equals("L")){
                                    jekel.setText("Laki - Laki");
                                }else if(jk.equals("P")){
                                    jekel.setText("Perempuan");
                                }
                                user.setText(user_);
                                ttl.setText(tp+", "+tl);
                                nama.setText(nm);
                                alamat.setText(alm);
                                nohp.setText(np);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(Profil.this, SweetAlertDialog.ERROR_TYPE)
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
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        super.onBackPressed();
    }
}
