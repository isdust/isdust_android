package com.isdust.www;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.isdust.www.baseactivity.BaseSubPageActivity_new;

/**
 * Created by Wang Ziqiang on 15/11/4.
 * isdust
 * Copyright (C) <2015>  <Wang Ziqiang,Leng Hanchao,Qing Wenkai,Huyang>

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
public class Library_personal_login extends BaseSubPageActivity_new {
    SharedPreferences.Editor preferences_editor;
    SharedPreferences preferences_data;
    CheckBox mCheckBox_savepwd;
    EditText mEditText_user,mEditText_password;
    Button mButton_login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_library_personal_login,"图书馆登录");
        mContext=this;
        //实例化SharedPreferences对象
        preferences_data = mContext.getSharedPreferences("LibraryData", Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        preferences_editor = preferences_data.edit();

        getview();
        getLocalData();
        mButton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user,password;
                user=mEditText_user.getText().toString();
                password=mEditText_password.getText().toString();
                preferences_editor.putString("username", user);
                if (mCheckBox_savepwd.isChecked()) {	//记住密码
                    preferences_editor.putBoolean("keeppwd", true);
                    preferences_editor.putString("password", password);
                }
                else {	//不记住密码
                    preferences_editor.putBoolean("keeppwd", false);
                    preferences_editor.putString("password", "");
                }
                //提交当前数据
                preferences_editor.commit();
                Intent intent=new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("username", user);
                bundle.putString("password", password);
                intent.putExtras(bundle);
                Library_personal_login.this.setResult(RESULT_OK, intent);
//                intent.setClass(mContext, ScheduleActivity.class);
//                startActivity(intent);
                finish();

            }
        });

    }
    private void getview(){
        mEditText_user=(EditText)findViewById(R.id.EditText_library_login_user);
        mEditText_password=(EditText)findViewById(R.id.EditText_library_login_password);
        mCheckBox_savepwd=(CheckBox)findViewById(R.id.checkbox_library_savepassword);
        mButton_login=(Button)findViewById(R.id.button_library_login);

    }
    private void getLocalData() {
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String name =preferences_data.getString("username", "");
        String pwd =preferences_data.getString("password", "");
        Boolean keeppwd;
        mEditText_user.setText(name);
        mEditText_password.setText(pwd);
        keeppwd = preferences_data.getBoolean("keeppwd", true);
        mCheckBox_savepwd.setChecked(keeppwd);
    }   //读取本地数据
}
