package com.isdust.www.baseactivity;

import android.app.Activity;

/**
 * Created by zor on 2016/9/27.
 * module的基类
 */

public abstract class BaseModule {
    protected int Image_id;
    protected int name;
    protected int desc;
    protected int catagory;


    public BaseModule(int image_id, int name, int desc, int catagory) {
        Image_id = image_id;
        this.name = name;
        this.catagory = catagory;
        this.desc = desc;
    }

    public int getCatagory() {

        return catagory;
    }

    public int getImage_id() {
        return Image_id;
    }

    public int getName() {
        return name;
    }

    public int getDesc() {
        return desc;
    }
    public abstract void lunchActivity(Activity activity);

}
