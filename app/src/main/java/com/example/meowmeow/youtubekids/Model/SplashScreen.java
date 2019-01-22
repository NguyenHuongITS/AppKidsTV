package com.example.meowmeow.youtubekids.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.example.meowmeow.youtubekids.R;

public class SplashScreen extends AppCompatActivity {

    VideoView videoView;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    boolean results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sharedPreferences = getSharedPreferences("prefs", 0);
        editor = sharedPreferences.edit();
        results = sharedPreferences.getBoolean("firstRun", true);

        videoView = findViewById(R.id.videoView);
        if(!results){
            Log.d("TAG1", String.valueOf(results));
            startActivity(new Intent(SplashScreen.this,RecommendedMovie.class));
        }else {
            String uriPath = "android.resource://" + getPackageName() + "/" + "raw/logo"; //
            Uri video = Uri.parse(uriPath);
            videoView.setVideoURI(video);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    startNextActivivy();
                }
            });
            videoView.start();
        }
    }

    private void startNextActivivy() {

        Log.d("TAG1", "firstRun(true): " + Boolean.valueOf(results).toString());
        editor.putBoolean("firstRun", false);
        editor.commit();
        Intent intent = new Intent(this, RecommendedMovie.class);
        startActivity(intent);
    }
}
