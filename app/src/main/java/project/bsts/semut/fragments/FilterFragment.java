package project.bsts.semut.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.genius.ui.widget.SeekBar;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.R;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.setup.Constants;

public class FilterFragment extends Fragment {
    @BindView(R.id.limit)
    EditText limitEdit;
    @BindView(R.id.radiusSeekBar)
    SeekBar radiusSeekbar;
    @BindView(R.id.filter_user)
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
    @BindView(R.id.filterOther)
    ToggleSwitch otherSwitch;
    @BindView(R.id.filterTranPost)
    ToggleSwitch transPostSwitch;
    
    PreferenceManager preferenceManager;
    
    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        preferenceManager = new PreferenceManager(getActivity());
        


        View view = inflater.inflate(R.layout.layout_filter, container, false);
        ButterKnife.bind(this, view);
        initialValue();
        return view;
    }

    private void initialValue() {
        limitEdit.setText(String.valueOf(preferenceManager.getInt(Constants.MAP_LIMIT, 10)));
        radiusSeekbar.setProgress(preferenceManager.getInt(Constants.MAP_RADIUS, 3));

        limitEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                preferenceManager.save(Integer.parseInt(textView.getText().toString()), Constants.MAP_LIMIT);
                preferenceManager.apply();
            }
            return false;
        });

        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preferenceManager.save(seekBar.getProgress(), Constants.MAP_RADIUS);
                preferenceManager.apply();
            }
        });

        Log.i(TAG, String.valueOf(preferenceManager.getInt(Constants.MAP_FILTER_USER, 1)));
        userSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_USER, 1));
        userSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_USER);
            preferenceManager.apply();
        });

        cctvSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_CCTV, 1));
        cctvSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_CCTV);
            preferenceManager.apply();
        });

        policeSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_POLICE_POST, 1));
        policeSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_POLICE_POST);
            preferenceManager.apply();
        });

        accidentSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_ACCIDENT_POST, 1));
        accidentSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_ACCIDENT_POST);
            preferenceManager.apply();
        });


        trafficSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_TRAFFIC_POST, 1));
        trafficSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_TRAFFIC_POST);
            preferenceManager.apply();
        });

        disasterSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_DISASTER_POST, 1));
        disasterSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_DISASTER_POST);
            preferenceManager.apply();
        });

        closureSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_CLOSURE_POST, 1));
        closureSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_CLOSURE_POST);
            preferenceManager.apply();
        });

        trafficSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_TRAFFIC_POST, 1));
        trafficSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_TRAFFIC_POST);
            preferenceManager.apply();
        });


        angkotSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_ANGKOT_LOCATION, 1));
        angkotSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_ANGKOT_LOCATION);
            preferenceManager.apply();
        });

        trainSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_COMMUTER_TRAIN, 1));
        trainSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_COMMUTER_TRAIN);
            preferenceManager.apply();
        });

        otherSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_OTHER_POST, 1));
        otherSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_OTHER_POST);
            preferenceManager.apply();
        });

        transPostSwitch.setCheckedTogglePosition(preferenceManager.getInt(Constants.MAP_FILTER_TRANSPOST, 1));
        transPostSwitch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            preferenceManager.save(position, Constants.MAP_FILTER_TRANSPOST);
            preferenceManager.apply();
        });
    }
}
