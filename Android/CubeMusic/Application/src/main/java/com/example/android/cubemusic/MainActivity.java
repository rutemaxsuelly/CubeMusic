package com.example.android.cubemusic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void onBackButton(View view){
        startActivity(new Intent(this, DeviceScanActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish();
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, DeviceScanActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    public void goToGame(View view)
    {
        Intent intent = new Intent(this, ModoActivity.class);
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
