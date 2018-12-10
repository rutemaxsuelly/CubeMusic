package com.example.android.cubemusic;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordesActivity extends Activity {

    private TextView lugar_1;
    private TextView lugar_2;
    private TextView lugar_3;
    private TextView lugar_4;
    private TextView lugar_5;
    private TextView lugar_6;
    private TextView recordes_txt;

    //Preferences
    private int c;
    private int N; //maximun number of players
    private String nPlayer[]; //name player
    private int rPlayer[];   //record player
    private SharedPreferences myScore;
    private SharedPreferences.Editor editor;
    private SharedPreferences myScoreg;
    private ListView lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_recordes);
        N = 6; //maximun number of players
        nPlayer = new String[N]; //name player
        rPlayer = new int[N];    //record player

        // Set up the user interaction to manually show or hide the system UI.
        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        recordes_txt = (TextView) findViewById(R.id.textView2);
        recordes_txt.setTypeface(font);

        lugar_1 = (TextView) findViewById(R.id.textView4);
        lugar_1.setTypeface(font);
        lugar_1.setText(R.string.primeiro_lugar);

        lugar_2 = (TextView) findViewById(R.id.textView5);
        lugar_2.setTypeface(font);
        lugar_2.setText(R.string.segundo_lugar);

        lugar_3 = (TextView) findViewById(R.id.textView6);
        lugar_3.setTypeface(font);
        lugar_3.setText(R.string.terceiro_lugar);

        lugar_4 = (TextView) findViewById(R.id.textView7);
        lugar_4.setTypeface(font);
        lugar_4.setText(R.string.quarto_lugar);

        lugar_5 = (TextView) findViewById(R.id.textView8);
        lugar_5.setTypeface(font);
        lugar_5.setText(R.string.quinto_lugar);

        lugar_6 = (TextView) findViewById(R.id.textView9);
        lugar_6.setTypeface(font);
        lugar_6.setText(R.string.sexto_lugar);

        lugar_6 = (TextView) findViewById(R.id.textView2);
        lugar_6.setTypeface(font);

        lista = (ListView) findViewById(R.id.lvRecordes);

        myScore = getSharedPreferences("MyAwesomeScore", Context.MODE_PRIVATE);
        editor = myScore.edit();
        myScoreg = this.getSharedPreferences("MyAwesomeScore", Context.MODE_PRIVATE);
        c = myScoreg.getInt("count", 0);
        loadScore();
    }

    public void loadScore(){
        for(int i = 0; i <= c; i++){
            nPlayer[i] = Integer.toString(i);//name player
            rPlayer[i] = myScoreg.getInt(nPlayer[i],0);
        }
        ArrayList<String> recordes = preencherDados();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, recordes);

        lista.setAdapter(arrayAdapter);
    }



    private ArrayList<String> preencherDados(){
        ArrayList dados=new ArrayList<String>();
        for(int i = 0; i <= N-1; i++){
            dados.add(Integer.toString(rPlayer[i]));
        }
        return dados;
    }

}
