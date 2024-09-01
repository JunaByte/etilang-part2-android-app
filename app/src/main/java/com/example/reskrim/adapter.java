package com.example.reskrim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<HashMap<String, Object>> data;

    public adapter(Context context, List<HashMap<String, Object>> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        CheckBox cbactivitieslistreg;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            cbactivitieslistreg = v.findViewById(R.id.checkBox);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_checkboxes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, Object> data_tampil = data.get(holder.getAdapterPosition());
        String nama = (String) data_tampil.get(koneksi.nama);
        boolean isChecked = (boolean) data_tampil.get("isChecked");

        holder.cbactivitieslistreg.setText(nama);
        holder.cbactivitieslistreg.setChecked(isChecked);

        holder.cbactivitieslistreg.setOnCheckedChangeListener(null); // Clear any previous listener
        holder.cbactivitieslistreg.setChecked(isChecked);
        holder.cbactivitieslistreg.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            data_tampil.put("isChecked", isChecked1);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();
        for (HashMap<String, Object> item : data) {
            if ((boolean) item.get("isChecked")) {
                selectedItems.add((String) item.get(koneksi.nama));
            }
        }
        return selectedItems;
    }
}
