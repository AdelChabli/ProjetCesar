package com.example.julesvoice.models;

public class ArgumentRequest
{
    private String key;
    private String valeur;

    public ArgumentRequest(String k, String v)
    {
        key = k;
        valeur = v;
    }

    public String getKey() { return key; }
    public String getValeur() { return valeur; }
}
