package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.CctvListAdapter;
import project.bsts.semut.pojo.CityCctv;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.setup.Constants;

public class CctvListActivity extends AppCompatActivity {

    @BindView(R.id.list_cctv)
    ListView mListViewCctv;

    private ArrayList<CctvMap> list = new ArrayList<CctvMap>();
    Intent intent;
    Context context;
    CctvListAdapter cctvListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        context = this;
        intent = getIntent();
        list = (ArrayList<CctvMap>) intent.getSerializableExtra(Constants.INTENT_CCTV_LIST);
        cctvListAdapter = new CctvListAdapter(context, list);
        mListViewCctv.setAdapter(cctvListAdapter);
       // list.clear();

    }

}
