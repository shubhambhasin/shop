package com.example.shubhambhasin.main;

/**
 * Created by Shubham Bhasin on 23-Mar-16.
 */

import android.app.Application;

import com.parse.Parse;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), "8jGtBksT9mDu54fiER3kdAFeP3UHvn3yOwKeGw5G", "p1YKaq6Dc35brH5hBQEBARNipmHgaTBlqQWu0L8G\n");
    }


}
