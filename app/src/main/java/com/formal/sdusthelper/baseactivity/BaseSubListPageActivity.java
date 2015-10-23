package com.formal.sdusthelper.baseactivity;

import android.app.ListActivity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.formal.sdusthelper.MyApplication;
import com.formal.sdusthelper.R;

/**
 * Created by Administrator on 2015/10/23.
 */
public class BaseSubListPageActivity extends ListActivity{
    protected MyApplication isdustapp;	//通过app调全局变量
    protected Context mContext;

    protected void INIT(int pageid,String title) {
        isdustapp = (MyApplication) this.getApplication();
        setContentView(pageid);
        mContext = this;
        TextView title_name = (TextView) findViewById(R.id.title_bar_name);
        title_name.setText(title);	//修改页面标题
    }   //初始化

    public void onTitleBarClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back_btn:
                finish();
                break;
        }
    }
}
