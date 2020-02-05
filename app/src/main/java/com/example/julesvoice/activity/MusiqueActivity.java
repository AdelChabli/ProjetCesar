package com.example.julesvoice.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.julesvoice.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MusiqueActivity extends AppCompatActivity
{
    private static final String TOOLBAR_TITLE = "Les musiques";
    private static final int TOOLBAR_COLOR = Color.WHITE;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musique);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configOfToolbar();
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
}
