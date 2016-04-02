package com.orangutandevelopment.wearablechess;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.LinearLayout;

public class UnlockActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_activity);
        setAmbientEnabled();
    }

    public void onUnlockButtonClick(View v) {
        Intent toggleAlarmOperation = new Intent(this, UpgradeNowServiceWear.class);
        toggleAlarmOperation.setAction(UpgradeNowServiceWear.ACTION_TOGGLE_ALARM);
        this.startService(toggleAlarmOperation);

        LinearLayout pnDefault = (LinearLayout) findViewById(R.id.pnDefault);
        pnDefault.setVisibility(View.GONE);
        LinearLayout pnDone = (LinearLayout) findViewById(R.id.pnClicked);
        pnDone.setVisibility(View.VISIBLE);
    }
}
