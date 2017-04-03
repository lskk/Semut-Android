package project.bsts.semut;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.CctvListAdapter;
import project.bsts.semut.fragments.CctvListFragment;
import project.bsts.semut.pojo.CityCctv;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.setup.Constants;

public class CctvListActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    Intent intent;
    private ArrayList<CctvMap> list = new ArrayList<CctvMap>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        intent = getIntent();
        list = (ArrayList<CctvMap>) intent.getSerializableExtra(Constants.INTENT_CCTV_LIST);
        initTab();


    }

    void initTab(){
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CctvListFragment cctvListFragment = new CctvListFragment();
        cctvListFragment.setData(list);
        adapter.addFragment(cctvListFragment, "LIST");

        CctvListFragment cctvListFragment2 = new CctvListFragment();
        cctvListFragment2.setData(list);
        adapter.addFragment(cctvListFragment2, "LIST");


        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
