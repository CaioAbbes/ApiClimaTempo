package com.example.apiclimatempo;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocalizacaoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {
    private static final String TRACKING_LOCATION_KEY = "tracking_location";
    // Constantes
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String CIDADE_KEY = "cidade";
    private static final String LASTDATE_KEY = "data";
    private static final String PREFERENCIAS_NAME = "com.example.android.LocalizacaoActivity";
    public String lat;
    public String lon;
    // Views
    private Button mLocationButton;
    private TextView mTempLocal, mClimaLocal, mCidadeLocal;
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    // private Location location;
    // classes Location
    private boolean mTrackingLocation;

    // Animação
    private AnimatorSet mRotateAnim;
    // Shared preferences
    private SharedPreferences mPreferences;
    private String lastLatitude = "";
    private String lastLongitude = "";
    private String lastCidade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mLocationButton = findViewById(R.id.btnLocalTemp);
        mTempLocal = findViewById(R.id.tvTemperaturaRec);
        mClimaLocal = findViewById(R.id.tvClimaRec);
        mCidadeLocal = findViewById(R.id.tvCidadeRec);

        // Listener do botão de localização.
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] coords =  PegarLatLon();
                lat = coords[0];
                lon = coords[1];
              /*  Log.d("Latitude:", lat);
                Log.d("Longitude:", lon);*/
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connMgr != null) {
                    networkInfo = connMgr.getActiveNetworkInfo();
                }
        /* Se a rede estiver disponivel e o campo de busca não estiver vazio
         iniciar o Loader */
                if (networkInfo != null && networkInfo.isConnected()
                        && lat.length() != 0) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString("lat", lat);
                    queryBundle.putString("lon", lon);
                    getSupportLoaderManager().restartLoader(0, queryBundle, LocalizacaoActivity.this);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Latitude e longitude não encontradas";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }
        });
        //inicializa as preferências do usuário
        mPreferences = getSharedPreferences(PREFERENCIAS_NAME, MODE_PRIVATE);
        //recupera as preferencias
        recuperar();
    }
    public String[] PegarLatLon() {
        String[] Coord = new String[2];
        if (!mTrackingLocation) {
            gpsTracker = new GpsTracker(LocalizacaoActivity.this);
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
//            Log.d("Lat:", lat);
//            Log.d("Lo:", lon);
            Coord[0] = lat;
            Coord[1] = lon;
            return Coord;
        }
        return Coord;
    }
        //inicializa as preferências do usuário
        //mPreferences = getSharedPreferences(PREFERENCIAS_NAME, MODE_PRIVATE);
        //recupera as preferencias
        //recuperar();

    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Latitude:", lat);
        Log.d("Longitude:", lon);
        if (args != null) {
            lat = args.getString("lat");
            lon = args.getString("lon");
        }
        return new AsyncTaskLocal(this, lat, lon);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {
        try {
            // Converte a resposta em Json
            JSONArray jsonArray = new JSONArray(data[0]);
            // Obtem o JSONArray
            JSONObject itemsObject = jsonArray.getJSONObject(0);
            // inicializa o contador
            String clima = null;
            String temperatura = null;
            String cidade = null;
            // Procura pro resultados nos itens do array
            while (clima == null && temperatura == null && cidade == null) {
                // Obtem a informação
                JSONObject temp = itemsObject.getJSONObject("Temperature");
                JSONObject met = temp.getJSONObject("Metric");
                //JSONObject city = temp.getJSONObject("City");
                //  Obter autor e titulo para o item,
                // erro se o campo estiver vazio

                temperatura = met.getString("Value");
                clima = itemsObject.getString("WeatherText");
                mTempLocal.setText(temperatura);
                mClimaLocal.setText(clima);
                mCidadeLocal.setText(data[1]);
                armazenar(lat, lon, data[1]);
            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TRACKING_LOCATION_KEY, mTrackingLocation);
        super.onSaveInstanceState(outState);
    }

    private void recuperar() {


        lastLatitude = mPreferences.getString(LATITUDE_KEY, "");
        lastLongitude = mPreferences.getString(LONGITUDE_KEY, "");
        long time = mPreferences.getLong(LASTDATE_KEY, 0);
        lastCidade = mPreferences.getString(CIDADE_KEY, "");

    }

    private void armazenar (String latitude, String longitude, String cidade){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(LATITUDE_KEY, latitude);
        preferencesEditor.putString(LONGITUDE_KEY, longitude);
        preferencesEditor.putLong(LASTDATE_KEY, System.currentTimeMillis());
        preferencesEditor.putString(CIDADE_KEY, cidade);
        preferencesEditor.apply();
    }


    public void LigarDev(View view){
        Uri number = Uri.parse("tel:+5511972503313");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }
    public void openActivitySuporte(View view ){
        Intent intent= new Intent(this, Suporte.class);
        startActivity(intent);
    }
}
