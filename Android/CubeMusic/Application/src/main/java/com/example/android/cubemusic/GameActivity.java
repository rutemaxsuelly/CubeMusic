package com.example.android.cubemusic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {
    private ImageView image_center;
    private ImageView image_1;
    private ImageView image_2;
    private ImageView image_3;
    private ImageView image_4;
    private ImageView image_5;
    private ImageView image_6;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress = "00:15:83:00:CA:B9";
    private String old_data = "";
    private String note_data = "";
    private MediaPlayer mediaPlayer;
    private TextView msg;
    private AssetManager assetManager;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private int user_counter = 0;
    private int counter = 0;
    private int advanceGameCounter = 0;
    private Handler handler_game_view;
    private Runnable runnable_game_view;
    private Handler handler_vamos_la;
    private Runnable runnable_vamos_la;
    private int pontos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        assetManager = getResources().getAssets();

        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        msg = (TextView) findViewById(R.id.textMsg);
        image_center = (ImageView) findViewById(R.id.image_center);
        msg.setTypeface(font);
        //Serviço -----------------------------
        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);


        msg.setVisibility(View.INVISIBLE);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        try {
            playGame();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }


    private void playGame() throws IOException, InterruptedException {
        //Lê sequência do arquivo
        inputStream = assetManager.open("music.txt");
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
        String linha = "";
        final List<String> seqMusica = new ArrayList<>();
        while((linha = bufferedReader.readLine())!=null){
            if(!linha.isEmpty())
                seqMusica.add(linha);
        }
        inputStream = assetManager.open("music_setup.txt");
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);

        final List<String> facesCubo = new ArrayList<>();
        final List<String> notasCubo = new ArrayList<>();
        while((linha = bufferedReader.readLine())!=null){
            facesCubo.add(linha);
            if((linha = bufferedReader.readLine())!=null){
                notasCubo.add(linha);
            }
        }


        counter = 0;
        advanceGameCounter = 0;
        //Vamos Lá!

        handler_vamos_la = new Handler();
        runnable_vamos_la = new Runnable() {

            public void run() {
                image_center.setImageResource(R.drawable.dialogo_fundo);
                msg.setVisibility(View.VISIBLE);
                msg.setText(R.string.vamos_la);



        handler_game_view = new Handler();
        runnable_game_view = new Runnable() {
            Intent intent=null;
            public void run() {
                //init game
                String nota = seqMusica.get(counter);
                msg.setVisibility(View.INVISIBLE);
                switch(nota){
                    case "c1":
                        updateImage(facesCubo.get(notasCubo.indexOf(nota)));
                        playSound(R.raw.c1);
                        break;
                    case "d1":
                        updateImage(facesCubo.get(notasCubo.indexOf(nota)));
                        playSound(R.raw.d1);
                        break;
                    case "e1":
                        updateImage(facesCubo.get(notasCubo.indexOf(nota)));
                        playSound(R.raw.e1);
                        break;
                    case "f1":
                        updateImage(facesCubo.get(notasCubo.indexOf(nota)));
                        playSound(R.raw.f1);
                        break;
                    case "g1":
                        updateImage(facesCubo.get(notasCubo.indexOf(nota)));
                        playSound(R.raw.g1);
                        break;
                    case "a1":
                        updateImage(facesCubo.get(notasCubo.indexOf(nota)));
                        playSound(R.raw.a1);
                        break;
                    default:
                        updateImage("nada");
                }

                if(counter < seqMusica.size()-1 && counter < advanceGameCounter) {
                    handler_game_view.postDelayed(this, 500);  //for interval...
                    counter++;
                }else {
                    final Handler handler_suaVez = new Handler();
                    Runnable runnable_suaVez = new Runnable() {
                        public void run() {
                            msgTela(getResources().getString(R.string.suaVez));
                            user_counter = 0;
                            final Handler handler_usuario = new Handler();
                            Runnable runnable_usuario = new Runnable() {
                                public void run() {
                                    msg.setVisibility(View.INVISIBLE);

                                    updateImageAndAudio(old_data);

                                    if(old_data.equals(facesCubo.get(notasCubo.indexOf(seqMusica.get(user_counter)))) && (user_counter < seqMusica.size()) && user_counter < advanceGameCounter){
                                        //Espera o usuário colocar a próxima figura
                                        user_counter++;
                                        pontos+=10;
                                        handler_usuario.postDelayed(this, 1500);
                                    }else if(old_data.equals(facesCubo.get(notasCubo.indexOf(seqMusica.get(user_counter)))) && (user_counter == seqMusica.size()-1)){
                                        //Usuário acertou tudo
                                        pontos+=30;
                                        intent = new Intent(GameActivity.this, VitoriaActivity.class);
                                        intent.putExtra("PONTOS", String.valueOf(pontos) + " Pontos");
                                        startActivity(intent);

                                    }else if(old_data.equals(facesCubo.get(notasCubo.indexOf(seqMusica.get(user_counter)))) && user_counter == advanceGameCounter) {
                                        //Usuário acertou a fase
                                        advanceGameCounter++;
                                          counter=0;
                                          pontos+=10;
                                          handler_vamos_la.postDelayed(runnable_vamos_la, 500);
                                    }else{
                                        //Usuário errou
                                        intent = new Intent(GameActivity.this, DerrotaActivity.class);
                                        intent.putExtra("PONTOS", String.valueOf(pontos) + " Pontos");
                                        startActivity(intent);
                                    }
                                }

                            };
                            handler_usuario.postDelayed(runnable_usuario, 2000);
                        }

                    };
                    handler_suaVez.postDelayed(runnable_suaVez, 2000);

                }
            }


        };
        handler_game_view.postDelayed(runnable_game_view, 3000); //for initial delay..
            }
        };
        handler_vamos_la.post(runnable_vamos_la);
