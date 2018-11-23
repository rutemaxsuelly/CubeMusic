package com.example.android.cubemusic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class VitoriaActivity extends Activity {
    private TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vitoria_);
        String pontos= getIntent().getStringExtra("PONTOS");
        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        msg = (TextView) findViewById(R.id.textView3);
        msg.setTypeface(font);
        msg.setText(pontos);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(VitoriaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        },3000);
    }

}
