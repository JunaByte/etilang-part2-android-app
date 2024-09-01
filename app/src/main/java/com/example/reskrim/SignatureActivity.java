package com.example.reskrim;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignatureActivity extends AppCompatActivity {

    private SignaturePad signaturePad;
    private Button btnClear;
    private Button btnSave;
    private String noHp; // Variable to hold the offender's name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        // Get the offender's name from the previous activity
        noHp = getIntent().getStringExtra("no_hp");

        // Initialize views
        signaturePad = findViewById(R.id.signaturePad);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);

        // Disable save button initially
        btnSave.setEnabled(false);

        // Set up button click listeners
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
                btnSave.setEnabled(false);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSignature();
            }
        });

        // Set up signature pad listeners
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                // Enable the clear and save buttons when user starts signing
                btnClear.setEnabled(true);
                btnSave.setEnabled(true);
            }

            @Override
            public void onSigned() {
                // Implementation if needed when user completes signing
            }

            @Override
            public void onClear() {
                // Disable save button when signature is cleared
                btnSave.setEnabled(false);
            }
        });
    }

    private void saveSignature() {
        // Get signature bitmap from SignaturePad
        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();

        // Convert bitmap to base64 string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        // Create a StringRequest to send data to PHP backend
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://etilang.us.to/layanan/save_signature.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log response for debugging
                            Log.d("SignatureActivity", "Response: " + response);

                            // Handle JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            // Show toast message based on response status
                            Toast.makeText(SignatureActivity.this, message, Toast.LENGTH_SHORT).show();

                            // Handle success or failure response accordingly
                            if ("success".equals(status)) {
                                // Signature image saved successfully
                                // Redirect to Home Activity
                                Intent intent = new Intent(SignatureActivity.this, Home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Error saving signature image
                                // Handle error case
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignatureActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SignatureActivity.this, "Error saving signature image", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_hp", noHp); // Sending offender' number
                params.put("signatureImage", encodedImage);
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
