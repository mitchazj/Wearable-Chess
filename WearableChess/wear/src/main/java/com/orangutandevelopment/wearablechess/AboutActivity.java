package com.orangutandevelopment.wearablechess;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

public class AboutActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        setAmbientEnabled();
    }

}
