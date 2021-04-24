package com.wizzapps.android.musicalstructure;

import android.graphics.drawable.Drawable;

public class Song {
    private String title;
    private String artistName;
    private int imageResource;
    private boolean favorite;

    public Song(String title, String artistName, int imageResource, boolean favorite) {
        this.title = title;
        this.artistName = artistName;
        this.imageResource = imageResource;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
