package com.orangutandevelopment.wearablechess;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChessboardFragment extends Fragment {

    public ChessboardView mChessboardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chessboard, container, false);
        mChessboardView = (ChessboardView) rootView.findViewById(R.id.chess_view);
        return rootView;
    }

}
