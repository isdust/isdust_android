package com.formal.sdusthelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.formal.sdusthelper.baseactivity.BaseSubPageActivity;

/**
 *
 * Created by Administrator on 2015/10/24.
 */
public class SetAccountActivity extends BaseSubPageActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_set_account, "账号管理");
    }

    public void onFormSetAcntClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.set_btn_card:

                break;
            case R.id.set_btn_jiaowu:

                break;
            case R.id.set_btn_cmcc:
                intent.setClass(this,GoNetCMCCAcntActivity.class);
                this.startActivity(intent);
                break;
//<<<<<<< HEAD

//=======
            case R.id.set_btn_chinaunicom:
                intent.setClass(this,GoNetChinaUnicomAcntActivity.class);
                this.startActivity(intent);
                break;
            case R.id.set_btn_kuaitong:

                break;
            case R.id.set_btn_library:

                break;
//>>>>>>> 1efe3ae89ab69dea886099a30f5d64cc02315f9a
        }
    }
}
