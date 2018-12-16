package com.ekiz.osman.yardim;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ekiz.osman.yardim.Veritabani.Yardimbilgi;

import java.util.ArrayList;

//Tablo şeklinde verilerin görülmesini sağlayan  ArrayAdapter türünden sınıfımız

public class MultiListAdapter extends ArrayAdapter<Yardimbilgi> {

    private LayoutInflater mInflater;
    private ArrayList<Yardimbilgi> yardimbilgiler;
    private int mViewResourceId;

    public MultiListAdapter(@NonNull Context context, int textViewResourceId, ArrayList<Yardimbilgi> yardimbilgiler) {
        super(context, textViewResourceId, yardimbilgiler);
        this.yardimbilgiler = yardimbilgiler;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Yardimbilgi yardimbilgi = yardimbilgiler.get(position);
        if (yardimbilgi != null) {
            TextView textTc = (TextView) convertView.findViewById(R.id.textTc);
            TextView textTelefon = (TextView) convertView.findViewById(R.id.textTelefon);
            TextView textEnlem = (TextView) convertView.findViewById(R.id.textEnlem);
            TextView textBoylam = (TextView) convertView.findViewById(R.id.textBoylam);
            TextView textAcilDurum = (TextView) convertView.findViewById(R.id.textAcilDurum);
            if (textTc != null) {
                textTc.setText(yardimbilgi.getTc());
            }
            if (textTelefon != null) {
                textTelefon.setText((yardimbilgi.getTelefon()));
            }
            if (textEnlem != null) {
                textEnlem.setText((yardimbilgi.getEnlem()));
            }
            if (textBoylam != null) {
                textBoylam.setText((yardimbilgi.getBoylam()));
            }
            if (textAcilDurum != null) {
                textAcilDurum.setText((yardimbilgi.getAcilDurum()));
            }
        }

        return convertView;

    }
}
