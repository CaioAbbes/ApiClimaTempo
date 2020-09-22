package com.example.apiclimatempo;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.apiclimatempo.PegarCod;

import org.json.JSONException;

public class CarregaCodigo extends AsyncTaskLoader<String> {
    private String mQueryString;
    public CarregaCodigo(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String loadInBackground() {
        return PegarCod.buscaTemperatura(mQueryString);
    }
}
