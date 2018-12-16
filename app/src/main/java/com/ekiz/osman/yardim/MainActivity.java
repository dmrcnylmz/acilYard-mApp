package com.ekiz.osman.yardim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText kullanicitc, kullanicitel, yetkiliadi, yetkiliparola;
    private Button kullanicigiris, giryapmadevam, yetkiligiris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bilesenleriyukle();

    }

    private void bilesenleriyukle() {
        kullanicitc = findViewById(R.id.kullanicitc);
        kullanicitel = findViewById(R.id.kullanicitel);
        kullanicigiris = findViewById(R.id.kullanicigiris);
        kullanicigiris.setOnClickListener(buton);
        giryapmadevam = findViewById(R.id.giryapmadevam);
        giryapmadevam.setOnClickListener(buton);
        yetkiligiris = findViewById(R.id.yetkiligiris);
        yetkiligiris.setOnClickListener(buton);
        //
        yetkiliadi = findViewById(R.id.yetkiliadi);
        yetkiliparola = findViewById(R.id.yetkiliparola);

    }
//Ana sayfada iki kullanıcının giriş yapmasını ve giriş yapmadan devam edilmesini sağlayan ayarlar.

    private View.OnClickListener buton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.kullanicigiris:
                    if (!(kullanicitc.getText().toString().equals("") || kullanicitel.getText().toString().equals(""))) {
                        if (kullanicitc.getText().toString().length() == 11 && kullanicitel.getText().toString().length() == 11) {
                           //Yardım clasında T.c ve tel bilgilerini çekmek için kullanıyoruz
                            Intent myIntent = new Intent(MainActivity.this, Yardim.class);
                            myIntent.putExtra("T.c.", kullanicitc.getText().toString());
                            myIntent.putExtra("Tel.", kullanicitel.getText().toString());
                            startActivity(myIntent);
                            break;
                        } else {
                            Toast.makeText(MainActivity.this, "T.c. veya Telefon Numarasını Doğru Giriniz.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } else if ((kullanicitc.getText().toString().equals("admin") || kullanicitel.getText().toString().equals("admin"))) {
                        Toast.makeText(MainActivity.this, "Yetkili Girişi", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, "Boşlukları doldurunuz", Toast.LENGTH_SHORT).show();
                        break;
                    }
                case R.id.giryapmadevam: {
                    //kullanıcı giriş yapmadığında verileri null olarak gönderiyor.
                    Intent myIntent = new Intent(MainActivity.this, Yardim.class);
                    myIntent.putExtra("T.c.", "null");
                    myIntent.putExtra("Tel.", "null");
                    startActivity(myIntent);
                    break;
                }
                case R.id.yetkiligiris:
                    //yetkili girişi için kontroller yapılıyor. Eğer admin kullanıcı adı ve şifre olmazsa giriş yapılamıyor.
                    if (!(yetkiliadi.getText().toString().equals("") || yetkiliparola.getText().toString().equals(""))) {
                        if ((yetkiliadi.getText().toString().equals("admin") || yetkiliadi.getText().toString().equals("admin"))) {
                            Toast.makeText(MainActivity.this, "Yetkili Girişi", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(MainActivity.this, Yetkili.class);
                            myIntent.putExtra("YetkiliAdi", yetkiliadi.getText().toString());
                            myIntent.putExtra("YetkiliParola", yetkiliparola.getText().toString());
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Yetkili Bilgilerini Doğru Giriniz", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Boşlukları doldurunuz", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    break;
            }
        }

    };
}