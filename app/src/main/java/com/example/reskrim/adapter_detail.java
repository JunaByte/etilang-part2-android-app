package com.example.reskrim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_detail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>> tampil=new ArrayList<HashMap<String, String>>();


    public adapter_detail(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_detail, null);
        MyHolder holder= new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        HashMap<String, String> data_tampil= new HashMap<String, String>();
        data_tampil=data.get(position);

        myHolder.nama.setText(data_tampil.get(koneksi.nama));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

   class MyHolder extends RecyclerView.ViewHolder {
        TextView nama;

        public MyHolder(View itemView){
            super(itemView);
             nama = (TextView) itemView.findViewById(R.id.dt_nm);
        }

        }

}
