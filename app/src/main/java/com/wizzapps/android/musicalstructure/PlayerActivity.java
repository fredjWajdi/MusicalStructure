package com.wizzapps.android.musicalstructure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity {

    public static final int MAX = 100;
    TextView nextSongTxtView;
    TextView artistNameTxtView;
    TextView songNameTxtView;
    ImageButton goToSongListActivity;
    ImageButton volumeDownBtn;
    ImageButton volumeUpBtn;
    ImageButton shuffleBtn;
    ImageButton favoriteBtn;
    ImageButton repeatBtn;
    RepeatState repeatState = RepeatState.DISABLED;
    ImageButton previousSongBtn;
    ImageButton pausePlayBtn;
    ImageButton nextSongBtn;
    SeekBar songSeekBar;
    ImageView albumImageView;
    int songNumber;
    boolean shuffleActivated = false;
    boolean songPaused = false;
    boolean favoriteActivated = false;
    int songProgress = 0;
    private Handler mSeekBarUpdateHandler;
    private Runnable mUpdateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        songNumber = intent.getIntExtra("position", 0);
        nextSongTxtView = findViewById(R.id.next_song_txt_view);
        if (songNumber == SongListActivity.songList.size() - 1) {
            nextSongTxtView.setText(SongListActivity.songList.get(0).getTitle());
        } else {
            nextSongTxtView.setText(SongListActivity.songList.get(songNumber + 1).getTitle());
        }
        artistNameTxtView = findViewById(R.id.artist_name_txt_view);
        artistNameTxtView.setText(SongListActivity.songList.get(songNumber).getArtistName());
        songNameTxtView = findViewById(R.id.song_title_txt_view);
        songNameTxtView.setText(SongListActivity.songList.get(songNumber).getTitle());
        goToSongListActivity = findViewById(R.id.song_list_activity_ImgBtn);
        volumeDownBtn = findViewById(R.id.volume_down);
        volumeUpBtn = findViewById(R.id.volume_up);
        favoriteBtn = findViewById(R.id.favorite_btn);
        shuffleBtn = findViewById(R.id.shuffle_btn);
        repeatBtn = findViewById(R.id.repeat_btn);
        previousSongBtn = findViewById(R.id.previous_btn);
        pausePlayBtn = findViewById(R.id.play_btn);
        pausePlayBtn.setImageResource(R.drawable.ic_pause_white);
        nextSongBtn = findViewById(R.id.next_btn);
        songSeekBar = findViewById(R.id.song_seek_bar);
        albumImageView = findViewById(R.id.album_img_player);
        albumImageView.setImageResource(SongListActivity.songList.get(songNumber).getImageResource());
        if (SongListActivity.songList.get(songNumber).isFavorite()) {
            favoriteBtn.setImageResource(R.drawable.ic_favorite_active);
        } else {
            favoriteBtn.setImageResource(R.drawable.ic_favorite_white);
        }
        goToSongListActivity.setOnClickListener(v -> finish());
        volumeDownBtn.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Volume down -", Toast.LENGTH_SHORT).show());
        volumeUpBtn.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Volume up +", Toast.LENGTH_SHORT).show());
        shuffleBtn.setOnClickListener(v -> {
            if (shuffleActivated) {
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_white);
                shuffleActivated = false;
            } else {
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_active);
                shuffleActivated = true;
            }
        });
        favoriteBtn.setOnClickListener(v -> {
            if (favoriteActivated) {
                favoriteBtn.setImageResource(R.drawable.ic_favorite_white);
                favoriteActivated = false;
                SongListActivity.songList.get(songNumber).setFavorite(false);
            } else {
                favoriteBtn.setImageResource(R.drawable.ic_favorite_active);
                favoriteActivated = true;
                SongListActivity.songList.get(songNumber).setFavorite(true);
            }
        });
        repeatBtn.setOnClickListener(v -> {
            if (repeatState == RepeatState.DISABLED) {
                repeatState = RepeatState.ACTIVATED;
                repeatBtn.setImageResource(R.drawable.ic_repeat_active);
            } else if (repeatState == RepeatState.ACTIVATED) {
                repeatState = RepeatState.REPEAT_ONE;
                repeatBtn.setImageResource(R.drawable.ic_repeat_one_white);
            } else {
                repeatState = RepeatState.DISABLED;
                repeatBtn.setImageResource(R.drawable.ic_repeat_white);
            }
        });
        pausePlayBtn.setOnClickListener(v -> {
            if (songPaused) {
                playSong();
            } else {
                songPaused = true;
                pausePlayBtn.setImageResource(R.drawable.ic_play_arrow_white);
                mSeekBarUpdateHandler.removeCallbacks(mUpdateSeekBar);
            }
        });
        previousSongBtn.setOnClickListener(v -> {
            songProgress = 0;
            if (songNumber > 0) {
                songNumber--;
            } else {
                songNumber = SongListActivity.songList.size() - 1;
            }
            setSong();
            setNextSongTitle();
            playSong();
        });
        nextSongBtn.setOnClickListener(v -> {
            songProgress = 0;
            if (songNumber < SongListActivity.songList.size() - 1) {
                songNumber++;
            } else {
                songNumber = 0;
            }
            setSong();
            setNextSongTitle();
            playSong();
        });
        songSeekBar.setProgress(0);
        songSeekBar.setMax(MAX);
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                songProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(songProgress);
            }
        });
        mSeekBarUpdateHandler = new Handler();
        mUpdateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (songProgress == MAX) {
                    if (songNumber < SongListActivity.songList.size() - 1) {
                        songNumber++;
                    } else {
                        songNumber = 0;
                    }
                    setSong();
                    setNextSongTitle();
                    songProgress = 0;
                    songSeekBar.setProgress(songProgress);
                }
                songSeekBar.setProgress(++songProgress);
                mSeekBarUpdateHandler.postDelayed(this, 1000);
            }
        };
        mSeekBarUpdateHandler.postDelayed(mUpdateSeekBar, 0);
    }

    private void playSong() {
        mSeekBarUpdateHandler.removeCallbacks(mUpdateSeekBar);
        songPaused = false;
        pausePlayBtn.setImageResource(R.drawable.ic_pause_white);
        mSeekBarUpdateHandler.postDelayed(mUpdateSeekBar, 0);
    }

    private void setNextSongTitle() {
        if (songNumber == SongListActivity.songList.size() - 1) {
            nextSongTxtView.setText(SongListActivity.songList.get(0).getTitle());
        } else {
            nextSongTxtView.setText(SongListActivity.songList.get(songNumber + 1).getTitle());
        }
    }

    private void setSong() {
        Song song = SongListActivity.songList.get(songNumber);
        artistNameTxtView.setText(song.getArtistName());
        songNameTxtView.setText(song.getTitle());
        albumImageView.setImageResource(song.getImageResource());
        int resource = song.isFavorite() ? R.drawable.ic_favorite_active : R.drawable.ic_favorite_white;
        favoriteBtn.setImageResource(resource);
        songProgress = 0;
    }

    public enum RepeatState {
        DISABLED, ACTIVATED, REPEAT_ONE
    }
}