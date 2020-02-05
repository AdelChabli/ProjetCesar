package com.example.julesvoice.models;

public class Musique
{
    private int image;
    private String titre;

    // Constructeur
    public Musique(int img, String t)
    {
        image = img;
        titre = t;
    }

    // Getters
    public int getImage() { return image; }
    public String getTitre() { return titre; }


    // Setters
    public void setImage(int image) { this.image = image; }
    public void setTitre(String titre) { this.titre = titre; }




}
