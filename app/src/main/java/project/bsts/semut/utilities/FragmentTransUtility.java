package project.bsts.semut.utilities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import project.bsts.semut.R;
import project.bsts.semut.fragments.FilterFragment;

public class FragmentTransUtility {
    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FragmentTransUtility(Context context){
        this.context = context;
        fragmentManager = ((Activity)context).getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    public void setFilterFragment(FilterFragment fragment, int id){
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}
