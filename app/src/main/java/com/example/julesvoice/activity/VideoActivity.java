package com.example.julesvoice.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.julesvoice.R;
import com.example.julesvoice.interfaces.PingAndInternetListener;
import com.example.julesvoice.models.ServerRequest;

public class VideoActivity extends AppCompatActivity implements PingAndInternetListener
{
    private static final String TOOLBAR_TITLE = "Les vidéos";
    private static final int TOOLBAR_COLOR = Color.WHITE;

    private Toolbar toolbar;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configOfToolbar();

        listView = findViewById(R.id.lvVideo);

        ServerRequest serverRequest = new ServerRequest(this, this);
        serverRequest.getFile(serverRequest.URL_GET_VIDEO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void configOfToolbar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TOOLBAR_TITLE);
        toolbar.setTitleTextColor(TOOLBAR_COLOR);
    }

    @Override
    public void onPingCompleted() {
        finish();
    }

    @Override
    public void onPingFailed() {
        finish();
    }

    @Override
    public void executeAction(String response)
    {
        String[] values = response.split("_");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
    }

    @Override
    public void noInternetConnexion() {
        finish();
    }
}
