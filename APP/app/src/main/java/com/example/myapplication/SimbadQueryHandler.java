package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimbadQueryHandler {

    private SimbadQueryListener listener;

    public SimbadQueryHandler(SimbadQueryListener listener) {
        this.listener = listener;
    }

    public void execute(String simbadApiUrl) {
        try {
            String result = HttpUtils.fetchData(simbadApiUrl);
            listener.onSimbadQueryResult(result);
        } catch (IOException e) {
            listener.onSimbadQueryResult(null);
            e.printStackTrace();
        }
    }

    public interface SimbadQueryListener {
        void onSimbadQueryResult(String result);
    }
}

