package com.atguigu.musicplay.listener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/5/29.
 */
public class PopuView extends LinearLayout implements View.OnClickListener {

    private TextView tv_newcreat;
    private TextView tv_seting;
    private TextView tv_updata;
    private TextView tv_exit;
    private Context context;
    public PopuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    private void initView() {
        tv_newcreat.setOnClickListener(this);
        tv_seting.setOnClickListener(this);
        tv_updata.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_newcreat = (TextView) getChildAt(0);
        tv_seting = (TextView) getChildAt(1);
        tv_updata = (TextView) getChildAt(2);
        tv_exit = (TextView) getChildAt(3);

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }


    @Override
    public void onClick(View v) {
        if(v==tv_newcreat){
            Toast.makeText(context, "新建", Toast.LENGTH_SHORT).show();
        }else if(v==tv_seting){
            Toast.makeText(context, "设置", Toast.LENGTH_SHORT).show();
        }else if(v==tv_updata){
            Toast.makeText(context, "更新", Toast.LENGTH_SHORT).show();
        }else if(v==tv_exit){
            EventBus.getDefault().post("1");
        }
        EventBus.getDefault().post("2");
    }
}
