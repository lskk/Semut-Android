package project.bsts.semut.fragments.map;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.utilities.DownloadImageTask;

public class MapCctvFragment extends Fragment {

    private ImageView thumb;
    private TextView detail;
    private Button watchBtn;

    CctvMap cctvMap;


    public void setData(CctvMap cctvMap){
        this.cctvMap = cctvMap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_cctv_fragment, container, false);

        thumb = (ImageView)view.findViewById(R.id.thumb);
        detail = (TextView)view.findViewById(R.id.cctv_location);
        watchBtn = (Button)view.findViewById(R.id.watch_btn);

        detail.setText(cctvMap.getName());

        new DownloadImageTask(thumb).execute(cctvMap.getUrlImage());



        return view;
    }
}