//        handler_game_view.removeCallbacks(runnable_game_view);



//                if (old_data.equals(getResources().getString(R.string.triangulo))){
//                    Log.d("Aqui!!", "triangulo");
//                    intent = new Intent(GameActivity.this, VitoriaActivity.class);
//                }else{
//                    Log.d("Aqui!!", old_data);
//                    intent = new Intent(GameActivity.this, DerrotaActivity.class);
//                }

    }

    private void msgTela(final String msgstr){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image_center.setImageResource(R.drawable.dialogo_fundo);
                msg.setVisibility(View.VISIBLE);
                msg.setText(msgstr);
            }
        });
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(this.getClass().getSimpleName(), "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                String received_data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA).trim();
                if(!(received_data.equals(old_data))) {
//                    msg.setVisibility(View.INVISIBLE);
//                    updateImageAndAudio(received_data);
                    old_data = received_data;
//                    cont_time=0;
                }else{
//                    if(cont_time==20){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                image_center.setImageResource(R.drawable.dialogo_fundo);
//                                msg.setText("Mova o Cubo!");
//                                msg.setVisibility(View.VISIBLE);
//                            }
//                        });
//                        cont_time=0;
//                    }else {
//                        cont_time++;
//                    }
                }
            }else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.dialogo_fundo);
                        msg.setText("Conexão perdida.");
                        msg.setVisibility(View.VISIBLE);
                    }
                });
                //Espera 3s para começar
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        msg.setVisibility(View.INVISIBLE);
                        Intent i = getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                },5000);
            }
        }
    };
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void playSound(int resId){
        try {
            mediaPlayer = MediaPlayer.create(GameActivity.this, resId);
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

    private void updateImageAndAudio(String image_str){
        switch (image_str) {
            case "triangulo":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.triangulo);
                    }
                });
                playSound(R.raw.c1);
                break;
            case "circulo":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.circulo);
                    }
                });
                playSound(R.raw.d1);
                break;
            case "quadrado":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.quadrado);
                    }
                });
                playSound(R.raw.e1);
                break;
            case "quadrante":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.quadrante);
                    }
                });
                playSound(R.raw.f1);
                break;
            case "hexagono":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.hexagono);
                    }
                });
                playSound(R.raw.g1);
                break;
            case "estrela":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.estrela);
                    }
                });
                playSound(R.raw.a1);
                break;
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.default_image);
                    }
                });
        }
    }
    private void updateImage(String image_str){
        switch (image_str) {
            case "triangulo":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.triangulo);
                        msg.setVisibility(View.INVISIBLE);

                    }
                });
                break;
            case "circulo":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.circulo);
                    }
                });
                break;
            case "quadrado":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.quadrado);
                    }
                });
                break;
            case "quadrante":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.quadrante);
                    }
                });
                break;
            case "hexagono":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.hexagono);
                    }
                });
                break;
            case "estrela":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.estrela);
                    }
                });
                break;
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_center.setImageResource(R.drawable.default_image);
                    }
                });
        }
    }
}
