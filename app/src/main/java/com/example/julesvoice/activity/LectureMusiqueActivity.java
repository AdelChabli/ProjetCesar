package com.example.julesvoice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.julesvoice.R;
import com.example.julesvoice.interfaces.PingAndInternetListener;
import com.example.julesvoice.models.LogApp;
import com.example.julesvoice.models.ServerRequest;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LectureMusiqueActivity extends AppCompatActivity implements PingAndInternetListener
{

    private LogApp log = LogApp.getInstance();
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private String titre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_musique);

        log.createLog("Dans LectureMusiqueActivity");

        Bundle param = getIntent().getExtras();
        titre = param.getString("titre");

        titre = titre.replaceAll("\\s+","_");
        titre = titre.toLowerCase();

        //https://www.youtube.com/watch?v=coIAggOC2OI pour l'utilisation de exo player

        exoPlayerView = findViewById(R.id.exoPlayerView);

        ServerRequest serverRequest = new ServerRequest(this, this);
        serverRequest.requestExistFile(titre);

    }

    private void lancerLeStream(String type)
    {
        try{
            BandwidthMeter bandwidthMeter =  new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            Uri audioUri;

            audioUri = Uri.parse(ServerRequest.URL_SERVER_STREAM +"/"+titre+"/"+type);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_audio");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(audioUri, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);

        exoPlayer.stop();
        finish();
    }

    @Override
    public void onPingCompleted() {

    }

    @Override
    public void onPingFailed() {

    }

    @Override
    public void executeAction(String response)
    {
        lancerLeStream(response);
    }

    @Override
    public void noInternetConnexion()
    {
        String leTitre = titre.replaceAll("_"," ");
        Toast.makeText(this, "Impossible de lancer \"" + leTitre + "\".", Toast.LENGTH_LONG).show();
        finish();
    }
}
