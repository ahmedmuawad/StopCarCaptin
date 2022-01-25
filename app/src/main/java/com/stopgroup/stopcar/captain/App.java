package com.stopgroup.stopcar.captain;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.emeint.android.fawryplugin.managers.FawryPluginAppClass;
public class App extends FawryPluginAppClass {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
