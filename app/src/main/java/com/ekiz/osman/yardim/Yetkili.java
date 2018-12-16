package com.ekiz.osman.yardim;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekiz.osman.yardim.Veritabani.DatabaseHelper;
import com.ekiz.osman.yardim.Veritabani.Yardimbilgi;

import java.util.ArrayList;

public class Yetkili extends AppCompatActivity {
    private Button cagrilar, cagriilet, sorunbildir;
    private TextView contact;
    private LinearLayout llcagrilargoster;
    private AlertDialog dialog;
    DatabaseHelper myDB;
    ArrayList<Yardimbilgi> yardimBilgilist;
    ListView listView;
    Yardimbilgi yardimbilgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yetkili);
        bilesenleriyukle();
        myDB = new DatabaseHelper(this);
        yardimBilgilist = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();
        //önceden veri tabanında veri eklendi mi onu kontrol ediyor.
        if (numRows == 0) {
            Toast.makeText(Yetkili.this, "Veritabanında veri bulunamadı.", Toast.LENGTH_LONG).show();
        } else {
            int i = 0;
            //erğer veri varsa sırayla çekiyor.
            while (data.moveToNext()) {
                yardimbilgi = new Yardimbilgi(data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5));
                yardimBilgilist.add(i, yardimbilgi);
                System.out.println(data.getString(1) + " " + data.getString(2) + " " + data.getString(3) + " " + data.getString(4) + " " + data.getString(5));
                System.out.println(yardimBilgilist.get(i).getAcilDurum());
                i++;
            }
            //çekilen verileri tablo şeklinde göstermek için MultiListAdapter sınıfını kullanıyoruz. Bu olmazsa tek sütun şeklinde 1 verimiz gözükür.
            MultiListAdapter adapter = new MultiListAdapter(this, R.layout.list_adapter_view, yardimBilgilist);
            listView = findViewById(R.id.listView);
            listView.setAdapter(adapter); // while ile çekilen verileri listview e yüklüyoruz.
        }
    }

    private void bilesenleriyukle() {
        // llcagrilargoster.setVisibility(View.INVISIBLE);
        cagrilar = findViewById(R.id.cagrilar);
        cagrilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llcagrilargoster.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        // sorun bildir butonu tıklanınca bilgilendirme kutucuğu alert çıkıyor.
        cagriilet = findViewById(R.id.cagriilet);
        sorunbildir = findViewById(R.id.sorunbildir);
        sorunbildir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yetkili.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.contact_dialog, null);
                contact = view.findViewById(R.id.contact);
                contact.setText("dmrcnylmz@gmail.com'a sorunu bildiriniz.");
                builder.setCancelable(true);
                builder.setView(view);
                // builder.setTitle("Öğrenci Kaydı Oluşturma");
                builder.setNegativeButton("Geri", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
        llcagrilargoster = findViewById(R.id.llcagrilargoster);
    }

}
