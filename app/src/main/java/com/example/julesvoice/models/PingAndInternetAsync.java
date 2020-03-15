package com.example.julesvoice.models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.julesvoice.interfaces.PingAndInternetListener;

public class PingAndInternetAsync extends AsyncTask<Void, Void, Void>
{
    private LogApp log = LogApp.getInstance();
    private static final String URL_PING_SPEECH = "http://192.168.42.157:8085";
    private PingAndInternetListener callback;
    private Context _context;
    private int _id;

    public PingAndInternetAsync(PingAndInternetListener cb, Context context, int id)
    {
        callback = cb;
        _context = context;
        _id = id;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try { Thread.sleep(1250); } catch (InterruptedException e) { e.printStackTrace(); }

        ServerRequest serverSpeech = new ServerRequest(callback, _context, _id);

        if(_id == 0) {
            serverSpeech.ping(URL_PING_SPEECH);
        }
        else if(_id == 1) {
            serverSpeech.ping(URL_PING_SPEECH);
        }
        else if(_id == 2) {
            serverSpeech.ping(URL_PING_SPEECH);
        }
        else {
            log.createLog("Test de la connexion internet");
            if(isNetworkAvailable())
            {
                log.createLog("Aucune connexion internet");
                callback.executeAction();
            }
            else
            {
                log.createLog("Test de la connexion internet");
                callback.noInternetConnexion();
            }
        }

        return null;
    }

    // Permet de vérifier si la connexion internet est activé
    // https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
