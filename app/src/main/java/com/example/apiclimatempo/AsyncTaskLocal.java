package com.example.apiclimatempo;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class AsyncTaskLocal extends AsyncTaskLoader<String[]> {
    private String latitude;
    private String longitude;
    public AsyncTaskLocal(Context context, String lat, String lon) {
        super(context);
        latitude= lat;
        longitude = lon;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String[] loadInBackground() {
        return PegarCod.buscaTemperaturaLocal(latitude,longitude);
    }
}
