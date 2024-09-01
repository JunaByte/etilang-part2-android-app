package com.example.reskrim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import pl.aprilapps.easyphotopicker.EasyImage;

public class Pengaduan extends AppCompatActivity {
    ImageView loc, date, jdwl, foto;
    EditText user, tanggal,nama, alamat,hp, stnk,merk,plat,warna, denda,keterangan,jadwal, lok_sidang;
    List<String> datakategori = new ArrayList<String>();
	List<String> datatujuan = new ArrayList<String>();
    String lng, lti;
    Button save;
    CheckBox cek;
    String user_, nama_;
    Spinner kate, tujuan;
    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALLERY = 002;
    Bitmap bitmap;
    String gmb="0";
    private static final int ADDRESS_PICKER_REQUEST = 1020;

	RecyclerView rv;
	ArrayList<HashMap<String, Object>> tampil_datapengaduan = new ArrayList<HashMap<String, Object>>();
	private adapter madapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);
        bacaPreferensi();
        tampil_kategori();
        tampil_tujaun();

		tampil_pasal();
		rv=findViewById(R.id.recyclerView);;
		rv.setHasFixedSize(true);
		LinearLayoutManager lim = new LinearLayoutManager(getApplicationContext());
		lim.setOrientation(LinearLayoutManager.VERTICAL);
		rv.setLayoutManager(lim);


        MapUtility.apiKey = getResources().getString(R.string.api_key);
        loc=findViewById(R.id.r_loc);
        cek=findViewById(R.id.ceklok);
        save=findViewById(R.id.m_btn);
        user=findViewById(R.id.m_user);
        kate=findViewById(R.id.spin_ket);
		date=findViewById(R.id.r_date);
		jdwl=findViewById(R.id.r_jadwal);
		foto=findViewById(R.id.foto);
		tujuan=findViewById(R.id.spin_tujuan);
		tanggal=findViewById(R.id.m_tgl);
		nama=findViewById(R.id.ed_pelanggar);
		alamat=findViewById(R.id.ed_alamat);
		hp=findViewById(R.id.ed_no);
		stnk=findViewById(R.id.ed_stnk);
		merk=findViewById(R.id.ed_merk);
		plat=findViewById(R.id.ed_plat);
		warna=findViewById(R.id.ed_warna);
		denda=findViewById(R.id.ed_denda);
		keterangan=findViewById(R.id.ed_uraian);
		jadwal=findViewById(R.id.ed_jadwal);
		lok_sidang=findViewById(R.id.ed_lokasi);




		kate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selectedKate=parent.getItemAtPosition(position).toString();
				handleKateSelection(selectedKate);
			}

			private void handleKateSelection(String selectedKate) {
				if (selectedKate.equals("Slip Merah")) {
					// Disable tujuan dan denda, kosongkan nilai mereka
					tujuan.setEnabled(false);
					tujuan.setFocusable(false);
					tujuan.setClickable(false);
					tujuan.setSelection(0); // Kosongkan nilai spinner

					denda.setEnabled(false);
					denda.setFocusable(false);
					denda.setClickable(false);
					denda.setText(""); // Kosongkan nilai teks

					// Enable jadwal dan lok_sidang
					jadwal.setEnabled(true);
					jadwal.setFocusableInTouchMode(true);
					jadwal.setClickable(true);

					jdwl.setEnabled(true);
					jdwl.setFocusableInTouchMode(true);
					jdwl.setClickable(true);

					lok_sidang.setEnabled(true);
					lok_sidang.setFocusableInTouchMode(true);
					lok_sidang.setClickable(true);
				} else if (selectedKate.equals("Slip Biru")) {
					// Enable tujuan dan denda
					tujuan.setEnabled(true);
					tujuan.setFocusableInTouchMode(true);
					tujuan.setClickable(true);

					denda.setEnabled(true);
					denda.setFocusableInTouchMode(true);
					denda.setClickable(true);

					// Disable jadwal dan lok_sidang, kosongkan nilai mereka
					jadwal.setEnabled(false);
					jadwal.setFocusable(false);
					jadwal.setClickable(false);
					jadwal.setText(""); //

					jdwl.setEnabled(false);
					jdwl.setFocusable(false);
					jdwl.setClickable(false);

					lok_sidang.setEnabled(false);
					lok_sidang.setFocusable(false);
					lok_sidang.setClickable(false);
					lok_sidang.setText(""); // Kosongkan nilai teks
				} else {
					// Enable semua
					tujuan.setEnabled(true);
					tujuan.setFocusableInTouchMode(true);
					tujuan.setClickable(true);

					denda.setEnabled(true);
					denda.setFocusableInTouchMode(true);
					denda.setClickable(true);

					jadwal.setEnabled(true);
					jadwal.setFocusableInTouchMode(true);
					jadwal.setClickable(true);

					lok_sidang.setEnabled(true);
					lok_sidang.setFocusableInTouchMode(true);
					lok_sidang.setClickable(true);
				}
			}

				@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


		foto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				izinaplikasi();
				pilih_gambar();
			}
		});
		Calendar calendar = Calendar.getInstance();
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
		String currentDate = dateFormat.format(calendar.getTime());

		tanggal.setText(currentDate);
		tanggal.setFocusable(false);
		tanggal.setFocusableInTouchMode(false);
		tanggal.setClickable(false);
		date.setEnabled(false);
		date.setFocusable(false);
		date.setClickable(false);

		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						Pengaduan.this, new DatePickerDialog.OnDateSetListener() {
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

		jdwl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						Pengaduan.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						month = month + 1;
						String date = dayOfMonth + "-" + month + "-" + year;
						jadwal.setText(date);
					}
				}, year, month, day
				);
				datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
				datePickerDialog.show();
			}
		});
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                simpan_pengaduan();
//				selectedlist=madapter.listofselectedactivites();
				simpan_tilang();
