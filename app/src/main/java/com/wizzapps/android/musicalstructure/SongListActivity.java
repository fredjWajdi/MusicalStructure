package com.wizzapps.android.musicalstructure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {

    public static List<Song> songList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        songList = new ArrayList<>();
        if (songList.isEmpty()) {
            songList.add(new Song("Meleğim", "Soolking, Dadju", R.drawable.square_music, false));
            songList.add(new Song("Tusa", "KAROL G, Nicki Minaj", R.drawable.square_music, false));
            songList.add(new Song("Yo Perreo Sola", "Bad Bunny, Nesi, Ivy Queen", R.drawable.square_music, false));
            songList.add(new Song("Kings & Queens", "Ava Max", R.drawable.square_music, false));
            songList.add(new Song("Ninja", "Soprano", R.drawable.square_music, false));
            songList.add(new Song("Some Say", "Nea", R.drawable.square_music, false));
            songList.add(new Song("Angela", "HATIK", R.drawable.square_music, false));
            songList.add(new Song("Say So", "Doja Cat", R.drawable.square_music, false));
            songList.add(new Song("Tahiti", "Keen'V", R.drawable.square_music, false));
            songList.add(new Song("Mistakes", "Jonas Blue, Paloma Faith", R.drawable.square_music, false));
            songList.add(new Song("Sicko", "Felix Jaehn", R.drawable.square_music, false));
        }
        SongsAdapter songsAdapter = new SongsAdapter(this, songList);
        listView = findViewById(R.id.list_songs);
        listView.setAdapter(songsAdapter);
    }
}