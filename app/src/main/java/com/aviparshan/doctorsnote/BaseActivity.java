package com.aviparshan.doctorsnote;

import android.app.Activity;
import android.content.res.Configuration;

/**
 * Created by avi on 2/28/2017 on com.aviparshan.doctorsnote
 */
public class BaseActivity extends Activity {
    public BaseActivity() {
        //LocaleUtils.updateConfig(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //LocaleUtils.updateConfig(this, newConfig);
    }
}