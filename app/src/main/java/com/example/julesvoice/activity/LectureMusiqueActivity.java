package com.example.julesvoice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.example.julesvoice.R;
import com.example.julesvoice.models.LogApp;
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

public class LectureMusiqueActivity extends AppCompatActivity
{
    private static final String URL_MUSIC = "http://pedago.univ-avignon.fr:8085/streamAudio";
    private static final String URL_VIDEO = "http://pedago.univ-avignon.fr:8085/streamVideo";
    private LogApp log = LogApp.getInstance();
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private String typeStream;
    private String titre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_musique);

        log.createLog("Dans LectureMusiqueActivity");

        Bundle param = getIntent().getExtras();
        typeStream = param.getString("type");

        titre = param.getString("titre");

        titre = titre.replaceAll("\\s+","");
        titre = titre.toLowerCase();

        exoPlayerView = findViewById(R.id.exoPlayerView);

        try{
            BandwidthMeter bandwidthMeter =  new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            Uri audioUri;

            if(typeStream.equals("musique"))
            {
                audioUri = Uri.parse(URL_MUSIC+"/"+titre);
            }
            else
            {
                audioUri = Uri.parse(URL_VIDEO);
            }

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

}
