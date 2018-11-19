package com.example.android.cubemusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class GameActivity extends Activity {
    private ImageView image_center;
    private ImageView image_1;
    private ImageView image_2;
    private ImageView image_3;
    private ImageView image_4;
    private ImageView image_5;
    private ImageView image_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
    }
}
