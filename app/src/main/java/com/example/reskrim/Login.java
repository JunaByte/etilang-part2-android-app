package com.example.reskrim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button masuk;
    String user_, nama_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bacaPreferensi();
        username = findViewById(R.id.ed_username);
        password = findViewById(R.id.ed_password);
        masuk = findViewById(R.id.bt_masuk);




        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        if (user_.equals("0")) {

        } else {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
    }
    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        user_ = pref.getString(koneksi.username, "0");
        nama_ = pref.getString(koneksi.nama, "0");
    }
    private void login() {
        String token = FirebaseInstanceId.getInstance().getToken();

        String ur=username.getText().toString();
        String pw=password.getText().toString();

        if (ur.equals("")) {
            username.setError("Belum diisi");
            username.requestFocus();
        } else if (pw.equals("")) {
            password.setError("Belum diisi");
            password.requestFocus();
        } else {
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.loading);
            if (dialog.getWindow()!=null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();
            dialog.setCancelable(false);
            StringRequest simpan = new StringRequest(Request.Method.POST, koneksi.login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject =new JSONObject(response);
                                if (jsonObject.getInt("hasil")==1) {
                                JSONArray hasil=jsonObject.getJSONArray("login");
                                for (int i = 0; i < hasil.length(); i++) {
                                    JSONObject c = hasil.getJSONObject(i);
                                    String id = c.getString(koneksi.username);
                                    String nm = c.getString(koneksi.nama);
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                    SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(koneksi.username, id.toString());
                                    editor.putString(koneksi.nama, nm.toString());

                                    editor.commit();
                                    finish();
                                }
                            }else   if (jsonObject.getInt("hasil")==0) {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Username atau Password anda salah!")
                                        .show();
                                dialog.dismiss();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("YWAN", "onErrorResponse: " + error.getMessage());
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("" + error.getMessage())
                            .show();
                    dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put(koneksi.username, ur);
                    param.put(koneksi.password, pw);
                    param.put("token", token);
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(simpan);
        }
    }
}
