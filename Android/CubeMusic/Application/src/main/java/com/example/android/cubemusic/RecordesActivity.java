package com.example.android.cubemusic;
import android.graphics.Typeface;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordesActivity extends Activity {

    private TextView lugar_1;
    private TextView lugar_2;
    private TextView lugar_3;
    private TextView lugar_4;
    private TextView lugar_5;
    private TextView lugar_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordes);

        // Set up the user interaction to manually show or hide the system UI.
        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        lugar_1 = (TextView) findViewById(R.id.textView4);
        lugar_1.setTypeface(font);
        lugar_1.setVisibility(View.VISIBLE);
        lugar_1.setText(R.string.primeiro_lugar);

        lugar_2 = (TextView) findViewById(R.id.textView5);
        lugar_2.setTypeface(font);
        lugar_2.setVisibility(View.VISIBLE);
        lugar_2.setText(R.string.segundo_lugar);

        lugar_3 = (TextView) findViewById(R.id.textView6);
        lugar_3.setTypeface(font);
        lugar_3.setVisibility(View.VISIBLE);
        lugar_3.setText(R.string.terceiro_lugar);

        lugar_4 = (TextView) findViewById(R.id.textView7);
        lugar_4.setTypeface(font);
        lugar_4.setVisibility(View.VISIBLE);
        lugar_4.setText(R.string.quarto_lugar);

        lugar_5 = (TextView) findViewById(R.id.textView8);
        lugar_5.setTypeface(font);
        lugar_5.setVisibility(View.VISIBLE);
        lugar_5.setText(R.string.quinto_lugar);

        lugar_6 = (TextView) findViewById(R.id.textView9);
        lugar_6.setTypeface(font);
        lugar_6.setVisibility(View.VISIBLE);
        lugar_6.setText(R.string.sexto_lugar);
    }

}
