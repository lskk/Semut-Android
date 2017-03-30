package project.bsts.semut.utilities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import project.bsts.semut.R;
import project.bsts.semut.fragments.FilterFragment;
import project.bsts.semut.fragments.map.MapCctvFragment;
import project.bsts.semut.fragments.map.MapReportFragment;
import project.bsts.semut.fragments.map.MapTrackerFragment;
import project.bsts.semut.fragments.map.MapUserFragment;
import project.bsts.semut.pojo.mapview.UserMap;

public class FragmentTransUtility {
    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FragmentTransUtility(Context context){
        this.context = context;
        fragmentManager = ((Activity)context).getFragmentManager();

    }

    public void setFilterFragment(FilterFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public void setUserMapFragment(MapUserFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public void setCctvMapFragment(MapCctvFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public void setTrackerMapFragment(MapTrackerFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }


    public void setReportMapFragment(MapReportFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}
