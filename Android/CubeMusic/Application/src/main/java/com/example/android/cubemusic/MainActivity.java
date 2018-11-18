package com.example.android.cubemusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Log.d("Chegou", "OK");
    }

    public void goToGame(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void goToTutorial(View view)
    {

        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    public void goToRecordes(View view)
    {

        Intent intent = new Intent(this, RecordesActivity.class);
        startActivity(intent);
    }
}
