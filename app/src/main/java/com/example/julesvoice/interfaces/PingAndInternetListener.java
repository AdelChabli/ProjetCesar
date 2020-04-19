package com.example.julesvoice.interfaces;

// https://stackoverflow.com/questions/19229204/android-waiting-for-response-from-server

public interface PingAndInternetListener
{
    public void onPingCompleted();
    public void onPingFailed();
    public void executeAction(final String response);
    public void noInternetConnexion();
}
