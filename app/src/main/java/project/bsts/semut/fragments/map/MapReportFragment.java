package project.bsts.semut.fragments.map;


import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import project.bsts.semut.R;
import project.bsts.semut.map.MapViewComponent;
import project.bsts.semut.map.MapViewType;
import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.TrafficMap;

public class MapReportFragment extends Fragment {

    ImageView mapIconType;
    TextView reportTitleText;
    ImageView imageDescription;
    TextView reportSubTitleText;
    TextView postDateText;
    TextView postedByText;
    TextView contentDescriptionText;
    String title, subTitle, postDate, postedBy, contentDescription;

    Object reportObject;
    private int reportType;
    public void setData(Object reportObject){
        this.reportObject = reportObject;
        reportType = MapViewType.get(reportObject);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_report_fragment, container, false);

        mapIconType = (ImageView)view.findViewById(R.id.map_icon_type);
        reportTitleText = (TextView)view.findViewById(R.id.report_title);
        imageDescription = (ImageView)view.findViewById(R.id.image_description);
        reportSubTitleText = (TextView)view.findViewById(R.id.report_sub_title);
        postDateText = (TextView)view.findViewById(R.id.post_date);
        postedByText = (TextView)view.findViewById(R.id.posted_by);
        contentDescriptionText = (TextView)view.findViewById(R.id.content_description);


        Drawable[] icons = checkType(reportType);
        mapIconType.setImageDrawable(icons[0]);
        imageDescription.setImageDrawable(icons[1]);
        reportTitleText.setText(title);
        reportSubTitleText.setText(subTitle);
        postDateText.setText(postDate);
        postedByText.setText(postedBy);
        contentDescriptionText.setText(contentDescription);

        return view;
    }

    private Drawable[] checkType(int type){
        Drawable[] mapIconDrawable = new Drawable[1];
        switch (type){
            case MapViewComponent.POLICE_MAP_COMPONENT:
                mapIconDrawable[0] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(34)
                        .icon(GoogleMaterial.Icon.gmd_place);
                mapIconDrawable[1] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(64)
                        .icon(FontAwesome.Icon.faw_taxi);

                PoliceMap policeMap = (PoliceMap) reportObject;
                title = policeMap.getType();
                subTitle = policeMap.getSubType();
                postDate = policeMap.getTimes();
                postedBy = policeMap.getPostedBy().getName();
                contentDescription = policeMap.getDescription();

                break;


            case MapViewComponent.ACCIDENT_MAP_COMPONENT:
                mapIconDrawable[0] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_cyan_700))
                        .sizeDp(34)
                        .icon(GoogleMaterial.Icon.gmd_place);
                mapIconDrawable[1] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(64)
                        .icon(CommunityMaterial.Icon.cmd_playlist_remove);

                AccidentMap accidentMap = (AccidentMap) reportObject;
                title = accidentMap.getType();
                subTitle = accidentMap.getSubType();
                postDate = accidentMap.getTimes();
                postedBy = accidentMap.getPostedBy().getName();
                contentDescription = accidentMap.getDescription();
                break;


            case MapViewComponent.TRAFFIC_MAP_COMPONENT:
                mapIconDrawable[0] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_blue_700))
                        .sizeDp(34)
                        .icon(GoogleMaterial.Icon.gmd_place);
                mapIconDrawable[1] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(64)
                        .icon(CommunityMaterial.Icon.cmd_road_variant);
                TrafficMap trafficMap = (TrafficMap) reportObject;
                title = trafficMap.getType();
                subTitle = trafficMap.getSubType();
                postDate = trafficMap.getTimes();
                postedBy = trafficMap.getPostedBy().getName();
                contentDescription = trafficMap.getDescription();
                break;


            case MapViewComponent.DISASTER_MAP_COMPONENT:
                mapIconDrawable[0] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_purple_400))
                        .sizeDp(34)
                        .icon(GoogleMaterial.Icon.gmd_place);
                mapIconDrawable[1] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(64)
                        .icon(FontAwesome.Icon.faw_globe);
                DisasterMap disasterMap = (DisasterMap) reportObject;
                title = disasterMap.getType();
                subTitle = disasterMap.getSubType();
                postDate = disasterMap.getTimes();
                postedBy = disasterMap.getPostedBy().getName();
                contentDescription = disasterMap.getDescription();
                break;


            case MapViewComponent.CLOSURE_MAP_COMPONENT:
                mapIconDrawable[0] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_red_A200))
                        .sizeDp(34)
                        .icon(GoogleMaterial.Icon.gmd_place);
                mapIconDrawable[1] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(64)
                        .icon(CommunityMaterial.Icon.cmd_sign_caution);
                ClosureMap closureMap = (ClosureMap) reportObject;
                title = closureMap.getType();
                subTitle = closureMap.getSubType();
                postDate = closureMap.getTimes();
                postedBy = closureMap.getPostedBy().getName();
                contentDescription = closureMap.getDescription();
                break;


            case MapViewComponent.OTHER_MAP_COMPONENT:
                mapIconDrawable[0] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.deep_purple_700))
                        .sizeDp(34)
                        .icon(GoogleMaterial.Icon.gmd_place);
                mapIconDrawable[1] = new IconicsDrawable(getActivity())
                        .color(getActivity().getResources().getColor(R.color.md_green_700))
                        .sizeDp(64)
                        .icon(FontAwesome.Icon.faw_map_signs);
                OtherMap otherMap = (OtherMap) reportObject;
                title = otherMap.getType();
                subTitle = otherMap.getSubType();
                postDate = otherMap.getTimes();
                postedBy = otherMap.getPostedBy().getName();
                contentDescription = otherMap.getDescription();
                break;
        }

        return mapIconDrawable;
    }

}
