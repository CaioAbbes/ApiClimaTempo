package com.example.apiclimatempo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LocalizacaoActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<String[]> {


    public String lat;
    public String lon;
    // Views
    private GpsTracker gpsTracker;
    public Previsao previsao;
    public boolean mTrackingLocation;
    /**
     * Pager para troca de telas
     */
    public Integer NUM_PAGES = 5;
    private ViewPager mPager;
    public Button botao;
    /**
     * O pager adapter, que popula o ViewPager com as pages.
     */
    private PagerAdapter pagerAdapter;
    ArrayList<Previsao> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        // Instantiate a ViewPager and a PagerAdapter.
        botao = findViewById(R.id.btnLocalTemp2);
        DatabaseHelper banco = new DatabaseHelper(LocalizacaoActivity.this);
        lista = banco.getPrevisaoProx();
        if(lista.size()==5){
        mPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);}
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] coords = PegarLatLon();
                lat = coords[0];
                lon = coords[1];
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connMgr != null) {
                    networkInfo = connMgr.getActiveNetworkInfo();
                }
                if (networkInfo != null && networkInfo.isConnected()
                        && lat != null) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString("lat", lat);
                    queryBundle.putString("lon", lon);
                    getSupportLoaderManager().restartLoader(0, queryBundle, LocalizacaoActivity.this);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Latitude e longitude não encontradas";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    gpsTracker.stopUsingGPS();
                }
                DatabaseHelper banco = new DatabaseHelper(LocalizacaoActivity.this);
                try {
                    lista = banco.getPrevisaoProx();
                    mPager = findViewById(R.id.pager);
                    pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(pagerAdapter);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //
        /*try{
        if(banco.numberOfRows()>=5) {

        }
        else{botao.performClick();}
        }
        catch (Exception e) {
            botao.performClick();
            e.printStackTrace();
        }*/
    }

    public String[] PegarLatLon() {
        String[] Coord = new String[2];
        if (!mTrackingLocation) {
            gpsTracker = new GpsTracker(this);
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
//            Log.d("Lat:", lat);
//            Log.d("Lo:", lon);
            Coord[0] = lat;
            Coord[1] = lon;
            mTrackingLocation= true;
            return Coord;
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = "Já buscando, espere por favor";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        gpsTracker.stopUsingGPS();
        return Coord;
    }

    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Latitude:", lat);
        Log.d("Longitude:", lon);
        if (args != null) {
            lat = args.getString("lat");
            lon = args.getString("lon");
        }
        return new AsyncTaskLocal(this, lat, lon);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {
        try {
            // Abre o Object que tá tudo
            JSONObject jsonObjectTudo = new JSONObject(data[0]);
            // Dentro do Array pega o JSONObject
            Log.d("Acesso", "valores" + jsonObjectTudo);
            JSONArray itemsArrayForecast = jsonObjectTudo.getJSONArray("DailyForecasts");
            Log.d("Acesso", "Previsao" + itemsArrayForecast);

            // inicializa o contador

            String clima;
            String temperatura;
            String cidade;
            Date Datahj = null;
            DatabaseHelper banco = new DatabaseHelper(this);
            Integer icone;
            for (int i = 0; i <= 4; i++) {
                JSONObject ObjetoInfoPrev = itemsArrayForecast.getJSONObject(i);
                JSONObject TemperaturaObj = ObjetoInfoPrev.getJSONObject("Temperature");
                Date DataHJ = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(DataHJ);
                c.add(Calendar.DATE, i);
                Datahj = c.getTime();
                JSONObject TemperaturaMinima = TemperaturaObj.getJSONObject("Minimum");
                temperatura = TemperaturaMinima.getString("Value");

                JSONObject Dia = ObjetoInfoPrev.getJSONObject("Day");
                icone = Integer.valueOf(Dia.getString("Icon"));
                clima = Dia.getString("IconPhrase");
                cidade = data[1];
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String Data = sdf.format(Datahj);
                //armazenar(lat, lon, data[1]);
                Log.d("Valores", "Dia" + Data);
                Log.d("Valores", "Icone" + icone);
                Log.d("Valores", "Clima" + clima);
                Log.d("Valores", "Temperatura" + temperatura);
                Log.d("Valores", cidade);
                banco.insertDia(new Previsao(Data, temperatura, clima, icone, cidade));

            }
            if (banco.getAllPrevisao().size()==5) {
                Log.d("Parabens", "funcionouuuuu!!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Log.d("Valores", lista.size() + "");*/
    }

    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            botao.performClick();
            DatabaseHelper banco = new DatabaseHelper(LocalizacaoActivity.this);
            try {
                lista = banco.getPrevisaoProx();
                return new Localizacao(lista.get(position));
            }
            catch (Exception e) {
                e.printStackTrace();
                return new Fragment();
            }
        }


        @Override
        public int getCount() {
            return lista.size();
        }
    }
}