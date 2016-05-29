package com.atguigu.musicplay;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.musicplay.service.MediaServier;
import com.atguigu.musicplay.uitls1.Utils;

public class AutdioActivity extends Activity implements View.OnClickListener {
    private static final int PROGRESS = 1;
    private TextView tvPlaytime;
    private SeekBar seekbar;
    private Button preButton;
    private Button pauseorplayButton;
    private Button nextButton;
    private IMediaServier mediaServier;
    private int position;
    private TextView tv_quku;
    private Button bt_back;
    private Button isplayiing;
    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaServier = IMediaServier.Stub.asInterface(service);
            try {
                mediaServier.openAuto(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Myrecier receive;
    private Utils utils;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-05-27 19:54:40 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        isplayiing = (Button)findViewById(R.id.isplayiing);
        bt_back = (Button)findViewById(R.id.bt_back);
        tvPlaytime = (TextView)findViewById( R.id.tv_playtime );
        seekbar = (SeekBar)findViewById( R.id.seekbar );
        preButton = (Button)findViewById( R.id.pre_button );
        pauseorplayButton = (Button)findViewById( R.id.pauseorplay_button );
        nextButton = (Button)findViewById( R.id.next_button );
        tv_quku = (TextView)findViewById(R.id.tv_quku);

        preButton.setOnClickListener( this );
        pauseorplayButton.setOnClickListener( this );
        nextButton.setOnClickListener( this );
        isplayiing.setEnabled(false);
        bt_back.setVisibility(View.VISIBLE);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                try {
                    mediaServier.seekto(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-05-27 19:54:40 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == preButton ) {
            // Handle clicks for preButton
            try {
                mediaServier.pre();
                pauseorplayButton.setBackgroundResource(R.drawable.pause_button_selector);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == pauseorplayButton ) {
            startOrpause();
            // Handle clicks for pauseorplayButton
        } else if ( v == nextButton ) {
            // Handle clicks for nextButton
            try {
                mediaServier.next();
                pauseorplayButton.setBackgroundResource(R.drawable.pause_button_selector);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void startOrpause() {
        try {
            if(mediaServier.isplaying()){
                mediaServier.pause();
                pauseorplayButton.setBackgroundResource(R.drawable.play_button_selector);
            }else {
                mediaServier.start();
                pauseorplayButton.setBackgroundResource(R.drawable.pause_button_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autdio);
        findViews();
        initData();
        getData();
        bindandstart();
    }

    private void initData() {
        utils = new Utils();
        receive = new Myrecier();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MediaServier.OPEN_AUTO);
        registerReceiver(receive, filter);
    }

     private Handler handler = new Handler(){
         public void handleMessage(Message msg){
             switch (msg.what) {
                 case PROGRESS :
                     try {
                         int crttenPosition = mediaServier.getCrttenPosition();
                         tvPlaytime.setText(utils.stringForTime(mediaServier.getCrttenPosition()) + "/" + utils.stringForTime((int) mediaServier.getDurition()));
                         seekbar.setProgress(crttenPosition);
                     } catch (RemoteException e) {
                         e.printStackTrace();
                     }

                        handler.removeMessages(PROGRESS);
                         handler.sendEmptyMessageDelayed(PROGRESS,1000);
                     break;
             }
         }
     };
    class Myrecier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            setViewData();
            try {
                seekbar.setMax((int) mediaServier.getDurition());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            handler.sendEmptyMessage(PROGRESS);
        }
    }

    private void setViewData() {
        try {
            tv_quku.setText(mediaServier.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        position = getIntent().getIntExtra("position",position);
    }

    private void bindandstart() {
        Intent intent = new Intent(this, MediaServier.class);
        intent.setAction(MediaServier.OPEN_AUTO);
        bindService(intent, con, Service.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(con!=null){
            unbindService(con);
            con =null;
        }

        if(receive!=null){
            unregisterReceiver(receive);
            receive =null;
        }
    }
}
