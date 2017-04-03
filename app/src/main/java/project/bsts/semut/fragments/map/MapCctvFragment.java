package project.bsts.semut.fragments.map;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.qiujuer.genius.ui.widget.Loading;

import project.bsts.semut.CctvPlayerActivity;
import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.utilities.DownloadImageTask;

public class MapCctvFragment extends Fragment {

    private ImageView thumb;
    private TextView detail;
    private Button watchBtn;
    private Loading loading;

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
        loading = (Loading)view.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
      //  loading.start();

        detail.setText(cctvMap.getName());


      //  new DownloadImageTask(thumb, loading).execute(cctvMap.getUrlImage());
        String urldisplay = cctvMap.getUrlImage().replace("push-ios", "247");
        Picasso.with(getActivity())
                .load(urldisplay)
                .placeholder(R.mipmap.loading_image)
                .error(R.mipmap.kamera_akses_error)
                .into(thumb);

        watchBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CctvPlayerActivity.class);
            intent.putExtra(Constants.INTENT_VIDEO_URL, cctvMap.getUrlVideo());
            startActivity(intent);
        });



        return view;
    }
}
