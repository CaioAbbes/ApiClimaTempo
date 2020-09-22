package com.example.apiclimatempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class Suporte extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suporte);
}

    public void MandarWhatsApp(View view){
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=5511976853158&text=Ol%C3%A1%2c%20preciso%20de%20ajuda%20com%20seu%20aplicativo%21%20Pode%20me%20ajudar%3f%20%3a%29&source=&data=&app_absent=");
        Intent it = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(it);

    }
}
