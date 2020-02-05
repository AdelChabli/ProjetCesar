package com.example.julesvoice.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class PingAndInternetAsync extends AsyncTask<Void, Void, Void>
{
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
            serverSpeech.ping("http://pedago.univ-avignon.fr:3012/ping");
        }
        else if(_id == 1) {
            serverSpeech.ping("http://pedago.univ-avignon.fr:3012/ping");
        }
        else if(_id == 2) {
            serverSpeech.ping("http://pedago.univ-avignon.fr:3012/ping");
        }
        else {
            if(isNetworkAvailable())
            {
                callback.executeAction();
            }
            else
            {
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
