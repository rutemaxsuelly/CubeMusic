package com.example.android.cubemusic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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
    private MediaPlayer mediaPlayer;
    private TextView msg;
    int cont_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");
        msg = (TextView) findViewById(R.id.textMsg);
        image_center = (ImageView) findViewById(R.id.image_center);
        image_center.setImageResource(R.drawable.dialogo_fundo);
        msg.setTypeface(font);
        msg.setVisibility(View.VISIBLE);
        msg.setText(R.string.vamos_la);
        //Espera 3s para começar
        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msg.setVisibility(View.INVISIBLE);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            }
        },3000);

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
                    msg.setVisibility(View.INVISIBLE);
                    updateImage(received_data);
                    old_data = received_data;
                    cont_time=0;
                }else{
                    if(cont_time==20){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                image_center.setImageResource(R.drawable.dialogo_fundo);
                                msg.setText("Mova o Cubo!");
                                msg.setVisibility(View.VISIBLE);
                            }
                        });
                        cont_time=0;
                    }else {
                        cont_time++;
                    }
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

    private void updateImage(String image_str){
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
}
