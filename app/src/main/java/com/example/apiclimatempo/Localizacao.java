package com.example.apiclimatempo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class Localizacao extends Fragment {
    private static final String TRACKING_LOCATION_KEY = "tracking_location";
    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String CIDADE_KEY = "cidade";
    private static final String LASTDATE_KEY = "data";
    private static final String PREFERENCIAS_NAME = "com.example.android.LocalizacaoActivity";
    public String lat;
    public String lon;
    // Views
    private Button mLocationButton;
    private TextView mTempLocal, mClimaLocal, mCidadeLocal, mDia;
    private GpsTracker gpsTracker;
    private boolean mTrackingLocation;
    private ImageButton imgbuton,imgLiga;

    // Animação
    public SharedPreferences mPreferences;
    private String lastLatitude = null;
    private String lastLongitude = null;
    private String lastCidade = null;
    public Previsao previsao;

    public Localizacao(Previsao previsao) {
        this.previsao = previsao;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getActivity().getSharedPreferences(PREFERENCIAS_NAME, getActivity().MODE_PRIVATE);
        recuperar();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_localizacao, container, false);
        mTempLocal = v.findViewById(R.id.tvTemperaturaRecMax);
        mClimaLocal = v.findViewById(R.id.tvClimaRecDia);
        mCidadeLocal = v.findViewById(R.id.tvCidadeRec);
        mDia = v.findViewById(R.id.tvDia);

        mCidadeLocal.setText(previsao.get_Cidade());
        mDia.setText(previsao.get_Data());
        mTempLocal.setText(previsao.get_Temperatura());
        mClimaLocal.setText(previsao.get_Clima());
        imgbuton = v.findViewById(R.id.imageButton2);
        imgLiga = v.findViewById(R.id.btnLigar);
        imgLiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:+5511972503313");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        imgbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Suporte.class);
                startActivity(intent);
            }
        });

        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
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
        Intent intent= new Intent(getActivity(), Suporte.class);
        startActivity(intent);
    }
    /*public void MudarFoto(Integer icon){
        switch (icon){
            case 1:
        }
    }*/

}