//
//				Log.d("list",selectedlist.toString()) ;
//
//				Toast.makeText(Pengaduan.this, ""+selectedlist.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pengaduan.this, LocationPickerActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
            }
        });

        user.setText(nama_);
    }

	private void tampil_tujaun() {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampil,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						try {
							Log.e("TAG", "onResponse: "+response );
							JSONObject jsonObject =new JSONObject(response);
							if (jsonObject.getInt("hasil") == 1){
								JSONArray result = jsonObject.getJSONArray("Tujuan");
								datatujuan.clear();
								for (int i = 0; i < result.length(); i++) {
									JSONObject c = result.getJSONObject(i);
									datatujuan.add(c.getString(koneksi.nama));
								}
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(Pengaduan.this, R.layout.item_spinner, datatujuan);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								tujuan.setAdapter(adapter);

							} else {
								Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", "onErrorResponse: "+error.getMessage());
						Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> param = new HashMap<>();
				param.put(koneksi.id, "tujuan");
				return param;
			}
		};
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}


//	private void simpan() {
//
//		List<String> selectedlist = madapter.listofselectedactivites();
//		Toast.makeText(this, selectedlist.toString(), Toast.LENGTH_SHORT).show();
//		final ProgressDialog progressDialog = new ProgressDialog(this);
//		progressDialog.setMessage("Mengirim data. . .");
//		progressDialog.show();
//		StringRequest simpan = new StringRequest(Request.Method.POST, koneksi.smp_ch,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String response) {
//						progressDialog.dismiss();
//						Log.e("YSPL", "onResponse: " + response);
//						try {
//							JSONObject jsonObject = new JSONObject(response);
//							if (jsonObject.getInt("hasil") == 1) {
//								new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.SUCCESS_TYPE)
//										.setTitleText("Data Terkirim")
//										.setContentText("Pengaduan Anda Telah Terkirim!")
//										.setConfirmText("Ok")
//										.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//											@Override
//											public void onClick(SweetAlertDialog sDialog) {
//												Intent hc = new Intent(Pengaduan.this, Home.class);
//												startActivity(hc);
//												finish();
//											}
//										})
//										.show();
//							} else if (response.contains("0")) {
//								new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
//										.setContentText("Pengaduan gagal")
//										.show();
//								progressDialog.dismiss();
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Log.e("Pengaduan", "onErrorResponse: " + error.getMessage());
//				new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
//						.setContentText("Jaringan tidak ada")
//						.show();
//				progressDialog.dismiss();
//			}
//		}) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> param = new HashMap<>();
//				param.put(koneksi.nama, selectedlist.toString());
//
//				return param;
//			}
//		};
//		RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//		requestQueue.add(simpan);
//	}


	private void tampil_pasal() {
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
						Log.e("TAG", "onResponse: " + response);
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject.getInt("hasil") == 1) {
								JSONArray hasil = jsonObject.getJSONArray("Pasal");
								tampil_datapengaduan.clear();
								for (int i = 0; i < hasil.length(); i++) {
									JSONObject c = hasil.getJSONObject(i);
									String nm = c.getString(koneksi.nama);
									HashMap<String, Object> map = new HashMap<>();
									map.put(koneksi.nama, nm);
									map.put("isChecked", false);
									tampil_datapengaduan.add(map);
								}
								madapter = new adapter(getApplicationContext(), tampil_datapengaduan);
								rv.setAdapter(madapter);
								dialog.dismiss();
							} else if (jsonObject.getInt("hasil") == 0) {
								dialog.dismiss();
								new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
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
				new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText("Oops...")
						.setContentText("Jaringan Tidak Ada")
						.show();
				dialog.dismiss();
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> param = new HashMap<>();
				param.put(koneksi.id, "pasal");
				return param;
			}
		};
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(list_guru);
	}

	private void simpan_tilang() {
		List<String> selectedPasal = madapter.getSelectedItems();
		String tgl = tanggal.getText().toString();
		String nm = nama.getText().toString();
		String alm = alamat.getText().toString();
		String nhp = hp.getText().toString();
		String sim = stnk.getText().toString();
		String mrk = merk.getText().toString();
		String plt = plat.getText().toString();
		String wrn = warna.getText().toString();
		String ket = keterangan.getText().toString();

		if (tgl.equals("")) {
			tanggal.setError("Belum diisi");
			tanggal.requestFocus();
		} else if (nm.equals("")) {
			nama.setError("Belum diisi");
			nama.requestFocus();
		} else if (alm.equals("")) {
			alamat.setError("Belum diisi");
			alamat.requestFocus();
		} else if (nhp.equals("")) {
			hp.setError("Belum diisi");
			hp.requestFocus();
		} else if (sim.equals("")) {
			stnk.setError("Belum diisi");
			stnk.requestFocus();
		} else if (mrk.equals("")) {
			merk.setError("Belum diisi");
			merk.requestFocus();
		} else if (plt.equals("")) {
			plat.setError("Belum diisi");
			plat.requestFocus();
		} else if (wrn.equals("")) {
			warna.setError("Belum diisi");
			warna.requestFocus();
		} else {
			if (gmb.equals("0")) {
				Toasty.info(Pengaduan.this, "Silahkan Lengkapi Data Terlebih Dahulu",
						Toasty.LENGTH_SHORT).show();
			} else {
				if (!cek.isChecked()) {
					Toasty.info(Pengaduan.this, "Lokasi Belum Ditentukan",
							Toasty.LENGTH_SHORT).show();
				} else {
					final ProgressDialog progressDialog = new ProgressDialog(this);
					progressDialog.setMessage("Mengirim data. . .");
					progressDialog.show();
					StringRequest simpan = new StringRequest(Request.Method.POST, koneksi.simpan,
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
									progressDialog.dismiss();
									Log.e("Tilang", "onResponse: " + response);
									try {
										JSONObject jsonObject = new JSONObject(response);
										if (jsonObject.getInt("hasil") == 1) {
											new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("Data Terkirim")
													.setContentText("Data Anda Telah Terkirim!")
													.setConfirmText("Ok")
													.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
														@Override
														public void onClick(SweetAlertDialog sDialog) {
															Intent hc = new Intent(Pengaduan.this, SignatureActivity.class);
															hc.putExtra("no_hp", nhp);
															startActivity(hc);
															finish();
														}
													})
													.show();
										} else {
											SweetAlertDialog dialog = new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("Data Terkirim")
													.setContentText("Data Anda Telah Terkirim!")
													.setConfirmText("Ok")
													.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
														@Override
														public void onClick(SweetAlertDialog sDialog) {
															Intent hc = new Intent(Pengaduan.this, SignatureActivity.class);
															hc.putExtra("no_hp", nhp);
															startActivity(hc);
															finish();
														}
													});
											dialog.show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("Tilang", "onErrorResponse: " + error.getMessage());
							new SweetAlertDialog(Pengaduan.this, SweetAlertDialog.ERROR_TYPE)
									.setContentText("Jaringan tidak ada")
									.show();
							progressDialog.dismiss();
						}
					}) {
						@Override
						protected Map<String, String> getParams() throws AuthFailureError {
							Map<String, String> param = new HashMap<>();
							param.put(koneksi.username, user_);
							param.put(koneksi.tanggal, tgl);
							param.put(koneksi.longi, lng);
							param.put(koneksi.lati, lti);
							param.put(koneksi.nama, nm);
							param.put(koneksi.alamat, alm);
							param.put(koneksi.no_hp, nhp);
							param.put(koneksi.stnk, sim);
							param.put(koneksi.merk, mrk);
							param.put(koneksi.plat, plt);
							param.put(koneksi.warna, wrn);
							param.put(koneksi.pasal, selectedPasal.toString());
							param.put(koneksi.denda, denda.getText().toString());
							param.put(koneksi.ket, kate.getSelectedItem().toString());
							param.put(koneksi.uraian, ket);
							param.put(koneksi.jadwal, jadwal.getText().toString());
							param.put(koneksi.lokasi, lok_sidang.getText().toString());
							param.put(koneksi.tujuan, tujuan.getSelectedItem().toString());
							param.put(koneksi.gambar, imageTostring(bitmap));
							return param;
						}
					};
					RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
					requestQueue.add(simpan);
				}
			}
		}
	}
    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        user_ = pref.getString(koneksi.username, "0");
        nama_ = pref.getString(koneksi.nama, "0");
    }
    private void izinaplikasi() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(Pengaduan.this, "You should accept permission", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
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
                                EasyImage.openCamera(Pengaduan.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                //Membuaka Galeri Untuk Mengambil Gambar
                                EasyImage.openGallery(Pengaduan.this, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        request.create();
        request.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        Glide.with(Pengaduan.this)
                                .asBitmap()
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        foto.setImageBitmap(resource);
                                        bitmap = resource;
                                        gmb="1";
                                    }
                                });
                        break;

                    case REQUEST_CODE_GALLERY:
                        Glide.with(Pengaduan.this)
                                .asBitmap()
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        foto.setImageBitmap(resource);
                                        bitmap = resource;
                                        gmb="1";
                                    }
                                });

                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress =data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */

//                    lokasi.setText(new StringBuilder().append("http://www.google.com/maps/place/").append(currentLatitude).append
//                            (",").append(currentLongitude).toString());
                    cek.setChecked(true);
                    cek.setText("Lokasi Terupdate");
                    lng=new StringBuilder().append(currentLongitude).toString();
                    lti=new StringBuilder().append(currentLatitude).toString();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private void tampil_kategori() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("TAG", "onResponse: "+response );
                            JSONObject jsonObject =new JSONObject(response);
                            if (jsonObject.getInt("hasil") == 1){
                                JSONArray result = jsonObject.getJSONArray("Kategori");
                                datakategori.clear();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    datakategori.add(c.getString(koneksi.nama));
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Pengaduan.this, R.layout.item_spinner, datakategori);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                kate.setAdapter(adapter);

                            } else {
                                Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
						Log.e("TAG", "onErrorResponse: "+error.getMessage());
                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put(koneksi.id, "kategori");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private String imageTostring(Bitmap bm){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
        byte[]imaBytes=outputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imaBytes,Base64.DEFAULT);
        return encodeImage;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        super.onBackPressed();
    }
}
