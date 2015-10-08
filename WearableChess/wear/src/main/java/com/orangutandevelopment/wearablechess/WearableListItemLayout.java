package com.orangutandevelopment.wearablechess;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mitch on 28/09/2015.
 */
public class WearableListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private ImageView mCircle;
    private TextView mHeader;
    private TextView mSubheader;
    public boolean isCentered = false;

    public WearableListItemLayout(Context context) {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mCircle = (ImageView) findViewById(R.id.image);
        mHeader = (TextView) findViewById(R.id.header);
        mSubheader = (TextView) findViewById(R.id.subheader);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        isCentered = true;
        mCircle.setAlpha(1f);
        mHeader.setAlpha(1f);
        mSubheader.setAlpha(1f);
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        isCentered = false;
        mCircle.setAlpha(.3f);
        mHeader.setAlpha(.3f);
        mSubheader.setAlpha(.3f);
    }
}
