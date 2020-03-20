package com.example.julesvoice.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.julesvoice.R;
import com.example.julesvoice.interfaces.PingAndInternetListener;
import com.example.julesvoice.models.LogApp;
import com.example.julesvoice.models.Recorder;

import cn.pedant.SweetAlert.SweetAlertDialog;

// L'enregistrement audio à été fait à partir de la documentation officiel et de l'application du projet M1 android pour les troubles de la parole
// https://developer.android.com/guide/topics/media/mediarecorder#java

public class AccueilActivity extends AppCompatActivity implements PingAndInternetListener
{
    private static final String TOOLBAR_TITLE = "César assistant";
    private static final int TOOLBAR_COLOR = Color.WHITE;

    private Toolbar toolbar;
    private ImageButton buttonRecord = null;
    private boolean isRecording = false;
    private Recorder record = new Recorder(AccueilActivity.this);

    // Requête de permission d'enregistrer
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    // Permet de vérifier l'autorisation d'enregistrer sinon quitte l'application
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configOfToolbar();

        // Initialisation des élements graphiques
        buttonRecord = findViewById(R.id.buttonRecord);

        ActivityCompat.requestPermissions(this, permissions, 200);

        // Listener sur le clique du bouton
        buttonRecord.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!isRecording)
                {
                    LogApp.getInstance().createLog("Lancement enregistrement");
                    buttonRecord.setImageResource(R.drawable.stop);
                    isRecording = true;
                    record.startRecording();
                }
                else
                {
                    LogApp.getInstance().createLog("Fin d'enregistrement");
                    buttonRecord.setImageResource(R.drawable.mic_48dp);
                    isRecording = false;
                    record.stopRecording(AccueilActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_music)
        {
            Intent i = new Intent(AccueilActivity.this,MusiqueActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);

        new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Êtes-vous sûr ?")
                .setContentText("Vous aller quitter l'application.")
                .setConfirmText("Quitter")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        finishAffinity();
                    }
                })
                .setCancelButton("Annuler", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void configOfToolbar()
    {
        getSupportActionBar().setTitle(TOOLBAR_TITLE);
        toolbar.setTitleTextColor(TOOLBAR_COLOR);
    }

    @Override
    public void onPingFailed()
    {
        String titleError = "Serveur Down";
        String contentError = "S'il vous plait, veuillez réessayer plus tard.";
        showErrorDialog(titleError, contentError);
    }

    private void showErrorDialog(String titleError, String contentError)
    {

        SweetAlertDialog sDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText(titleError);
        sDialog.setContentText(contentError);
        sDialog.setConfirmText("Ok");
        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        });
        sDialog.show();
    }

    @Override
    public void executeAction()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run()
            {

            }
        });
    }

    @Override
    public void noInternetConnexion()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run()
            {
                String titleError = "Aucune connection";
                String contentError = "Veuillez activer votre connection internet et relancer l'application";
                showErrorDialog(titleError, contentError);
            }
        });
    }

    @Override
    public void onPingCompleted()
    {

    }
}
