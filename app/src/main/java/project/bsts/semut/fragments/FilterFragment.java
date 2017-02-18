package project.bsts.semut.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.R;
import project.bsts.semut.helper.PreferenceManager;

public class FilterFragment extends Fragment {
    @BindView(R.id.limit)
    EditText limitEdit;
    @BindView(R.id.radiusSeekBar)
    SeekBar radiusSeekbar;
    @BindView(R.id.filterUser)
    ToggleSwitch userSwitch;
    @BindView(R.id.filterCctv)
    ToggleSwitch cctvSwitch;
    @BindView(R.id.filterPolice)
    ToggleSwitch policeSwitch;
    @BindView(R.id.filterAccident)
    ToggleSwitch accidentSwitch;
    @BindView(R.id.filterTraffic)
    ToggleSwitch trafficSwitch;
    @BindView(R.id.filterDisaster)
    ToggleSwitch disasterSwitch;
    @BindView(R.id.filterClosure)
    ToggleSwitch closureSwitch;
    @BindView(R.id.filterCommuter)
    ToggleSwitch trainSwitch;
    @BindView(R.id.filterAngkot)
    ToggleSwitch angkotSwitch;
    
    PreferenceManager preferenceManager;
    
    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        preferenceManager = new PreferenceManager(getActivity());
        
        initialValue();

        View view = inflater.inflate(R.layout.layout_filter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initialValue() {
    }
}
