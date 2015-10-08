package com.orangutandevelopment.wearablechess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.*;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

public class MainActivity extends WearableActivity {
    private GridViewPager mPager;
    private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;
    private CustomGridPagerAdapter adapter;

    public static final String PREFS_NAME = "WearableChessPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mPager = (GridViewPager) findViewById(R.id.pager);

        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                mDismissOverlay.show();
            }
        });

        mPager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                int rowMargin = getResources().getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = getResources().getDimensionPixelOffset(insets.isRound() ? R.dimen.page_column_margin_round : R.dimen.page_column_margin);

                mPager.setPageMargins(rowMargin, colMargin);
                mPager.onApplyWindowInsets(insets);

                return insets;
            }
        });

        adapter = new CustomGridPagerAdapter(this, getFragmentManager());
        adapter.settingsFragment.addListener(new OnOptionsClickListener() {
            @Override
            public void onEvent(int index) {
                if (index == 0 || index == 1)
                    mPager.scrollTo(0, 0);
                if (index == 2 || index == 3) {
                    Intent myIntent = new Intent(getBaseContext(), UnlockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(myIntent);
                }
                if (index == 6) {
                    Intent myIntent = new Intent(getBaseContext(), AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(myIntent);
                }
            }
        });

        mPager.setAdapter(adapter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("vibration", adapter.chessboardFragment.mChessboardView.Vibration);
        editor.putBoolean("highlights", adapter.chessboardFragment.mChessboardView.Highlights);
        int[] I = adapter.chessboardFragment.mChessboardView.toledo.I;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < I.length; ++i) {
            str.append(I[i]).append(",");
        }
        editor.putString("board", str.toString());
        editor.putInt("B", adapter.chessboardFragment.mChessboardView.toledo.B);
        editor.putInt("i", adapter.chessboardFragment.mChessboardView.toledo.i);
        editor.putInt("y", adapter.chessboardFragment.mChessboardView.toledo.y);
        editor.putInt("u", adapter.chessboardFragment.mChessboardView.toledo.u);
        editor.putInt("L", adapter.chessboardFragment.mChessboardView.toledo.L);
        editor.putInt("b", adapter.chessboardFragment.mChessboardView.toledo.b);
        editor.commit();
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent e) {
        return mDetector.onTouchEvent(e) || super.dispatchTouchEvent(e);
    }
}
