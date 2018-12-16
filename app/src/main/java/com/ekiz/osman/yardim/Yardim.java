package com.ekiz.osman.yardim;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekiz.osman.yardim.Veritabani.DatabaseHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Yardim extends AppCompatActivity {
    private ImageView acilyardim, itfaiyeyardim, polisyardim, zehiryardim, ormanyardim, jandarmayardim;
    private TextView kullanicitc, kullanicitel;

    //
    private static final String TAG = Yardim.class.getSimpleName();
    /*   @BindView(R.id.acilyardim)
       ImageView acilyardim;
       @BindView(R.id.itfaiyeyardim)
       ImageView itfaiyeyardim;
       @BindView(R.id.polisyardim)
       ImageView polisyardim;
       @BindView(R.id.zehiryardim)
       ImageView zehiryardim;
       @BindView(R.id.ormanyardim)
       ImageView ormanyardim;
       @BindView(R.id.jandarmayardim)
       ImageView jandarmayardim;
   */
    @BindView(R.id.location_result)
    TextView txtLocationResult;

    @BindView(R.id.updated_on)
    TextView txtUpdatedOn;

   /* @BindView(R.id.btn_start_location_updates)
    Button btnStartUpdates;*/

 /*   @BindView(R.id.btn_stop_location_updates)
    Button btnStopUpdates;*/

    @BindView(R.id.durumgonder)
    Button durumgonder;

    // location last updated time
    private String mLastUpdateTime, Enlem = "", Boylam = "", Yardımturu = "", Tc = "", Telefon = "";

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    //
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yardim);
        //Kullanmış olduğumuz hazır kütüphaneyi tanıtıyoruz.
        ButterKnife.bind(this);
        //layoutlarda kullanılanları tanıtıyoruz
        bilesenleriyukle();
        //locasyon bilgilerini tanıyoruz. Son güncelleme ve son lokasyon yeri gibi.
        init();
        // DatabaseHelper sınıfında veri tabanı ayarlamalarımızı yapıyoruz.
        myDB = new DatabaseHelper(this);

        // Tc, telefon bilgilerini mainactivityden putextra ile  girilen bilgileri bu activity de görmek için getExtra komutunu kullanıyoruz.
        //Böylece activityler arası veri alış verişi sağlanıyor.
        Tc = getIntent().getStringExtra("T.c.");
        Telefon = getIntent().getStringExtra("Tel.");
        kullanicitc.setText(Tc);
        kullanicitel.setText(Telefon);

        // restore the values from saved instance state
        //bu metodda sistemde son kayıtlı konum ve zaman bilgileri çekmek için kullanılıyor.
        restoreValuesFromBundle(savedInstanceState);

        //Dexter sınıfını kullanarak konum bilgileri açma izni isteniyor eğer biz izin verirsek konum bilgileri alınmaya başlanıyor.
        konumubaslat();
        //startLocationButtonClick();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    //burada verileri göndereceğimiz zaman konum bilgilerini virgülden sonra 5 karakter alacak şekilde gösteriyor.
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Enlem = new DecimalFormat("#,#####0.00000").format(mCurrentLocation.getLatitude());
            Boylam = new DecimalFormat("#,#####0.00000").format(mCurrentLocation.getLongitude());
            txtLocationResult.setText(
                    "Enlem: " + Enlem + ", " +
                            "Boylam: " + Boylam
            );

            // giving a blink animation on TextView
            txtLocationResult.setAlpha(0);
            txtLocationResult.animate().alpha(1).setDuration(300);

            // location last updated time
            txtUpdatedOn.setText("Konumun son güncellenme zamanı " + mLastUpdateTime);
        }

        //toggleButtons();
    }

    //son konum bilgilerini save instance metoduyla hafızada tutuyor. Aynı Mainactivityden Yardim classına veri gönderdiğimizde değer tutumasu gibi.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

   /* private void toggleButtons() {
        if (mRequestingLocationUpdates) {
            btnStartUpdates.setEnabled(false);
            btnStopUpdates.setEnabled(true);
        } else {
            btnStartUpdates.setEnabled(true);
            btnStopUpdates.setEnabled(false);
        }
    }*/

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    //konumu baslat kısmında konum izni verilince konum bilgileri çekiliyor.
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Konum bilgileri alınmaya başlandı", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Yardim.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Yardim.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    //  @OnClick(R.id.btn_start_location_updates)
    public void konumubaslat() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //@OnClick(R.id.btn_stop_location_updates)
    //veri gönderme işlemi istenildiğinde son konum virgülden sonra 3 karakter olacak şekilde veri tabanına iletiliyor
    @OnClick(R.id.durumgonder)
    public void stopLocationButtonClick() {
        if (!(Yardımturu.equals(""))) {
            //  if(!(Double.toString(mCurrentLocation.getLatitude()).equals("null") && Double.toString(mCurrentLocation.getLongitude()).equals("null"))) {
            if (!(txtLocationResult.getText().equals("Konum bilgisi için 'KONUM' özelliğini açınız."))) {
                mRequestingLocationUpdates = false;
                stopLocationUpdates();
                Enlem = new DecimalFormat("#,###0.000").format(mCurrentLocation.getLatitude());
                Boylam = new DecimalFormat("#,###0.000").format(mCurrentLocation.getLongitude());

                //verileri veri tabanına ekleme bölümü
                AddData(Tc, Telefon, Enlem, Boylam, Yardımturu);
            } else
                Toast.makeText(this, "Enlem ve Boylam bilgileri alınamadı. Konumun açık olduğundan emin olunuz.", Toast.LENGTH_SHORT).show();
            // Toast.makeText(Yardim.this,Double.toString( mCurrentLocation.getLatitude()),Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Yardımlardan birini seçiniz.", Toast.LENGTH_SHORT).show();
    }

    //verileri ekle
    public void AddData(String tc, String telefon, String enlem, String boylam, String acildurum) {
        boolean insertData = myDB.addData(tc, telefon, enlem, boylam, acildurum);

        if (insertData == true) {
            Toast.makeText(Yardim.this, "Veriler Başarıyla Gönderildi", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Yardim.this, "Veri Göndermede Hata Oluştu", Toast.LENGTH_LONG).show();
        }
    }

    //konum bilgilerini almayı durdurma yerimiz
    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        // toggleButtons();
                    }
                });
    }


    //

    /* @OnClick(R.id.btn_get_last_location)
     public void showLastKnownLocation() {
         if (mCurrentLocation != null) {
             Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                     + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
         } else {
             Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
         }
     }
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    //konum açılmadığında telefonun ayar kısmına bizi yönlendiren kısım
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //durdurulan konum bilgisini tekrar almaya yarıyor.
  /*  @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }
*/
    //konum için izin alındı mı onu kontrol ediyor
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    //konum almayı durdurma
   /* @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }*/

    private void bilesenleriyukle() {

        kullanicitc = findViewById(R.id.kullanicitc);
        kullanicitel = findViewById(R.id.kullanicitel);
        acilyardim = (ImageView) findViewById(R.id.acilyardim);
        acilyardim.setOnClickListener(imgview);
        itfaiyeyardim = findViewById(R.id.itfaiyeyardim);
        itfaiyeyardim.setOnClickListener(imgview);
        polisyardim = findViewById(R.id.polisyardim);
        polisyardim.setOnClickListener(imgview);
        zehiryardim = findViewById(R.id.zehiryardim);
        zehiryardim.setOnClickListener(imgview);
        ormanyardim = findViewById(R.id.ormanyardim);
        ormanyardim.setOnClickListener(imgview);
        jandarmayardim = findViewById(R.id.jandarmayardim);
        jandarmayardim.setOnClickListener(imgview);

    }

    //yardım türlerinden hangisi seçildiyse onu kayıt altına alıyor.
    private View.OnClickListener imgview = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acilyardim:
                    Toast.makeText(Yardim.this, "Acil Yardım(112) seçildi.", Toast.LENGTH_SHORT).show();
                    acilyardim.setFocusable(true);
                    Yardımturu = "Acil Yardım";
                    break;
                case R.id.itfaiyeyardim:
                    Toast.makeText(Yardim.this, "İtfaiye(110) seçildi.", Toast.LENGTH_SHORT).show();
                    Yardımturu = "İtfaiye";
                    break;
                case R.id.polisyardim:
                    Toast.makeText(Yardim.this, "Polis(155) seçildi.", Toast.LENGTH_SHORT).show();
                    Yardımturu = "Polis";
                    break;
                case R.id.zehiryardim:
                    Toast.makeText(Yardim.this, "Zehir Danışma(114) seçildi.", Toast.LENGTH_SHORT).show();
                    Yardımturu = "Zehir Danışma";
                    break;
                case R.id.ormanyardim:
                    Toast.makeText(Yardim.this, "Orman Yangını(177) seçildi.", Toast.LENGTH_SHORT).show();
                    Yardımturu = "Orman Yangını";
                    break;
                case R.id.jandarmayardim:
                    Toast.makeText(Yardim.this, "Jandarma(156) seçildi.", Toast.LENGTH_SHORT).show();
                    Yardımturu = "Jandarma";
                    break;

            }

        }
    };
}
