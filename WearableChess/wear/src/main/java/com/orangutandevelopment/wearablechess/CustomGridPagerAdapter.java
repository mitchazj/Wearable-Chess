package com.orangutandevelopment.wearablechess;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitch on 4/10/2015.
 */
public class CustomGridPagerAdapter extends FragmentGridPagerAdapter{
    private List<Row> mRows;

    public ChessboardFragment chessboardFragment;
    public SettingsFragment settingsFragment;

    public CustomGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);

        chessboardFragment = new ChessboardFragment();
        settingsFragment = new SettingsFragment();

        mRows = new ArrayList<>();
        mRows.add(new Row(chessboardFragment, settingsFragment));

        settingsFragment.addListener(new OnOptionsClickListener() {
            public void onEvent(int index) {
                if (index == 0) {
                    chessboardFragment.mChessboardView.Takeback(1);
                }
                else if (index == 1) {
                    chessboardFragment.mChessboardView.NewGame();
                }
                else if (index == 4) {
                    chessboardFragment.mChessboardView.Highlights = !chessboardFragment.mChessboardView.Highlights;
                    chessboardFragment.mChessboardView.postInvalidate();
                }
                else if (index == 5) {
                    chessboardFragment.mChessboardView.Vibration = !chessboardFragment.mChessboardView.Vibration;
                }
            }
        });
    }

    //This is a convenient class from the Google Samples.
    //It makes GridPagerAdapters much easier to build.
    private class Row {
        final List<Fragment> columns = new ArrayList<>();

        public Row(Fragment... fragments) {
            for (Fragment f : fragments) {
                add(f);
            }
        }

        public void add(Fragment f) {
            columns.add(f);
        }

        Fragment getColumn(int i) {
            return columns.get(i);
        }

        public int getColumnCount() {
            return columns.size();
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Row adapterRow = mRows.get(row);
        return adapterRow.getColumn(col);
    }

    @Override
    public int getRowCount() {
        return mRows.size();
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mRows.get(rowNum).getColumnCount();
    }
}
