package project.bsts.semut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import project.bsts.semut.pre1;
import project.bsts.semut.pre2;
import project.bsts.semut.pre3;


public class PreloginAdapter extends FragmentStatePagerAdapter {

    public PreloginAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:

                return new pre1();

            case 1:

                return new pre2();
            case 2:

                return new pre3();

        }
        return null;

    }

    @Override
    public int getCount() {
        return 3;
    }

}
