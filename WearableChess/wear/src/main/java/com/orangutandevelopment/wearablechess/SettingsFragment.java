package com.orangutandevelopment.wearablechess;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mitch on 5/10/2015.
 */
public class SettingsFragment  extends Fragment implements WearableListView.ClickListener{

    String[] headers = { "Takeback", "New Game", "Difficulty", "Style", "Highlights", "Vibrations", "About" };
    String[] subheaders = { "Undo last move", "Start a new game", "4 (Normal)", "Staunton", "On", "Off", "About this app"};
    int[] resources = { R.drawable.ic_undo_black_24dp, R.drawable.white_king_st, R.drawable.brain, R.drawable.black_knight_st, R.drawable.highlights, R.drawable.vibration_on, R.drawable.ic_info_outline_black_24dp};

    ArrayList<OnOptionsClickListener> mListeners = new ArrayList<>();
    public static final String PREFS_NAME = "WearableChessPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences settings = rootView.getContext().getSharedPreferences(PREFS_NAME, 0);
        boolean Vibration = settings.getBoolean("vibration", false);
        boolean Highlights = settings.getBoolean("highlights", true);
        subheaders[4] = Highlights ? "On" : "Off";
        subheaders[5] = Vibration ? "On" : "Off";

        WearableListView listView = (WearableListView) rootView.findViewById(R.id.wearable_list);
        listView.setAdapter(new CustomListAdapter(rootView.getContext(), headers, subheaders, resources));
        listView.setClickListener(this);
        listView.setGreedyTouchMode(true);

        return rootView;
    }

    public void addListener(OnOptionsClickListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
        for (OnOptionsClickListener hl : mListeners)
            hl.onEvent(tag);

        //Switching values
        if (tag == 4 || tag == 5) {
            TextView view = (TextView) v.itemView.findViewById(R.id.subheader);
            view.setText(view.getText() == "Off" ? "On" : "Off");
        }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
