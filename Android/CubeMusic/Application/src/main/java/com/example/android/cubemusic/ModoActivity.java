package com.example.android.cubemusic;

import android.media.MediaPlayer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ModoActivity extends Activity {
    public static int modogenius;

    private TextView selecao;
    private TextView modotop;
    private MediaPlayer mediaPlayer;

    private void playSound(int resId){
        try {
            mediaPlayer = MediaPlayer.create(ModoActivity.this, resId);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modo_);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        // Set up the user interaction to manually show or hide the system UI.

        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        selecao= (TextView) findViewById(R.id.textView);
        selecao.setTypeface(font);
        selecao.setText(R.string.selecao_nivel);

        modotop= (TextView) findViewById(R.id.textviewmodogenius);
        modotop.setTypeface(font);
        modotop.setText(R.string.mod_genius);
        playSound(R.raw.admodogenius);


    }

    public void onBackButton(View view){
        startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish();
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    public void goToGenius(View view){
        modogenius=1;
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
   public void goToGenius2(View view){
        modogenius=2;
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
