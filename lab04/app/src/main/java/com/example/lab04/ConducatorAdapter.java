package com.example.lab04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ConducatorAdapter extends ArrayAdapter<Conducator> {
    public ConducatorAdapter(Context context, ArrayList<Conducator> conducatori) {
        super(context, 0, conducatori);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Conducator conducator = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_conducator, parent, false);
        }

        // Lookup views for data population
        TextView tvNume = convertView.findViewById(R.id.tvNume);
        TextView tvPermis = convertView.findViewById(R.id.tvPermis);
        TextView tvExperienta = convertView.findViewById(R.id.tvExperienta);
        TextView tvTipPermis = convertView.findViewById(R.id.tvTipPermis);
        TextView tvVarsta = convertView.findViewById(R.id.tvVarsta);
        TextView tvData = convertView.findViewById(R.id.tvData);

        // Populate the data
        tvNume.setText(conducator.getNume());
        tvPermis.setText("Permis: " + (conducator.isArePermis() ? "Da" : "Nu"));
        tvExperienta.setText("Experiență: " + conducator.getAniExperienta() + " ani");
        tvTipPermis.setText("Tip: " + conducator.getTipPermis());
        tvVarsta.setText("Vârsta: " + conducator.getVarsta());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvData.setText("Data: " + sdf.format(conducator.getDataObtinerePermis()));

        return convertView;
    }
}