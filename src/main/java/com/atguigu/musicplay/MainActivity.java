package com.atguigu.musicplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity implements View.OnClickListener {
    private ViewPager viewpager;
    private MyViewAdapter adapter;
    private List<View> data;
    private RadioGroup rg_bottom;
    private RadioButton quk_bottom,suib_bottom,soug_bottom,gnc__bottom;
    private PopupWindow popupWindow;
    private LinearLayout ll_music;
    private Button isplayiing;
    //protected ListView one_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isplayiing = (Button)findViewById(R.id.isplayiing);
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        rg_bottom = (RadioGroup)findViewById(R.id.rg_bottom);
        quk_bottom = (RadioButton)findViewById(R.id.quk_bottom);
        suib_bottom = (RadioButton)findViewById(R.id.suib_bottom);
        soug_bottom = (RadioButton)findViewById(R.id.soug_bottom);
        gnc__bottom = (RadioButton)findViewById(R.id.gnc__bottom);

        inintData();
        isplayiing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AutdioActivity.class));
            }
        });
        adapter = new MyViewAdapter();
        viewpager.setAdapter(adapter);
        rg_bottom.setOnCheckedChangeListener(new MyOnChange());
        quk_bottom.setChecked(true);
       viewpager.addOnPageChangeListener(new OnMyPage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_music :
        startActivity(new Intent(this,MusicListActivity.class));
                break;
        }
    }


    class MyOnChange implements RadioGroup.OnCheckedChangeListener{
     @Override
     public void onCheckedChanged(final RadioGroup group, int checkedId) {

          if(checkedId == R.id.quk_bottom){
              viewpager.setCurrentItem(0);
              quk_bottom.setChecked(true);
              suib_bottom.setChecked(false);
              soug_bottom.setChecked(false);
              gnc__bottom.setChecked(false);
              if(popupWindow!=null && popupWindow.isShowing()){
                  popupWindow.dismiss();
                  popupWindow = null;
              }
              gnc__bottom.setBackgroundResource(R.drawable.tab_menu_default);
          }else if(checkedId == R.id.suib_bottom){
              viewpager.setCurrentItem(1);
              suib_bottom.setChecked(true);
              quk_bottom.setChecked(false);
              soug_bottom.setChecked(false);
              gnc__bottom.setChecked(false);
              if(popupWindow!=null && popupWindow.isShowing()){
                  popupWindow.dismiss();
                  popupWindow = null;
              }
              gnc__bottom.setBackgroundResource(R.drawable.tab_menu_default);
          }else if(checkedId == R.id.soug_bottom){
              viewpager.setCurrentItem(2);
              suib_bottom.setChecked(false);
              quk_bottom.setChecked(false);
              soug_bottom.setChecked(true);
              gnc__bottom.setChecked(false);
              if(popupWindow!=null && popupWindow.isShowing()){
                  popupWindow.dismiss();
                  popupWindow = null;
              }
              gnc__bottom.setBackgroundResource(R.drawable.tab_menu_default);
          }else if(checkedId == R.id.gnc__bottom){
              if(popupWindow == null){
                  initpopuWindow();
              }
              if(popupWindow.isShowing()){

                  popupWindow.dismiss();
                  gnc__bottom.setBackgroundResource(R.drawable.tab_menu_default);
                  rg_bottom.clearCheck();
              }else {
                  popupWindow.showAsDropDown(group, 0, 0);
                  gnc__bottom.setBackgroundResource(R.drawable.tab_menu_open_up);
                  rg_bottom.clearCheck();
              }
          }
     }
 }
    private void inintData() {
        EventBus.getDefault().register(this);
        data = new ArrayList<>();
        View view1 = View.inflate(MainActivity.this, R.layout.item_one_list, null);
        View view2 = View.inflate(MainActivity.this, R.layout.shearch_view, null);
        View view3 = View.inflate(MainActivity.this, R.layout.suibian_view, null);
        data.add(view1);
        data.add(view3);
        data.add(view2);
        ll_music = (LinearLayout)view1.findViewById(R.id.ll_music);
        ll_music.setOnClickListener(this);
    }

    private void initpopuWindow() {
        popupWindow = new PopupWindow(viewpager.getWidth(), 2 * rg_bottom.getHeight());
        View view = View.inflate(MainActivity.this, R.layout.item_gridview, null);
        popupWindow.setContentView(view);
    }
    public void onEventMainThread(String xx) {
        if(xx.equals("1")){
            finish();
        }else {
            rg_bottom.clearCheck();
            popupWindow.dismiss();
            gnc__bottom.setBackgroundResource(R.drawable.tab_menu_default);
        }
    }

    class MyViewAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(data.get(position));
            return data.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(data.get(position));
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
    class OnMyPage implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if(popupWindow!=null && popupWindow.isShowing()){
                popupWindow.dismiss();
                popupWindow = null;
                gnc__bottom.setChecked(false);
            }
            if(position == 0){
                quk_bottom.setChecked(true);
                suib_bottom.setChecked(false);
                soug_bottom.setChecked(false);
                gnc__bottom.setChecked(false);

            }else if(position == 1){
                suib_bottom.setChecked(true);
                quk_bottom.setChecked(false);
                soug_bottom.setChecked(false);
                gnc__bottom.setChecked(false);
              //  popupWindow.dismiss();
            }else if(position == 2){
                soug_bottom.setChecked(true);
                suib_bottom.setChecked(false);
                quk_bottom.setChecked(false);
                gnc__bottom.setChecked(false);
              //  popupWindow.dismiss();
            }
            gnc__bottom.setBackgroundResource(R.drawable.tab_menu_default);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
