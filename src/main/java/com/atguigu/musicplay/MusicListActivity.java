package com.atguigu.musicplay;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.musicplay.uitls.Utils;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends Activity implements AdapterView.OnItemClickListener {
    private Button bt_back;
    private ListView lv_musiclist;
    private MyMusiacAdapter adapter;
    private List<MusicItems> data;
    private Utils utils;
    private Button isplayiing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        bt_back = (Button)findViewById(R.id.bt_back);
        lv_musiclist = (ListView)findViewById(R.id.lv_musiclist);
        isplayiing = (Button)findViewById(R.id.isplayiing);
        bt_back.setVisibility(View.VISIBLE);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        utils = new Utils();
        initData();
        isplayiing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MusicListActivity.this,AutdioActivity.class));
            }
        });
        adapter = new MyMusiacAdapter();
        lv_musiclist.setAdapter(adapter);
        lv_musiclist.setOnItemClickListener(this);
    }

    private void initData() {
       ContentResolver resolver = this.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] object = {
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA
        };
        Cursor cursor = resolver.query(uri, object, null, null, null);
        if(cursor!=null){
            data = new ArrayList<>();
            while (cursor.moveToNext()){
               MusicItems musicItems = new MusicItems();
                String name = cursor.getString(0);
                musicItems.setName(name);
                long duration = cursor.getLong(1);
                musicItems.setDuration(duration);
                long size = cursor.getLong(2);
                musicItems.setSize(size);
                String dataStr = cursor.getString(3);
                musicItems.setDataStr(dataStr);
                data.add(musicItems);
            }
            cursor.close();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //MusicItems musicItems = data.get(position);
        Intent intent = new Intent(this,AutdioActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    class MyMusiacAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
    }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder holder;
            if(convertView==null){
               convertView =  View.inflate(MusicListActivity.this,R.layout.item_music_list,null);
                holder = new Viewholder();
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_durarion = (TextView) convertView.findViewById(R.id.tv_duration);
                holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
                convertView.setTag(holder);
            }else {
                holder= (Viewholder) convertView.getTag();
            }
           MusicItems musicItems = data.get(position);
            holder.tv_content.setText(musicItems.getName());
            holder.tv_durarion.setText(utils.stringForTime((int) musicItems.getDuration()));
            holder.tv_size.setText(Formatter.formatFileSize(MusicListActivity.this,musicItems.getSize()));
            return convertView;
        }
    }

    static class Viewholder{
        TextView tv_content;
        TextView tv_durarion;
        TextView tv_size;
    }
}
