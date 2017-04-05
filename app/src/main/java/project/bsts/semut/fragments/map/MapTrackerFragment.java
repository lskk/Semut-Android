package project.bsts.semut.fragments.map;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.Tracker;

public class MapTrackerFragment extends Fragment {

    ImageView mapIconType;
    TextView reportTitleText;
    ImageView imageDescription;
    TextView reportSubTitleText;
    TextView postDateText;
    TextView speedText;
    TextView detailText;
    TextView contentDescriptionText;
    String title, subTitle, postDate, contentDescription, speed, detail;

    Tracker tracker;

    public void setData(Tracker trackerObject){
        this.tracker = trackerObject;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_tracker_fragment, container, false);

        mapIconType = (ImageView)view.findViewById(R.id.map_icon_type);
        reportTitleText = (TextView)view.findViewById(R.id.report_title);
        imageDescription = (ImageView)view.findViewById(R.id.image_description);
        reportSubTitleText = (TextView)view.findViewById(R.id.report_sub_title);
        postDateText = (TextView)view.findViewById(R.id.post_date);
        speedText = (TextView)view.findViewById(R.id.speed);
        contentDescriptionText = (TextView)view.findViewById(R.id.content_description);
        detailText = (TextView)view.findViewById(R.id.post_detail);


        mapIconType.setImageDrawable(new IconicsDrawable(getActivity())
                .color(getActivity().getResources().getColor(R.color.lynch))
                .sizeDp(34)
                .icon(GoogleMaterial.Icon.gmd_place));

        title = "Angkutan Umum";
        subTitle = "Tipe : "+tracker.getType();
        postDate = tracker.getDate()+" "+tracker.getTime();
        speed = String.valueOf(tracker.getSpeed())+" KM/H";
        contentDescription = tracker.getLokasi();
        detail = tracker.getKeterangan();


        imageDescription.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.angkot_icon));
        reportTitleText.setText(title);
        reportSubTitleText.setText(subTitle);
        String tmp = "<b>Lokasi Tanggal : </b>"+postDate;
        postDateText.setText(Html.fromHtml(tmp));
        tmp = "<b>Detail : </b>"+detail;
        detailText.setText(Html.fromHtml(tmp));
        tmp = "<b>Kecepatan : </b>"+speed;
        speedText.setText(Html.fromHtml(tmp));
        tmp = "<b>Lokasi : </b>"+contentDescription;
        contentDescriptionText.setText(Html.fromHtml(tmp));

        return view;
    }

}
