package com.example.julesvoice.classes;

// https://stackoverflow.com/questions/19229204/android-waiting-for-response-from-server

public interface PingAndInternetListener
{
    public void onPingCompleted();
    public void onPingFailed();
    public void executeAction();
    public void noInternetConnexion();
}
