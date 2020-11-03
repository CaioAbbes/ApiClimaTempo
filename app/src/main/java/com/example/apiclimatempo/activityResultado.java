package com.example.apiclimatempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activityResultado extends AppCompatActivity {
    private TextView nmTemp;
    private TextView nmClima;
    public void openActivityMain(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado2);
        Button button = findViewById(R.id.buttonVoltar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMain();
            }
        });
        nmTemp = findViewById(R.id.tvTemperatura);
        nmClima = findViewById(R.id.tvClima);

        Bundle bundle = getIntent().getExtras();
        String Clima = bundle.getString("clima");
        String Temp = bundle.getString("temperatura");
        final String link = bundle.getString("link");
        nmTemp.setText(Temp);
        nmClima.setText(Clima);

        Button btnEx = findViewById(R.id.btnExterna);
        btnEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MandarUsu(v,link);
            }
        });
    }

    public void MandarUsu(View view,String Link){
        Uri uri = Uri.parse(Link);
        Intent it = new Intent(Intent.ACTION_VIEW,uri);
        Log.d("url:", Link);
        startActivity(it);

    }
}
