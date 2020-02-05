package com.example.julesvoice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.julesvoice.R;
import com.example.julesvoice.models.PingAndInternetAsync;
import com.example.julesvoice.interfaces.PingAndInternetListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements PingAndInternetListener
{
    private static final boolean MODE_PROGRAMMATEUR = true;
    int maxServer = 3;
    private int id = 0;
    private TextView textLoad;
    private ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        textLoad = findViewById(R.id.textLoad);
        progressBar = findViewById(R.id.progressBarIntro);

        textLoad.setText("Vérification de la connection internet.");

        if(!MODE_PROGRAMMATEUR)
        {
            // Avec -1 pingAsync va tester tout d'abord la connexion internet
            new PingAndInternetAsync(this, MainActivity.this, -1).execute();
        }
        else
        {
            changePage();
        }


    }

    // Si le teste de la connexion est bon, on lance les pings aux serveurs
    @SuppressLint("SetTextI18n")
    @Override
    public void executeAction()
    {
        textLoad.setText("Vérification du fonctionnement des serveurs ...");

        // Ici l'id correspond aux serveurs
        startPing(id);
    }

    @Override
    public void noInternetConnexion()
    {
        // On ne peut venir dans cette fonction qu'à partir d'un thread
        // Pour éviter tout soucis, on attend que le thread termine pour effectué des changements
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run()
            {
                String textLoadError = "ERREUR ! AUCUNE CONNEXION INTERNET DETECTE !";
                String titleError = "Aucune connection";
                String contentError = "Veuillez activer votre connection internet !";
                showErrorDialog(textLoadError, titleError, contentError);
            }
        });
    }

    // Lance la procédure de ping avec les serveurs associés
    @SuppressLint("SetTextI18n")
    public void startPing(int numServeur)
    {
        if(id == 0) { textLoad.setText("Attente d'une réponse du serveur SpeechToText");}
        if(id == 1) { textLoad.setText("Attente d'une réponse du serveur DetectCommand");}
        if(id == 2) { textLoad.setText("Attente d'une réponse du serveur AudioStreaming");}
        new PingAndInternetAsync(this, MainActivity.this, numServeur).execute();
    }

    // Cette fonction est appelé lorsqu'un ping est réussi avec succès
    // Elle permet de tester le serveur suivant, et si tous les serveurs ont été testé avec succès, on change de page
    @Override
    public void onPingCompleted()
    {

        id++;
        if(id < maxServer) {
            startPing(0);
        }
        else {
            changePage();
        }
    }

    // Cette fonction est appelé lorsqu'un ping n'a pas de retour au bout de 2-3 sec, cela veut dire que le serveur est down
    @SuppressLint("SetTextI18n")
    @Override
    public void onPingFailed()
    {
        String textLoadError = "ERREUR ! LES SERVEURS NE SONT PAS TOUS DISPONIBLE !";
        String titleError = "Serveur Down";
        String contentError = "S'il vous plait, veuillez réessayer plus tard.";
        showErrorDialog(textLoadError, titleError, contentError);
    }

    // Permet d'aller sur la page d'accueil
    private void changePage()
    {
        Intent i = new Intent(MainActivity.this,AccueilActivity.class);
        startActivity(i);
    }

    // Permet d'afficher une popup d'erreur avec un message personnalisé
    private void showErrorDialog(String textLoadError, String titleError, String contentError)
    {
        textLoad.setText(textLoadError);
        textLoad.setTextColor(Color.RED);
        progressBar.setVisibility(View.GONE);

        SweetAlertDialog sDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText(titleError);
        sDialog.setContentText(contentError);
        sDialog.setConfirmText("Ok");
        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                finish();
            }
        });
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.show();
    }

    // Permet de vérifier si la connexion internet est activé
    // https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


