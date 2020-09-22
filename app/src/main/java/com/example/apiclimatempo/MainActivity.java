package com.example.apiclimatempo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private EditText nmCidade;
    private TextView nmClima;
    private TextView nmTemperatura;
    private Button saveButton,readButton;
    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;
    String myData = "";
// 1 Pegar key e botar em outra api
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nmCidade = findViewById(R.id.txtCidade);
        nmClima = findViewById(R.id.tvClima);
        nmTemperatura = findViewById(R.id.tvTemperatura);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }

        saveButton = findViewById(R.id.btnSalvar);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write(nmCidade.getText().toString().getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nmCidade.setText("");
                Context context = getApplicationContext();
                CharSequence text = "SampleFile.txt foi salvo no armazenamento externo...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        readButton = findViewById(R.id.btnLer);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fis = new FileInputStream(myExternalFile);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        myData = myData + strLine;
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nmCidade.setText(myData);
                Context context = getApplicationContext();
                CharSequence text = "Dados SampleFile.txt retornada do armazenamento interno...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    public void openActivity2(String temperatura, String clima, String link ){
        Intent intent= new Intent(this, activityResultado.class);
        intent.putExtra("temperatura", temperatura);
        intent.putExtra("clima", clima);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    public void openActivityLocal(View view ){
        Intent intent= new Intent(this, LocalizacaoActivity.class);
        startActivity(intent);
    }

       public void buscaTemperatura(View view) {
        // Recupera a string de busca.
           Log.d("asd","asdsd");
        String queryString = nmCidade.getText().toString();
        // esconde o teclado qdo o botão é clicado
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        /* Se a rede estiver disponivel e o campo de busca não estiver vazio
         iniciar o Loader */
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            /*nmClima.setText("");
            nmTemperatura.setText(R.string.str_empty);*/

        }
    }
    @NonNull
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new CarregaCodigo(this, queryString);
    }
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // Converte a resposta em Json
            JSONArray jsonArray = new JSONArray(data);
            // Obtem o JSONArray
            JSONObject itemsObject = jsonArray.getJSONObject(0);
            // inicializa o contador
            String clima = null;
            String temperatura = null;
            String link;
            // Procura pro resultados nos itens do array
            while (clima == null && temperatura== null) {
                // Obtem a informação
                JSONObject temp = itemsObject.getJSONObject("Temperature");
                JSONObject met = temp.getJSONObject("Metric");
                //  Obter autor e titulo para o item,
                // erro se o campo estiver vazio
                link = itemsObject.getString("MobileLink");
                    temperatura = met.getString("Value");
                    clima = itemsObject.getString("WeatherText");
                openActivity2(temperatura, clima, link );
            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            e.printStackTrace();
        }
    }
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // obrigatório implementar, nenhuma ação executada
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


}