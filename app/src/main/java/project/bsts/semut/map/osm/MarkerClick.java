package project.bsts.semut.map.osm;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import org.osmdroid.views.overlay.Marker;

import project.bsts.semut.R;
import project.bsts.semut.fragments.map.MapCctvFragment;
import project.bsts.semut.fragments.map.MapReportFragment;
import project.bsts.semut.fragments.map.MapTrackerFragment;
import project.bsts.semut.fragments.map.MapUserFragment;
import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.Tracker;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.TranspostMap;
import project.bsts.semut.pojo.mapview.UserMap;
import project.bsts.semut.ui.AnimationView;
import project.bsts.semut.utilities.FragmentTransUtility;

public class MarkerClick {
    private Context context;
    private View frameView;
    private FragmentTransUtility fragmentTransUtility;
    private Animation fromRight;
    private AnimationView animationView;

    public MarkerClick(Context context, View frameView){
        this.context = context;
        this.frameView = frameView;
        fragmentTransUtility = new FragmentTransUtility(context);
        animationView = new AnimationView(context);
        fromRight = animationView.getAnimation(R.anim.slide_up, null);
    }

    public void checkMarker(Marker marker){
        if(marker.getRelatedObject() instanceof UserMap){
            MapUserFragment mapUserFragment = new MapUserFragment();
            mapUserFragment.setData((UserMap) marker.getRelatedObject());
            fragmentTransUtility.setUserMapFragment(mapUserFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }else if(marker.getRelatedObject() instanceof CctvMap){
            MapCctvFragment mapCctvFragment = new MapCctvFragment();
            mapCctvFragment.setData((CctvMap) marker.getRelatedObject());
            fragmentTransUtility.setCctvMapFragment(mapCctvFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }else if(marker.getRelatedObject() instanceof Tracker){
            MapTrackerFragment mapTrackerFragment = new MapTrackerFragment();
            mapTrackerFragment.setData((Tracker) marker.getRelatedObject());
            fragmentTransUtility.setTrackerMapFragment(mapTrackerFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }else if(marker.getRelatedObject() instanceof PoliceMap ||
                marker.getRelatedObject() instanceof AccidentMap ||
                marker.getRelatedObject() instanceof TrafficMap ||
                marker.getRelatedObject() instanceof DisasterMap ||
                marker.getRelatedObject() instanceof ClosureMap ||
                marker.getRelatedObject() instanceof TranspostMap ||
                marker.getRelatedObject() instanceof OtherMap){
            MapReportFragment mapReportFragment = new MapReportFragment();
            mapReportFragment.setData(marker.getRelatedObject());
            fragmentTransUtility.setReportMapFragment(mapReportFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }
    }
}
