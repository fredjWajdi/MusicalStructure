package com.wizzapps.android.musicalstructure;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class SongsAdapter extends ArrayAdapter<Song> {

    public SongsAdapter(@NonNull Context context, @NonNull List<Song> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Song currentSong = getItem(position);
        ImageView imageView = listItemView.findViewById(R.id.album_img);
        imageView.setImageResource(currentSong.getImageResource());
        TextView songName = listItemView.findViewById(R.id.song_name);
        songName.setText(currentSong.getTitle());
        TextView artistName = listItemView.findViewById(R.id.artist_name);
        artistName.setText(currentSong.getArtistName());
        listItemView.setOnClickListener(v -> {
            Intent intent = new Intent(parent.getContext(), PlayerActivity.class);
            intent.putExtra("position", position);
            parent.getContext().startActivity(intent);
        });
        return listItemView;
    }
}
