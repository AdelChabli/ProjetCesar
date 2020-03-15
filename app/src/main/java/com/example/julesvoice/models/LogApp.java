package com.example.julesvoice.models;

import android.util.Log;

public class LogApp
{
    public static String LOG_NAME = "appLog";
    public static boolean LOG_ON = true;

    // ----------------------- SINGLETON -----------------
    private static LogApp INSTANCE = null;

    private LogApp() {}

    public static synchronized LogApp getInstance()
    {
        if (INSTANCE == null)
        {   INSTANCE = new LogApp();
        }
        return INSTANCE;
    }
    // ----------------------------------------------------

    public void createLog(String info)
    {
        if(LOG_ON)
        {
            Log.d(LOG_NAME, info);
        }
    }
}
