package com.example.reskrim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class adapter_datapengaduan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;


    public adapter_datapengaduan(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_order, null);
        MyHolder holder= new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        HashMap<String, String> data_tampil= new HashMap<String, String>();
        data_tampil=data.get(position);
		String tanggal=data_tampil.get(koneksi.tanggal);
		String id=data_tampil.get(koneksi.id);
		String kat=data_tampil.get(koneksi.ket);
		String urai=data_tampil.get(koneksi.uraian);
		String st=data_tampil.get(koneksi.status);
		String nama_pelanggar=data_tampil.get(koneksi.nama);
		String nama_petugas=data_tampil.get(koneksi.username);
		String gmbar=data_tampil.get(koneksi.gambar);
		String itp=data_tampil.get(koneksi.itp);
		String idp=data_tampil.get(koneksi.idp);
		String lati=data_tampil.get(koneksi.lati);
		String longi=data_tampil.get(koneksi.longi);
		String alamat=data_tampil.get(koneksi.alamat);
		String nohp=data_tampil.get(koneksi.no_hp);
		String stnk=data_tampil.get(koneksi.stnk);
		String merk=data_tampil.get(koneksi.merk);
		String plat=data_tampil.get(koneksi.plat);
		String warna=data_tampil.get(koneksi.warna);
		String denda=data_tampil.get(koneksi.denda);
		String jadwal=data_tampil.get(koneksi.jadwal);
		String lokasi=data_tampil.get(koneksi.lokasi);
		String tujuan=data_tampil.get(koneksi.tujuan);

        myHolder.tanggal.setText(data_tampil.get(koneksi.tanggal));
        myHolder.status.setText(data_tampil.get(koneksi.status));
        myHolder.id_tilang.setText(data_tampil.get(koneksi.id));
        myHolder.kategori.setText(data_tampil.get(koneksi.ket));

        if (st.equals("Proses")){
            myHolder.status.setTextColor(Color.parseColor("#655802"));
            myHolder.ln.setBackgroundResource(R.drawable.bg_proses1);
        }else if(st.equals("Ditanggapi")||st.equals("Selesai")){
            myHolder.status.setTextColor(Color.parseColor("#146502"));
            myHolder.ln.setBackgroundResource(R.drawable.bg_proses3);
        }else if (st.equals("Ditolak")){
            myHolder.status.setTextColor(Color.parseColor("#650202"));
            myHolder.ln.setBackgroundResource(R.drawable.bg_proses2);
        }

        myHolder.vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				CharSequence[] item = {"Lihat"};
				AlertDialog.Builder request = new AlertDialog.Builder(view.getContext())
						.setTitle("Pilihan")
						.setItems(item, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								switch (i){
									case 0:
//										AlertDialog.Builder builderr = new AlertDialog.Builder(view.getRootView().getContext());
//										View dialogvieww = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.popup_pengaduan, null);
//										ImageView gmb1 = dialogvieww.findViewById(R.id.pg_gambar);
//										TextView id1 = dialogvieww.findViewById(R.id.pg_id);
//										TextView tgl1 = dialogvieww.findViewById(R.id.pg_tgl);
//										TextView nama_petugas1 = dialogvieww.findViewById(R.id.pg_namapetugas);
//										TextView nama_pelanggar1 = dialogvieww.findViewById(R.id.pg_namapelanggar);
//										TextView alamat1 = dialogvieww.findViewById(R.id.pg_alamat);
//										TextView nohp1 = dialogvieww.findViewById(R.id.pg_nohp);
//										TextView stnk1 = dialogvieww.findViewById(R.id.pg_stnk);
//										TextView merk1 = dialogvieww.findViewById(R.id.pg_merk);
//										TextView plat1 = dialogvieww.findViewById(R.id.pg_plat);
//										TextView warna1 = dialogvieww.findViewById(R.id.pg_warna);
//										TextView denda1 = dialogvieww.findViewById(R.id.pg_denda);
//										TextView jadwal1 = dialogvieww.findViewById(R.id.pg_jadwal);
//										TextView lokasi_sidang1 = dialogvieww.findViewById(R.id.pg_lokasi);
//										TextView tujuan1 = dialogvieww.findViewById(R.id.pg_tujuan);
//										TextView kategori1 = dialogvieww.findViewById(R.id.pg_kategori);
//										TextView keterangan1 = dialogvieww.findViewById(R.id.pg_isi);
//										TextView status1 = dialogvieww.findViewById(R.id.pg_status);
//
//										Glide.with(view.getContext()).load(gmbar)
//												.into(gmb1);
//										id1.setText(id);
//										tgl1.setText(tanggal);
//										nama_petugas1.setText(nama_petugas);
//										nama_pelanggar1.setText(nama_pelanggar);
//										alamat1.setText(alamat);
//										nohp1.setText(nohp);
//										stnk1.setText(stnk);
//										merk1.setText(merk);
//										plat1.setText(plat);
//										warna1.setText(warna);
//										denda1.setText(denda);
//										jadwal1.setText(jadwal);
//										lokasi_sidang1.setText(lokasi);
//										tujuan1.setText(tujuan);
//										kategori1.setText(kat);
//										keterangan1.setText(urai);
//										status1.setText(st);
//
//
//										builderr.setView(dialogvieww);
//										final AlertDialog alertDialog1 = builderr.create();
//										alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//										alertDialog1.show();

										Intent intent = new Intent(context, Detail_Tilang.class);
										intent.putExtra(koneksi.id, id);
										intent.putExtra(koneksi.tanggal, tanggal);
										intent.putExtra(koneksi.username, nama_petugas);
										intent.putExtra(koneksi.nama, nama_pelanggar);
										intent.putExtra(koneksi.alamat, alamat);
										intent.putExtra(koneksi.no_hp, nohp);
										intent.putExtra(koneksi.stnk, stnk);
										intent.putExtra(koneksi.merk, merk);
										intent.putExtra(koneksi.plat, plat);
										intent.putExtra(koneksi.warna, warna);
										intent.putExtra(koneksi.denda, denda);
										intent.putExtra(koneksi.jadwal, jadwal);
										intent.putExtra(koneksi.lokasi, lokasi);
										intent.putExtra(koneksi.tujuan, tujuan);
										intent.putExtra(koneksi.ket, kat);
										intent.putExtra(koneksi.uraian, urai);
										intent.putExtra(koneksi.status, st);
										intent.putExtra(koneksi.gambar, gmbar);

										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										context.startActivity(intent);
										break;

								}
							}
						});
				request.create();
				request.show();
            }
        });




    }



    @Override
    public int getItemCount() {
        return data.size();
    }

   class MyHolder extends RecyclerView.ViewHolder {
        TextView tanggal, status, id_tilang, kategori,uraian, tanggap;

       LinearLayout ln, vw;
        public MyHolder(View itemView){
            super(itemView);
            tanggal = (TextView) itemView.findViewById(R.id.pg_tgl);
            status = (TextView) itemView.findViewById(R.id.pg_proses);
            id_tilang = (TextView) itemView.findViewById(R.id.pd_id);
            kategori = (TextView) itemView.findViewById(R.id.pg_kategori);
            uraian = (TextView) itemView.findViewById(R.id.pg_isi);
            tanggap = (TextView) itemView.findViewById(R.id.pg_tanggapan);

            ln=(LinearLayout) itemView.findViewById(R.id.Lny);
            vw=(LinearLayout) itemView.findViewById(R.id.view);
        }

        }

}
