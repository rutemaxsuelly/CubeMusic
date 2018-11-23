package com.example.android.cubemusic;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TutorialActivity extends Activity {
    private ImageView image;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress = "00:15:83:00:CA:B9";
    private String old_data = "";
    private MediaPlayer mediaPlayer;
    private TextView msg;
    int cont_time = 0;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        Typeface font = Typeface.createFromAsset(getAssets(), "impact.ttf");

        msg = (TextView) findViewById(R.id.textMsg);
        image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.dialogo_fundo);
        msg.setTypeface(font);
        msg.setVisibility(View.VISIBLE);
        msg.setText(R.string.vamos_la);
        playSound(R.raw.advamoscomecar);
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
                                image.setImageResource(R.drawable.dialogo_fundo);
                                msg.setText("Mova o Cubo!");
                                msg.setVisibility(View.VISIBLE);
                                playSound(R.raw.admovacubo);
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
                        image.setImageResource(R.drawable.dialogo_fundo);
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
            mediaPlayer = MediaPlayer.create(TutorialActivity.this, resId);
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
                        image.setImageResource(R.drawable.triangulo);
                    }
                });
                playSound(R.raw.c1);
                break;
            case "circulo":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.circulo);
                    }
                });
                playSound(R.raw.d1);
                break;
            case "quadrado":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.quadrado);
                    }
                });
                playSound(R.raw.e1);
                break;
            case "quadrante":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.quadrante);
                    }
                });
                playSound(R.raw.f1);
                break;
            case "hexagono":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.hexagono);
                    }
                });
                playSound(R.raw.g1);
                break;
            case "estrela":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.estrela);
                    }
                });
                playSound(R.raw.a1);
                break;
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.default_image);
                    }
                });

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    public void onDestroy() {
        super.onDestroy();
        if(mServiceConnection != null)
            unbindService(mServiceConnection);
        if(mGattUpdateReceiver != null)
            unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService = null;
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
