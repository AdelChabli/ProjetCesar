package com.example.julesvoice.models;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.julesvoice.interfaces.PingAndInternetListener;

public class PingAndInternetAsync extends AsyncTask<Void, Void, Void>
{
    private LogApp log = LogApp.getInstance();
    //private static final String URL_PING_SPEECH = "http://pedago.univ-avignon.fr:3012/ping";
    private static final String URL_PING_SPEECH = "http://" + ServerRequest.IP_LOCALHOST + ":3012/ping";
    //private static final String URL_PING_COMMAND = "http://pedago.univ-avignon.fr:3013/ping";
    private static final String URL_PING_COMMAND = "http://" + ServerRequest.IP_LOCALHOST + ":8080/exo/ping";
    //private static final String URL_PING_STREAM = "http://pedago.univ-avignon.fr:8085/ping";
    private static final String URL_PING_STREAM = "http://"+ ServerRequest.IP_LOCALHOST +":8085/ping";
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
            LogApp.getInstance().createLog("Lancement ping à " + URL_PING_SPEECH );
            serverSpeech.ping(URL_PING_SPEECH);
        }
        else if(_id == 1) {
            LogApp.getInstance().createLog("Lancement ping à " + URL_PING_COMMAND );
            serverSpeech.ping(URL_PING_COMMAND);
        }
        else if(_id == 2) {
            serverSpeech.ping(URL_PING_STREAM);
        }
        else {
            log.createLog("Test de la connexion internet");
            if(isNetworkAvailable())
            {
                log.createLog("Aucune connexion internet");
                callback.executeAction("");
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
