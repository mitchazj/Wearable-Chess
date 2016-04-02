package com.orangutandevelopment.wearablechess;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by mitch on 2/04/2016.
 */
public class UpgradeNowService extends WearableListenerService {
    private static final String TAG = "WearableChessUpgrade";
    private static final String FIELD_ALARM_ON = "alarm_on";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_DELETED) {

            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                //int alarmOn2 = DataMap.fromByteArray(event.getDataItem().getData()).get(FIELD_ALARM_ON);
                //Boolean alarmOn = DataMap.fromByteArray(event.getDataItem().getData()).get(FIELD_ALARM_ON);
                //if (alarmOn) {
                    //Start activity here
                    openAppRating(getApplicationContext(), "com.orangutandevelopment.wearablechesspro");
                //} else {
                //    //Well, that's a bother.
                //}
                try {
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
                }
                catch (Exception e) {

                }
            }
        }
    }

    public static void openAppRating(Context context, String package_name) {
        try {
            Intent dialogIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + package_name));
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent dialogIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + package_name));
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);
        }
    }
}
