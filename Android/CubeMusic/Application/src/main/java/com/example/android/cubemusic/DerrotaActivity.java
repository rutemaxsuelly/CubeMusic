package com.example.android.cubemusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class DerrotaActivity extends Activity {
    private TextView msg;
    private MediaPlayer mediaPlayer;
    //Preferences
    private int c;
    private int N; //maximun number of players
    private String nPlayer[]; //name player
    private int rPlayer[];   //record player
    private SharedPreferences myScore;
    private SharedPreferences.Editor editor;
    private SharedPreferences myScoreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_derrota_);
        String pontos= getIntent().getStringExtra("PONTOS");
        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        msg = (TextView) findViewById(R.id.textView10);
        msg.setTypeface(font);
        msg.setText(pontos + " Pontos");
        playSound(R.raw.adderrota);

        N = 6; //maximun number of players
        nPlayer = new String[N]; //name player
        rPlayer = new int[N];    //record player
        myScore = getSharedPreferences("MyAwesomeScore", Context.MODE_PRIVATE);
        editor = myScore.edit();
        myScoreg = this.getSharedPreferences("MyAwesomeScore", Context.MODE_PRIVATE);
        c = myScoreg.getInt("count", 0);
        saveScore(Integer.parseInt(pontos));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(DerrotaActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        },5000);
    }

    private void playSound(int resId){
        try {
            mediaPlayer = MediaPlayer.create(DerrotaActivity.this, resId);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                }
            });
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }

    }
    public void backerrou(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveScore(int value){
        int raux;
        String naux;

        //Load score

        for(int i = 0; i <= c; i++){
            nPlayer[i] = Integer.toString(i);//name player
            rPlayer[i] = myScoreg.getInt(nPlayer[i],0);
        }

        if(rPlayer[N-1] == 0) {
            if(c < N-1 && c != 0)
                c++;

            nPlayer[c] = Integer.toString(c);//name player
            rPlayer[c] = value;//record player

            //Save score
            if(c==0) {
                editor.putInt("count", c + 1);
            }else {
                editor.putInt("count", c);
            }

            editor.putInt(nPlayer[c], rPlayer[c]);
            editor.commit();




        }else if(value > rPlayer[c]){

            nPlayer[c] = Integer.toString(c);//name player
            rPlayer[c] = value;//record player

            //Save score
            editor.putInt("count", c);
            editor.putInt(nPlayer[c], rPlayer[c]);
            editor.commit();
        }

        for(int i=0; i <= c; i++){
            for(int j=0; j <= c; j++){
                if(rPlayer[i]>rPlayer[j]){
                    raux=rPlayer[i];
                    naux=nPlayer[i];
                    rPlayer[i]=rPlayer[j];
                    nPlayer[i]=nPlayer[j];
                    rPlayer[j]=raux;
                    nPlayer[j]=naux;
                }
            }
        }

        for(int i=0; i <= c; i++){
            editor.putInt(Integer.toString(i),rPlayer[i]);
            editor.commit();
        }
    }

}
