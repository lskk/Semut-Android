package project.bsts.semut.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.bsts.semut.R;

public class FilterFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_filter, container, false);
        Log.i(TAG, "fragment");
        return view;
    }
}
