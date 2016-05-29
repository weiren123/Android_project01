package com.atguigu.musicplay.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.atguigu.musicplay.IMediaServier;
import com.atguigu.musicplay.MusicItems;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MediaServier extends Service {
    public static final String OPEN_AUTO = "com.atguigu.musicplay.OPEN_AUDIO";
    private IMediaServier.Stub stub = new IMediaServier.Stub() {
        MediaServier servier = MediaServier.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAuto(int position) throws RemoteException {
            servier.openAuto(position);
        }

        @Override
        public void start() throws RemoteException {
            servier.start();
        }

        @Override
        public void pause() throws RemoteException {
            servier.pause();
        }

        @Override
        public void pre() throws RemoteException {
            servier.pre();
        }

        @Override
        public void next() throws RemoteException {
            servier.next();
        }

        @Override
        public int getCrttenPosition() throws RemoteException {
            return servier.getCrttenPosition();
        }

        @Override
        public long getDurition() throws RemoteException {
            return servier.getDurition();
        }

        @Override
        public String getName() throws RemoteException {
            return servier.getName();
        }

        @Override
        public String getlire() throws RemoteException {
            return servier.getlire();
        }

        @Override
        public boolean isplaying() throws RemoteException {
            return servier.isplaying();
        }

        @Override
        public void seekto(int seekto) throws RemoteException {
            servier.seekto(seekto);
        }
    };
    private int position;
    private final ArrayList<MusicItems> musicItemses = new ArrayList<>();
    private MusicItems musicItems;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        ContentResolver resolver = MediaServier.this.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] object = {MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        Cursor cursor = resolver.query(uri, object, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicItems musicItems = new MusicItems();
                String nameStr = cursor.getString(0);
                musicItems.setName(nameStr);
                long size = cursor.getLong(1);
                musicItems.setSize(size);
                long duration = cursor.getLong(2);
                musicItems.setDuration(duration);
                String data = cursor.getString(3);
                musicItems.setDataStr(data);
                musicItemses.add(musicItems);
            }
             }
            }
        }).start();
    }
    public void openAuto(int position) {
        this.position = position;
        if (musicItemses!=null&&musicItemses.size()>0) {
            musicItems = musicItemses.get(position);
            if(mediaPlayer !=null){
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            mediaPlayer.setOnErrorListener(new MyOnErrorListener());
            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
            try {
                mediaPlayer.setDataSource(musicItems.getDataStr());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }
    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return false;
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
            notifychange(OPEN_AUTO);
        }
    }

    private void notifychange(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    public boolean isplaying(){
        return mediaPlayer.isPlaying();
    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void pre() {
        position--;
        if(position<0){
            position=0;
        }
        openAuto(position);
    }

    public void next() {
        position++;
        if(position>musicItemses.size()-1){
            position=musicItemses.size()-1;
        }
        openAuto(position);
    }

    public int getCrttenPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public long getDurition() {
        return musicItems.getDuration();
    }

    public String getName() {
        return musicItems.getName();
    }

    public String getlire() {
        return null;
    }

    public void seekto(int seekto){
        mediaPlayer.seekTo(seekto);
    }

}
