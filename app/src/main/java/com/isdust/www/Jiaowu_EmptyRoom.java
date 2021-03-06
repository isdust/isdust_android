package com.isdust.www;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.isdust.www.baseactivity.BaseSubPageActivity;
import com.isdust.www.datatype.ScheduleInformation;
import com.isdust.www.view.IsdustDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pw.isdust.isdust.function.EmptyClassroom;
import pw.isdust.isdust.function.SchoolDate;

/**
 * Created by Wang Ziqiang on 15/10/16.
 * isdust
 Copyright (C) <2015>  <Wang Ziqiang,Leng Hanchao,Qing Wenkai,Huyang>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Jiaowu_EmptyRoom extends BaseSubPageActivity {

    ListView mListView;

    //储存教学楼选择
    SharedPreferences.Editor preferences_editor;
    SharedPreferences preferences_data;

    //按钮
    private Button mButton_chaxun_kongzixishi;


    // 工具栏
    private RelativeLayout mRelativeLayout_building;
    private RelativeLayout mRelativeLayout_zhoushu;
    private RelativeLayout mRelativeLayout_xingqi;
    private RelativeLayout mRelativeLayout_jieci;


    // 左中右三个控件（工具栏里）

    private TextView mTextView_building;
    private TextView mTextView_zhoushu;
    private TextView mTextView_xingqi;
    private TextView mTextView_jieci;


    // 左中右三个弹出窗口

    private PopupWindow mPopupWindow_building;
    private PopupWindow mPopupWindow_zhoushu;
    private PopupWindow mPopupWindow_xingqi;
    private PopupWindow mPopupWindow_jieci;

    // 左中右三个layout

    private View mView_building;
    private View mView_zhoushu;
    private View mView_xingqi;
    private View mView_jieci;


    // 左中右三个ListView控件（弹出窗口里）

    private ListView mListView_building;
    private ListView mListView_xingqi;
    private ListView mListView_zhoushu;
    private ListView mListView_jieci;

    private List<Map<String, String>> mList_building;
    private List<Map<String, String>> mList_zhoushu;
    private List<Map<String, String>> mList_xingqi;
    private List<Map<String, String>> mList_jieci;




    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private String xiancheng_location,xiancheng_building;
    private int xiancheng_zhoushu,xiancheng_xingqi,xiancheng_jieci;

    private boolean xiancheng_first;
    private ScheduleInformation xiancheng_Kebiaoxinxi[];
    //?method=4&building=&xingqi=&xingqi=&jieci=

    private EmptyClassroom mKongzixishi;

    private List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();	//列表框的数据
    //private ProgressDialog dialog;
    IsdustDialog customRuningDialog;
    SimpleAdapter adapter;	//列表的适配器
    Context mContext;
    final android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                listdata.clear();//清空列表
                for (int i=0;i< xiancheng_Kebiaoxinxi.length;i++){
                    Map<String, Object> map;

                    map = new HashMap<String, Object>();
                    map.put("location", " "+ xiancheng_Kebiaoxinxi[i].getlocation());
                    map.put("zhoushu","     第"+ xiancheng_Kebiaoxinxi[i].getzhoushu()+"周");
                    map.put("xingqi","     "+xingqizhuanhuan(xiancheng_Kebiaoxinxi[i].getxinqi()));
                    map.put("jieci",jiecizhuanhuan(xiancheng_Kebiaoxinxi[i].getjieci()));
                    listdata.add(map);
                }
                adapter = new SimpleAdapter(mContext, listdata,
                        R.layout.activity_emptyroom_item, new String[] { "location", "zhoushu", "xingqi", "jieci"},
                        new int[] { R.id.textView_kongzixishi_location, R.id.textView_kongzixishi_zhoushu,
                                R.id.textView_kongzixishi_xingqi,	R.id.textView_kongzixishi_jieci});
                mListView.setAdapter(adapter);	//捆绑适配器
                customRuningDialog.dismiss();// }

            }else if(msg.what==10){

                customRuningDialog.dismiss();
                Toast.makeText(mContext, "网络访问超时，请重试", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 11){//网络超时
                customRuningDialog.dismiss();
                Toast.makeText(mContext, "在线参数获取失败，请保证网络正常的情况下重启app", Toast.LENGTH_SHORT).show();
            }


        }

    };
    Runnable mRunnable_jiazaizixishi=new Runnable(){
        @Override
        public void run() {

            //listdata=null;
            try {
                mKongzixishi=new EmptyClassroom(mContext);
            } catch (Exception e) {
                Message message=new Message();
                message.what=11;
                mHandler.sendMessage(message);
            return;}
            try {
                xiancheng_Kebiaoxinxi = mKongzixishi.getEmptyClassroom(xiancheng_building, xiancheng_zhoushu, xiancheng_xingqi, xiancheng_jieci);

                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }catch (Exception e){
                Message message=new Message();
                message.what=10;
                mHandler.sendMessage(message);
            }



        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        //MobclickAgent.onEvent(this, "jiaowu_Emptyroom");

        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_emptyroom_listview,"空自习室查询");
        //setContentView(R.layout.activity_emptyroom_listview);
        mListView=(ListView)findViewById(R.id.listview_emptyroom);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map;
                map=listdata.get(i);
                String mlocation=getMapValue(map,"location").replace(" ","");
                String mzhoushu=getMapValue(map,"zhoushu").replace(" ", "");
                String mxingqi=getMapValue(map, "xingqi").replace(" ", "");
                String mjieci=getMapValue(map,"jieci").replace(" ", "");
                String neirong="地点:"+mlocation+",周次:"+mzhoushu+",星期:"+mxingqi+",节次:"+mjieci;
                setClipboard(mContext,neirong);
                Toast.makeText(mContext, "信息已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });
        mContext=this;
        xiancheng_first=false;
        customRuningDialog = new IsdustDialog(mContext,
                IsdustDialog.RUNING_DIALOG, R.style.DialogTheme);   //初始化加载对话框
        TextView title_name = (TextView) findViewById(R.id.title_bar_name);
        title_name.setText("空自习室查询");
        mButton_chaxun_kongzixishi=(Button)findViewById(R.id.button_chaxun_kongzixishi);
        mButton_chaxun_kongzixishi.setOnClickListener(mButton_onclick);
        //实例化SharedPreferences对象
        preferences_data = mContext.getSharedPreferences("EmptyRoomData", Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        preferences_editor = preferences_data.edit();
        initParam();
        zhinengxuanze();









    }


    private void cha(String building,int zhoushu,int xingqi,int jieci){
        xiancheng_building=building;
        xiancheng_zhoushu=zhoushu;
        xiancheng_xingqi=xingqi;
        xiancheng_jieci=jieci;
        customRuningDialog.show();
        customRuningDialog.setMessage("正在获取空自习室……");
//        dialog = ProgressDialog.show(
//                mContext, "提示",
//                "正在获取空自习室……", true, true);// }
        executorService.execute(mRunnable_jiazaizixishi);
    }
    public static  String xingqizhuanhuan(int raw){
        switch (raw){
            case 1:return "星期一";
            case 2:return "星期二";
            case 3:return "星期三";
            case 4:return "星期四";
            case 5:return "星期五";
            case 6:return "星期六";
            case 7:return "星期日";

        }
        return "error_xingqizhuanhuan";
    }

    public static  int xingqizhuanhuan(String raw){
        switch (raw){
            case "星期一":return 1;
            case "星期二":return 2;
            case "星期三":return 3;
            case "星期四":return 4;
            case "星期五":return 5;
            case "星期六":return 6;
            case "星期日":return 7;

        }
        return 0;
    }


    public static String jiecizhuanhuan(int raw){
        switch (raw){
            case 1:return "第一、二节";
            case 2:return "第三、四节";
            case 3:return "第五、六节";
            case 4:return "第七、八节";
            case 5:return "第九、十节";

        }
        return "error_jiecizhuanhuan";
    }

    public static int jiecizhuanhuan(String raw){
        switch (raw){
            case "第一、二节":return 1;
            case "第三、四节":return 2;
            case "第五、六节":return 3;
            case "第七、八节":return 4;
            case "第九、十节":return 5;

        }
        return 0;
    }



    private void initParam() {
        //初始化building
        mRelativeLayout_building = (RelativeLayout) this.findViewById(R.id.relativeLayout_building);
        mTextView_building = (TextView) this.findViewById(R.id.textView_building);
        mTextView_building.setOnClickListener(myListener);
        //初始化xingqi
        mRelativeLayout_xingqi= (RelativeLayout) this.findViewById(R.id.relativeLayout_xingqi);
        mTextView_xingqi = (TextView) this.findViewById(R.id.textView_xingqi);
        mTextView_xingqi.setOnClickListener(myListener);
        //初始化jieci
        mRelativeLayout_jieci= (RelativeLayout) this.findViewById(R.id.relativeLayout_jieci);
        mTextView_jieci = (TextView) this.findViewById(R.id.textView_jieci);
        mTextView_jieci.setOnClickListener(myListener);
        //初始化zhoushu
        mRelativeLayout_zhoushu= (RelativeLayout) this.findViewById(R.id.relativeLayout_zhoushu);
        mTextView_zhoushu = (TextView) this.findViewById(R.id.textView_zhoushu);
        mTextView_zhoushu.setOnClickListener(myListener);



        // 初始化building数据项
        String [] building={"","J1", "Js1","J3","J5","J7","J11","J14","J15"};
        mList_building = new ArrayList<Map<String, String>>();
        for (int i = 1; i < building.length;i++) {
            HashMap<String, String> mapTemp = new HashMap<String, String>();
            mapTemp.put("building", building[i]);
            mList_building.add(mapTemp);
        }


        // 初始化zhouci数据项
        String [] xingqi={"","星期一", "星期二","星期三","星期四","星期五","星期六","星期天"};
        mList_xingqi = new ArrayList<Map<String, String>>();
        for (int i = 1; i < xingqi.length;i++) {
            HashMap<String, String> mapTemp = new HashMap<String, String>();
            mapTemp.put("xingqi", xingqi[i]);
            mList_xingqi.add(mapTemp);
        }

        String [] jieci={"","第一、二节", "第三、四节","第五、六节","第七、八节","第九、十节"};
        mList_jieci = new ArrayList<Map<String, String>>();
        for (int i = 1; i < jieci.length;i++) {
            HashMap<String, String> mapTemp = new HashMap<String, String>();
            mapTemp.put("jieci", jieci[i]);
            mList_jieci.add(mapTemp);
        }


        mList_zhoushu = new ArrayList<Map<String, String>>();
        int flag=0;

        for (int i = 1; i < 22;i++) {
            HashMap<String, String> mapTemp = new HashMap<String, String>();
            if (flag==0){
                flag=1;
                mapTemp.put("zhoushu", "");
                continue;
            }
            mapTemp.put("zhoushu", (i+1)+"");
            mList_zhoushu.add(mapTemp);
        }
    }


    private View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textView_building:
                    if (mPopupWindow_building != null && mPopupWindow_building.isShowing()) {
                        mPopupWindow_building.dismiss();
                    } else {
                        mView_building = getLayoutInflater().inflate(
                                R.layout.pop_menulist, null);
                        mListView_building = (ListView) mView_building.findViewById(R.id.menulist);
                        SimpleAdapter listAdapter = new SimpleAdapter(
                                mContext, mList_building, R.layout.pop_menuitem,
                                new String[] { "building" },
                                new int[] { R.id.menuitem });
                        mListView_building.setAdapter(listAdapter);

                        // 点击listview中item的处理
                        mListView_building
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0,
                                                            View arg1, int arg2, long arg3) {
                                        String strItem = mList_building.get(arg2).get(
                                                "building");

//                                        bangding(strItem);
                                        mTextView_building.setText(strItem);
                                        preferences_editor.putString("building",strItem);
                                        preferences_editor.commit();

                                        if (mPopupWindow_building != null && mPopupWindow_building.isShowing()) {
                                            mPopupWindow_building.dismiss();
                                        }
                                    }
                                });

                        mPopupWindow_building = new PopupWindow(mView_building, mTextView_building.getWidth(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        ColorDrawable cd = new ColorDrawable(-0000);
                        mPopupWindow_building.setBackgroundDrawable(cd);
                        mPopupWindow_building.setAnimationStyle(R.style.PopupAnimation);
                        mPopupWindow_building.update();
                        mPopupWindow_building.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                        mPopupWindow_building.setTouchable(true); // 设置popupwindow可点击
                        mPopupWindow_building.setOutsideTouchable(true); // 设置popupwindow外部可点击
                        mPopupWindow_building.setFocusable(true); // 获取焦点

                        // 设置popupwindow的位置
                        int topBarHeight = mRelativeLayout_building.getBottom();
                        mPopupWindow_building.showAsDropDown(mTextView_building, 0,
                              0);

                        mPopupWindow_building.setTouchInterceptor(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // 如果点击了popupwindow的外部，popupwindow也会消失
                                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    mPopupWindow_building.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                    break;

                case R.id.textView_xingqi:
                    if (mPopupWindow_xingqi != null && mPopupWindow_xingqi.isShowing()) {
                        mPopupWindow_xingqi.dismiss();
                    } else {
                        mView_xingqi = getLayoutInflater().inflate(
                                R.layout.pop_menulist, null);
                        mListView_xingqi = (ListView) mView_xingqi.findViewById(R.id.menulist);
                        SimpleAdapter listAdapter = new SimpleAdapter(
                                mContext, mList_xingqi, R.layout.pop_menuitem,
                                new String[] { "xingqi" },
                                new int[] { R.id.menuitem });
                        mListView_xingqi.setAdapter(listAdapter);

                        // 点击listview中item的处理
                        mListView_xingqi
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0,
                                                            View arg1, int arg2, long arg3) {
                                        String strItem = mList_xingqi.get(arg2).get(
                                                "xingqi");

//                                        bangding(strItem);
                                        mTextView_xingqi.setText(strItem);

                                        if (mPopupWindow_xingqi != null && mPopupWindow_xingqi.isShowing()) {
                                            mPopupWindow_xingqi.dismiss();
                                        }
                                    }
                                });

                        mPopupWindow_xingqi = new PopupWindow(mView_xingqi, mTextView_xingqi.getWidth(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        ColorDrawable cd = new ColorDrawable(-0000);
                        mPopupWindow_xingqi.setBackgroundDrawable(cd);
                        mPopupWindow_xingqi.setAnimationStyle(R.style.PopupAnimation);
                        mPopupWindow_xingqi.update();
                        mPopupWindow_xingqi.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                        mPopupWindow_xingqi.setTouchable(true); // 设置popupwindow可点击
                        mPopupWindow_xingqi.setOutsideTouchable(true); // 设置popupwindow外部可点击
                        mPopupWindow_xingqi.setFocusable(true); // 获取焦点

                        // 设置popupwindow的位置
                        int topBarHeight = mRelativeLayout_xingqi.getBottom();
                        mPopupWindow_xingqi.showAsDropDown(mTextView_xingqi, 0,
                              0);

                        mPopupWindow_xingqi.setTouchInterceptor(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // 如果点击了popupwindow的外部，popupwindow也会消失
                                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    mPopupWindow_xingqi.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                    break;

                case R.id.textView_jieci:
                    if (mPopupWindow_jieci != null && mPopupWindow_jieci.isShowing()) {
                        mPopupWindow_jieci.dismiss();
                    } else {
                        mView_jieci = getLayoutInflater().inflate(
                                R.layout.pop_menulist, null);
                        mListView_jieci = (ListView) mView_jieci.findViewById(R.id.menulist);
                        SimpleAdapter listAdapter = new SimpleAdapter(
                                mContext, mList_jieci, R.layout.pop_menuitem,
                                new String[] { "jieci" },
                                new int[] { R.id.menuitem });
                        mListView_jieci.setAdapter(listAdapter);

                        // 点击listview中item的处理
                        mListView_jieci
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0,
                                                            View arg1, int arg2, long arg3) {
                                        String strItem = mList_jieci.get(arg2).get(
                                                "jieci");

//                                        bangding(strItem);
                                        mTextView_jieci.setText(strItem);

                                        if (mPopupWindow_jieci != null && mPopupWindow_jieci.isShowing()) {
                                            mPopupWindow_jieci.dismiss();
                                        }
                                    }
                                });

                        mPopupWindow_jieci = new PopupWindow(mView_jieci, mTextView_jieci.getWidth(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        ColorDrawable cd = new ColorDrawable(-0000);
                        mPopupWindow_jieci.setBackgroundDrawable(cd);
                        mPopupWindow_jieci.setAnimationStyle(R.style.PopupAnimation);
                        mPopupWindow_jieci.update();
                        mPopupWindow_jieci.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                        mPopupWindow_jieci.setTouchable(true); // 设置popupwindow可点击
                        mPopupWindow_jieci.setOutsideTouchable(true); // 设置popupwindow外部可点击
                        mPopupWindow_jieci.setFocusable(true); // 获取焦点

                        // 设置popupwindow的位置
                        int topBarHeight = mRelativeLayout_jieci.getBottom();
                        mPopupWindow_jieci.showAsDropDown(mTextView_jieci, 0,
                               0);

                        mPopupWindow_jieci.setTouchInterceptor(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // 如果点击了popupwindow的外部，popupwindow也会消失
                                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    mPopupWindow_jieci.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                    break;

                case R.id.textView_zhoushu:
                    if (mPopupWindow_zhoushu != null && mPopupWindow_zhoushu.isShowing()) {
                        mPopupWindow_zhoushu.dismiss();
                    } else {
                        mView_zhoushu = getLayoutInflater().inflate(
                                R.layout.pop_menulist, null);
                        mListView_zhoushu = (ListView) mView_zhoushu.findViewById(R.id.menulist);
                        SimpleAdapter listAdapter = new SimpleAdapter(
                                mContext, mList_zhoushu, R.layout.pop_menuitem,
                                new String[] { "zhoushu" },
                                new int[] { R.id.menuitem });
                        mListView_zhoushu.setAdapter(listAdapter);

                        // 点击listview中item的处理
                        mListView_zhoushu
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0,
                                                            View arg1, int arg2, long arg3) {
                                        String strItem = mList_zhoushu.get(arg2).get(
                                                "zhoushu");

//                                        bangding(strItem);
                                        mTextView_zhoushu.setText(strItem);

                                        if (mPopupWindow_zhoushu != null && mPopupWindow_zhoushu.isShowing()) {
                                            mPopupWindow_zhoushu.dismiss();
                                        }
                                    }
                                });

                        mPopupWindow_zhoushu = new PopupWindow(mView_zhoushu, mTextView_zhoushu.getWidth(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        ColorDrawable cd = new ColorDrawable(-0000);
                        mPopupWindow_zhoushu.setBackgroundDrawable(cd);
                        mPopupWindow_zhoushu.setAnimationStyle(R.style.PopupAnimation);
                        mPopupWindow_zhoushu.update();
                        mPopupWindow_zhoushu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                        mPopupWindow_zhoushu.setTouchable(true); // 设置popupwindow可点击
                        mPopupWindow_zhoushu.setOutsideTouchable(true); // 设置popupwindow外部可点击
                        mPopupWindow_zhoushu.setFocusable(true); // 获取焦点

                        // 设置popupwindow的位置
                        int topBarHeight = mRelativeLayout_zhoushu.getBottom();
                        mPopupWindow_zhoushu.showAsDropDown(mTextView_zhoushu, 0,
                                0);

                        mPopupWindow_zhoushu.setTouchInterceptor(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // 如果点击了popupwindow的外部，popupwindow也会消失
                                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    mPopupWindow_zhoushu.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                    break;

                default:
                    break;
            }
        }

    };
    private Button.OnClickListener mButton_onclick=new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_chaxun_kongzixishi:
                    String mString_building= mTextView_building.getText().toString();
                    String mString_zhoushu= mTextView_zhoushu.getText().toString();
                    int mString_xingqi=xingqizhuanhuan(mTextView_xingqi.getText().toString());
                    int mString_jieci = jiecizhuanhuan(mTextView_jieci.getText().toString());
                    cha(mString_building,Integer.valueOf(mString_zhoushu) ,mString_xingqi,mString_jieci);
            }
        }

    };



    private String getMapValue(Map map,String key){

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object mkey = entry.getKey();
            Object mval = entry.getValue();
            if(mkey.toString().equals(key)){
                return mval.toString();
            }
        }

        return null;
    }

    private void setClipboard(Context context,String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
    public void zhinengxuanze(){//按照日期自动填写表单
        int zhoushu,xingqi,jieci;
        String building= preferences_data.getString("building", "");

        Date mDate = new Date();
        int hours = mDate.getHours();
        try {
            zhoushu= SchoolDate.get_xiaoli();
        } catch (Exception e) {
            Message message=new Message();
            message.what=11;
            mHandler.sendMessage(message);
            return;
        }
        jieci=SchoolDate.get_jieci(hours);
        xingqi=SchoolDate.gei_xingqi();
        if (hours>21){
            if (xingqi!=7){
                xingqi=xingqi+1;
            }else{
                xingqi=1;
                zhoushu=zhoushu+1;
            }

        }
        mTextView_zhoushu.setText(zhoushu+"");
        mTextView_xingqi.setText(xingqizhuanhuan(xingqi));
        mTextView_jieci.setText(jiecizhuanhuan(jieci));
        if (building!=""){
            mTextView_building.setText(building);
        }



    }

}
